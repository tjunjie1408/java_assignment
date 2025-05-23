package UIPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesmanMain extends JFrame{
    private JPanel panel1;
    private JLabel SalesmanProfile;
    private JButton editProfileButton;
    private JButton viewAndUpdateStatusButton;
    private JButton collectPaymentAndCommentButton;
    private JButton viewSalesRecordsButton;
    private JButton exitButton;

    public SalesmanMain() {
        setContentPane(panel1);
        setTitle(SalesmanProfile.getText());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        editProfileButton.addActionListener(e -> {

        });
        viewAndUpdateStatusButton.addActionListener(e -> {

        });
        collectPaymentAndCommentButton.addActionListener(e -> {

        });
        viewSalesRecordsButton.addActionListener(e -> {

        });
        exitButton.addActionListener(e -> {
            new LoginPage();
            this.dispose();
        });
    }
}
