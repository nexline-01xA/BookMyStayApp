import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// AddOnService class
class AddOnService {
    private String serviceName;
    private double serviceCost;

    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() { return serviceName; }
    public double getServiceCost() { return serviceCost; }

    public void displayService() {
        System.out.println("  Service: " + serviceName + " | Cost: Rs." + serviceCost);
    }
}

// AddOnServiceManager - manages reservation to services mapping
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("Service added to " + reservationId + ": " + service.getServiceName());
    }

    // Calculate total cost for reservation
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        double total = 0;
        for (AddOnService service : services) {
            total += service.getServiceCost();
        }
        return total;
    }

    // Display services for reservation
    public void displayServices(String reservationId) {
        System.out.println("\n--- Add-On Services for " + reservationId + " ---");
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }
        for (AddOnService service : services) {
            service.displayService();
        }
        System.out.println("Total Add-On Cost: Rs." + calculateTotalCost(reservationId));
    }
}

public class UC7BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Welcome to Book My Stay App   ");
        System.out.println("==================================");
        System.out.println("UC7 - Add-On Service Selection");
        System.out.println("Version : 7.0");
        System.out.println("----------------------------------");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define available services
        AddOnService breakfast = new AddOnService("Breakfast", 299.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 599.0);
        AddOnService spa = new AddOnService("Spa", 999.0);
        AddOnService laundry = new AddOnService("Laundry", 199.0);

        System.out.println("\n--- Guest Alice selecting services for RES001 ---");
        serviceManager.addService("RES001", breakfast);
        serviceManager.addService("RES001", airportPickup);
        serviceManager.addService("RES001", spa);

        System.out.println("\n--- Guest Bob selecting services for RES002 ---");
        serviceManager.addService("RES002", breakfast);
        serviceManager.addService("RES002", laundry);

        // Display services and costs
        serviceManager.displayServices("RES001");
        serviceManager.displayServices("RES002");

        System.out.println("\n==================================");
        System.out.println("Application Terminated Successfully.");
        System.out.println("==================================");
    }
}