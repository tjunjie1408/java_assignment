package CustomerPackage;

import UserPackage.*;

import java.util.*;

public class Customer extends User {
    private String customerId;
    private String phoneNumber;
    private String email;
    private String membershipLevel; // BASIC, SILVER, GOLD, PLATINUM
    private List<String> purchaseHistory;
    private String preferredPaymentMethod;
    private String preferredContactMethod;

    public Customer(String username, String password, String email, String phoneNumber) {
        super(UUID.randomUUID().toString(), username, password, email, phoneNumber, UserRole.CUSTOMER);
        setStatus(UserStatus.PENDING);
    }


    // Getters and setters
    public String getCustomerId() { return "C-" + getId(); }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    }
    public void setAddress(String email ) { this.email = email; }
    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }
    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String level) {
        this.membershipLevel = level != null ? level : MembershipLevel.BASIC.name();
    }
    public List<String> getPurchaseHistory() {
        return Collections.unmodifiableList(purchaseHistory);
    }
    public void addPurchase(String purchaseId) {
        if (purchaseId != null && !purchaseId.isEmpty()) {
            purchaseHistory.add(purchaseId);
        }
    }
    public String getPreferredPaymentMethod() { return preferredPaymentMethod; }
    public void setPreferredPaymentMethod(String method) {
        this.preferredPaymentMethod = method != null ? method : "";
    }

    public String getPreferredContactMethod() { return preferredContactMethod; }
    public void setPreferredContactMethod(String method) {
        this.preferredContactMethod = method != null ? method : "";
    }

    @Override
    public String toCSV() {
        return String.join(",",
                username, password, email, status.toString(), role.toString(),
                customerId, phoneNumber,membershipLevel,
                preferredPaymentMethod, preferredContactMethod,
                String.join(";", purchaseHistory));
    }

    public static Customer fromCSV(String csv) {
        String[] parts = csv.split(",");
        Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3]);
        customer.customerId = parts[6];
        customer.phoneNumber = parts[7];
        customer.email = parts[8];
        customer.membershipLevel = parts[9];
        customer.preferredPaymentMethod = parts[10];
        customer.preferredContactMethod = parts[11];
        if (parts.length > 12) {
            customer.purchaseHistory = Arrays.asList(parts[12].split(";"));
        }
        return customer;
    }
}
