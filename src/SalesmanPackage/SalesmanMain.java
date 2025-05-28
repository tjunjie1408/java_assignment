package SalesmanPackage;

import CarPackage.CarManagement;
import CustomerPackage.CustomerManagement;
import MainPackage.AppContext;
import MainPackage.IdShortenerRenderer;
import MainPackage.LoginPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
                Salesman s = context.getSalesmanManagement().findById(salesmanId);
                switch (field) {
                    case "Username" -> s.setUsername(newValue.trim());
                    case "Password" -> s.setPassword(newValue.trim());
                    case "Email"    -> s.setEmail(newValue.trim());
                    case "Phone"    -> s.setPhoneNumber(newValue.trim());
                }
                context.getSalesmanManagement().updateSalesman(s);
                JOptionPane.showMessageDialog(this, field + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to update " + field + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
