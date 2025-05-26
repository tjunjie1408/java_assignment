package ManagerPackage;

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

    public ManagerLogin(){
        setContentPane(panel1);
        setTitle(ManagerLoginPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        cancelButton.addActionListener( e ->{
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            new LoginPage();
            this.dispose();
        });
        loginButton.addActionListener(e -> {
            String username = NameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                try {
                    new ManagersMain();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.dispose();
            }else if(username.equals("superadmin") && password.equals("admin123")){
                new SuperAdminPage();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("managers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 &&
                        parts[1].trim().equals(username) &&
                        parts[2].trim().equals(password) &&
                        parts[6].trim().equals("MANAGER")) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading managers file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return false;
    }
}
