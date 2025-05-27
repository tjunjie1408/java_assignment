package ManagerPackage;

import CarPackage.*;
import CustomerPackage.*;
import MainPackage.AppContext;
import SalesmanPackage.*;
import MainPackage.LoginPage;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManagersMain extends JFrame{
    private JPanel panel1;
    private JButton manageManagerProfileButton;
    private JButton manageSalesmanProfileButton;
    private JButton manageCustomerProfileButton;
    private JButton manageCarDetailsButton;
    private JButton paymentAndFeedbackAnalysisButton;
    private JButton exitButton;
    private JLabel ManagerMainPage;
    private final CarManagement carManagement;
    private final CustomerManagement customerManagement;
    private final ManagerManagement managerManagement;
    private SalesmanManagement salesmanManagement;
    private AppContext context;

    public ManagersMain(AppContext context) {
        this.context = context;
        this.managerManagement  = context.getManagerManagement();
        this.customerManagement = context.getCustomerManagement();
        this.carManagement      = context.getCarManagement();
        setContentPane(panel1);
        setTitle(ManagerMainPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        manageManagerProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Manager Profile clicked");
            String managerId = context.getCurrentManagerId();
            Manager m = managerManagement.findById(managerId);
            if (m == null) {
                JOptionPane.showMessageDialog(this,
                        "Current manager information not loaded, please log in again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] actions = {"Username", "Password", "Email", "Phone", "Delete Profile"};
            String action = (String) JOptionPane.showInputDialog(
                    this,
                    "Select action:",
                    "Manage Profile",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    actions,
                    actions[0]
            );
            if (action == null) return;

            // 处理删除
            if ("Delete Profile".equals(action)) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to DELETE your profile? This cannot be undone.",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = managerManagement.deleteManager(managerId);
                    if (deleted) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Your profile has been deleted.",
                                "Deleted",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        new LoginPage(context);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Failed to delete profile. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                return;  // 退出，不继续执行下面的更新逻辑
            }

            // 对于更新字段，先输入新值
            String newValue = JOptionPane.showInputDialog(
                    this,
                    "Enter new " + action + ":",
                    "Edit " + action,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (newValue == null || newValue.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        action + " cannot be empty!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 根据选择更新对象
            switch (action) {
                case "Username" -> m.setUsername(newValue.trim());
                case "Password" -> m.setPassword(newValue.trim());
                case "Email"    -> m.setEmail(newValue.trim());
                case "Phone"    -> m.setPhoneNumber(newValue.trim());
                default -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "Unknown action: " + action,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            // 持久化更新
            try {
                boolean ok = managerManagement.updateManager(m);
                if (ok) {
                    JOptionPane.showMessageDialog(
                            this,
                            action + " updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    throw new IOException("Failed to write managers.txt");
                }
            } catch (IOException ioEx) {
                JOptionPane.showMessageDialog(
                        this,
                        "File write failed: " + ioEx.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        manageSalesmanProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Salesman Profile clicked");
            new ManagerManageSalesman(context);
            this.dispose();
        });
        manageCustomerProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Customer Profile clicked");
            new ManagerManageCustomer(context);
            this.dispose();
        });
        manageCarDetailsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Car Details clicked");
            new ManagerManageCars(context);
            this.dispose();
        });
        paymentAndFeedbackAnalysisButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Payment and Feedback Analysis clicked");
            try {
                // --- PAYMENTS ---
                // Read and parse payments.txt
                List<Payment> payments = new ArrayList<>();
                Path payPath = Paths.get("payments.txt");
                if (Files.exists(payPath)) {
                    for (String line : Files.readAllLines(payPath)) {
                        Payment p = Payment.fromCSV(line);
                        if (p != null) payments.add(p);
                    }
                }

                // Build payments table (no Date column)
                String[] payCols = {"Payment ID", "Order ID", "Amount"};
                DefaultTableModel payModel = new DefaultTableModel(payCols, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                for (Payment p : payments) {
                    payModel.addRow(new Object[]{
                            p.getPaymentId(),
                            p.getOrderId(),
                            String.format("$%.2f", p.getAmount())
                    });
                }
                JTable payTable = new JTable(payModel);
                payTable.setAutoCreateRowSorter(true);
                JScrollPane payScroll = new JScrollPane(payTable);
                payScroll.setBorder(BorderFactory.createTitledBorder("Payment Records"));
                payScroll.setPreferredSize(new Dimension(600, 200));

                // --- FEEDBACK ---
                // Read and parse feedback.txt
                List<Feedback> feedbacks = new ArrayList<>();
                Path fbPath = Paths.get("feedback.txt");
                if (Files.exists(fbPath)) {
                    for (String line : Files.readAllLines(fbPath)) {
                        Feedback f = Feedback.fromCSV(line);
                        if (f != null) feedbacks.add(f);
                    }
                }

                // Build feedback table
                String[] fbCols = {"Feedback ID", "Order ID", "Rating", "Comment"};
                DefaultTableModel fbModel = new DefaultTableModel(fbCols, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                for (Feedback f : feedbacks) {
                    fbModel.addRow(new Object[]{
                            f.getFeedbackId(),
                            f.getOrderId(),
                            f.getRating(),
                            f.getComment()
                    });
                }
                JTable fbTable = new JTable(fbModel);
                fbTable.setAutoCreateRowSorter(true);
                JScrollPane fbScroll = new JScrollPane(fbTable);
                fbScroll.setBorder(BorderFactory.createTitledBorder("Feedback Records"));
                fbScroll.setPreferredSize(new Dimension(600, 200));

                // --- COMBINE AND SHOW ---
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(payScroll);
                panel.add(Box.createVerticalStrut(10));
                panel.add(fbScroll);

                JOptionPane.showMessageDialog(
                        this,
                        panel,
                        "Payment and Feedback Records",
                        JOptionPane.PLAIN_MESSAGE
                );
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to load records: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage(context);
            this.dispose();
        });
    }
}
