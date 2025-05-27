package ManagerPackage;

import MainPackage.AppContext;
import MainPackage.LoginPage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ManagerLogin extends JFrame{
    private JPanel panel1;
    private JLabel ManagerLoginPage;
    private JTextField NameTextField;
    private JButton cancelButton;
    private JButton loginButton;
    private JPasswordField PasswordField;
    private AppContext context;

    public ManagerLogin(AppContext context){
        this.context = context;
        setContentPane(panel1);
        setTitle(ManagerLoginPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        cancelButton.addActionListener( e ->{
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage(context);
            this.dispose();
        });
        loginButton.addActionListener(e -> {
            String username = NameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = authenticateAndGetId(username, password);
            if (id != null) {
                context.setCurrentManagerId(id);
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new ManagersMain(context);
                this.dispose();
            } else if ("superadmin".equals(username) && "admin123".equals(password)) {
                context.setCurrentManagerId(username);
                new SuperAdminPage(context);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private String authenticateAndGetId(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("managers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6
                        && parts[1].trim().equals(username)
                        && parts[2].trim().equals(password)
                        && "MANAGER".equalsIgnoreCase(parts[6].trim())) {
                    return parts[0].trim();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading managers file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
