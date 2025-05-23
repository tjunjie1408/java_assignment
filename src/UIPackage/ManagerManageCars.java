package UIPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import CarPackage.*;
import ManagerPackage.*;

public class ManagerManageCars extends JFrame {
    private JPanel panel1;
    private JTextField SearchTextField;
    private JLabel Search;
    private JTable CarTable;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton exitButton;
    private DefaultTableModel tableModel;
    private CarManagement carManagement;

    public ManagerManageCars() {
        setContentPane(panel1);
        setTitle("Manage Cars");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        carManagement = new CarManagement();

        // Single-column table with custom renderer
        carManagement = new CarManagement();
        tableModel = new DefaultTableModel(new Object[]{"Car ID", "Brand", "Model", "Color", "Price", "Photo Path", "Status"}, 0);
        CarTable.setDefaultRenderer(Object.class, new CarTableCellRenderer());
        CarTable.setModel(tableModel);
        CarTable.setRowHeight(150);

        loadCarData();
        setVisible(true);
        CarTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Add Car Button
        addButton.addActionListener(e -> {
            AddCarDialog dialog = new AddCarDialog(this);
            dialog.setVisible(true);
            Car newCar = dialog.getNewCar();
            if (newCar != null) {
                carManagement.addCar(newCar);
                loadCarData(); // Refresh table
            }
        });

        // Update Car Button
        updateButton.addActionListener(e -> {
            int selectedRow = CarTable.getSelectedRow();
            if (selectedRow != -1) {
                String carId = (String) tableModel.getValueAt(selectedRow, 0);
                Car car = carManagement.getCar(carId);
                if (car != null) {
                    UpdateCarDialog dialog = new UpdateCarDialog(this, car);
                    dialog.setVisible(true);
                    loadCarData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a car to update!");
            }
        });

        // Delete Car Button
        deleteButton.addActionListener(e -> {
            int[] selectedRows = CarTable.getSelectedRows();
            if (selectedRows.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected cars?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        String carId = (String) tableModel.getValueAt(selectedRows[i], 0);
                        carManagement.deleteCar(carId);
                    }
                    loadCarData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select at least one car to delete!");
            }
        });


        // Exit Button
        exitButton.addActionListener(e -> {
            new ManagersMain();
            this.dispose();
        });

        // Search Field
        SearchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchCars();
                }
            }
        });

        setVisible(true);
    }

    private void loadCarData() {
        List<Car> cars = carManagement.getAllCars();
        System.out.println("Loading " + cars.size() + " cars into table.");
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getCarId(), car.getBrand(), car.getModel(), car.getColor(),
                    car.getPrice(), car.getPhotoPath(), car.getStatus()
            });
        }
    }

    private void searchCars() {
        String searchTerm = SearchTextField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Car> cars = carManagement.getAllCars();
        for (Car car : cars) {
            if (car.getCarId().toLowerCase().contains(searchTerm) ||
                    car.getBrand().toLowerCase().contains(searchTerm) ||
                    car.getModel().toLowerCase().contains(searchTerm) ||
                    car.getColor().toLowerCase().contains(searchTerm) ||
                    car.getStatus().toLowerCase().contains(searchTerm)) {
                tableModel.addRow(new Object[]{car});
            }
        }
    }

    class AddCarDialog extends JDialog {
        private JTextField carIdField, modelField, brandField, colorField, priceField, photoField, statusField;
        private Car newCar;

        public AddCarDialog(JFrame parent) {
            super(parent, "Add Car", true);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Padding

            // Car ID
            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Car ID:"), gbc);
            gbc.gridx = 1;
            carIdField = new JTextField(15);
            add(carIdField, gbc);

            // Model
            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Brand:"), gbc);
            gbc.gridx = 1;
            brandField = new JTextField(15);
            add(brandField, gbc);

            //Model
            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Model:"), gbc);
            gbc.gridx = 1;
            modelField = new JTextField(15);
            add(modelField, gbc);

            // Color
            gbc.gridx = 0; gbc.gridy = 3;
            add(new JLabel("Color:"), gbc);
            gbc.gridx = 1;
            colorField = new JTextField(15);
            add(colorField, gbc);

            // Price
            gbc.gridx = 0; gbc.gridy = 4;
            add(new JLabel("Price:"), gbc);
            gbc.gridx = 1;
            priceField = new JTextField(15);
            add(priceField, gbc);

            // Photo
            gbc.gridx = 0; gbc.gridy = 5;
            add(new JLabel("Photo Path:"), gbc);
            gbc.gridx = 1;
            photoField = new JTextField(15);
            add(photoField, gbc);

            gbc.gridx = 2; gbc.gridy = 5;
            JButton selectPhotoButton = new JButton("Select Photo");
            selectPhotoButton.addActionListener(e -> selectPhoto());
            add(selectPhotoButton, gbc);

            // Status
            gbc.gridx = 0; gbc.gridy = 6;
            add(new JLabel("Status:"), gbc);
            gbc.gridx = 1;
            statusField = new JTextField(15);
            add(statusField, gbc);

            // OK Button
            gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> {
                addCar();
                dispose();
            });
            add(okButton, gbc);
            pack();
            setLocationRelativeTo(parent);
        }
        public Car getNewCar() {
            return newCar;
        }

        private void selectPhoto() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("src/resources/CarsImage"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String relativePath = "resources/CarsImage/" + selectedFile.getName();
                photoField.setText(relativePath);
            }
        }

        private void addCar() {
            String carId = carIdField.getText().trim();
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String color = colorField.getText().trim();
            String priceStr = priceField.getText().trim();
            String status = statusField.getText().trim();
            String photoPath = photoField.getText().trim();

            if (carId.isEmpty() || brand.isEmpty() || model.isEmpty() || color.isEmpty() || priceStr.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields except photo path are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    JOptionPane.showMessageDialog(this, "Price cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Price must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Car newCar = new Car(carId, brand, model, color, price, photoPath, status);
            if (carManagement.addCar(newCar)) {
                dispose();
                JOptionPane.showMessageDialog(this, "Car added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Car ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class UpdateCarDialog extends JDialog {
        private Car car;
        private JTextField brandField, modelField, colorField, priceField, statusField, photoPathField;
        private JButton selectPhotoButton, updateButton, cancelButton;

        public UpdateCarDialog(JFrame parent, Car car) {
            super(parent, "Update Car", true);
            this.car = car;
            setLayout(new GridLayout(7, 2, 5, 5));

            add(new JLabel("Car ID:"));
            add(new JLabel(car.getCarId()));

            add(new JLabel("Brand:"));
            brandField = new JTextField(car.getBrand(), 10);
            add(brandField);

            add(new JLabel("Model:"));
            modelField = new JTextField(car.getModel(), 10);
            add(modelField);

            add(new JLabel("Color:"));
            colorField = new JTextField(car.getColor(), 10);
            add(colorField);

            add(new JLabel("Price:"));
            priceField = new JTextField(String.valueOf(car.getPrice()), 10);
            add(priceField);

            add(new JLabel("Status:"));
            statusField = new JTextField(car.getStatus(), 10);
            add(statusField);

            add(new JLabel("Photo Path:"));
            photoPathField = new JTextField(car.getPhotoPath(), 10);
            add(photoPathField);

            selectPhotoButton = new JButton("Select Photo");
            selectPhotoButton.addActionListener(e -> selectPhoto());
            add(selectPhotoButton);

            updateButton = new JButton("Update");
            updateButton.addActionListener(e -> updateCar());
            add(updateButton);

            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);

            pack();
            setLocationRelativeTo(parent);
        }

        private void selectPhoto() {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedFile.getAbsolutePath());
            }
        }

        private void updateCar() {
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String color = colorField.getText().trim();
            String priceStr = priceField.getText().trim();
            String status = statusField.getText().trim();
            String photoPath = photoPathField.getText().trim();

            if (brand.isEmpty() || model.isEmpty() || color.isEmpty() || priceStr.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields except photo path are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    JOptionPane.showMessageDialog(this, "Price cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Price must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            car.setBrand(brand);
            car.setModel(model);
            car.setColor(color);
            car.setPrice(price);
            car.setStatus(status);
            car.setPhotoPath(photoPath);

            if (carManagement.updateCar(car.getCarId(), car)) {
                dispose();
                JOptionPane.showMessageDialog(this, "Car updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update car!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
