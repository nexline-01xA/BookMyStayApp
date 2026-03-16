import java.util.HashMap;

// Abstract Room class
abstract class Room2 {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected String size;

    public Room2(String roomType, int numberOfBeds, double pricePerNight, String size) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.size = size;
    }

    public abstract void displayRoomDetails();
}

class SingleRoom2 extends Room2 {
    public SingleRoom2() { super("Single Room", 1, 999.0, "Small"); }
    @Override
    public void displayRoomDetails() {
        System.out.println("  Type: " + roomType + " | Beds: " + numberOfBeds + " | Price: Rs." + pricePerNight + " | Size: " + size);
    }
}

class DoubleRoom2 extends Room2 {
    public DoubleRoom2() { super("Double Room", 2, 1799.0, "Medium"); }
    @Override
    public void displayRoomDetails() {
        System.out.println("  Type: " + roomType + " | Beds: " + numberOfBeds + " | Price: Rs." + pricePerNight + " | Size: " + size);
    }
}

class SuiteRoom2 extends Room2 {
    public SuiteRoom2() { super("Suite Room", 3, 4999.0, "Large"); }
    @Override
    public void displayRoomDetails() {
        System.out.println("  Type: " + roomType + " | Beds: " + numberOfBeds + " | Price: Rs." + pricePerNight + " | Size: " + size);
    }
}

// RoomInventory class
class RoomInventory2 {
    private HashMap<String, Integer> inventory;

    public RoomInventory2() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 2);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }
}

// Search Service - read only
class RoomSearchService {
    private RoomInventory2 inventory;
    private HashMap<String, Room2> roomCatalog;

    public RoomSearchService(RoomInventory2 inventory) {
        this.inventory = inventory;
        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom2());
        roomCatalog.put("Double Room", new DoubleRoom2());
        roomCatalog.put("Suite Room", new SuiteRoom2());
    }

    // Search - read only, no state change
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        boolean found = false;
        for (String roomType : inventory.getInventory().keySet()) {
            int available = inventory.getAvailability(roomType);
            if (available > 0) {
                System.out.println("\nRoom Type: " + roomType + " | Available: " + available);
                roomCatalog.get(roomType).displayRoomDetails();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

public class UseCase4BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC4 - Room Search & Availability Check");
        System.out.println("Version : 4.0");
        System.out.println("----------------------------------");

        RoomInventory2 inventory = new RoomInventory2();
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Guest searches for available rooms
        searchService.searchAvailableRooms();

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}