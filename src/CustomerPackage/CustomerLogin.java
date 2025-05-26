package CustomerPackage;

import MainPackage.AppContext;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomerLogin extends JFrame{
    private JPanel panel1;
    private JTextField UsernameTextField;
    private JButton Cancel;
    private JButton Login;
    private JLabel LoginPage;
    private JLabel Username;
    private JLabel Password;
    private JPasswordField PasswordField;
    private String customerId;
    private AppContext context;
    public CustomerLogin(AppContext context, String customerId) {
        this.context = context;
        this.customerId = customerId;
        initUI();
    }
    private void initUI() {
        setContentPane(panel1);
        assert LoginPage != null;
        setTitle(LoginPage.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        assert Cancel != null;
        Cancel.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You are back to the main page");
            dispose();
            new CustomerMain(context);
        });

        assert Login != null;
        Login.addActionListener(e -> {
            String username = UsernameTextField.getText().trim();
            String password = new String(PasswordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = checkLogin(username, password);
            if (id != null) {
                JOptionPane.showMessageDialog(this,
                        "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new CustomerProfile(context, id);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    public String checkLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 &&
                        parts[1].trim().equals(username) &&
                        parts[2].trim().equals(password) &&
                        parts[5].trim().equals("APPROVED")) {
                    return parts[0];
                }
            }
        }catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading customers.txt: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}

