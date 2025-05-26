package SalesmanPackage;

import CarPackage.CarManagement;
import CustomerPackage.CustomerManagement;
import MainPackage.AppContext;
import MainPackage.LoginPage;

import javax.swing.*;

public class SalesmanMain extends JFrame{
    private final String salesmanId;
    private JPanel panel1;
    private JLabel SalesmanProfile;
    private JButton editProfileButton;
    private JButton viewAndUpdateStatusButton;
    private JButton collectPaymentAndCommentButton;
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

        });
        viewAndUpdateStatusButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "View and Update Status");
            new SalesmanEditCar(context ,salesmanId);
            this.dispose();
        });
        collectPaymentAndCommentButton.addActionListener(e -> {

        });
        viewSalesRecordsButton.addActionListener(e -> {

        });
        exitButton.addActionListener(e -> {
            new LoginPage();
            this.dispose();
        });
    }
}
