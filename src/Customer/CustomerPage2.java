package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerPage2 implements ActionListener {
    public void actionPerformed(ActionEvent e){
        try{
            if (e.getSource() == exit){
                int input = JOptionPane.showConfirmDialog(null,"Do you want logout user features page?","Confirmation", JOptionPane.YES_NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {
                    Main.loginCustomer = null;
                    Main.first.x.setVisible(true);
                    Main.second.x.setVisible(false);
                }

            }else if(e.getSource() == profile){
            if (Main.loginCustomer == null) {
                JOptionPane.showMessageDialog(null, "No user is currently logged in.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
                }
                User currentUser = DataIO.searchName(Main.loginCustomer.name);
                Object[] options = {"Edit", "Exit"};
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "User Profile:\n" +
                                "ID: " + currentUser.customerID + "\n" +
                                "Name: " + currentUser.name + "\n" +
                                "Password: " + currentUser.password + "\n" +
                                "Phone: " + currentUser.phone_number + "\n" +
                                "Address: " + currentUser.address,
                        "User Profile",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == 0) {
                    String newName = JOptionPane.showInputDialog("Enter new name:", currentUser.name);
                    if (newName != null && !newName.trim().isEmpty()) {
                        currentUser.name = newName;
                    }else if (newName == null) {
                       JOptionPane.showMessageDialog(null, "Profile edit canceled. Returning to feature page.", "Info", JOptionPane.INFORMATION_MESSAGE);
                       return;
                    }

                    String newPassword = JOptionPane.showInputDialog("Enter new password:", currentUser.password);
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        currentUser.password = Integer.parseInt(newPassword);
                    }else if (newPassword == null) {
                        JOptionPane.showMessageDialog(null, "Profile edit canceled. Returning to feature page.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    String newPhone = JOptionPane.showInputDialog("Enter new phone number:", currentUser.phone_number);
                    try {
                        if (newPhone != null && !newPhone.trim().isEmpty()) {
                            currentUser.phone_number = Integer.parseInt(newPhone);
                        }else if (newPhone == null) {
                            JOptionPane.showMessageDialog(null, "Profile edit canceled. Returning to feature page.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                         "Invalid phone number format.",
                         "Error",
                         JOptionPane.ERROR_MESSAGE);
                    }

                    String newAddress = JOptionPane.showInputDialog("Enter new address:", currentUser.address);
                    if (newAddress != null && !newAddress.trim().isEmpty()) {
                        currentUser.address = newAddress;
                    }else if (newAddress == null) {
                        JOptionPane.showMessageDialog(null, "Profile edit canceled. Returning to feature page.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    DataIO.write();
                    JOptionPane.showMessageDialog(null,
                            "Profile updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (choice == 1) {
                    JOptionPane.showMessageDialog(null, "Exiting profile section.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User profile not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }

        }else if(e.getSource() == car_detail){
                //output car details

        }else if(e.getSource() == car_request){
            String customerID = "customerID123";
            CustomerRequest request = new CustomerRequest();
            request.requestCar(customerID);

        }else if(e.getSource() == feedback){
                int number = DataIO.allfeedback.size()+10001;
                Object[] options = {"1", "2", "3", "4", "5"};
                int rating = JOptionPane.showOptionDialog(
                        null,
                        "Please rate the service",
                        "Rating",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                String review = JOptionPane.showInputDialog("Enter your feedback:");
                if(review == null || review.trim().isEmpty()){
                    throw new Exception();
                }
                feedback t = new feedback(number,String.valueOf(rating+1),review,Main.loginCustomer);
                DataIO.allfeedback.add(t);
                Main.loginCustomer.userfeedback.add(t);
                DataIO.write();
                JOptionPane.showMessageDialog(null,
                        "Thank you for your rating and feedback!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

        }else if(e.getSource() == history){

        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Invalid input!");
        }
    }
    JFrame x;
    Button profile,car_detail, car_request,feedback,history,exit;
    public CustomerPage2(String username){
        x = new JFrame();
        x.setSize(600,500);
        x.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome " +username+" ! " +
                "Here is user features page! " +
                "Please choose the option below:");
        welcomeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        profile = new Button("User Profile");
        car_detail = new Button("Car Details");
        car_request = new Button("Car Booking");
        feedback = new Button("Rating and Feedback");
        history = new Button("Purchase History");
        exit = new Button("Exit");

        profile.addActionListener(this);
        car_detail.addActionListener(this);
        car_request.addActionListener(this);
        feedback.addActionListener(this);
        history.addActionListener(this);
        exit.addActionListener(this);

        x.setLayout(new BoxLayout(x.getContentPane(), BoxLayout.Y_AXIS));
        x.add(welcomeLabel);

        x.add(createButtonBox(profile, 200, 50));
        x.add(createButtonBox(car_detail, 200, 50));
        x.add(createButtonBox(car_request, 200, 50));
        x.add(createButtonBox(feedback, 200, 50));
        x.add(createButtonBox(history, 200, 50));
        x.add(createButtonBox(exit, 200, 50));

    }

    private JPanel createButtonBox(Button button, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        button.setPreferredSize(new Dimension(width, height));
        panel.add(button);
        return panel;
    }
}
