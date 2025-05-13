package UIPackage;

import javax.swing.*;

public class LoginPage extends JFrame{
    private JPanel panel1;
    private JButton CustomerChoice;
    private JButton SalesmanChoice;
    private JButton ManagerChoice;
    private JLabel LoginTitle;
    private JButton Exit;

    public LoginPage() {
        setContentPane(panel1);
        setTitle(LoginTitle.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        CustomerChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Customer Login");
            new CustomerMain();
            this.dispose();
        });
        SalesmanChoice.addActionListener(e -> {

        });
        ManagerChoice.addActionListener(e -> {

        });
        Exit.addActionListener(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
