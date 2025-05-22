package UserPackage;

import CustomerPackage.Customer;
import ManagerPackage.Manager;
import SalesmanPackage.Salesman;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected UserStatus status;
    protected UserRole role;
    protected LocalDateTime lastLogin;
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
    public UserRole getRole() { return role; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    public void setStatus(UserStatus status) { this.status = status; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // CSV conversion
    public abstract String toCSV();
    public static User fromCSV(String csv) {
        String[] parts = csv.split(",");
        UserRole role = UserRole.valueOf(parts[5]);
        return switch (role) {
            case MANAGER -> Manager.fromCSV(csv);
            case SALESMAN -> Salesman.fromCSV(csv);
            case CUSTOMER -> Customer.fromCSV(csv);
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}