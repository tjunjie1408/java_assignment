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
        // 初始化其他默认值
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
                getStatus().name(),
                getRole().name(),
                getPhoneNumber(),
                preferredPaymentMethod,
                preferredContactMethod,
                purchaseHistory.stream()
                        .filter(Objects::nonNull)
                        .map(s -> s.replace(";", "\\;"))
                        .collect(Collectors.joining(";"))
        );
    }

    public static Customer fromCSV(String csv) {
        csv = csv.replaceAll(",+$", "");
        String[] parts = csv.split(",", 10);
        String[] cols = new String[10];
        for (int i = 0; i < 10; i++) {
            cols[i] = i < parts.length ? parts[i] : "";
        }
        Customer c = new Customer(cols[0], cols[1], cols[2], cols[3], cols[6]);
        if (!cols[4].isEmpty()) c.setStatus(UserStatus.valueOf(cols[4]));
        c.setPreferredPaymentMethod(cols[7]);
        c.setPreferredContactMethod(cols[8]);
        if (!cols[9].isEmpty()) {
            for (String pid : cols[9].split(";")) {
                c.addPurchase(pid.replace("\\;", ";"));
            }
        }
        return c;
    }
}
