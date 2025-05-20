package CustomerPackage;

import javax.swing.*;

public class CustomerProfile extends JFrame{
    private JPanel panel1;
    private JLabel Profile;
    private JButton EditProfile;
    private JButton ViewAvailableCar;
    private JButton GiveFeedbackAndRatings;
    private JButton Logout;
    private JButton ViewPurchaseHistories;
    private CustomerManagement customerManagement;

    public CustomerProfile(CustomerManagement cm) {
        this.customerManagement = cm;
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

        });
        GiveFeedbackAndRatings.addActionListener(e -> {

        });
        ViewPurchaseHistories.addActionListener(e -> {

        });
    }
}
