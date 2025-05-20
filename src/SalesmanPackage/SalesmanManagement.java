package SalesmanPackage;

import UserPackage.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SalesmanManagement {
    private List<Salesman> salesmen;
    private List<UserActivity> activityLog;
    private static final String SALESMEN_FILE = "salesmen.txt";
    private static final String ACTIVITY_LOG = "salesman_activity.log";

    public SalesmanManagement() {
        salesmen = new ArrayList<>();
        activityLog = new ArrayList<>();
        loadSalesmen();
    }

    public boolean addSalesman(Salesman salesman) {
        for (Salesman s : salesmen) {
            if (s.getUsername().equals(salesman.getUsername())) {
                return false;
            }
        }
        salesmen.add(salesman);
        logActivity(salesman.getUsername(), "ADD", "New salesman added");
        saveSalesmen();
        return true;
    }

    public Salesman getSalesman(String username) {
        for (Salesman s : salesmen) {
            if (s.getUsername().equals(username)) {
                return s;
            }
        }
        return null;
    }

    public boolean updateSalesman(String username, Salesman updatedSalesman) {
        for (int i = 0; i < salesmen.size(); i++) {
            if (salesmen.get(i).getUsername().equals(username)) {
                salesmen.set(i, updatedSalesman);
                logActivity(username, "UPDATE", "Salesman information updated");
                saveSalesmen();
                return true;
            }
        }
        return false;
    }

    public boolean deleteSalesman(String username) {
        for (int i = 0; i < salesmen.size(); i++) {
            if (salesmen.get(i).getUsername().equals(username)) {
                salesmen.remove(i);
                logActivity(username, "DELETE", "Salesman deleted");
                saveSalesmen();
                return true;
            }
        }
        return false;
    }

    public boolean updateProfile(String username, String phoneNumber, String address) {
        Salesman salesman = getSalesman(username);
        if (salesman == null) {
            return false;
        }
        salesman.setPhoneNumber(phoneNumber);
        salesman.setAddress(address);
        logActivity(username, "PROFILE_UPDATE", "Profile information updated");
        saveSalesmen();
        return true;
    }

    public boolean addSale(String username, String saleId) {
        Salesman salesman = getSalesman(username);
        if (salesman == null) {
            return false;
        }
        salesman.addSale(saleId);
        salesman.incrementSales();
        logActivity(username, "SALE_ADDED", "New sale added: " + saleId);
        saveSalesmen();
        return true;
    }

    private void loadSalesmen() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SALESMEN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Salesman salesman = Salesman.fromCSV(line);
                salesmen.add(salesman);
            }
        } catch (IOException e) {
            System.out.println("Error loading salesmen: " + e.getMessage());
        }
    }

    private void saveSalesmen() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALESMEN_FILE))) {
            for (Salesman salesman : salesmen) {
                writer.write(salesman.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving salesmen: " + e.getMessage());
        }
    }

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

    public List<Salesman> searchByManager(String managerId) {
        List<Salesman> result = new ArrayList<>();
        for (Salesman s : salesmen) {
            if (s.getManagerId().equals(managerId)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Salesman> searchBySalesRange(int minSales, int maxSales) {
        List<Salesman> result = new ArrayList<>();
        for (Salesman s : salesmen) {
            if (s.getTotalSales() >= minSales && s.getTotalSales() <= maxSales) {
                result.add(s);
            }
        }
        return result;
    }

    public List<UserActivity> getActivityHistory(String username) {
        List<UserActivity> result = new ArrayList<>();
        for (UserActivity a : activityLog) {
            if (a.getUsername().equals(username)) {
                result.add(a);
            }
        }
        return result;
    }
}
