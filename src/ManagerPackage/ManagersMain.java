package ManagerPackage;

import CarPackage.CarManagement;
import CustomerPackage.CustomerManagement;
import SalesmanPackage.SalesmanManagement;
import MainPackage.LoginPage;

import javax.swing.*;

public class ManagersMain extends JFrame{
    private JPanel panel1;
    private JButton manageManagerProfileButton;
    private JButton manageSalesmanProfileButton;
    private JButton manageCustomerProfileButton;
    private JButton manageCarDetailsButton;
    private JButton paymentAndFeedbackAnalysisButton;
    private JButton exitButton;
    private JLabel ManagerMainPage;
    private CarManagement carManagement;
    private CustomerManagement customerManagement;
    private SalesmanManagement salesmanManagement;

    public ManagersMain() {
        carManagement = new CarManagement();
        customerManagement = new CustomerManagement();
        salesmanManagement = new SalesmanManagement();
        setContentPane(panel1);
        setTitle(ManagerMainPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        manageManagerProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Manager Profile clicked");
        });
        manageSalesmanProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Salesman Profile clicked");
            new ManagerManageSalesman(salesmanManagement);
            this.dispose();
        });
        manageCustomerProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Customer Profile clicked");
            new ManagerManageCustomer();
            this.dispose();
        });
        manageCarDetailsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Car Details clicked");
            new ManagerManageCars();
            this.dispose();
        });
        paymentAndFeedbackAnalysisButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Payment and Feedback Analysis clicked");
        });
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage();
            this.dispose();
        });
    }
}
