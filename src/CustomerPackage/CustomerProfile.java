package CustomerPackage;

import MainPackage.AppContext;
import CarPackage.*;
import MainPackage.IdShortenerRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

public class CustomerProfile extends JFrame{
    private JPanel panel1;
    private JLabel Profile;
    private JButton EditProfile;
    private JButton ViewAvailableCar;
    private JButton Logout;
    private JButton ViewPurchaseHistories;
    private final CustomerManagement customerManagement;
    private final String customerId;
    private final AppContext context;
    private DefaultTableModel tableModel;
    private CarManagement carManagement;

    public CustomerProfile(AppContext context, String customerId) {
        if (context == null || customerId == null) {
            throw new IllegalArgumentException("A valid context and customerId must be passed.");
        }
        this.context = context;
        this.customerId = customerId;
        this.customerManagement = context.getCustomerManagement();
        this.carManagement = context.getCarManagement();
        initUI();
    }

    private void initUI() {
        setContentPane(panel1);
        assert Profile != null;
        setTitle(Profile.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        assert Logout != null;
        Logout.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new CustomerMain(context);
            this.dispose();
        });

        assert EditProfile != null;

        EditProfile.addActionListener(e -> {
            String[] options = {"Username", "Password", "Email", "Phone"};
            String field = (String) JOptionPane.showInputDialog(
                    this,
                    "Select field to edit:",
                    "Edit Profile",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (field == null) return;
            String newValue = JOptionPane.showInputDialog(
                    this,
                    "Enter new " + field + ":",
                    "Edit " + field,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (newValue == null || newValue.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, field + " cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String rawId = customerId.startsWith("C-") ? customerId.substring(2) : customerId;
                Customer c = customerManagement.findById(rawId);
                switch (field) {
                    case "Username" -> c.setUsername(newValue.trim());
                    case "Password" -> c.setPassword(newValue.trim());
                    case "Email"    -> c.setEmail(newValue.trim());
                    case "Phone"    -> c.setPhoneNumber(newValue.trim());
                }
                customerManagement.updateCustomer(c);
                JOptionPane.showMessageDialog(this, field + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to update " + field + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        assert ViewAvailableCar != null;
        ViewAvailableCar.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are viewing available cars");
            new CustomerBookedCar(context,customerId);
        });

        assert ViewPurchaseHistories != null;
        ViewPurchaseHistories.addActionListener(e -> {
            try {
                String rawId = customerId.startsWith("C-")
                        ? customerId.substring(2)
                        : customerId;
                Customer c = customerManagement.findById(rawId);
                String username = c.getUsername();
                Path paymentsPath = Paths.get("payments.txt");
                if (!Files.exists(paymentsPath)) {
                    JOptionPane.showMessageDialog(this,
                            "No payment records found!",
                            "Purchase History",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                List<Payment> allPayments = Files.readAllLines(paymentsPath).stream()
                        .map(Payment::fromCSV)
                        .filter(Objects::nonNull)
                        .toList();
                List<Payment> userPayments = allPayments.stream()
                        .filter(p -> {
                            Order o = customerManagement.findOrder(p.getOrderId());
                            return o != null && username.equals(o.getUsername());
                        })
                        .toList();

                if (userPayments.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No purchase history found!",
                            "Purchase History",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String[] cols = {
                        "Order ID", "Car ID", "Brand", "Model",
                        "Status", "Price", "Payment ID"
                };

                DefaultTableModel model = new DefaultTableModel(cols, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };

                for (Payment p : userPayments) {
                    Order o = customerManagement.findOrder(p.getOrderId());
                    String carId = o.getCarId();
                    Car car = carManagement.getCar(carId);

                    String brand     = car  != null ? car.getBrand()     : "N/A";
                    String modelName = car  != null ? car.getModel()     : "N/A";
                    double price     = car  != null ? car.getPrice()     : 0.0;

                    model.addRow(new Object[]{
                            o.getOrderId(),
                            carId,
                            brand,
                            modelName,
                            o.getStatus(),
                            String.format("$%.2f", price),
                            p.getPaymentId()
                    });
                }
                JTable table = new JTable(model);
                table.setAutoCreateRowSorter(true);
                table.getColumnModel().getColumn(0).setCellRenderer(new IdShortenerRenderer());
                table.getColumnModel().getColumn(6).setCellRenderer(new IdShortenerRenderer());
                JScrollPane scroll = new JScrollPane(table);
                scroll.setPreferredSize(new Dimension(700, 300));
                JOptionPane.showMessageDialog(
                        this,
                        scroll,
                        "Your Purchase History",
                        JOptionPane.PLAIN_MESSAGE
                );
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to load purchase history: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
