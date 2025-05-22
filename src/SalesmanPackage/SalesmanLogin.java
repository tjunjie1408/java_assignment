package SalesmanPackage;
import UIPackage.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SalesmanLogin extends JFrame{
    private JPanel panel1;
    private JTextField UsernameTextField;
    private JPasswordField PasswordField;
    private JLabel SalesmanLoginPage;
    private JLabel Username;
    private JLabel Password;
    private JButton cancelButton;
    private JButton loginButton;

    public SalesmanLogin() {
        setContentPane(panel1);
        setTitle(SalesmanLoginPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        cancelButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage();
            this.dispose();
        });
        loginButton.addActionListener(e -> {
            String username = UsernameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();

            // Check for empty fields
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(SalesmanLogin.this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Authenticate the user
            if (authenticate(username, password)) {
                JOptionPane.showMessageDialog(SalesmanLogin.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new SalesmanMain();
            } else {
                JOptionPane.showMessageDialog(SalesmanLogin.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 &&
                        parts[1].trim().equals(username) &&
                        parts[2].trim().equals(password) &&
                        parts[5].trim().equals("SALESMAN")) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading salesmen file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return false;
    }
}
