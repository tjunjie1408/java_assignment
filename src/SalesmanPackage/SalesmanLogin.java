package SalesmanPackage;

import MainPackage.AppContext;
import MainPackage.LoginPage;
import javax.swing.*;
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
    private AppContext context;

    public SalesmanLogin(AppContext context) {
        this.context = context;
        setContentPane(panel1);
        setTitle(SalesmanLoginPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        cancelButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage(context);
            this.dispose();
        });
        loginButton.addActionListener(e -> {
            String username = UsernameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username and password cannot be empty",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String salesmanId = authenticateAndGetId(username, password);
            if (salesmanId != null) {
                JOptionPane.showMessageDialog(this,
                        "Login successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new SalesmanMain(context, salesmanId);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private String authenticateAndGetId(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7
                        && parts[1].trim().equals(username)
                        && parts[2].trim().equals(password)
                        && parts[6].trim().equalsIgnoreCase("SALESMAN")) {
                    return parts[0].trim();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading salesmen file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
