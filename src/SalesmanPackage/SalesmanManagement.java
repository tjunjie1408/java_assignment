package SalesmanPackage;

import UserPackage.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class SalesmanManagement {
    private HashMap<String, Salesman> salesmen;
    private List<UserActivity> activityLog;
    private static final String SALESMEN_FILE = "salesmen.txt";
    private static final String ACTIVITY_LOG = "salesman_activity.log";

    public SalesmanManagement() {
        salesmen = new HashMap<>();
        activityLog = new ArrayList<>();
        loadSalesmen();
    }

    // CRUD Operations
    public boolean addSalesman(Salesman salesman) {
        if (salesmen.containsKey(salesman.getUsername())) {
            return false;
        }
        salesmen.put(salesman.getUsername(), salesman);
        logActivity(salesman.getUsername(), "ADD", "New salesman added");
        saveSalesmen();
        return true;
    }

    public Salesman getSalesman(String username) {
        return salesmen.get(username);
    }

    public boolean updateSalesman(String username, Salesman updatedSalesman) {
        if (!salesmen.containsKey(username)) {
            return false;
        }
        salesmen.put(username, updatedSalesman);
        logActivity(username, "UPDATE", "Salesman information updated");
        saveSalesmen();
        return true;
    }

    public boolean deleteSalesman(String username) {
        if (!salesmen.containsKey(username)) {
            return false;
        }
        salesmen.remove(username);
        logActivity(username, "DELETE", "Salesman deleted");
        saveSalesmen();
        return true;
    }

    // Profile Management
    public boolean updateProfile(String username, String phoneNumber, String address) {
        Salesman salesman = salesmen.get(username);
        if (salesman == null) {
            return false;
        }
        salesman.setPhoneNumber(phoneNumber);
        salesman.setAddress(address);
        logActivity(username, "PROFILE_UPDATE", "Profile information updated");
        saveSalesmen();
        return true;
    }

    // Sales Management
    public boolean addSale(String username, String saleId) {
        Salesman salesman = salesmen.get(username);
        if (salesman == null) {
            return false;
        }
        salesman.addSale(saleId);
        salesman.incrementSales();
        logActivity(username, "SALE_ADDED", "New sale added: " + saleId);
        saveSalesmen();
        return true;
    }

    // File Operations
    private void loadSalesmen() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SALESMEN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Salesman salesman = Salesman.fromCSV(line);
                salesmen.put(salesman.getUsername(), salesman);
            }
        } catch (IOException e) {
            System.out.println("Error loading salesmen: " + e.getMessage());
        }
    }

    private void saveSalesmen() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALESMEN_FILE))) {
            for (Salesman salesman : salesmen.values()) {
                writer.write(salesman.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving salesmen: " + e.getMessage());
        }
    }

    // Activity Logging
    private void logActivity(String username, String action, String details) {
        UserActivity activity = new UserActivity(username, action, details);
        activityLog.add(activity);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACTIVITY_LOG, true))) {
            writer.write(activity.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging activity: " + e.getMessage());
        }
    }

    // Search Operations
    public List<Salesman> searchByManager(String managerId) {
        return salesmen.values().stream()
                .filter(s -> s.getManagerId().equals(managerId))
                .collect(Collectors.toList());
    }

    public List<Salesman> searchBySalesRange(int minSales, int maxSales) {
        return salesmen.values().stream()
                .filter(s -> s.getTotalSales() >= minSales && s.getTotalSales() <= maxSales)
                .collect(Collectors.toList());
    }

    // Activity History
    public List<UserActivity> getActivityHistory(String username) {
        return activityLog.stream()
                .filter(a -> a.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}
