package ManagerPackage;

import javax.swing.*;

public class ManagerManageCustomer extends JFrame{
    private JPanel panel1;
    private JButton approveCustomerRegistrationButton;
    private JButton deleteCustomerProfileButton;
    private JButton searchCustomerProfileButton;
    private JButton updateCustomerProfileButton;
    private JLabel CustomerProfileManagement;

    public ManagerManageCustomer() {
        setContentPane(panel1);
        setTitle(CustomerProfileManagement.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        approveCustomerRegistrationButton.addActionListener(e -> {
            new ManagerApproved();
            this.dispose();
        });
        deleteCustomerProfileButton.addActionListener(e -> {

        });
        searchCustomerProfileButton.addActionListener(e -> {

        });
        updateCustomerProfileButton.addActionListener(e -> {

        });
    }
}
