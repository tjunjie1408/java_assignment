package CarPackage;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.logging.Logger;

public class MainCar {
    private static final Logger logger = Logger.getLogger(MainCar.class.getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CarSystem carSystem = new CarSystem("cars.txt");
                CarGUI gui = new CarGUI(carSystem);
                gui.setVisible(true);
            } catch (IOException e) {
                logger.severe("Failed to start application: " + e.getMessage());
                System.exit(1);
            }
        });
    }
}
