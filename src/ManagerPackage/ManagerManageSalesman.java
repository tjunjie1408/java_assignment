package ManagerPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import CarPackage.CarManagement;
import MainPackage.AppContext;
import SalesmanPackage.*;

public class ManagerManageSalesman extends JFrame {
    private JPanel panel1;
    private JTextField SearchTextField;
    private JTable SalesmanTable;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JLabel Search;
    private DefaultTableModel tableModel;
    private SalesmanManagement salesmanManagement;
    private AppContext context;

    public ManagerManageSalesman(AppContext context) {
        this.salesmanManagement = context.getSalesmanManagement();
        this.context = context;
        setContentPane(panel1);
        setTitle("Manage Salesmen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        String[] columnNames = {"Salesman ID", "Username", "Password", "Email", "Phone Number"};
        tableModel = new DefaultTableModel(columnNames, 0) ;
        SalesmanTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        SalesmanTable.setModel(tableModel);
        loadSalesmanData();
        addButton.addActionListener(e -> addSalesman());
        updateButton.addActionListener(e -> updateSalesman());
        deleteButton.addActionListener(e -> deleteSalesman());
        exitButton.addActionListener(e -> {
            new ManagersMain(context);
            this.dispose();
        });
        SearchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchSalesman();
                }
            }
        });
        setVisible(true);
    }

    private void loadSalesmanData() {
        tableModel.setRowCount(0);
        List<Salesman> salesmen = context.getSalesmanManagement().getSalesmen();
        for (Salesman salesman : salesmen) {
            tableModel.addRow(new Object[]{
                    salesman.getId(),
                    salesman.getUsername(),
                    salesman.getPassword(),
                    salesman.getEmail(),
                    salesman.getPhoneNumber()
            });
        }
    }

    private void searchSalesman() {
        String searchTerm = SearchTextField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Salesman> salesmen = context.getSalesmanManagement().getSalesmen();
        for (Salesman salesman : salesmen) {
            if (salesman.getId().toLowerCase().contains(searchTerm) ||
                    salesman.getUsername().toLowerCase().contains(searchTerm) ||
                    salesman.getEmail().toLowerCase().contains(searchTerm) ||
                    salesman.getPhoneNumber().toLowerCase().contains(searchTerm)) {
                tableModel.addRow(new Object[]{
                        salesman.getId(),
                        salesman.getUsername(),
                        salesman.getPassword(),
                        salesman.getEmail(),
                        salesman.getPhoneNumber()
                });
            }
        }
    }

    private void addSalesman() {
        String id = generateNewId();
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        String email = JOptionPane.showInputDialog(this, "Enter email:");
        String phoneNumber = JOptionPane.showInputDialog(this, "Enter phone number:");

        if (username != null && password != null && email != null && phoneNumber != null) {
            Salesman newSalesman = new Salesman(id, username, password, email, phoneNumber);
            if (salesmanManagement.addSalesman(newSalesman)) {
                loadSalesmanData();
                JOptionPane.showMessageDialog(this, "Salesman added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSalesman() {
        int selectedRow = SalesmanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a salesman to update!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Salesman salesman = salesmanManagement.findById(id);
        if (salesman == null) {
            JOptionPane.showMessageDialog(this,
                    "Selected salesman not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = {"Username", "Password", "Email", "Phone Number"};
        String field = (String) JOptionPane.showInputDialog(
                this, "Select field to update:", "Update Salesman",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
        if (field == null) return;
        String newValue = JOptionPane.showInputDialog(
                this, "Enter new value for " + field + ":"
        );
        if (newValue == null || newValue.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    field + " cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (field) {
            case "Username"     -> salesman.setUsername(newValue.trim());
            case "Password"     -> salesman.setPassword(newValue.trim());
            case "Email"        -> salesman.setEmail(newValue.trim());
            case "Phone Number" -> salesman.setPhoneNumber(newValue.trim());
        }
        boolean ok = salesmanManagement.updateSalesman(salesman);
        if (ok) {
            loadSalesmanData();
            JOptionPane.showMessageDialog(this,
                    field + " updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update salesman " + salesman.getId(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSalesman() {
        int selectedRow = SalesmanTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Salesman salesman = salesmanManagement.getSalesmanById(id);
            if (salesman != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete salesman: " + salesman.getUsername() + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    salesmanManagement.deleteSalesman(salesman.getUsername());
                    loadSalesmanData();
                    JOptionPane.showMessageDialog(this, "Salesman deleted successfully!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a salesman to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateNewId() {
        int maxId = 0;
        for (Salesman s : context.getSalesmanManagement().getSalesmen()) {
            try {
                int currentId = Integer.parseInt(s.getId());
                maxId = Math.max(maxId, currentId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid id format: " + s.getId());
            }
        }
        return String.valueOf(maxId + 1);
    }
}
