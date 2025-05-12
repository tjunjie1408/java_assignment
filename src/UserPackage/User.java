package UserPackage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected String email;
    protected UserStatus status;
    protected UserRole role;
    protected LocalDateTime lastLogin;
    protected LocalDateTime createdAt;

    public User(String id, String username, String password, String email, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
        this.role = role;
        this.status = UserStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    protected String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Getters and setters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public UserStatus getStatus() { return status; }
    public UserRole getRole() { return role; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(UserStatus status) { this.status = status; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // CSV conversion
    public abstract String toCSV();
    public static User fromCSV(String csv) {
        String[] parts = csv.split(",");
        UserRole role = UserRole.valueOf(parts[5]);
        switch (role) {
            case MANAGER:
                return Manager.fromCSV(csv);
            case SALESMAN:
                return Salesman.fromCSV(csv);
            case CUSTOMER:
                return CustomerPackage.fromCSV(csv);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}