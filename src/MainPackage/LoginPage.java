package MainPackage;

import CustomerPackage.CustomerMain;
import ManagerPackage.ManagerLogin;
import SalesmanPackage.SalesmanLogin;
import UserPackage.*;

import javax.swing.*;

public class LoginPage extends JFrame{
    private JPanel panel1;
    private JButton CustomerChoice;
    private JButton SalesmanChoice;
    private JButton ManagerChoice;
    private JLabel LoginTitle;
    private JButton Exit;
    private AppContext context = new AppContext();
    private String salesId;

    public LoginPage() {
        setContentPane(panel1);
        setTitle(LoginTitle.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        CustomerChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Customer Main Page");
            new CustomerMain();
            this.dispose();
        });
        SalesmanChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Salesman Main Page");
            new SalesmanLogin(context, salesId);
            this.dispose();
        });
        ManagerChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Manager Main Page");
            new ManagerLogin();
            this.dispose();
        });
        Exit.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thank you for using our system");
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
