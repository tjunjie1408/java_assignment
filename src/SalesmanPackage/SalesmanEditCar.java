package SalesmanPackage;
import CarPackage.*;
import CustomerPackage.*;
import MainPackage.AppContext;

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

        tableModel = new DefaultTableModel(new Object[]{"Car ID", "Brand", "Model", "Color", "Price", "Photo Path", "Status"}, 0);
        CarTable.setModel(tableModel);
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
                    JOptionPane.showMessageDialog(SalesmanEditCar.this, "Car status updated successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(SalesmanEditCar.this, "Please select a car to update!");
            }
        });

        // Edit later
        paymentServiceButton.addActionListener(e -> JOptionPane.showMessageDialog(SalesmanEditCar.this, "Payment service not implemented yet!"));


        salesRecordButton.addActionListener(e -> {
            String orderId = JOptionPane.showInputDialog(SalesmanEditCar.this, "Enter Order ID to record sale:");
            if (orderId != null && !orderId.trim().isEmpty()) {
                Order order = customerManagement.findOrder(orderId);
                if (order != null && "PAID".equals(order.getStatus())) {
                    String comment = JOptionPane.showInputDialog(SalesmanEditCar.this, "Enter comment for the sale:");
                    if (comment != null) {
                        salesmanManagement.recordSale(orderId, salesmanId, comment);
                        JOptionPane.showMessageDialog(SalesmanEditCar.this, "Sale recorded successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(SalesmanEditCar.this, "Invalid order ID or order not paid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                    car.getPrice(), car.getPhotoPath(), car.getStatus()
            });
        }
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
}
