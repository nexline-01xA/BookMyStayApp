import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Reservation class
class Reservation8 {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private int numberOfNights;

    public Reservation8(String reservationId, String guestName, String roomType, String roomId, int numberOfNights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.numberOfNights = numberOfNights;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public int getNumberOfNights() { return numberOfNights; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Nights: " + numberOfNights);
    }
}

// Booking History - stores confirmed reservations in order
class BookingHistory {
    private List<Reservation8> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addBooking(Reservation8 reservation) {
        history.add(reservation);
        System.out.println("Booking recorded: " + reservation.getReservationId() + " for " + reservation.getGuestName());
    }

    public List<Reservation8> getHistory() {
        return history;
    }

    public void displayHistory() {
        System.out.println("\n--- Booking History (Insertion Order) ---");
        if (history.isEmpty()) {
            System.out.println("No bookings recorded.");
            return;
        }
        for (Reservation8 reservation : history) {
            reservation.displayReservation();
        }
    }
}

// Booking Report Service - generates reports from history
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void generateSummaryReport() {
        List<Reservation8> history = bookingHistory.getHistory();
        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + history.size());

        Map<String, Integer> roomTypeCount = new HashMap<>();
        int totalNights = 0;

        for (Reservation8 reservation : history) {
            String roomType = reservation.getRoomType();
            roomTypeCount.put(roomType, roomTypeCount.getOrDefault(roomType, 0) + 1);
            totalNights += reservation.getNumberOfNights();
        }

        System.out.println("Total Nights Booked: " + totalNights);
        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " booking(s)");
        }
    }

    public void generateGuestReport(String guestName) {
        System.out.println("\n--- Guest Report for: " + guestName + " ---");
        boolean found = false;
        for (Reservation8 reservation : bookingHistory.getHistory()) {
            if (reservation.getGuestName().equalsIgnoreCase(guestName)) {
                reservation.displayReservation();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No bookings found for " + guestName);
        }
    }
}

public class UC8BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC8 - Booking History & Reporting");
        System.out.println("Version : 8.0");
        System.out.println("----------------------------------");

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        System.out.println("\n--- Recording Confirmed Bookings ---");
        bookingHistory.addBooking(new Reservation8("RES001", "Alice", "Single Room", "S001", 2));
        bookingHistory.addBooking(new Reservation8("RES002", "Bob", "Double Room", "D001", 3));
        bookingHistory.addBooking(new Reservation8("RES003", "Charlie", "Suite Room", "T001", 1));
        bookingHistory.addBooking(new Reservation8("RES004", "Diana", "Single Room", "S002", 4));
        bookingHistory.addBooking(new Reservation8("RES005", "Alice", "Double Room", "D002", 2));

        bookingHistory.displayHistory();
        reportService.generateSummaryReport();
        reportService.generateGuestReport("Alice");

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}