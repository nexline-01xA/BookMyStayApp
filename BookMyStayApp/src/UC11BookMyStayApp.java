import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

// Reservation class
class Reservation11 {
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public Reservation11(String guestName, String roomType, int numberOfNights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNumberOfNights() { return numberOfNights; }
}

// Thread-safe Inventory Service
class InventoryService11 {
    private HashMap<String, Integer> inventory;

    public InventoryService11() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public synchronized boolean decrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + ": " + inventory.get(roomType) + " available");
        }
    }
}

// Thread-safe Booking Service
class ConcurrentBookingService {
    private InventoryService11 inventoryService;
    private Set<String> allRoomIds;
    private HashMap<String, Set<String>> allocatedRooms;
    private int roomCounter;

    public ConcurrentBookingService(InventoryService11 inventoryService) {
        this.inventoryService = inventoryService;
        allRoomIds = new HashSet<>();
        allocatedRooms = new HashMap<>();
        roomCounter = 1;
    }

    private synchronized String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 1).toUpperCase();
        String roomId = prefix + String.format("%03d", roomCounter++);
        while (allRoomIds.contains(roomId)) {
            roomId = prefix + String.format("%03d", roomCounter++);
        }
        return roomId;
    }

    public synchronized void processBooking(Reservation11 reservation) {
        String roomType = reservation.getRoomType();
        String guestName = reservation.getGuestName();
        String threadName = Thread.currentThread().getName();

        System.out.println("[" + threadName + "] Processing: " + guestName + " -> " + roomType);

        if (!inventoryService.decrementAvailability(roomType)) {
            System.out.println("[" + threadName + "] FAILED: No availability for " +
                    roomType + " (Guest: " + guestName + ")");
            return;
        }

        String roomId = generateRoomId(roomType);
        allRoomIds.add(roomId);
        allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        System.out.println("[" + threadName + "] CONFIRMED: " + guestName +
                " assigned " + roomId + " (" + roomType + ") for " +
                reservation.getNumberOfNights() + " nights");
    }

    public synchronized void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");
        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + ": " + allocatedRooms.get(roomType));
        }
    }
}

// Booking Thread
class BookingThread extends Thread {
    private ConcurrentBookingService bookingService;
    private Queue<Reservation11> requestQueue;

    public BookingThread(String name, ConcurrentBookingService bookingService,
                         Queue<Reservation11> requestQueue) {
        super(name);
        this.bookingService = bookingService;
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (true) {
            Reservation11 reservation;
            synchronized (requestQueue) {
                if (requestQueue.isEmpty()) break;
                reservation = requestQueue.poll();
            }
            bookingService.processBooking(reservation);
        }
    }
}

public class UC11BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC11 - Concurrent Booking Simulation");
        System.out.println("Version : 11.0");
        System.out.println("----------------------------------");

        InventoryService11 inventoryService = new InventoryService11();
        ConcurrentBookingService bookingService = new ConcurrentBookingService(inventoryService);

        // Shared booking queue
        Queue<Reservation11> requestQueue = new LinkedList<>();
        requestQueue.add(new Reservation11("Alice",   "Single Room", 2));
        requestQueue.add(new Reservation11("Bob",     "Double Room", 3));
        requestQueue.add(new Reservation11("Charlie", "Suite Room",  1));
        requestQueue.add(new Reservation11("Diana",   "Single Room", 4));
        requestQueue.add(new Reservation11("Eve",     "Double Room", 2));
        requestQueue.add(new Reservation11("Frank",   "Single Room", 1));
        requestQueue.add(new Reservation11("Grace",   "Single Room", 3));

        inventoryService.displayInventory();

        System.out.println("\n--- Launching Concurrent Booking Threads ---");

        // Create and start threads
        BookingThread t1 = new BookingThread("Thread-1", bookingService, requestQueue);
        BookingThread t2 = new BookingThread("Thread-2", bookingService, requestQueue);
        BookingThread t3 = new BookingThread("Thread-3", bookingService, requestQueue);

        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();

        bookingService.displayAllocations();
        inventoryService.displayInventory();

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}