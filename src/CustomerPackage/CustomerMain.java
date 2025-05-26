package CustomerPackage;

import javax.swing.*;

import CarPackage.CarManagement;
import MainPackage.LoginPage;

public class CustomerMain extends JFrame{
    private JPanel panel1;
    private JLabel CustomerLoginTitle;
    private JButton Register;
    private JButton Login;
    private JButton Exit;
    private String customerId;

    public CustomerMain() {
        CarManagement carManagement = new CarManagement();
        CustomerManagement cm = new CustomerManagement();
        setContentPane(panel1);
        setTitle(CustomerLoginTitle.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        Register.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Customer Register");
            new CustomerRegister(cm);
            this.dispose();
        });
        Login.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Customer Login");
            new CustomerLogin(cm,customerId);
            this.dispose();
        });
        Exit.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Welcome to use our system again");
            new LoginPage();
            this.dispose();
        });
    }
}
