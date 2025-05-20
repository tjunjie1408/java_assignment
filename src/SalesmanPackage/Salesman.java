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
    private String address;

    public Salesman(String id, String username, String password, String email, String phoneNumber) {
        super(id, username, password, email, UserRole.SALESMAN);
        this.salesId = "S" + id;
        this.totalSales = 0;
        this.commissionRate = 0.05;
        this.salesHistory = new ArrayList<>();
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public String getSalesId() { return salesId; }
    public int getTotalSales() { return totalSales; }
    public void incrementSales() { this.totalSales++; }
    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double rate) { this.commissionRate = rate; }
    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    public List<String> getSalesHistory() { return salesHistory; }
    public void addSale(String saleId) { salesHistory.add(saleId); }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toCSV() {
        return String.join(",",
                id, username, password, email, status.toString(), role.toString(),
                salesId, String.valueOf(totalSales), String.valueOf(commissionRate),
                managerId, phoneNumber, address, String.join(";", salesHistory));
    }

    public static Salesman fromCSV(String csv) {
        String[] parts = csv.split(",");
        Salesman salesman = new Salesman(parts[0], parts[1], parts[2], parts[3], parts[4]);
        salesman.salesId = parts[6];
        salesman.totalSales = Integer.parseInt(parts[7]);
        salesman.commissionRate = Double.parseDouble(parts[8]);
        salesman.managerId = parts[9];
        salesman.phoneNumber = parts[10];
        salesman.address = parts[11];
        if (parts.length > 12) {
            salesman.salesHistory = Arrays.asList(parts[12].split(";"));
        }
        return salesman;
    }
}
