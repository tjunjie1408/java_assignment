package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPage1 implements ActionListener {
    public void actionPerformed(ActionEvent e){
    try{
        if(e.getSource() == logout){
            int input = JOptionPane.showConfirmDialog(null,"Do you want logout user page?");
            if (input == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }else if(e.getSource() == register){
            String name = JOptionPane.showInputDialog("Enter your name:");
            if(DataIO.searchName(name) != null){
                throw new Exception();
            }
            int password = Integer.parseInt(JOptionPane.showInputDialog("Enter your password:"));
            int phone_number = Integer.parseInt(JOptionPane.showInputDialog("Enter your phone number:"));
            String userID = DataIO.getNextUserID();
            DataIO.allUser.add(new User(userID,name,password,phone_number));
            DataIO.write();
            JOptionPane.showMessageDialog(null,"Register successfully! Your userID is: " + userID);
        }else if(e.getSource() == login){
            String name = JOptionPane.showInputDialog("Enter your name:");
            if(DataIO.searchName(name) == null){
                throw new Exception();
            }
            int password = Integer.parseInt(JOptionPane.showInputDialog("Enter your password:"));
            if(password != DataIO.searchName(name).password){
                throw new Exception();
            }
            Main.loginUser = DataIO.searchName(name);
            Main.second = new UserPage2(Main.loginUser.name);
            Main.first.x.setVisible(false);
            Main.second.x.setVisible(true);
        }
    }catch (Exception ex){
        JOptionPane.showMessageDialog(null,"Invalid input!");
    }
}

    JFrame x;
    Button register, login, logout;
    public UserPage1(){
        x = new JFrame();
        x.setSize(300,100);
        x.setLocation(1000,200);

        JLabel welcomeLabel = new JLabel("Welcome user page!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        register = new Button("Register");
        login = new Button("Login");
        logout = new Button("Logout");

        register.addActionListener(this);
        login.addActionListener(this);
        logout.addActionListener(this);

        x.setLayout(new BorderLayout());
        x.add(welcomeLabel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(register);
        buttonPanel.add(login);
        buttonPanel.add(logout);
        x.add(buttonPanel, BorderLayout.CENTER);

        x.setVisible(true);

    }
}
