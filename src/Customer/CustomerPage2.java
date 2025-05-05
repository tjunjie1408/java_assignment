package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class CustomerPage2 implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == exit) {
                int input = JOptionPane.showConfirmDialog(null, "Do you want logout user features page?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {
                    Main.loginCustomer = null;
                    Main.first.x.setVisible(true);
                    Main.second.x.setVisible(false);
                }

            } else if (e.getSource() == profile) {
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
                    } else if (newName == null) {
                        JOptionPane.showMessageDialog(null, "Profile edit canceled. Returning to feature page.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    String newPassword = JOptionPane.showInputDialog("Enter new password:", currentUser.password);
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        currentUser.password = Integer.parseInt(newPassword);
                    } else if (newPassword == null) {
                        JOptionPane.showMessageDialog(null, "Profile edit canceled. Returning to feature page.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    String newPhone = JOptionPane.showInputDialog("Enter new phone number:", currentUser.phone_number);
                    try {
                        if (newPhone != null && !newPhone.trim().isEmpty()) {
                            currentUser.phone_number = Integer.parseInt(newPhone);
                        } else if (newPhone == null) {
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
                    } else if (newAddress == null) {
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

        } else if (e.getSource() == car_detail) {
                JFrame carFrame = new JFrame("Select Car by Brand & Model");
                carFrame.setSize(400, 300);
                carFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                carFrame.setLayout(null);

                ArrayList<CarDetails> carDetailsList = new ArrayList<>();
                LinkedHashSet<String> carBrandSet = new LinkedHashSet<>();
                List<JFrame> detailsFrames = new ArrayList<>();

                JComboBox<String> carBrandDropdown = new JComboBox<>();
                JComboBox<String> carModelDropdown = new JComboBox<>();
                JButton enterButton = new JButton("Enter");
                JButton exitButton = new JButton("Exit");

                try (BufferedReader reader = new BufferedReader(new FileReader("CarDetails.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }

                        String carBrand = reader.readLine();
                        String carModel = reader.readLine();
                        String carColour = reader.readLine();
                        String carStatus = reader.readLine();
                        String carPriceString = reader.readLine();

                        if (carBrand == null || carModel == null || carColour == null
                                || carStatus == null || carPriceString == null) {
                            throw new IllegalArgumentException("CarDetails.txt file is incomplete or improperly formatted.");
                        }

                        int carPrice;
                        try {
                            carPrice = Integer.parseInt(carPriceString.trim());
                        } catch (NumberFormatException nfe) {
                            throw new NumberFormatException("Invalid car price value: " + carPriceString);
                        }

                        if (!"Available".equalsIgnoreCase(carStatus.trim())) {
                            continue;
                        }

                        carBrandSet.add(carBrand.trim());

                        carDetailsList.add(new CarDetails(line, carBrand.trim(),
                                carModel.trim(), carColour.trim(), carStatus.trim(), carPrice));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(carFrame,
                            "Error loading car details. Please check the file format.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                carBrandDropdown.addItem("Select Brand");
                for (String brand : carBrandSet) {
                    carBrandDropdown.addItem(brand);
                }

                carModelDropdown.addItem("Select Model");

                carBrandDropdown.addActionListener(event -> {
                    String selectedBrand = (String) carBrandDropdown.getSelectedItem();

                    if (!"Select Brand".equalsIgnoreCase(selectedBrand)) {
                        carModelDropdown.removeAllItems();
                        carModelDropdown.addItem("Select Model");

                        LinkedHashSet<String> uniqueModels = new LinkedHashSet<>();
                        for (CarDetails car : carDetailsList) {
                            if (car.carBrand.equalsIgnoreCase(selectedBrand)) {
                                uniqueModels.add(car.carModel);
                            }
                        }

                        for (String model : uniqueModels) {
                            carModelDropdown.addItem(model);
                        }
                    }
                });

                enterButton.addActionListener(event -> {
                    String selectedBrand = (String) carBrandDropdown.getSelectedItem();
                    String selectedModel = (String) carModelDropdown.getSelectedItem();

                    if (!"Select Brand".equalsIgnoreCase(selectedBrand) &&
                            !"Select Model".equalsIgnoreCase(selectedModel)) {
                        JFrame detailsFrame = new JFrame("Car Details - " + selectedBrand + " " + selectedModel);
                        detailsFrame.setSize(400, 400);
                        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel detailsPanel = new JPanel();
                        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        for (CarDetails car : carDetailsList) {
                            if (car.carBrand.equalsIgnoreCase(selectedBrand) &&
                                    car.carModel.equalsIgnoreCase(selectedModel)) {
                                detailsPanel.add(new JLabel("ID: " + car.carID));
                                detailsPanel.add(new JLabel("Brand: " + car.carBrand));
                                detailsPanel.add(new JLabel("Model: " + car.carModel));
                                detailsPanel.add(new JLabel("Colour: " + car.carColour));
                                detailsPanel.add(new JLabel("Status: " + car.carStatus));
                                detailsPanel.add(new JLabel("Price: $" + car.carPrice));
                                detailsPanel.add(new JLabel("--------------------------"));
                            }
                        }

                        detailsFrame.add(new JScrollPane(detailsPanel));
                        detailsFrames.add(detailsFrame);
                        detailsFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(carFrame,
                                "Please select both Brand and Model.",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                });

                exitButton.addActionListener(event -> {
                    for (JFrame detailsFrame : detailsFrames) {
                        detailsFrame.dispose();
                    }
                    carFrame.dispose();
                });

                JLabel brandLabel = new JLabel("Select Brand:");
                brandLabel.setBounds(50, 50, 100, 30);
                carBrandDropdown.setBounds(150, 50, 200, 30);

                JLabel modelLabel = new JLabel("Select Model:");
                modelLabel.setBounds(50, 100, 100, 30);
                carModelDropdown.setBounds(150, 100, 200, 30);

                enterButton.setBounds(150, 150, 100, 30);
                exitButton.setBounds(150, 200, 100, 30);

                carFrame.add(brandLabel);
                carFrame.add(carBrandDropdown);
                carFrame.add(modelLabel);
                carFrame.add(carModelDropdown);
                carFrame.add(enterButton);
                carFrame.add(exitButton);

                carFrame.setVisible(true);
                carFrame.setLocationRelativeTo(null);

        }else if (e.getSource() == feedback) {
                JFrame ratingFrame = new JFrame("Rate Our Service");
                ratingFrame.setSize(400, 300);
                ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ratingFrame.setLayout(null);

                JLabel promptLabel = new JLabel("Please rate the service:");
                promptLabel.setBounds(50, 30, 300, 30);

                JSlider ratingSlider = new JSlider(1, 5, 3); // Min: 1, Max: 5, Default: 3
                ratingSlider.setBounds(50, 70, 300, 50);
                ratingSlider.setMajorTickSpacing(1);
                ratingSlider.setPaintLabels(true);
                ratingSlider.setPaintTicks(true);

                JLabel feedbackLabel = new JLabel("Enter your feedback:");
                feedbackLabel.setBounds(50, 130, 300, 30);

                JTextField feedbackField = new JTextField();
                feedbackField.setBounds(50, 170, 300, 30);

                JButton submitButton = new JButton("Submit");
                submitButton.setBounds(50, 220, 100, 30);

                JButton exitButton = new JButton("Exit");
                exitButton.setBounds(250, 220, 100, 30);

                submitButton.addActionListener(event -> {
                    int selectedRating = ratingSlider.getValue();
                    String review = feedbackField.getText();

                    if (review == null || review.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(ratingFrame,
                                "Feedback cannot be empty. Please provide a review.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String feedbackID = DataIO.getNextFeedbackID();
                    DataIO.allfeedback.add(new feedback(feedbackID, String.valueOf(selectedRating), review, Main.loginCustomer));
                    DataIO.write();

                    // Success message
                    JOptionPane.showMessageDialog(ratingFrame,
                            "Thank you for your rating and feedback!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    ratingFrame.dispose();
                });

                exitButton.addActionListener(event -> {
                    int confirmation = JOptionPane.showConfirmDialog(ratingFrame,
                            "Are you sure you want to exit without providing feedback?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        ratingFrame.dispose();
                    }
                });

                ratingFrame.add(promptLabel);
                ratingFrame.add(ratingSlider);
                ratingFrame.add(feedbackLabel);
                ratingFrame.add(feedbackField);
                ratingFrame.add(submitButton);
                ratingFrame.add(exitButton);

                ratingFrame.setVisible(true);
                ratingFrame.setLocationRelativeTo(null);

            }else if(e.getSource() == history) {
                String currentUserID = Main.loginCustomer.customerID;

                StringBuilder orderHistory = new StringBuilder();
                boolean hasOrders = false;
                for (order ord : DataIO.allorder) {
                    if (ord.owner.customerID.equals(currentUserID)) {
                        orderHistory.append("Order ID: ").append(ord.orderID).append("\n");
                        orderHistory.append("Car Details: ").append(ord.car.toString()).append("\n\n");
                        hasOrders = true;
                    }
                }

                if (hasOrders) {
                    JOptionPane.showMessageDialog(null, orderHistory.toString(), "Your Order History", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No orders found for your account.", "Order History", JOptionPane.INFORMATION_MESSAGE);
                }

        }else if(e.getSource() == loancalculate) {
                JTextField totalAmountField = new JTextField(10);
                JTextField downPaymentField = new JTextField(10);
                JTextField interestRateField = new JTextField(10);
                JTextField loanPeriodField = new JTextField(10);

                JPanel inputPanel = new JPanel(new GridLayout(4, 2));
                inputPanel.add(new JLabel("Car Total Amount:"));
                inputPanel.add(totalAmountField);
                inputPanel.add(new JLabel("Down Payment (Deposit):"));
                inputPanel.add(downPaymentField);
                inputPanel.add(new JLabel("Annual Interest Rate (%):"));
                inputPanel.add(interestRateField);
                inputPanel.add(new JLabel("Loan Period (Years):"));
                inputPanel.add(loanPeriodField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Loan Calculator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        double totalAmount = Double.parseDouble(totalAmountField.getText());
                        double downPayment = Double.parseDouble(downPaymentField.getText());
                        double annualInterestRate = Double.parseDouble(interestRateField.getText());
                        int loanPeriodYears = Integer.parseInt(loanPeriodField.getText());

                        double loanAmount = totalAmount - downPayment;
                        double monthlyInterestRate = (annualInterestRate / 100) / 12;
                        int totalMonths = loanPeriodYears * 12;

                        double monthlyPayment = (loanAmount * monthlyInterestRate) /
                                (1 - Math.pow(1 + monthlyInterestRate, -totalMonths));

                        String message = String.format("Loan Amount: %.2f\nMonthly Payment: %.2f", loanAmount, monthlyPayment);
                        JOptionPane.showMessageDialog(null, message, "Loan Calculation Result", JOptionPane.INFORMATION_MESSAGE);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter valid numeric values!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Invalid input!");
        }
    }

    JFrame x;
    Button profile, car_detail,feedback,history,loancalculate,exit;
    public CustomerPage2(String username){
        x = new JFrame();
        x.setSize(400,450);
        x.setLocationRelativeTo(null);

        JTextArea welcomeLabel = new JTextArea("Welcome " + username + "!\n" +
                "Here is the user features page!\n" +
                "Please choose the option below:");
        welcomeLabel.setBackground(null);
        welcomeLabel.setFocusable(false);
        welcomeLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));

        profile = new Button("User Profile");
        car_detail = new Button("Car Available");
        feedback = new Button("Rating and Feedback");
        history = new Button("Purchase History");
        loancalculate = new Button("Loan Calculator");
        exit = new Button("Exit");

        profile.addActionListener(this);
        car_detail.addActionListener(this);
        feedback.addActionListener(this);
        history.addActionListener(this);
        loancalculate.addActionListener(this);
        exit.addActionListener(this);

        x.setLayout(new BoxLayout(x.getContentPane(), BoxLayout.Y_AXIS));
        x.add(welcomeLabel);
        x.add(createButtonBox(profile));
        x.add(createButtonBox(car_detail));
        x.add(createButtonBox(feedback));
        x.add(createButtonBox(history));
        x.add(createButtonBox(loancalculate));
        x.add(createButtonBox(exit));

    }

    private JPanel createButtonBox(Button button) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        button.setPreferredSize(new Dimension(200, 50));
        panel.add(button);
        return panel;
    }
}
