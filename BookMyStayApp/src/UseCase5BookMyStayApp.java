import java.util.LinkedList;
import java.util.Queue;

// Reservation class - represents a guest's booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public Reservation(String guestName, String roomType, int numberOfNights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNumberOfNights() { return numberOfNights; }

    public void displayReservation() {
        System.out.println("  Guest: " + guestName + " | Room: " + roomType + " | Nights: " + numberOfNights);
    }
}

// Booking Request Queue - manages incoming requests in FIFO order
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request added: " + reservation.getGuestName() + " -> " + reservation.getRoomType());
    }

    // Display all queued requests
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO) ---");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }
        int position = 1;
        for (Reservation r : requestQueue) {
            System.out.print("Position " + position + ": ");
            r.displayReservation();
            position++;
        }
    }

    public int getQueueSize() { return requestQueue.size(); }
}

public class UseCase5BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC5 - Booking Request (First-Come-First-Served)");
        System.out.println("Version : 5.0");
        System.out.println("----------------------------------");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        System.out.println("\n--- Guests Submitting Booking Requests ---");

        // Guests submit requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room", 2));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room", 3));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room", 1));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room", 4));

        // Display queue
        bookingQueue.displayQueue();

        System.out.println("\nTotal Pending Requests: " + bookingQueue.getQueueSize());

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}
