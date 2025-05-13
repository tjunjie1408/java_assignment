package UIPackage;

import javax.swing.*;

import CustomerPackage.*;
import UserPackage.*;

public class CustomerRegister extends JFrame{
    private JPanel panel1;
    private JTextField UsernameTextField;
    private JLabel Username;
    private JTextField PasswordTextField;
    private JLabel Password;
    private JTextField EmailTextField;
    private JLabel Email;
    private JButton Cancel;
    private JButton Submit;
    private JFormattedTextField PhoneNumberFormattedTextField1;
    private JLabel PhoneNumber;

    CustomerManagement customerManagement = new CustomerManagement();
    CustomerRegister customerRegister = new CustomerRegister(customerManagement);
    public CustomerRegister(CustomerManagement cm) {
        this.customerManagement = cm;
    }


    public CustomerRegister() {
        setContentPane(panel1);
        setTitle("Customer Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        Cancel.addActionListener(e -> {
            this.dispose();
        });
        Submit.addActionListener(e -> {
            String username = UsernameTextField.getText().trim();
            String password = PasswordTextField.getText().trim();
            String email = EmailTextField.getText().trim();
            String phoneNumber = PhoneNumberFormattedTextField1.getText().trim();
            Customer newCustomer = new Customer(username, password, email, phoneNumber);
            boolean success = customerManagement.registerCustomer(newCustomer);
            if (success) {
                JOptionPane.showMessageDialog(this, "Customer registered successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.");
            }
        });
    }
}
