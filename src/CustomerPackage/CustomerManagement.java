package CustomerPackage;

import SalesmanPackage.Salesman;
import UserPackage.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class CustomerManagement {
    private HashMap<String, Customer> customers;
    private List<UserActivity> activityLog;
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String ACTIVITY_LOG = "customer_activity.log";

    public CustomerManagement() {
        customers = new HashMap<>();
        activityLog = new ArrayList<>();
        loadCustomers();
    }

    // Registration and Profile Management
    public boolean registerCustomer(Customer customer) {
        if (customers.containsKey(customer.getUsername())) {
            return false;
        }
        customers.put(customer.getUsername(), customer);
        logActivity(customer.getUsername(), "REGISTER", "New customer registration");
        saveCustomers();
        sendRegistrationEmail(customer);
        return true;
    }


    public boolean updateProfile(String username, String phoneNumber, String address) {
        Customer customer = customers.get(username);
        if (customer == null) {
            return false;
        }
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        logActivity(username, "PROFILE_UPDATE", "Profile information updated");
        saveCustomers();
        return true;
    }

    // Approval Workflow
    public boolean approveCustomer(String username) {
        Customer customer = customers.get(username);
        if (customer == null) {
            return false;
        }
        customer.setStatus(UserStatus.APPROVED);
        logActivity(username, "APPROVE", "Customer account approved");
        saveCustomers();
        sendApprovalEmail(customer);
        return true;
    }

    public boolean rejectCustomer(String username) {
        Customer customer = customers.get(username);
        if (customer == null) {
            return false;
        }
        customer.setStatus(UserStatus.REJECTED);
        logActivity(username, "REJECT", "Customer account rejected");
        saveCustomers();
        sendRejectionEmail(customer);
        return true;
    }

    // Purchase History
    public boolean addPurchase(String username, String purchaseId) {
        Customer customer = customers.get(username);
        if (customer == null) {
            return false;
        }
        customer.addPurchase(purchaseId);
        logActivity(username, "PURCHASE", "New purchase added: " + purchaseId);
        saveCustomers();
        return true;
    }

    // File Operations
    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = Customer.fromCSV(line);
                customers.put(customer.getUsername(), customer);
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    private void saveCustomers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers.values()) {
                writer.write(customer.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    // Activity Logging
    private void logActivity(String username, String action, String details) {
        UserActivity activity = new UserActivity(username, action, details);
        activityLog.add(activity);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACTIVITY_LOG, true))) {
            writer.write(activity.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging activity: " + e.getMessage());
        }
    }

    // Email Notifications
    private void sendRegistrationEmail(Customer customer) {
        // Simulate sending registration email
        System.out.println("Sending registration email to: " + customer.getEmail());
        System.out.println("Subject: Welcome to Car Sales System");
        System.out.println("Body: Dear " + customer.getUsername() +
                ", thank you for registering. Your account is pending approval.");
    }

    private void sendApprovalEmail(Customer customer) {
        // Simulate sending approval email
        System.out.println("Sending approval email to: " + customer.getEmail());
        System.out.println("Subject: Your Account Has Been Approved");
        System.out.println("Body: Dear " + customer.getUsername() +
                ", your account has been approved. You can now log in.");
    }

    private void sendRejectionEmail(Customer customer) {
        // Simulate sending rejection email
        System.out.println("Sending rejection email to: " + customer.getEmail());
        System.out.println("Subject: Account Registration Status");
        System.out.println("Body: Dear " + customer.getUsername() +
                ", we regret to inform you that your account registration has been rejected.");
    }

    // Search Operations
    public List<Customer> searchByStatus(UserStatus status) {
        return customers.values().stream()
                .filter(c -> c.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Customer> searchByMembershipLevel(String level) {
        return customers.values().stream()
                .filter(c -> c.getMembershipLevel().equals(level))
                .collect(Collectors.toList());
    }

    // Activity History
    public List<UserActivity> getActivityHistory(String username) {
        return activityLog.stream()
                .filter(a -> a.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}
