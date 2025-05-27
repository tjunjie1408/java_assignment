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
        this.accessLevel = "JUNIOR"; // Default access level
    }

    // Constructor for loading from file (with id)
    public Manager(String id, String username, String password, String email, String phoneNumber) {
        this(username, password, email, phoneNumber);
        this.id = id;
        this.managerId = "M-" + id;
    }

    // Getters and setters
    public String getManagerId() { return managerId; }
    public void setId(String id) {
        this.id = id;
        this.managerId = "M-" + id;
    }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public List<String> getManagedSalesmen() { return managedSalesmen; }
    public void addManagedSalesman(String salesmanId) { managedSalesmen.add(salesmanId); }
    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public List<String> getApprovalHistory() { return approvalHistory; }
    public void addApproval(String approvalId) { approvalHistory.add(approvalId); }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public void setRole(UserRole role) { this.role = role; }

    @Override
    public String toCSV() {
        return String.join(",",
                id, username, password, email, status.toString(), role.toString(),
                department, position, getPhoneNumber(), officeLocation, employeeId, accessLevel,
                String.join(";", managedSalesmen),
                String.join(";", approvalHistory));
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