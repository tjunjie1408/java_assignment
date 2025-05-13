package UIPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMain extends JFrame{
    private JPanel panel1;
    private JLabel CustomerLoginTitle;
    private JButton Register;
    private JButton Login;
    private JButton Exit;

    public CustomerMain() {
        setContentPane(panel1);
        setTitle(CustomerLoginTitle.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        Register.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Customer Register");
            new CustomerRegister();
            this.dispose();
        });
        Login.addActionListener(e -> {

        });
        Exit.addActionListener(e -> {
            System.exit(0);
        });
    }
}
