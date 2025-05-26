package CustomerPackage;

import MainPackage.AppContext;
import CarPackage.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

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

    public CustomerProfile(AppContext context, String customerId) {
        if (context == null || customerId == null) {
            throw new IllegalArgumentException("A valid context and customerId must be passed.");
        }
        this.context = context;
        this.customerId = customerId;
        this.customerManagement = context.getCustomerManagement();
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
            List<Order> customerOrders = customerManagement.listOrdersByCustomer(customerId);
            if (customerOrders.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No purchase history found!", "Purchase History", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String[] columnNames = {"Order ID", "Car ID", "Brand", "Model", "Status", "Photo Path", "Price"};
            DefaultTableModel historyTableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Order order : customerOrders) {
                String brand = "N/A";
                String model = "N/A";
                String photoPath = "N/A";
                double price = 0.0;
                if (order.getCarId() != null) {
                    try {
                        Car car = customerManagement.carManagement.getCar(order.getCarId());
                        if (car != null) {
                            brand = car.getBrand();
                            model = car.getModel();
                            photoPath = car.getPhotoPath();
                            price = car.getPrice();
                        }
                    } catch (Exception ex) {
                        brand = "Car Not Found";
                        model = "Car Not Found";
                        photoPath = "N/A";
                    }
                }
                historyTableModel.addRow(new Object[]{
                        order.getOrderId(),
                        order.getCarId(),
                        brand,
                        model,
                        order.getStatus(),
                        photoPath,
                        String.format("$%.2f", price)
                });
            }
            tableModel.setDataVector(historyTableModel.getDataVector(),
                    new Vector<>(List.of(columnNames)));
            int totalOrders = customerOrders.size();
            long paidOrders = customerOrders.stream().filter(o -> "PAID".equals(o.getStatus())).count();
            long confirmedOrders = customerOrders.stream().filter(o -> "CONFIRMED".equals(o.getStatus())).count();

            JOptionPane.showMessageDialog(this,
                    String.format("Purchase History Loaded!\nTotal Orders: %d | Paid: %d | Confirmed: %d",
                            totalOrders, paidOrders, confirmedOrders),
                    "Purchase History", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
