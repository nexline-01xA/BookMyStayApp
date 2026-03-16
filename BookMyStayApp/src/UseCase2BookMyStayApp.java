abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected String size;

    public Room(String roomType, int numberOfBeds, double pricePerNight, String size) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.size = size;
    }

    public abstract void displayRoomDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 999.0, "Small");
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Price/Night   : Rs." + pricePerNight);
        System.out.println("Size          : " + size);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1799.0, "Medium");
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Price/Night   : Rs." + pricePerNight);
        System.out.println("Size          : " + size);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 4999.0, "Large");
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Price/Night   : Rs." + pricePerNight);
        System.out.println("Size          : " + size);
    }
}

public class UseCase2BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC2 - Basic Room Types & Static Availability");
        System.out.println("Version : 2.0");
        System.out.println("----------------------------------");

        boolean singleRoomAvailable = true;
        boolean doubleRoomAvailable = true;
        boolean suiteRoomAvailable = false;

        Room single = new SingleRoom();
        Room doublRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("\n--- Single Room ---");
        single.displayRoomDetails();
        System.out.println("Available     : " + singleRoomAvailable);

        System.out.println("\n--- Double Room ---");
        doublRoom.displayRoomDetails();
        System.out.println("Available     : " + doubleRoomAvailable);

        System.out.println("\n--- Suite Room ---");
        suite.displayRoomDetails();
        System.out.println("Available     : " + suiteRoomAvailable);

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}