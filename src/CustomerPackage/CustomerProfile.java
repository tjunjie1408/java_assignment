package CustomerPackage;

import javax.swing.*;

public class CustomerProfile extends JFrame{
    private JPanel panel1;
    private JLabel Profile;
    private JButton EditProfile;
    private JButton ViewAvailableCar;
    private JButton Logout;
    private JButton ViewPurchaseHistories;
    private CustomerManagement customerManagement;
    private String customerId;

    public CustomerProfile(CustomerManagement cm, String customerId) {
        this.customerManagement = cm;
        this.customerId = customerId;
        initUI();
    }

    private void initUI() {
        setContentPane(panel1);
        setTitle(Profile.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        Logout.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new CustomerMain();
            this.dispose();
        });

        EditProfile.addActionListener(e -> {

        });
        ViewAvailableCar.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are viewing available cars");
            new CustomerBookedCar(customerManagement,customerId);
        });
        ViewPurchaseHistories.addActionListener(e -> {

        });
    }
}
