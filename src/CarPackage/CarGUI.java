package CarPackage;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class CarGUI extends JFrame{
    private static final Logger logger = Logger.getLogger(CarGUI.class.getName());
    private final CarSystem carSystem;

    public CarGUI(CarSystem carSystem) {
        super("Car Management");
        this.carSystem = carSystem;

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Car Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton addBtn    = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton updateBtn = new JButton("Update");

        addBtn.addActionListener(this::onAdd);
        deleteBtn.addActionListener(this::onDelete);
        searchBtn.addActionListener(this::onSearch);
        updateBtn.addActionListener(this::onUpdate);

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(updateBtn);
        add(buttonPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);  // 居中
    }

    private void onAdd(ActionEvent e) {
        try {
            String data = JOptionPane.showInputDialog(
                    this,
                    "Enter Car (id,make,model,year,price,color,status):",
                    "Add Car",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (data != null) {
                String[] parts = data.split(",", -1);
                Car car = new Car(
                        parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]),
                        Double.parseDouble(parts[4]),
                        parts[5], parts[6]
                );
                boolean ok = carSystem.addCar(car);
                JOptionPane.showMessageDialog(this,
                        ok ? "Added successfully." : "Add failed.",
                        "Result",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception ex) {
            logger.warning("Add error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Invalid input format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onDelete(ActionEvent e) {
        String id = JOptionPane.showInputDialog(this, "Enter Car ID to delete:", "Delete Car", JOptionPane.PLAIN_MESSAGE);
        if (id != null) {
            boolean ok = carSystem.deleteCar(id);
            JOptionPane.showMessageDialog(this,
                    ok ? "Deleted successfully." : "Delete failed.",
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void onSearch(ActionEvent e) {
        String id = JOptionPane.showInputDialog(this, "Enter Car ID to search:", "Search Car", JOptionPane.PLAIN_MESSAGE);
        if (id != null) {
            Car car = carSystem.findCarById(id);
            JOptionPane.showMessageDialog(this,
                    car != null ? car.toString() : "Car not found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void onUpdate(ActionEvent e) {
        try {
            String data = JOptionPane.showInputDialog(
                    this,
                    "Enter updated Car (id,make,model,year,price,color,status):",
                    "Update Car",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (data != null) {
                String[] parts = data.split(",", -1);
                Car car = new Car(
                        parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]),
                        Double.parseDouble(parts[4]),
                        parts[5], parts[6]
                );
                boolean ok = carSystem.updateCar(car);
                JOptionPane.showMessageDialog(this,
                        ok ? "Updated successfully." : "Update failed.",
                        "Result",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception ex) {
            logger.warning("Update error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Invalid input format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
