package CustomerPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;
import CarPackage.*;
import MainPackage.*;

public class CustomerBookedCar extends JFrame {
    private final CarManagement carManagement;
    private JPanel panel1;
    private JTextField SearchTextField;
    private JTable CarTable;
    private JButton bookButton;
    private JButton payButton;
    private JButton feedbackButton;
    private JButton exitButton;
    private JLabel Search;
    private DefaultTableModel tableModel;
    private CustomerManagement customerManagement;
    private String customerId;
    private AppContext context;

    public CustomerBookedCar(AppContext context, String customerId) {
        if (context == null) {
            throw new IllegalArgumentException("AppContext cannot be null");
        }
        this.customerManagement = context.getCustomerManagement();
        this.carManagement = context.getCarManagement();
        this.context = context;
        this.customerId = customerId;

        setContentPane(panel1);
        setTitle("Book Cars");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{"Car ID", "Brand", "Model", "Color", "Price", "Photo Path", "Status"}, 0);
        CarTable.setModel(tableModel);
        CarTable.setDefaultRenderer(Object.class, new CarTableCellRenderer());
        CarTable.setRowHeight(150);
        loadCarData();

        bookButton.addActionListener(e -> {
            int selectedRow = CarTable.getSelectedRow();
            if (selectedRow != -1) {
                String carId = (String) tableModel.getValueAt(selectedRow, 0);
                try {
                    Order order = customerManagement.placeOrder(customerId, carId);
                    JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + order.getOrderId()
                            + "Please wait for the salesperson to confirm the order.");
                    loadCarData();
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a car to book!");
            }
        });

        payButton.addActionListener(e -> {
            Customer c = customerManagement.findById(
                    customerId.startsWith("C-") ? customerId.substring(2) : customerId
            );
            String username = c.getUsername();

            List<Order> customerOrders = customerManagement.listOrdersByCustomer(username);
            List<Order> payableOrders = customerOrders.stream()
                    .filter(o -> "CONFIRMED".equalsIgnoreCase(o.getStatus()))
                    .toList();

            if (payableOrders.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No orders to pay!");
                return;
            }

            String[] orderIds = payableOrders.stream()
                    .map(Order::getOrderId)
                    .toArray(String[]::new);
            String selectedOrderId = (String) JOptionPane.showInputDialog(
                    this,
                    "Select Order to Pay:",
                    "Pay Order",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    orderIds,
                    orderIds[0]
            );
            if (selectedOrderId == null) return;

            Order order = payableOrders.stream()
                    .filter(o -> o.getOrderId().equals(selectedOrderId))
                    .findFirst().get();
            double amount = context
                    .getCarManagement()
                    .getCar(order.getCarId())
                    .getPrice();
            try {
                Payment payment = customerManagement.makePayment(selectedOrderId, amount);
                JOptionPane.showMessageDialog(
                        this,
                        "Payment successful! Payment ID: " + payment.getPaymentId() +
                                "\nThank you for your payment!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadCarData();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        feedbackButton.addActionListener(e -> {

            Customer c = customerManagement.findById(
                    customerId.startsWith("C-") ? customerId.substring(2) : customerId
            );
            String username = c.getUsername();

            List<Order> customerOrders = customerManagement.getCustomerOrdersByUsername(username);
            List<Order> feedbackOrders = customerOrders.stream()
                    .filter(o -> "PAID".equalsIgnoreCase(o.getStatus()))
                    .toList();

            if (feedbackOrders.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No orders to provide feedback for!",
                        "Feedback", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] orderIds = feedbackOrders.stream()
                    .map(Order::getOrderId)
                    .toArray(String[]::new);
            String selectedOrderId = (String) JOptionPane.showInputDialog(
                    this,
                    "Select Order for Feedback:",
                    "Feedback",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    orderIds,
                    orderIds[0]
            );
            if (selectedOrderId == null) return;

            String comment = JOptionPane.showInputDialog(
                    this,
                    "Enter your feedback comment:",
                    "Feedback Comment",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (comment == null || comment.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Comment cannot be empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String ratingStr = JOptionPane.showInputDialog(
                    this,
                    "Enter rating between 1.0 and 5.0: ",
                    "Feedback Rating",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (ratingStr == null) return;

            try {
                double ratingVal = Double.parseDouble(ratingStr.trim());
                if (ratingVal < 1.0 || ratingVal > 5.0) {
                    JOptionPane.showMessageDialog(this,
                            "Rating must be between 1.0 and 5.0!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int rating = (int) Math.round(ratingVal);
                Feedback feedback = customerManagement.submitFeedback(
                        selectedOrderId,
                        username,
                        rating,
                        comment.trim()
                );

                JOptionPane.showMessageDialog(this,
                        "Feedback submitted successfully! ID: " + feedback.getFeedbackId(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Rating must be a valid number!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> dispose());

        SearchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchCars();
                }
            }
        });

        setVisible(true);
    }

    private void loadCarData() {
        tableModel.setRowCount(0);
        List<Car> cars = context.getCarManagement().getAllCars();
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getCarId(), car.getBrand(), car.getModel(), car.getColor(),
                    car.getPrice(), car.getPhotoPath(), car.getStatus()
            });
        }
    }

    private void searchCars() {
        String searchTerm = SearchTextField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Car> cars = context.getCarManagement().getAllCars();
        for (Car car : cars) {
            if (car.getCarId().toLowerCase().contains(searchTerm) ||
                    car.getBrand().toLowerCase().contains(searchTerm) ||
                    car.getModel().toLowerCase().contains(searchTerm) ||
                    car.getColor().toLowerCase().contains(searchTerm) ||
                    car.getStatus().toLowerCase().contains(searchTerm)) {
                tableModel.addRow(new Object[]{
                        car.getCarId(), car.getBrand(), car.getModel(), car.getColor(),
                        car.getPrice(), car.getPhotoPath(), car.getStatus()
                });
            }
        }
    }
}
