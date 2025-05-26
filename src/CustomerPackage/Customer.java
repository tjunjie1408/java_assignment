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
        String[] parts = csv.split(",", -1); // -1 保留所有空项
        // parts: [0]=id, [1]=username, [2]=password, [3]=email,
        //        [4]=phone, [5]=status, … （如果还有更多列）
        Customer c = new Customer(
                parts[0],              // id
                parts[1],              // username
                parts[2],              // password
                parts[3],              // email
                parts[4]               // phone number
        );
        // 现在把 status 从 parts[5] 读取
        if (!parts[5].isBlank()) {
            c.setStatus(UserStatus.valueOf(parts[5]));
        }
        // 如果你后面又加了 preferredPaymentMethod、preferredContactMethod、purchaseHistory
        // 就继续读取 parts[6], parts[7], parts[8]...
        return c;
    }
}
