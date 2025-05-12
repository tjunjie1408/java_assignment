package ManagerPackage;

import UserPackage.*;

import java.util.*;

public class Manager extends User {
    private String managerId;
    private String department;
    private String position;
    private List<String> managedSalesmen;
    private String phoneNumber;
    private String officeLocation;
    private String employeeId;
    private List<String> approvalHistory;
    private String accessLevel; // ADMIN, SENIOR, JUNIOR

    public Manager(String id, String username, String password, String email) {
        super(id, username, password, email, UserRole.MANAGER);
        this.managerId = "M" + id;
        this.managedSalesmen = new ArrayList<>();
        this.approvalHistory = new ArrayList<>();
        this.accessLevel = "JUNIOR"; // Default access level
    }

    // Getters and setters
    public String getManagerId() { return managerId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public List<String> getManagedSalesmen() { return managedSalesmen; }
    public void addManagedSalesman(String salesmanId) { managedSalesmen.add(salesmanId); }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
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
                managerId, department, position, phoneNumber, officeLocation,
                employeeId, accessLevel, String.join(";", managedSalesmen),
                String.join(";", approvalHistory));
    }

    public static Manager fromCSV(String csv) {
        String[] parts = csv.split(",");
        Manager manager = new Manager(parts[0], parts[1], parts[2], parts[3]);
        manager.managerId = parts[6];
        manager.department = parts[7];
        manager.position = parts[8];
        manager.phoneNumber = parts[9];
        manager.officeLocation = parts[10];
        manager.employeeId = parts[11];
        manager.accessLevel = parts[12];
        if (parts.length > 13) {
            manager.managedSalesmen = Arrays.asList(parts[13].split(";"));
        }
        if (parts.length > 14) {
            manager.approvalHistory = Arrays.asList(parts[14].split(";"));
        }
        return manager;
    }
}