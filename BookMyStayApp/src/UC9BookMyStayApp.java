// Custom Exceptions
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InvalidNightsException extends Exception {
    public InvalidNightsException(String message) {
        super(message);
    }
}

class NoAvailabilityException extends Exception {
    public NoAvailabilityException(String message) {
        super(message);
    }
}

class InvalidGuestNameException extends Exception {
    public InvalidGuestNameException(String message) {
        super(message);
    }
}

// Inventory Service
class InventoryService9 {
    private java.util.HashMap<String, Integer> inventory;

    public InventoryService9() {
        inventory = new java.util.HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) throws NoAvailabilityException {
        int current = inventory.getOrDefault(roomType, 0);
        if (current <= 0) {
            throw new NoAvailabilityException("No availability for room type: " + roomType);
        }
        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + ": " + inventory.get(roomType) + " available");
        }
    }
}

// Validator
class BookingValidator {
    private InventoryService9 inventoryService;

    public BookingValidator(InventoryService9 inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void validate(String guestName, String roomType, int numberOfNights)
            throws InvalidGuestNameException, InvalidRoomTypeException,
            InvalidNightsException, NoAvailabilityException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidGuestNameException("Guest name cannot be null or empty.");
        }

        if (!inventoryService.isValidRoomType(roomType)) {
            throw new InvalidRoomTypeException("Invalid room type: '" + roomType + "'. Valid types: Single Room, Double Room, Suite Room.");
        }

        if (numberOfNights <= 0) {
            throw new InvalidNightsException("Number of nights must be greater than zero. Provided: " + numberOfNights);
        }

        if (inventoryService.getAvailability(roomType) <= 0) {
            throw new NoAvailabilityException("No rooms available for type: " + roomType);
        }
    }
}

// Booking Service
class BookingService9 {
    private InventoryService9 inventoryService;
    private BookingValidator validator;

    public BookingService9(InventoryService9 inventoryService) {
        this.inventoryService = inventoryService;
        this.validator = new BookingValidator(inventoryService);
    }

    public void processBooking(String guestName, String roomType, int numberOfNights) {
        System.out.println("\nProcessing: " + guestName + " -> " + roomType + " for " + numberOfNights + " night(s)");
        try {
            validator.validate(guestName, roomType, numberOfNights);
            inventoryService.decrementAvailability(roomType);
            System.out.println("CONFIRMED: Booking successful for " + guestName + " (" + roomType + ")");
        } catch (InvalidGuestNameException e) {
            System.out.println("VALIDATION ERROR [Guest Name]: " + e.getMessage());
        } catch (InvalidRoomTypeException e) {
            System.out.println("VALIDATION ERROR [Room Type]: " + e.getMessage());
        } catch (InvalidNightsException e) {
            System.out.println("VALIDATION ERROR [Nights]: " + e.getMessage());
        } catch (NoAvailabilityException e) {
            System.out.println("VALIDATION ERROR [Availability]: " + e.getMessage());
        }
    }
}

public class UC9BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC9 - Error Handling & Validation");
        System.out.println("Version : 9.0");
        System.out.println("----------------------------------");

        InventoryService9 inventoryService = new InventoryService9();
        BookingService9 bookingService = new BookingService9(inventoryService);

        inventoryService.displayInventory();

        System.out.println("\n--- Processing Booking Requests ---");

        // Valid bookings
        bookingService.processBooking("Alice", "Single Room", 2);
        bookingService.processBooking("Bob", "Double Room", 3);

        // Invalid - empty guest name
        bookingService.processBooking("", "Single Room", 2);

        // Invalid - wrong room type
        bookingService.processBooking("Charlie", "Penthouse", 2);

        // Invalid - zero nights
        bookingService.processBooking("Diana", "Suite Room", 0);

        // Invalid - no availability (Double Room already booked)
        bookingService.processBooking("Eve", "Double Room", 1);

        // Valid - Suite Room
        bookingService.processBooking("Frank", "Suite Room", 1);

        inventoryService.displayInventory();

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}
