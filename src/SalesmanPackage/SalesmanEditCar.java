package SalesmanPackage;
import CarPackage.*;
import CustomerPackage.*;
import MainPackage.AppContext;
import MainPackage.IdShortenerRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SalesmanEditCar extends JFrame {
    private JPanel panel1;
    private JTable CarTable;
    private JTextField SearchTextField;
    private JButton orderServiceButton;
    private JButton paymentServiceButton;
    private JButton salesRecordButton;
    private JButton exitButton;
    private JLabel Search;
    private DefaultTableModel tableModel;
    private CarManagement carManagement;
    private SalesmanManagement salesmanManagement;
    private CustomerManagement customerManagement;
    private String salesmanId;

    public SalesmanEditCar(AppContext context, String salesmanId) {
        this.carManagement = context.getCarManagement();
        this.salesmanManagement = context.getSalesmanManagement();
        this.customerManagement = context.getCustomerManagement();
        this.salesmanId = salesmanId;

        setContentPane(panel1);
        setTitle("Edit Car Status");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{"Car ID", "Brand", "Model", "Color", "Price", "Photo Path", "Status", "Order ID"}, 0);
        CarTable.setModel(tableModel);
        CarTable.getColumnModel().getColumn(7).setCellRenderer(new IdShortenerRenderer());
        CarTable.setDefaultRenderer(Object.class, new CarTableCellRenderer());
        CarTable.setRowHeight(150);
        loadCarData();

        orderServiceButton.addActionListener(e -> {
            int selectedRow = CarTable.getSelectedRow();
            if (selectedRow != -1) {
                String carId = (String) tableModel.getValueAt(selectedRow, 0);
                String[] statuses = {"AVAILABLE", "BOOKED", "PAID", "CANCELLED"};
                String newStatus = (String) JOptionPane.showInputDialog(
                        SalesmanEditCar.this,
                        "Select new status:",
                        "Update Car Status",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        statuses,
                        statuses[0]
                );
                if (newStatus != null) {
                    salesmanManagement.updateCarStatus(carId, newStatus);
                    loadCarData();
                    JOptionPane.showMessageDialog(SalesmanEditCar.this, "Car status updated successfully! " +
                            "Please remind customer for paying! " + newStatus );
                }
            } else {
                JOptionPane.showMessageDialog(SalesmanEditCar.this, "Please select a car to update!");
            }
        });

        paymentServiceButton.addActionListener(e -> {
            int selectedRow = CarTable.getSelectedRow();
            if (selectedRow == -1) {
                showMessage("Please select a car to collect payment for!");
                return;
            }
            String orderId = (String) tableModel.getValueAt(selectedRow, 7);
            if ("N/A".equals(orderId)) {
                showMessage("No order associated with this car!");
                return;
            }
            Order order = customerManagement.findOrder(orderId);
            if (order == null) {
                showMessage("Order not found!");
                return;
            }
            if (!"CONFIRMED".equals(order.getStatus())) {
                showMessage("Order is not in CONFIRMED status, cannot collect payment.");
                return;
            }

            double amount = carManagement.getCar(order.getCarId()).getPrice();
            try {
                Payment payment = customerManagement.makePayment(orderId, amount);
                showMessage("Payment collected! Payment ID: " + payment.getPaymentId());
                salesmanManagement.updateCarStatus(order.getCarId(), "PAID");
                loadCarData();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        salesRecordButton.addActionListener(e -> {
            int selectedRow = CarTable.getSelectedRow();
            if (selectedRow != -1) {
                String orderId = (String) tableModel.getValueAt(selectedRow, 7);
                if (!"N/A".equals(orderId)) {
                    Order order = customerManagement.findOrder(orderId);
                    if (order != null && "PAID".equals(order.getStatus())) {
                        String comment = JOptionPane.showInputDialog(SalesmanEditCar.this, "Enter comment for the sale:");
                        if (comment != null) {
                            salesmanManagement.recordSale(orderId, salesmanId, comment);
                            JOptionPane.showMessageDialog(SalesmanEditCar.this, "Sale recorded successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(SalesmanEditCar.this, "Invalid order or order not paid!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(SalesmanEditCar.this, "No order associated with this car!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(SalesmanEditCar.this, "Please select a car to record sale!");
            }
        });

        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(SalesmanEditCar.this, "Exiting...");
            new SalesmanMain(context, salesmanId);
            this.dispose();
        });

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
        List<Car> cars = carManagement.getAllCars();
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getCarId(), car.getBrand(), car.getModel(), car.getColor(),
                    car.getPrice(), car.getPhotoPath(), car.getStatus(), getOrderIdForCar(car.getCarId())
            });
        }
    }

    private String getOrderIdForCar(String carId) {
        List<Order> orders = customerManagement.getOrdersByCarId(carId);
        if (orders != null && !orders.isEmpty()) {
            return orders.get(0).getOrderId();
        }
        return "N/A";
    }

    private void searchCars() {
        String searchTerm = SearchTextField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Car> cars = carManagement.getAllCars();
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
    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
