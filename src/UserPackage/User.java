package UserPackage;

import CustomerPackage.Customer;
import ManagerPackage.Manager;
import SalesmanPackage.Salesman;
import java.time.LocalDateTime;

public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected UserStatus status;
    protected UserRole role;
    protected LocalDateTime createdAt;

    public User(String username, String password, String email, String phoneNumber, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = UserStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }


    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id;}
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }
    public UserStatus getStatus() { return status; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setStatus(UserStatus status) { this.status = status; }

    public abstract String toCSV();
    public static User fromCSV(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Insufficient CSV fields");
        }
        UserRole role = UserRole.valueOf(parts[6]);
        return switch (role) {
            case CUSTOMER -> Customer.fromCSV(csv);
            case MANAGER -> Manager.fromCSV(csv);
            case SALESMAN -> Salesman.fromCSV(csv);
        };
    }
}