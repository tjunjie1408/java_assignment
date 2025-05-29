package CustomerPackage;

import CarPackage.Car;
import CarPackage.CarManagement;
import UserPackage.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerManagement {
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String PAYMENTS_FILE = "payments.txt";
    private static final String FEEDBACK_FILE = "feedback.txt";
    private static final String ACTIVITY_LOG = "customer_activity.log";
    private static final int ID_START = 1;
    private final List<Customer> customers = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    public CarManagement carManagement;

    public CustomerManagement() {
        loadCustomers();
        loadOrders();
    }
    public void setCarManagement(CarManagement carManagement) {
        this.carManagement = carManagement;
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
        c.setEmail(email);
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

    public List<Order> getOrdersByCarId(String carId) {
        return orders.stream()
                .filter(order -> order.getCarId().equals(carId))
                .collect(Collectors.toList());
    }

    // Place an order
    public Order placeOrder(String customerId, String carId) {
        Customer c = findById(customerId);
        String username = c.getUsername();
        Car car = carManagement.getCar(carId);
        if (car == null || !"Available".equalsIgnoreCase(car.getStatus())) {
            throw new IllegalStateException("Car not available");
        }
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, username, carId, "PENDING", new Date());
        orders.add(order);
        saveOrders();
        log("ORDER", username + ", order=" + orderId);
        return order;
    }

    // Make a payment
    public Payment makePayment(String orderId, double amount) {
        Order order = findOrder(orderId);
        if (order == null || !"CONFIRMED".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Order not ready for payment");
        }
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, orderId, amount, new Date());
        appendToFile(PAYMENTS_FILE, payment.toCSV());
        order.setStatus("PAID");
        saveOrders();
        log("PAYMENT", orderId + ", payment=" + paymentId);
        return payment;
    }

    // Submit feedback
    public Feedback submitFeedback(String orderId, String customerId, int rating, String comment) {
        Order order = findOrder(orderId);
        if (order == null || !"PAID".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot submit feedback for unpaid order");
        }
        String feedbackId = UUID.randomUUID().toString();
        Feedback feedback = new Feedback(feedbackId, orderId, customerId, rating, comment);
        appendToFile(FEEDBACK_FILE, feedback.toCSV());
        log("FEEDBACK", customerId + ", order=" + orderId);
        return feedback;
    }

    private Customer find(String username) {
        return customers.stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such user: " + username));
    }

    public Order findOrder(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
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
        } catch (IOException e) { throw new UncheckedIOException("Failed to load customers", e); }
    }

    private void loadOrders() {
        File f = new File(ORDERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isEmpty()) continue;
                orders.add(Order.fromCSV(line));
            }
        } catch (IOException e) { System.out.println("Error loading orders: " + e.getMessage()); }
    }

    public void saveOrders() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order o : orders) {
                w.write(o.toCSV());
                w.newLine();
            }
        } catch (IOException e) { System.out.println("Error saving orders: " + e.getMessage()); }
    }

    private void appendToFile(String fileName, String data) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(fileName, true))) {
            w.write(data);
            w.newLine();
        } catch (IOException e) { System.out.println("Error appending to " + fileName + ": " + e.getMessage()); }
    }

    private void log(String action, String detail) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(ACTIVITY_LOG, true))) {
            w.write(System.currentTimeMillis() + "," + action + "," + detail);
            w.newLine();
        } catch (IOException ignored) {}
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

    public List<Order> listOrdersByCustomer(String username) {
        return orders.stream()
                .filter(order -> order.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public void updateCustomer(Customer updatedCustomer) {
        boolean found = false;
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(updatedCustomer.getId())) {
                customers.set(i, updatedCustomer);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException("No such customer: " + updatedCustomer.getId());
        }
        saveAll();
        log("UPDATE_PROFILE", updatedCustomer.getUsername());
    }

    public void deleteCustomer(String customerId) {
        Customer c = findById(customerId);
        customers.remove(c);
        saveAll();
        log("DELETE", c.getUsername());
    }

    public List<Order> getCustomerOrdersByUsername(String username) {
        return orders.stream()
                .filter(o -> username.equals(o.getUsername()))
                .collect(Collectors.toList());
    }
}
