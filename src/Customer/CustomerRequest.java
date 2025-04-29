package Customer;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

    public class CustomerRequest {

        public void requestCar(String customerID) {
            try {
                List<String[]> availableCars = getAvailableCars("CarDetails.txt");

                if (availableCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No cars are currently available for request.",
                            "No Availability", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                StringBuilder carList = new StringBuilder("Available Cars:\n");
                for (String[] car : availableCars) {
                    carList.append("Car ID: ").append(car[0]).append(", Name: ").append(car[1])
                            .append(", ").append(car[3]).append("\n");
                }

                String selectedCarID = JOptionPane.showInputDialog(null, carList +
                        "\nEnter the Car ID to request:");
                if (selectedCarID == null || selectedCarID.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Request canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                boolean carRequested = processCarRequest("CarDetails.txt", "CarRequests.txt", customerID, selectedCarID);

                if (carRequested) {
                    JOptionPane.showMessageDialog(null, "Car request successfully placed!",
                            "Request Successful", JOptionPane.INFORMATION_MESSAGE);
                    String requestID = DataIO.getNextRequestID();
                    DataIO.allrequest.add(new request(requestID, DataIO.searchCar(selectedCarID), DataIO.searchName(Main.loginCustomer.customerID)));
                    DataIO.write();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Car ID or the car is not available.",
                            "Request Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred while processing the request.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private List<String[]> getAvailableCars(String filePath) throws IOException {
            List<String[]> availableCars = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details[2].equalsIgnoreCase("Available")) {
                        availableCars.add(details);
                    }
                }
            }
            return availableCars;
        }

        private boolean processCarRequest(String carFilePath, String requestFilePath,
                                          String customerID, String selectedCarID) throws IOException {
            List<String> carLines = new ArrayList<>();
            boolean carFound = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(carFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details[0].equals(selectedCarID) && details[2].equalsIgnoreCase("Available")) {
                        details[2] = "Not Available";
                        carFound = true;

                        try (FileWriter requestWriter = new FileWriter(requestFilePath, true)) {
                            requestWriter.write("CustomerID: " + customerID + ", CarID: " + selectedCarID + "\n");
                        }
                    }
                    carLines.add(String.join(",", details));
                }
            }

            if (carFound) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(carFilePath))) {
                    for (String carLine : carLines) {
                        writer.write(carLine);
                        writer.newLine();
                    }
                }
            }

            return carFound;
        }
    }
