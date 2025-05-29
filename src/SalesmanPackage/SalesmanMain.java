package SalesmanPackage;

import CarPackage.CarManagement;
import CustomerPackage.CustomerManagement;
import MainPackage.AppContext;
import MainPackage.IdShortenerRenderer;
import MainPackage.LoginPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SalesmanMain extends JFrame{
    private final String salesmanId;
    private JPanel panel1;
    private JLabel SalesmanProfile;
    private JButton editProfileButton;
    private JButton viewAndUpdateStatusButton;
    private JButton viewSalesRecordsButton;
    private JButton exitButton;
    private AppContext context;

    public SalesmanMain(AppContext context, String salesmanId) {
        this.context = context;
        this.salesmanId = salesmanId;
        setContentPane(panel1);
        setTitle(SalesmanProfile.getText());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        editProfileButton.addActionListener(e -> {
            Salesman s = context.getSalesmanManagement().findById(salesmanId);
            if (s == null) {
                JOptionPane.showMessageDialog(this,
                        "Your profile could not be loaded. Please log in again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String info = String.format(
                    "Username: %s%nEmail:    %s%nPhone:    %s",
                    s.getUsername(),
                    s.getEmail(),
                    s.getPhoneNumber()
            );
            JOptionPane.showMessageDialog(this,
                    info,
                    "Your Profile",
                    JOptionPane.INFORMATION_MESSAGE);

            String[] options = {
                    "Edit Username",
                    "Edit Password",
                    "Edit Email",
                    "Edit Phone",
                    "Delete Profile"
            };
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
                    boolean ok = context.getSalesmanManagement().deleteSalesman(s.getUsername());
                    if (ok) {
                        JOptionPane.showMessageDialog(this,
                                "Your profile has been deleted.",
                                "Deleted", JOptionPane.INFORMATION_MESSAGE);
                        new LoginPage(context);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete profile.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }

            String field = action.replace("Edit ", "");  // yields "Username", "Password", etc.
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
                case "Username" -> s.setUsername(newValue.trim());
                case "Password" -> s.setPassword(newValue.trim());
                case "Email"    -> s.setEmail(newValue.trim());
                case "Phone"    -> s.setPhoneNumber(newValue.trim());
                default -> {
                    JOptionPane.showMessageDialog(this,
                            "Unknown action: " + action,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try {
                boolean updated = context.getSalesmanManagement().updateSalesman(s);
                if (updated) {
                    JOptionPane.showMessageDialog(this,
                            field + " updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new IOException("Failed to save changes");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to update " + field + ": " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewAndUpdateStatusButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "View and Update Status");
            new SalesmanEditCar(context ,salesmanId);
            this.dispose();
        });

        viewSalesRecordsButton.addActionListener(e -> {
            List<SalesRecord> recs = context
                        .getSalesmanManagement()
                        .getSalesRecordsBySalesman(salesmanId);
            if (recs.isEmpty()) {
                JOptionPane.showMessageDialog(
                            this,
                            "No sales records found.",
                            "Sales Records",
                            JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            String[] cols = {"Record ID", "Order ID", "Comment"};
            DefaultTableModel m = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            for (SalesRecord r : recs) {
                m.addRow(new Object[]{
                        r.getRecordId(),
                        r.getOrderId(),
                        r.getComment()
                });
            }
            JTable table = new JTable(m);
            table.setAutoCreateRowSorter(true);
            table.getColumnModel().getColumn(1).setCellRenderer(new IdShortenerRenderer());
            table.getColumnModel().getColumn(0).setCellRenderer(new IdShortenerRenderer());
                JScrollPane scroll = new JScrollPane(table);
                scroll.setPreferredSize(new Dimension(600, 300));
                JOptionPane.showMessageDialog(
                        this,
                        scroll,
                        "Your Sales Records",
                        JOptionPane.PLAIN_MESSAGE
                );
        });
        exitButton.addActionListener(e -> {
            new LoginPage(context);
            this.dispose();
        });
    }
}
