package CustomerPackage;

import UserPackage.*;

import java.util.*;

public class Customer extends User {
    private String customerId;
    private final List<String> purchaseHistory = new ArrayList<>();
    public Customer(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber, UserRole.CUSTOMER);
        this.customerId = null;
    }
    public Customer(String id, String username, String password, String email, String phoneNumber) {
        this(username, password, email, phoneNumber);
        this.id = id;
        this.customerId = "C-" + id;
    }

    // Getters and setters
    public void setId(String id) {
        this.id = id;
        this.customerId = "C-" + id;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toCSV() {
        return String.join(",",
                getId(),
                getUsername(),
                getPassword(),
                getEmail(),
                getPhoneNumber(),
                getStatus().name()
        );
    }

    public static Customer fromCSV(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid customer CSV: " + csv);
        }
        String id        = parts[0].trim();
        String username  = parts[1].trim();
        String password  = parts[2].trim();
        String email     = parts[3].trim();
        String phone     = parts[4].trim();
        String statusStr = parts[5].trim();
        Customer c = new Customer(id, username, password, email, phone);
        if (!statusStr.isEmpty()) {
            c.setStatus(UserStatus.valueOf(statusStr));
        }
        c.setRole(UserRole.CUSTOMER);
        return c;
    }
}
