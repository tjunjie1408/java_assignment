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

    @Override
    public String toCSV() {
        return String.join(",",
                id, username, password, email, status.toString(), role.toString(),
                department, position, getPhoneNumber(), officeLocation, employeeId, accessLevel,
                String.join(";", managedSalesmen),
                String.join(";", approvalHistory));
    }

    public static Manager fromCSV(String csv) {
        String[] parts = csv.split(",", 14);
        Manager manager = new Manager(parts[0], parts[1], parts[2], parts[3], parts[8]);
        manager.setStatus(UserStatus.valueOf(parts[4]));
        manager.setDepartment(parts[6]);
        manager.setPosition(parts[7]);
        manager.setOfficeLocation(parts[9]);
        manager.setEmployeeId(parts[10]);
        manager.setAccessLevel(parts[11]);
        if (parts.length > 12 && !parts[12].isEmpty()) {
            manager.managedSalesmen = Arrays.asList(parts[12].split(";"));
        }
        if (parts.length > 13 && !parts[13].isEmpty()) {
            manager.approvalHistory = Arrays.asList(parts[13].split(";"));
        }
        return manager;
    }
}