package CustomerPackage;

import UserPackage.*;

import java.io.*;
import java.util.*;

public class CustomerManagement {
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String ACTIVITY_LOG = "customer_activity.log";
    private static final int ID_START = 1;
    private final List<Customer> customers = new ArrayList<>();

    public CustomerManagement() {
        loadCustomers();
    }

    // Register new customer: ensure unique username, email, phone
    public void registerCustomer(Customer c) {
        for (Customer ex : customers) {
            if (ex.getUsername().equals(c.getUsername()))
                throw new IllegalArgumentException("Username exists");
            if (ex.getEmail().equalsIgnoreCase(c.getEmail()))
                throw new IllegalArgumentException("Email exists");
            if (!c.getPhoneNumber().isEmpty() && ex.getPhoneNumber().equals(c.getPhoneNumber()))
                throw new IllegalArgumentException("Phone exists");
        }
        c.setId(String.valueOf(ID_START + customers.size()));
        c.setStatus(UserStatus.PENDING);
        customers.add(c);
        saveAll();
        log("REGISTER", c.getUsername());
    }

    public void approveCustomer(String username) {
        Customer c = find(username);
        c.setStatus(UserStatus.APPROVED);
        saveAll();
        log("APPROVE", username);
    }

    public void rejectCustomer(String username) {
        Customer c = find(username);
        c.setStatus(UserStatus.REJECTED);
        saveAll();
        log("REJECT", username);
    }

    public void updateProfile(String username, String phone, String email) {
        Customer c = find(username);
        c.setPhoneNumber(phone);
        c.setEmail(email)  ;
        saveAll();
        log("UPDATE", username);
    }

    public Customer login(String username, String password) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                if (c.getStatus() == UserStatus.APPROVED) {
                    return c;
                } else {
                    throw new IllegalStateException("Account not yet approved");
                }
            }
        }
        throw new NoSuchElementException("Invalid username or password");
    }

    private Customer find(String username) {
        return customers.stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such user: " + username));
    }

    private void saveAll() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer c : customers) {
                w.write(c.toCSV());
                w.newLine();
            }
        } catch (IOException e) { throw new UncheckedIOException("Failed to save customers", e); }
    }

    private void loadCustomers() {
        File f = new File(CUSTOMERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isEmpty()) continue;
                customers.add(Customer.fromCSV(line));
            }
        } catch (IOException e) { throw new UncheckedIOException("Failed to load customers", e);}
    }

    private void log(String action, String user) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(ACTIVITY_LOG, true))) {
            w.write(user + "," + action + "," + new Date().getTime());
            w.newLine();
        } catch (IOException e) { /* ignore */ }
    }
    public Customer findById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        throw new NoSuchElementException("No such customer: " + id);
    }
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
    public void updateCustomer(Customer updatedCustomer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(updatedCustomer.getId())) {
                customers.set(i, updatedCustomer);
                saveAll();
                log("UPDATE", updatedCustomer.getUsername());
                return;
            }
        }
        throw new NoSuchElementException("No such customer: " + updatedCustomer.getId());
    }
    public void deleteCustomer(String customerId) {
        Customer c = findById(customerId);
        customers.remove(c);
        saveAll();
        log("DELETE", c.getUsername());
    }
}

