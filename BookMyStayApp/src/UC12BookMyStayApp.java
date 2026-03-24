import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Reservation class (Serializable)
class Reservation12 implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private int numberOfNights;

    public Reservation12(String reservationId, String guestName,
                         String roomType, String roomId, int numberOfNights) {
        this.reservationId = reservationId;
        this.guestName     = guestName;
        this.roomType      = roomType;
        this.roomId        = roomId;
        this.numberOfNights = numberOfNights;
    }

    public String getReservationId()  { return reservationId; }
    public String getGuestName()      { return guestName; }
    public String getRoomType()       { return roomType; }
    public String getRoomId()         { return roomId; }
    public int    getNumberOfNights() { return numberOfNights; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: "     + guestName     +
                " | Room Type: " + roomType      +
                " | Room ID: "   + roomId        +
                " | Nights: "    + numberOfNights);
    }
}

// System State (Serializable) - wraps everything that must survive restart
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer>     inventory;
    private List<Reservation12>          bookingHistory;

    public SystemState(HashMap<String, Integer> inventory,
                       List<Reservation12> bookingHistory) {
        this.inventory      = inventory;
        this.bookingHistory = bookingHistory;
    }

    public HashMap<String, Integer> getInventory()      { return inventory; }
    public List<Reservation12>      getBookingHistory() { return bookingHistory; }
}

// Persistence Service - saves and loads system state
class PersistenceService {
    private static final String FILE_PATH = "system_state.dat";

    public void saveState(HashMap<String, Integer> inventory,
                          List<Reservation12> bookingHistory) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {

            SystemState state = new SystemState(inventory, bookingHistory);
            oos.writeObject(state);
            System.out.println("System state saved to: " + FILE_PATH);

        } catch (IOException e) {
            System.out.println("ERROR: Failed to save state - " + e.getMessage());
        }
    }

    public SystemState loadState() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_PATH))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded from: " + FILE_PATH);
            return state;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR: Failed to load state - " + e.getMessage());
            return null;
        }
    }
}

// Inventory Service
class InventoryService12 {
    private HashMap<String, Integer> inventory;

    public InventoryService12() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room",  1);
    }

    public void restoreInventory(HashMap<String, Integer> savedInventory) {
        this.inventory = savedInventory;
        System.out.println("Inventory restored from saved state.");
    }

    public HashMap<String, Integer> getInventory() { return inventory; }

    public boolean decrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + ": " + inventory.get(roomType) + " available");
        }
    }
}

// Booking History Service
class BookingHistoryService12 {
    private List<Reservation12> history;

    public BookingHistoryService12() {
        history = new ArrayList<>();
    }

    public void restoreHistory(List<Reservation12> savedHistory) {
        this.history = savedHistory;
        System.out.println("Booking history restored: " + history.size() + " record(s).");
    }

    public void addBooking(Reservation12 reservation) {
        history.add(reservation);
    }

    public List<Reservation12> getHistory() { return history; }

    public void displayHistory() {
        System.out.println("\n--- Booking History ---");
        if (history.isEmpty()) {
            System.out.println("No bookings recorded.");
            return;
        }
        for (Reservation12 r : history) {
            r.displayReservation();
        }
    }
}

public class UC12BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC12 - Data Persistence & System Recovery");
        System.out.println("Version : 12.0");
        System.out.println("----------------------------------");

        PersistenceService    persistenceService = new PersistenceService();
        InventoryService12    inventoryService   = new InventoryService12();
        BookingHistoryService12 historyService   = new BookingHistoryService12();

        // --- Attempt Recovery ---
        System.out.println("\n--- Attempting System Recovery ---");
        SystemState savedState = persistenceService.loadState();

        if (savedState != null) {
            inventoryService.restoreInventory(savedState.getInventory());
            historyService.restoreHistory(savedState.getBookingHistory());
        } else {
            System.out.println("Initializing default inventory.");
        }

        inventoryService.displayInventory();
        historyService.displayHistory();

        // --- Simulate New Bookings ---
        System.out.println("\n--- Processing New Bookings ---");

        String[][] bookings = {
                {"RES001", "Alice",   "Single Room", "S001", "2"},
                {"RES002", "Bob",     "Double Room", "D001", "3"},
                {"RES003", "Charlie", "Suite Room",  "T001", "1"},
                {"RES004", "Diana",   "Single Room", "S002", "4"}
        };

        for (String[] b : bookings) {
            if (inventoryService.decrementAvailability(b[2])) {
                Reservation12 res = new Reservation12(
                        b[0], b[1], b[2], b[3], Integer.parseInt(b[4]));
                historyService.addBooking(res);
                System.out.println("CONFIRMED: " + b[1] + " -> " + b[3] +
                        " (" + b[2] + ")");
            } else {
                System.out.println("FAILED: No availability for " + b[2]);
            }
        }

        inventoryService.displayInventory();
        historyService.displayHistory();

        // --- Save State Before Shutdown ---
        System.out.println("\n--- Saving System State Before Shutdown ---");
        persistenceService.saveState(
                inventoryService.getInventory(),
                historyService.getHistory()
        );

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}