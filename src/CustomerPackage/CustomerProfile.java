package CustomerPackage;

import MainPackage.AppContext;
import CarPackage.*;
import MainPackage.IdShortenerRenderer;
import MainPackage.LoginPage;

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
            String rawId = customerId.startsWith("C-") ? customerId.substring(2) : customerId;
            Customer c = customerManagement.findById(rawId);
            if (c == null) {
                JOptionPane.showMessageDialog(this,
                        "Your profile could not be loaded. Please log in again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String info = String.format(
                    "Username: %s%nEmail:    %s%nPhone:    %s",
                    c.getUsername(),
                    c.getEmail(),
                    c.getPhoneNumber()
            );
            JOptionPane.showMessageDialog(this,
                    info,
                    "Your Profile",
                    JOptionPane.INFORMATION_MESSAGE);

            String[] options = {"Edit Username", "Edit Password", "Edit Email", "Edit Phone", "Delete Profile"};
            String action = (String) JOptionPane.showInputDialog(
                    this,
                    "Select action:",
                    "Manage Profile",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (action == null) return;

            if ("Delete Profile".equals(action)) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to DELETE your profile? This cannot be undone.",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        customerManagement.deleteCustomer(rawId);
                        JOptionPane.showMessageDialog(this,
                                "Your profile has been deleted.",
                                "Deleted", JOptionPane.INFORMATION_MESSAGE);
                        new LoginPage(context);
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete profile: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }

            String field = action.replace("Edit ", "");  // yields "Username"/"Password"/ etc.
            String newValue = JOptionPane.showInputDialog(
                    this,
                    "Enter new " + field + ":",
                    action,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (newValue == null || newValue.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        field + " cannot be empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            switch (field) {
                case "Username": c.setUsername(newValue.trim()); break;
                case "Password": c.setPassword(newValue.trim()); break;
                case "Email":    c.setEmail(newValue.trim());    break;
                case "Phone":    c.setPhoneNumber(newValue.trim()); break;
                default:
                    JOptionPane.showMessageDialog(this,
                            "Unknown action: " + action,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            try {
                customerManagement.updateCustomer(c);
                JOptionPane.showMessageDialog(this,
                        field + " updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to update " + field + ": " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
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
