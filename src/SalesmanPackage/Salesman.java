package SalesmanPackage;
import UserPackage.*;
import java.util.*;

public class Salesman extends User {
    private String salesId;
    private int totalSales;
    private double commissionRate;
    private String managerId;
    private List<String> salesHistory;
    private String phoneNumber;

    public Salesman(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber,UserRole.SALESMAN);
        this.totalSales = 0;
        this.commissionRate = 0.05;
        this.salesHistory = new ArrayList<>();
        this.phoneNumber = phoneNumber;
    }
    public Salesman(String id, String username, String password, String email, String phoneNumber) {
        this(username, password, email, phoneNumber);
        this.id = id;
        this.managerId = "S-" + id;
    }

    // Getters and setters
    public void setId(String id) {
        this.id = id;
        this.salesId = "S-" + id;
    }
    public int getTotalSales() { return totalSales; }
    public void incrementSales() { this.totalSales++; }
    public String getManagerId() { return managerId; }
    public void addSale(String saleId) { salesHistory.add(saleId); }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(UserRole role) { this.role = role; }

    @Override
    public String toCSV() {
        return String.join(",",
                id, username, password, email, getPhoneNumber(),status.toString(), role.toString()
                );
    }

    public static Salesman fromCSV(String csv) {
        String[] parts = csv.split(",", 14);
        Salesman s = new Salesman(
                parts[0].trim(),  // id
                parts[1].trim(),  // username
                parts[2].trim(),  // password
                parts[3].trim(),  // email
                parts[4].trim()   // phone
        );
        String statusStr = parts.length > 5 ? parts[5].trim() : "";
        if (!statusStr.isEmpty()) {
            s.setStatus(UserStatus.valueOf(statusStr));
        }
        String roleStr = parts.length > 6 ? parts[6].trim() : "";
        if (!roleStr.isEmpty()) {
            s.setRole(UserRole.valueOf(roleStr));
        }
        return s;
    }
}
