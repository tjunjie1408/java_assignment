package SalesmanPackage;

import UserPackage.*;
import CustomerPackage.*;
import CarPackage.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SalesmanManagement {
    private List<Salesman> salesmen;
    private List<UserActivity> activityLog;
    private List<SalesRecord> SalesRecords;
    private static final String SALESMEN_FILE = "salesmen.txt";
    private static final String ACTIVITY_LOG = "salesman_activity.log";
    private static final String SALES_RECORDS_FILE = "sales_records.txt";
    private CustomerManagement customerManagement; // Dependency to access orders
    private CarManagement carManagement; // Assumed dependency
    private String email;

    public SalesmanManagement() {
        salesmen = new ArrayList<>();
        activityLog = new ArrayList<>();
        SalesRecords = new ArrayList<>();
        loadSalesmen();
        loadSalesRecords();
    }
    public void setCustomerManagement(CustomerManagement customerManagement) {
        this.customerManagement = customerManagement;
    }

    public void setCarManagement(CarManagement carManagement) {
        this.carManagement = carManagement;
    }
    public List<Salesman> getSalesmen() {
        return salesmen;
    }

    public Salesman getSalesmanById(String id) {
        for (Salesman s : salesmen) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public Salesman findById(String id) {
        for (Salesman s : salesmen) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
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

    public boolean updateSalesman(Salesman updatedSalesman) {
        for (int i = 0; i < salesmen.size(); i++) {
            if (salesmen.get(i).getId().equals(updatedSalesman.getId())) {
                salesmen.set(i, updatedSalesman);
                logActivity(updatedSalesman.getUsername(), "UPDATE", "Salesman information updated");
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
        salesman.setEmail(email);
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
    public List<SalesRecord> getSalesRecordsBySalesman(String salesmanId) {
        return SalesRecords.stream()
                .filter(r -> r.getSalesmanId().equals(salesmanId))
                .collect(Collectors.toList());
    }

    // Confirm an order
    public void confirmOrder(String orderId, String salesmanId) {
        Order order = customerManagement.findOrder(orderId);
        if (order == null || !"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Cannot confirm: order not in PENDING.");
        }
        order.setStatus("CONFIRMED");
        Car car = carManagement.getCar(order.getCarId());
        if (car != null) {
            car.setStatus("BOOKED");
            carManagement.updateCar(car.getCarId(), car);
        }
        customerManagement.saveOrders();
        logActivity(salesmanId, "CONFIRM_ORDER", "Order confirmed: " + orderId);
    }

    // Update car status
    public void updateCarStatus(String carId, String newStatus) {
        Car car = carManagement.getCar(carId);
        if (car == null) return;
        car.setStatus(newStatus);
        carManagement.updateCar(carId, car);
        List<Order> orders = customerManagement.getOrdersByCarId(carId);
        for (Order order : orders) {
            switch (newStatus) {
                case "BOOKED"    -> order.setStatus("CONFIRMED");
                case "PAID"      -> order.setStatus("PAID");
                case "SOLD"      -> order.setStatus("COMPLETED");
                case "CANCELLED" -> order.setStatus("CANCELLED");
                default          -> order.setStatus("PENDING");
            }
        }
        customerManagement.saveOrders();
        logActivity("SYSTEM", "UPDATE_CAR_STATUS",
                "Car status updated: " + carId + " → " + newStatus);
    }

    // Record a sale
    public void recordSale(String orderId, String salesmanId, String comment) {
        Order order = customerManagement.findOrder(orderId);
        if (order == null || !"PAID".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot record sale for unpaid order");
        }
        String recordId = UUID.randomUUID().toString();
        SalesRecord record = new SalesRecord(recordId, orderId, salesmanId, comment);
        appendToFile(SALES_RECORDS_FILE, record.toCSV());
        SalesRecords.add(record);
        addSale(getSalesmanById(salesmanId).getUsername(), recordId);
        logActivity(salesmanId, "RECORD_SALE", "Sale recorded for order: " + orderId);
    }

    private void loadSalesmen() {
        File file = new File(SALESMEN_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("A new salesmen.txt file has been created.");
            } catch (IOException e) {
                System.out.println("Error creating salesmen.txt file: " + e.getMessage());
            }
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(SALESMEN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Salesman salesman = Salesman.fromCSV(line);
                if (salesman != null) {
                    salesmen.add(salesman);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading salesmen.txt file: " + e.getMessage());
        }
    }

    public void saveSalesmen() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALESMEN_FILE))) {
            for (Salesman salesman : salesmen) {
                writer.write(salesman.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving salesmen.txt file: " + e.getMessage());
        }
    }

    private void appendToFile(String fileName, String data) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(fileName, true))) {
            w.write(data);
            w.newLine();
        } catch (IOException e) { System.out.println("Error appending to " + fileName + ": " + e.getMessage()); }
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

    private void loadSalesRecords() {
        File file = new File(SALES_RECORDS_FILE);
        if (!file.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                SalesRecord rec = SalesRecord.fromCSV(line);
                if (rec != null) SalesRecords.add(rec);
            }
        } catch (IOException e) {
            System.out.println("Error loading sales records: " + e.getMessage());
        }
    }
}