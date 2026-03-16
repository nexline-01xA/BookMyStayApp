import java.util.HashMap;

class RoomInventory {
    // HashMap - single source of truth for room availability
    private HashMap<String, Integer> inventory;

    // Constructor - initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType) + " available");
        }
    }
}

public class UseCase3BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC3 - Centralized Room Inventory Management");
        System.out.println("Version : 3.0");
        System.out.println("----------------------------------");

        // Initialize inventory
        RoomInventory roomInventory = new RoomInventory();

        // Display initial inventory
        roomInventory.displayInventory();

        // Update availability
        System.out.println("\n--- Updating Single Room availability to 3 ---");
        roomInventory.updateAvailability("Single Room", 3);

        // Display updated inventory
        roomInventory.displayInventory();

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}
