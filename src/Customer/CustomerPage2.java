package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

            } else if (e.getSource() == car_request) {
                try {
                    List<String> carDetails = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new FileReader("CarDetails.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.isBlank()) carDetails.add(line);
                        }
                    }

                    List<String[]> availableCars = new ArrayList<>();
                    Set<String> availableBrands = new LinkedHashSet<>();
                    for (int i = 0; i < carDetails.size(); i += 6) {
                        String carID = carDetails.get(i);
                        String brand = carDetails.get(i + 1);
                        String model = carDetails.get(i + 2);
                        String colour = carDetails.get(i + 3);
                        String status = carDetails.get(i + 4).trim();
                        String priceStr = carDetails.get(i + 5).trim();
                        double price;

                        // Validate and parse the car price
                        try {
                            price = Double.parseDouble(priceStr);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Invalid price format in CarDetails.txt for Car ID: " + carID + ". Please fix the file.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            continue; // Skip invalid entries
                        }

                        if ("Available".equalsIgnoreCase(status)) {
                            String[] car = {carID, brand, model, colour, status, String.valueOf(price)};
                            availableCars.add(car);
                            availableBrands.add(brand);
                        }
                    }

                    if (availableBrands.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No available cars at the moment.");
                        return;
                    }

                    String selectedBrand = (String) JOptionPane.showInputDialog(
                            null,
                            "Select a car brand:",
                            "Available Brands",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            availableBrands.toArray(),
                            availableBrands.iterator().next()
                    );

                    if (selectedBrand != null) {
                        // Prompt user to enter max price
                        String priceInput = JOptionPane.showInputDialog(
                                null,
                                "Enter your maximum price:",
                                "Filter by Price",
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (priceInput != null && !priceInput.isBlank()) {
                            try {
                                double maxPrice = Double.parseDouble(priceInput);
                                if (maxPrice < 0) {
                                    throw new NumberFormatException("Price cannot be negative.");
                                }

                                // Filter available cars by selected brand and max price
                                List<String[]> matchingCars = new ArrayList<>();
                                for (String[] car : availableCars) {
                                    if (car[1].equalsIgnoreCase(selectedBrand) && Double.parseDouble(car[5]) <= maxPrice) {
                                        matchingCars.add(car);
                                    }
                                }

                                if (matchingCars.isEmpty()) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "No cars match your selected brand and price range.",
                                            "No Matches Found",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    return;
                                }

                                String[] carOptions = matchingCars.stream()
                                        .map(car -> "Car ID: " + car[0] + ", Model: " + car[2] + ", Colour: " + car[3] + " (RM" + car[5] + ")")
                                        .toArray(String[]::new);

                                String selectedCar = (String) JOptionPane.showInputDialog(
                                        null,
                                        "Select a car to book:",
                                        "Available Cars within Price Range: RM" + maxPrice,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        carOptions,
                                        carOptions[0]
                                );

                                if (selectedCar != null) {
                                    for (String[] car : matchingCars) {
                                        if (selectedCar.contains(car[0])) {
                                            String requestDetails = "RequestID: " + DataIO.getNextRequestID() + "\n"
                                                    + "Car ID: " + car[0] + "\n"
                                                    + "UserID: " + Main.loginCustomer.name + "\n\n";

                                            try (BufferedWriter requestWriter = new BufferedWriter(new FileWriter("CarRequest.txt", true))) {
                                                requestWriter.write(requestDetails);
                                            }

                                            // Mark car as not available
                                            for (int i = 0; i < carDetails.size(); i += 6) {
                                                if (carDetails.get(i).equalsIgnoreCase(car[0])) {
                                                    carDetails.set(i + 4, "Not Available");
                                                    break;
                                                }
                                            }

                                            // Update CarDetails.txt file
                                            try (BufferedWriter carDetailsWriter = new BufferedWriter(new FileWriter("CarDetails.txt", false))) {
                                                for (int i = 0; i < carDetails.size(); i++) {
                                                    carDetailsWriter.write(carDetails.get(i));
                                                    carDetailsWriter.newLine();
                                                    if ((i + 1) % 6 == 0) carDetailsWriter.newLine();
                                                }
                                            }

                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Car booked successfully! Your request has been recorded.",
                                                    "Booking Confirmation",
                                                    JOptionPane.INFORMATION_MESSAGE
                                            );
                                            return;
                                        }
                                    }
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Invalid price entered. Please enter a non-negative number.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
        }else if(e.getSource() == feedback){
            Object[] options = {"1", "2", "3", "4", "5"};
                Integer rating = JOptionPane.showOptionDialog(
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
                String feedbackID = DataIO.getNextFeedbackID();
                DataIO.allfeedback.add(new feedback(feedbackID, String.valueOf(rating), review, Main.loginCustomer));
                DataIO.write();
                JOptionPane.showMessageDialog(null,
                        "Thank you for your rating and feedback!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

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
    Button profile, car_request,feedback,history,loancalculate,exit;
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
        car_request = new Button("Booking Request");
        feedback = new Button("Rating and Feedback");
        history = new Button("Purchase History");
        loancalculate = new Button("Loan Calculator");
        exit = new Button("Exit");

        profile.addActionListener(this);
        car_request.addActionListener(this);
        feedback.addActionListener(this);
        history.addActionListener(this);
        loancalculate.addActionListener(this);
        exit.addActionListener(this);

        x.setLayout(new BoxLayout(x.getContentPane(), BoxLayout.Y_AXIS));
        x.add(welcomeLabel);
        x.add(createButtonBox(profile));
        x.add(createButtonBox(car_request));
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
