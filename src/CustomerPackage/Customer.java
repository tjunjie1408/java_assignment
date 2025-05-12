package CustomerPackage;

import UserPackage.*;

import java.util.*;

public class Customer extends User {
    private String customerId;
    private String phoneNumber;
    private String address;
    private String membershipLevel; // BASIC, SILVER, GOLD, PLATINUM
    private List<String> purchaseHistory;
    private String preferredPaymentMethod;
    private String preferredContactMethod;

    public Customer(String id, String username, String password, String email) {
        super(id, username, password, email, UserRole.CUSTOMER);
        this.customerId = "C" + id;
        this.membershipLevel = "BASIC";
        this.purchaseHistory = new ArrayList<>();
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String level) { this.membershipLevel = level; }
    public List<String> getPurchaseHistory() { return purchaseHistory; }
    public void addPurchase(String purchaseId) { purchaseHistory.add(purchaseId); }
    public String getPreferredPaymentMethod() { return preferredPaymentMethod; }
    public void setPreferredPaymentMethod(String method) { this.preferredPaymentMethod = method; }
    public String getPreferredContactMethod() { return preferredContactMethod; }
    public void setPreferredContactMethod(String method) { this.preferredContactMethod = method; }

    @Override
    public String toCSV() {
        return String.join(",",
                id, username, password, email, status.toString(), role.toString(),
                customerId, phoneNumber, address, membershipLevel,
                preferredPaymentMethod, preferredContactMethod,
                String.join(";", purchaseHistory));
    }

    public static Customer fromCSV(String csv) {
        String[] parts = csv.split(",");
        Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3]);
        customer.customerId = parts[6];
        customer.phoneNumber = parts[7];
        customer.address = parts[8];
        customer.membershipLevel = parts[9];
        customer.preferredPaymentMethod = parts[10];
        customer.preferredContactMethod = parts[11];
        if (parts.length > 12) {
            customer.purchaseHistory = Arrays.asList(parts[12].split(";"));
        }
        return customer;
    }
}
