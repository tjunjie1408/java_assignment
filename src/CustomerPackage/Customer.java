package CustomerPackage;

import UserPackage.*;

import java.util.*;
import java.util.stream.Collectors;

public class Customer extends User {
    private String customerId;
    private List<String> purchaseHistory = new ArrayList<>();
    private String preferredPaymentMethod;
    private String preferredContactMethod;

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

    public List<String> getPurchaseHistory() {
        return Collections.unmodifiableList(purchaseHistory);
    }

    public void addPurchase(String purchaseId) {
        if (purchaseId != null && !purchaseId.isEmpty()) {
            purchaseHistory.add(purchaseId);
        }
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }

    public void setPreferredPaymentMethod(String method) {
        this.preferredPaymentMethod = method != null ? method : "";
    }

    public String getPreferredContactMethod() {
        return preferredContactMethod;
    }

    public void setPreferredContactMethod(String method) {
        this.preferredContactMethod = method != null ? method : "";
    }

    @Override
    public String toCSV() {
        return String.join(",",
                getId(),
                getUsername(),
                getPassword(),
                getEmail(),
                getPhoneNumber(),
                getStatus().name(),
                getRole().name()
        );
    }

    public static Customer fromCSV(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid customer CSV: " + csv);
        }
        // parts: 0=id,1=user,2=pass,3=email,4=phone,5=status,6=role
        Customer c = new Customer(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim()
        );
        String statusStr = parts[5].trim();
        if (!statusStr.isEmpty()) c.setStatus(UserStatus.valueOf(statusStr));
        String roleStr = parts[6].trim();
        if (!roleStr.isEmpty()) c.setRole(UserRole.valueOf(roleStr));
        return c;
    }
}
