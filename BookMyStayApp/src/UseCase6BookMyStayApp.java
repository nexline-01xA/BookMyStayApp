import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

// Reservation class
class Reservation6 {
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public Reservation6(String guestName, String roomType, int numberOfNights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNumberOfNights() { return numberOfNights; }
}

// Inventory Service
class InventoryService6 {
    private HashMap<String, Integer> inventory;

    public InventoryService6() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType) + " available");
        }
    }
}

// Booking Service - allocates rooms and prevents double booking
class BookingService6 {
    private InventoryService6 inventoryService;
    private HashMap<String, Set<String>> allocatedRooms;
    private Set<String> allRoomIds;
    private int roomCounter;

    public BookingService6(InventoryService6 inventoryService) {
        this.inventoryService = inventoryService;
        allocatedRooms = new HashMap<>();
        allRoomIds = new HashSet<>();
        roomCounter = 1;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 1).toUpperCase();
        String roomId = prefix + String.format("%03d", roomCounter++);
        // Ensure uniqueness
        while (allRoomIds.contains(roomId)) {
            roomId = prefix + String.format("%03d", roomCounter++);
        }
        return roomId;
    }

    // Process booking request
    public void processBooking(Reservation6 reservation) {
        String roomType = reservation.getRoomType();
        String guestName = reservation.getGuestName();

        System.out.println("\nProcessing request for: " + guestName + " -> " + roomType);

        if (inventoryService.getAvailability(roomType) <= 0) {
            System.out.println("FAILED: No availability for " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Record allocation
        allRoomIds.add(roomId);
        allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        // Update inventory
        inventoryService.decrementAvailability(roomType);

        System.out.println("CONFIRMED: " + guestName + " assigned " + roomId + " (" + roomType + ") for " + reservation.getNumberOfNights() + " nights");
    }

    public void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");
        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + " : " + allocatedRooms.get(roomType));
        }
    }
}

public class UseCase6BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC6 - Reservation Confirmation & Room Allocation");
        System.out.println("Version : 6.0");
        System.out.println("----------------------------------");

        // Setup
        InventoryService6 inventoryService = new InventoryService6();
        BookingService6 bookingService = new BookingService6(inventoryService);

        // Queue of booking requests
        Queue<Reservation6> requestQueue = new LinkedList<>();
        requestQueue.add(new Reservation6("Alice", "Single Room", 2));
        requestQueue.add(new Reservation6("Bob", "Double Room", 3));
        requestQueue.add(new Reservation6("Charlie", "Suite Room", 1));
        requestQueue.add(new Reservation6("Diana", "Single Room", 4));
        requestQueue.add(new Reservation6("Eve", "Single Room", 2));

        inventoryService.displayInventory();

        System.out.println("\n--- Processing Booking Requests (FIFO) ---");
        while (!requestQueue.isEmpty()) {
            bookingService.processBooking(requestQueue.poll());
        }

        bookingService.displayAllocations();
        inventoryService.displayInventory();

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}