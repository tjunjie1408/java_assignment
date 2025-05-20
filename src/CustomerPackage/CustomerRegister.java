package CustomerPackage;

import javax.swing.*;

public class CustomerRegister extends JFrame {
    private JPanel panel1;
    private JTextField UsernameTextField;
    private JLabel Username;
    private JLabel Password;
    private JTextField EmailTextField;
    private JLabel Email;
    private JButton Cancel;
    private JButton Submit;
    private JFormattedTextField PhoneNumberFormattedTextField1;
    private JLabel PhoneNumber;
    private JPasswordField PasswordField;

    private final CustomerManagement customerManagement;

    public CustomerRegister(CustomerManagement cm) {
        this.customerManagement = cm;
        initUI();
    }

    private void initUI() {
        setContentPane(panel1);
        setTitle("Customer Register");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        assert Cancel != null;
        Cancel.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new CustomerMain();
            this.dispose();
        });
        assert Submit != null;
        Submit.addActionListener(e -> {
            String username = UsernameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();
            String email = EmailTextField.getText().trim();
            String phoneNumber = PhoneNumberFormattedTextField1.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username, password and email cannot be empty", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            Customer newCustomer = new Customer(username, password, email, phoneNumber);
            newCustomer.setEmail(email);
            newCustomer.setPhoneNumber(phoneNumber);

            if (!phoneNumber.isEmpty()) {
                newCustomer.setPhoneNumber(phoneNumber);
            }
            try {
                customerManagement.registerCustomer(newCustomer);
                String id = newCustomer.getCustomerId();
                JOptionPane.showMessageDialog(this, "Registration successful! Your ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new CustomerProfile(customerManagement);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}


