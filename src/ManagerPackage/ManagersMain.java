package ManagerPackage;

import CarPackage.*;
import CustomerPackage.*;
import MainPackage.AppContext;
import MainPackage.IdShortenerRenderer;
import MainPackage.TableExporter;
import MainPackage.LoginPage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
            String managerId = context.getCurrentManagerId();
            Manager m = managerManagement.findById(managerId);
            if (m == null) {
                JOptionPane.showMessageDialog(this,
                        "Current manager information not loaded, please log in again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String profileInfo =
                    "Username: " + m.getUsername() + "\n" +
                            "Email:    " + m.getEmail()    + "\n" +
                            "Phone:    " + m.getPhoneNumber();
            JOptionPane.showMessageDialog(this,
                    profileInfo,
                    "Your Profile",
                    JOptionPane.INFORMATION_MESSAGE);

            String[] actions = {"Edit Username", "Edit Password", "Edit Email", "Edit Phone", "Delete Profile"};
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
                        JOptionPane.showMessageDialog(this,
                                "Your profile has been deleted.",
                                "Deleted",
                                JOptionPane.INFORMATION_MESSAGE);
                        new LoginPage(context);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete profile. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }

            String field = action.replace("Edit ", "");
            String newValue = JOptionPane.showInputDialog(
                    this,
                    "Enter new " + field + ":",
                    action,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (newValue == null || newValue.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        field + " cannot be empty!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            switch (field) {
                case "Username" -> m.setUsername(newValue.trim());
                case "Password" -> m.setPassword(newValue.trim());
                case "Email"    -> m.setEmail(newValue.trim());
                case "Phone"    -> m.setPhoneNumber(newValue.trim());
                default -> {
                    JOptionPane.showMessageDialog(this,
                            "Unknown action: " + action,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            try {
                boolean ok = managerManagement.updateManager(m);
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            field + " updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new IOException("Failed to write managers.txt");
                }
            } catch (IOException ioEx) {
                JOptionPane.showMessageDialog(this,
                        "File write failed: " + ioEx.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE);
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


        paymentAndFeedbackAnalysisButton.addActionListener(e -> onPaymentFeedbackAnalysis());
    }
    private void onPaymentFeedbackAnalysis() {
        try {
            List<Payment> payments = new ArrayList<>();
            Path payPath = Path.of("payments.txt");
            if (Files.exists(payPath)) {
                for (String line : Files.readAllLines(payPath)) {
                    Payment p = Payment.fromCSV(line);
                    if (p != null) payments.add(p);
                }
            }
            String[] payCols = {"Payment ID", "Order ID", "Amount"};
            DefaultTableModel payModel = new DefaultTableModel(payCols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            for (Payment p : payments) {
                payModel.addRow(new Object[]{
                        p.getPaymentId(),
                        p.getOrderId(),
                        String.format("%.2f", p.getAmount())
                });
            }
            JTable payTable = new JTable(payModel);
            payTable.setAutoCreateRowSorter(true);
            payTable.getColumnModel().getColumn(0).setCellRenderer(new IdShortenerRenderer());
            payTable.getColumnModel().getColumn(1).setCellRenderer(new IdShortenerRenderer());
            JScrollPane payScroll = new JScrollPane(payTable);
            payScroll.setBorder(BorderFactory.createTitledBorder("Payment Records"));
            payScroll.setPreferredSize(new Dimension(400, 200));

            List<Feedback> feedbacks = new ArrayList<>();
            Path fbPath = Path.of("feedback.txt");
            if (Files.exists(fbPath)) {
                for (String line : Files.readAllLines(fbPath)) {
                    Feedback f = Feedback.fromCSV(line);
                    if (f != null) feedbacks.add(f);
                }
            }
            String[] fbCols = {"Feedback ID", "Order ID", "Rating", "Comment"};
            DefaultTableModel fbModel = new DefaultTableModel(fbCols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            for (Feedback f : feedbacks) {
                fbModel.addRow(new Object[]{
                        f.getFeedbackId(),
                        f.getOrderId(),
                        String.format("%.1f", f.getRating()),
                        f.getComment()
                });
            }
            JTable fbTable = new JTable(fbModel);
            fbTable.setAutoCreateRowSorter(true);
            fbTable.getColumnModel().getColumn(0).setCellRenderer(new IdShortenerRenderer());
            fbTable.getColumnModel().getColumn(1).setCellRenderer(new IdShortenerRenderer());
            JScrollPane fbScroll = new JScrollPane(fbTable);
            fbScroll.setBorder(BorderFactory.createTitledBorder("Feedback Records"));
            fbScroll.setPreferredSize(new Dimension(400, 200));

            JButton exportBtn = new JButton("Export to CSV");
            exportBtn.addActionListener(evt -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File dir = chooser.getSelectedFile();
                    try {
                        TableExporter.exportToCSV(payModel, new File(dir, "payments.csv"));
                        TableExporter.exportToCSV(fbModel,  new File(dir, "feedback.csv"));
                        JOptionPane.showMessageDialog(this,
                                "Exported to:\n" +
                                        dir + "/payments.csv\n" +
                                        dir + "/feedback.csv",
                                "Export Successful",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } catch (IOException io) {
                        JOptionPane.showMessageDialog(this,
                                "Export failed: " + io.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });
            JPanel tablesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            tablesPanel.add(payScroll);
            tablesPanel.add(fbScroll);

            JPanel container = new JPanel(new BorderLayout(0, 10));
            container.add(tablesPanel, BorderLayout.CENTER);
            JPanel btnPanel = new JPanel();
            btnPanel.add(exportBtn);
            container.add(btnPanel, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(
                    this,
                    container,
                    "Payment & Feedback Analysis",
                    JOptionPane.PLAIN_MESSAGE
            );

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load records: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage(context);
            this.dispose();
        });
    }
}
