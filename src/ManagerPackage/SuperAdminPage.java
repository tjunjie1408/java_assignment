package ManagerPackage;

import javax.swing.*;

public class SuperAdminPage extends JFrame{
    private JPanel panel1;
    private JLabel SuperAdminPage;
    private JTextField UsernameTextField;
    private JTextField EmailTextField;
    private JLabel Username;
    private JButton exitButton;
    private JButton addButton;
    private JPasswordField PasswordField;
    private JLabel PhoneNumber;
    private JLabel Email;
    private JLabel Password;
    private JTextField PhoneNumberTextField;

    public SuperAdminPage() {
        setContentPane(panel1);
        setTitle(SuperAdminPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        exitButton.addActionListener( e ->{
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new ManagerLogin();
            this.dispose();
        });
        addButton.addActionListener(e -> {
            String username = UsernameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();
            String email = EmailTextField.getText().trim();
            String phoneNumber = PhoneNumberTextField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Manager newManager = new Manager(username, password, email, phoneNumber);
            ManagerManagement managerManagement = ManagerManagement.getInstance();
            if (managerManagement.addManager(newManager)) {
                JOptionPane.showMessageDialog(this, "Manager added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                UsernameTextField.setText("");
                PasswordField.setText("");
                EmailTextField.setText("");
                PhoneNumberTextField.setText("");
                new ManagerLogin();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}