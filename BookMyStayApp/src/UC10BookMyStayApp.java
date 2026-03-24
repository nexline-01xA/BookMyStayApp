import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

// Reservation class
class Reservation10 {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private int numberOfNights;
    private boolean cancelled;

    public Reservation10(String reservationId, String guestName, String roomType, String roomId, int numberOfNights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.numberOfNights = numberOfNights;
        this.cancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public int getNumberOfNights() { return numberOfNights; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Nights: " + numberOfNights +
                " | Status: " + (cancelled ? "CANCELLED" : "CONFIRMED"));
    }
}

// Inventory Service
class InventoryService10 {
    private HashMap<String, Integer> inventory;

    public InventoryService10() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void decrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void incrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + ": " + inventory.get(roomType) + " available");
        }
    }
}

// Cancellation Service
class CancellationService {
    private HashMap<String, Reservation10> bookingRegistry;
    private InventoryService10 inventoryService;
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService10 inventoryService) {
        this.inventoryService = inventoryService;
        bookingRegistry = new HashMap<>();
        rollbackStack = new Stack<>();
    }

    public void registerBooking(Reservation10 reservation) {
        bookingRegistry.put(reservation.getReservationId(), reservation);
        inventoryService.decrementAvailability(reservation.getRoomType());
        System.out.println("Booking registered: " + reservation.getReservationId() +
                " for " + reservation.getGuestName());
    }

    public void cancelBooking(String reservationId) {
        System.out.println("\nAttempting cancellation for: " + reservationId);

        if (!bookingRegistry.containsKey(reservationId)) {
            System.out.println("FAILED: Reservation " + reservationId + " does not exist.");
            return;
        }

        Reservation10 reservation = bookingRegistry.get(reservationId);

        if (reservation.isCancelled()) {
            System.out.println("FAILED: Reservation " + reservationId + " is already cancelled.");
            return;
        }

        // Push room ID to rollback stack
        rollbackStack.push(reservation.getRoomId());

        // Restore inventory
        inventoryService.incrementAvailability(reservation.getRoomType());

        // Mark as cancelled
        reservation.setCancelled(true);

        System.out.println("CANCELLED: " + reservationId + " | Room " +
                reservation.getRoomId() + " released | Inventory restored for " +
                reservation.getRoomType());
    }

    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (Recently Released Rooms - LIFO) ---");
        if (rollbackStack.isEmpty()) {
            System.out.println("No rollbacks recorded.");
            return;
        }
        Stack<String> temp = new Stack<>();
        temp.addAll(rollbackStack);
        while (!temp.isEmpty()) {
            System.out.println("Released Room ID: " + temp.pop());
        }
    }

    public void displayAllBookings() {
        System.out.println("\n--- All Bookings ---");
        for (Reservation10 reservation : bookingRegistry.values()) {
            reservation.displayReservation();
        }
    }
}

public class UC10BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC10 - Booking Cancellation & Inventory Rollback");
        System.out.println("Version : 10.0");
        System.out.println("----------------------------------");

        InventoryService10 inventoryService = new InventoryService10();
        CancellationService cancellationService = new CancellationService(inventoryService);

        System.out.println("\n--- Registering Confirmed Bookings ---");
        cancellationService.registerBooking(new Reservation10("RES001", "Alice", "Single Room", "S001", 2));
        cancellationService.registerBooking(new Reservation10("RES002", "Bob", "Double Room", "D001", 3));
        cancellationService.registerBooking(new Reservation10("RES003", "Charlie", "Suite Room", "T001", 1));
        cancellationService.registerBooking(new Reservation10("RES004", "Diana", "Single Room", "S002", 4));

        inventoryService.displayInventory();
        cancellationService.displayAllBookings();

        System.out.println("\n--- Processing Cancellation Requests ---");

        // Valid cancellations
        cancellationService.cancelBooking("RES002");
        cancellationService.cancelBooking("RES003");

        // Invalid - already cancelled
        cancellationService.cancelBooking("RES002");

        // Invalid - does not exist
        cancellationService.cancelBooking("RES999");

        cancellationService.displayRollbackStack();
        cancellationService.displayAllBookings();
        inventoryService.displayInventory();

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}