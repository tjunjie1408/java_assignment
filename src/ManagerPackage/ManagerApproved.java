package ManagerPackage;

import CustomerPackage.*;
import UserPackage.UserStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerApproved extends JFrame {
    private JPanel panel1;
    private JTable ManagerApproval;
    private JButton Cancel;
    private JButton Approve;
    private DefaultTableModel tableModel;

    public ManagerApproved() {
        String[] columnNames = {"Select", "Customer ID", "Name", "Phone Number", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                return String.class;
            }
        };
        ManagerApproval.setModel(tableModel);

        loadCustomersFromFile("customers.txt");

        ManagerApproval.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    approveSelectedCustomers();
                }
            }
        });

        Cancel.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the Manager main page");
            new ManagersMain();
            this.dispose();
        });

        Approve.addActionListener(e -> approveSelectedCustomers());

        setContentPane(panel1);
        setTitle("Manager Approval");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void loadCustomersFromFile(String filename) {
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Customer customer = Customer.fromCSV(line);
                if (customer.getStatus() == UserStatus.PENDING) {
                    tableModel.addRow(new Object[]{
                            false,
                            customer.getId(),
                            customer.getUsername(),
                            customer.getPhoneNumber(),
                            customer.getEmail()
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Approve selected customers
    private void approveSelectedCustomers() {
        List<String> selectedCustomers = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                String customerId = (String) tableModel.getValueAt(i, 1);
                selectedCustomers.add(customerId);
            }
        }
        if (!selectedCustomers.isEmpty()) {
            updateCustomerStatus(selectedCustomers);
            tableModel.setRowCount(0);
            loadCustomersFromFile("customers.txt");
            JOptionPane.showMessageDialog(this,
                    "Approved " + selectedCustomers.size() + " customers successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No customers selected",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    private void updateCustomerStatus(List<String> customerIds) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && customerIds.contains(parts[0])) {
                    parts[4] = "APPROVED";
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error writing file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
