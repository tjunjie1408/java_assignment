package ManagerPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerManageCustomer extends JFrame {
    private JPanel panel1;
    private JTextField SearchTextField;
    private JLabel Search;
    private JTable CustomerTable;
    private JButton approveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JLabel CustomerProfileManagement;

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private static final String DATA_FILE = "customers.txt";

    public ManagerManageCustomer() {
        setContentPane(panel1);
        setTitle("Customer Profile Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initTable();
        initListeners();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initTable() {
        String[] columns = {"ID", "Username", "Password", "Email", "Phone", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent direct table edits
            }
        };
        CustomerTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        CustomerTable.setModel(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        CustomerTable.setRowSorter(rowSorter);
        loadCustomerData();
    }

    private void initListeners() {
        // Search functionality
        SearchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = SearchTextField.getText().trim();
                rowSorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        // Approve functionality
        approveButton.addActionListener(e -> {
            int[] rows = CustomerTable.getSelectedRows();
            if (rows.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select at least one customer to approve!");
                return;
            }
            boolean updated = false;
            for (int viewRow : rows) {
                int modelRow = CustomerTable.convertRowIndexToModel(viewRow);
                String status = (String) tableModel.getValueAt(modelRow, 5);
                System.out.println("Row " + modelRow + " status: " + status);
                if ("PENDING".equalsIgnoreCase(status.trim())) {
                    tableModel.setValueAt("APPROVED", modelRow, 5);
                    updated = true;
                }
            }
            if (updated) {
                saveCustomerData();
                JOptionPane.showMessageDialog(this, "Selected customers approved.");
            } else {
                JOptionPane.showMessageDialog(this, "No pending customers selected.");
            }
        });

        // Update functionality
        updateButton.addActionListener(e -> {
            int row = CustomerTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer to update!");
                return;
            }
            int modelRow = CustomerTable.convertRowIndexToModel(row);
            String[] options = {"Username", "Password", "Email", "Phone"};
            String field = (String) JOptionPane.showInputDialog(
                    this, "Select field to update:", "Update Field", JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
            if (field != null) {
                String newValue = JOptionPane.showInputDialog(this, "Enter new " + field + ":");
                if (newValue != null && !newValue.trim().isEmpty()) {
                    int col = getColumnIndex(field);
                    tableModel.setValueAt(newValue.trim(), modelRow, col);
                    saveCustomerData();
                    JOptionPane.showMessageDialog(this, field + " updated.");
                } else {
                    JOptionPane.showMessageDialog(this, "Value cannot be empty!");
                }
            }
        });

        // Delete functionality
        deleteButton.addActionListener(e -> {
            int[] rows = CustomerTable.getSelectedRows();
            if (rows.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select at least one customer to delete!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete selected customer(s)?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = rows.length - 1; i >= 0; i--) {
                    int modelRow = CustomerTable.convertRowIndexToModel(rows[i]);
                    tableModel.removeRow(modelRow);
                }
                saveCustomerData();
                JOptionPane.showMessageDialog(this, "Selected customers deleted.");
            }
        });

        // Exit functionality
        exitButton.addActionListener(e ->{
            this.dispose();
            new ManagersMain();
        });
    }

    private int getColumnIndex(String field) {
        switch (field) {
            case "Username":
                return 1;
            case "Password":
                return 2;
            case "Email":
                return 3;
            case "Phone":
                return 4;
            default:
                return -1;
        }
    }

    private void loadCustomerData() {
        tableModel.setRowCount(0); // Clear existing rows
        Path path = Paths.get(DATA_FILE);
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) { // Ensure enough fields
                    // Map file fields to table columns:
                    // ID (0), Username (1), Password (2), Email (3), Phone (6), Status (4)
                    tableModel.addRow(new Object[]{
                            parts[0], // ID
                            parts[1], // Username
                            parts[2], // Password
                            parts[3], // Email
                            parts[6], // Phone
                            parts[4]  // Status
                    });
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    private void saveCustomerData() {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                sb.append(tableModel.getValueAt(i, j));
                if (j < tableModel.getColumnCount() - 1) sb.append(",");
            }
            lines.add(sb.toString());
        }
        try {
            Files.write(Paths.get(DATA_FILE), lines);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving customers: " + ex.getMessage());
        }
    }
}

