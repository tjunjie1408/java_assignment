package ManagerPackage;

import UserPackage.*;
import java.util.*;

public class Manager extends User {
    private String managerId;
    private String department;
    private String position;
    private List<String> managedSalesmen;
    private String officeLocation;
    private String employeeId;
    private List<String> approvalHistory;
    private String accessLevel; // ADMIN, SENIOR, JUNIOR

    // Constructor for registration (no id provided)
    public Manager(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber, UserRole.MANAGER);
        this.managedSalesmen = new ArrayList<>();
        this.approvalHistory = new ArrayList<>();
        this.accessLevel = "JUNIOR";
    }

    // Constructor for loading from file (with id)
    public Manager(String id, String username, String password, String email, String phoneNumber) {
        this(username, password, email, phoneNumber);
        this.id = id;
        this.managerId = "M-" + id;
    }

    // Getters and setters
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public List<String> getManagedSalesmen() { return managedSalesmen; }
    public void addManagedSalesman(String salesmanId) { managedSalesmen.add(salesmanId); }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }
    public void addApproval(String approvalId) { approvalHistory.add(approvalId); }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public void setRole(UserRole role) { this.role = role; }

    @Override
    public String toCSV() {
        return String.join(",",
                id, username,
                password,
                email,
                getPhoneNumber(),
                status.toString(),
                role.toString(),
                accessLevel);
    }

    public static Manager fromCSV(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid manager CSV: " + csv);
        }
        String id       = parts[0].trim();
        String username = parts[1].trim();
        String password = parts[2].trim();
        String email    = parts[3].trim();
        String phone    = parts[4].trim();
        String statusStr= parts[5].trim();
        String roleStr  = parts[6].trim();

        Manager m = new Manager(id, username, password, email, phone);
        if (!statusStr.isEmpty()) {
            m.setStatus(UserStatus.valueOf(statusStr));
        }
        if (!roleStr.isEmpty()) {
            m.setRole(UserRole.valueOf(roleStr));
        }
        return m;
    }
}