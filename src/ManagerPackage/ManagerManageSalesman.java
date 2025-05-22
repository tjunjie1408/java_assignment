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

    public ManagerManageSalesman() {
        setContentPane(panel1);
        setTitle("Manage Salesmen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        salesmanManagement = new SalesmanManagement();
        String[] columnNames = {"Salesman ID", "Username", "Password", "Email", "Phone Number"};
        tableModel = new DefaultTableModel(columnNames, 0) ;
        SalesmanTable.setModel(tableModel);
        loadSalesmanData();
        addButton.addActionListener(e -> addSalesman());
        updateButton.addActionListener(e -> updateSalesman());
        deleteButton.addActionListener(e -> deleteSalesman());
        exitButton.addActionListener(e -> {
            new ManagersMain();
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
        List<Salesman> salesmen = salesmanManagement.getSalesmen();
        for (Salesman salesman : salesmen) {
            tableModel.addRow(new Object[]{
                    salesman.getSalesId(),
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
        List<Salesman> salesmen = salesmanManagement.getSalesmen();
        for (Salesman salesman : salesmen) {
            if (salesman.getSalesId().toLowerCase().contains(searchTerm) ||
                    salesman.getUsername().toLowerCase().contains(searchTerm) ||
                    salesman.getEmail().toLowerCase().contains(searchTerm) ||
                    salesman.getPhoneNumber().toLowerCase().contains(searchTerm)) {
                tableModel.addRow(new Object[]{
                        salesman.getSalesId(),
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
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Salesman salesman = salesmanManagement.getSalesmanById(id);
            if (salesman != null) {
                String[] options = {"Username", "Password", "Email", "Phone Number"};
                String selectedField = (String) JOptionPane.showInputDialog(
                        this,
                        "Select field to update:",
                        "Update Salesman",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (selectedField != null) {
                    String newValue = JOptionPane.showInputDialog(
                            this,
                            "Enter new value for " + selectedField + ":"
                    );
                    if (newValue != null && !newValue.trim().isEmpty()) {
                        switch (selectedField) {
                            case "Username":
                                salesman.setUsername(newValue);
                                break;
                            case "Password":
                                salesman.setPassword(newValue);
                                break;
                            case "Email":
                                salesman.setEmail(newValue);
                                break;
                            case "Phone Number":
                                salesman.setPhoneNumber(newValue);
                                break;
                        }
                        salesmanManagement.updateSalesman(salesman.getUsername(), salesman);
                        loadSalesmanData();
                        JOptionPane.showMessageDialog(this, selectedField + " updated successfully!");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a salesman to update!", "Error", JOptionPane.ERROR_MESSAGE);
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
        for (Salesman s : salesmanManagement.getSalesmen()) {
            int currentId = Integer.parseInt(s.getId().substring(1));
            maxId = Math.max(maxId, currentId);
        }
        return String.valueOf(maxId + 1);
    }
}
