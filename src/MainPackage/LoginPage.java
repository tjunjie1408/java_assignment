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
    private AppContext context;
    private String salesId;
    private String customerId;

    public LoginPage(AppContext context) {
        this.context = context;
        setContentPane(panel1);
        setTitle(LoginTitle.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        CustomerChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Customer Main Page");
            new CustomerMain(context);
            this.dispose();
        });
        SalesmanChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Salesman Main Page");
            new SalesmanLogin(context);
            this.dispose();
        });
        ManagerChoice.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Manager Main Page");
            new ManagerLogin(context);
            this.dispose();
        });
        Exit.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thank you for using our system");
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        AppContext appContext = new AppContext();
        new LoginPage(appContext);
    }
}
