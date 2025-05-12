package CarPackage;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Logger;

public class MainCar {
    private static final Logger logger = Logger.getLogger(MainCar.class.getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CarSystem carSystem = new CarSystem("cars.txt");
                JFrame frame = new JFrame("CarUI");
                frame.setContentPane(new AddCarUI().panel1);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            } catch (IOException e) {
                logger.severe("Failed to start application: " + e.getMessage());
                System.exit(1);
            }
        });
    }
}
