package CarPackage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarSystem {
    private static final Logger logger = Logger.getLogger(CarSystem.class.getName());
    private final CarInventory inventory;

    public CarSystem(String dataFilePath) throws IOException {
        FileHandling fileHandling = new FileHandling(dataFilePath);
        this.inventory = new CarInventory(fileHandling);
        logger.info(String.format("System started, %d vehicle records loaded", inventory.size()));
    }

    public boolean saveInventoryData() {
        try {
            inventory.getFileHandler().saveCarsToFile(inventory.getAllCars());
            return true;
        } catch (UncheckedIOException e) {
            logger.log(Level.SEVERE, "Failure to save inventory data", e);
            return false;
        }
    }

    public boolean createDataBackup() {
        boolean ok = inventory.getFileHandler().createBackup();
        if (!ok) {
            logger.warning("Failed to create data backup");
        }
        return ok;
    }

    public boolean addCar(Car car) {
        if (inventory.addCar(car)) {
            logger.info("Vehicle added successfully: ID=" + car.getId());
            return saveInventoryData();
        } else {
            logger.warning("Failed to add vehicle (invalid or duplicate) ID=" + car.getId());
            return false;
        }
    }

    public boolean addCar(String id, String make, String model, int year, double price, String color, String status) {
        Car car = new Car(id, make, model, year, price, color, status);
        return addCar(car);
    }

    public boolean deleteCar(String id) {
        if (inventory.deleteCar(id)) {
            logger.info("Delete Vehicle Successfully: ID=" + id);
            return saveInventoryData();
        } else {
            logger.warning("Failed to delete vehicle (not found) ID=" + id);
            return false;
        }
    }

    public Car findCarById(String id) {
        return inventory.searchCar(id);
    }

    public boolean updateCar(Car car) {
        if (inventory.updateCar(car)) {
            logger.info("Vehicle updated successfully. ID=" + car.getId());
            return saveInventoryData();
        } else {
            logger.warning("Failed to update vehicle (invalid or not found) ID=" + car.getId());
            return false;
        }
    }

    public List<Car> getAllCars() {
        return inventory.getAllCars();
    }

    public List<Car> searchCarsByMake(String make) {
        return inventory.searchCarsByMake(make);
    }

    public List<Car> searchCarsByModel(String model) {
        return inventory.searchCarsByModel(model);
    }

    public List<Car> searchCarsByPriceRange(double minPrice, double maxPrice) {
        return inventory.searchCarsByPriceRange(minPrice, maxPrice);
    }

    public List<Car> searchCarsByStatus(String status) {
        return inventory.searchCarsByStatus(status);
    }

    public List<Car> searchCarsByYear(int year) {
        return inventory.searchCarsByYear(year);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public void changeDataFilePath(String newPath) {
        inventory.getFileHandler().setFilePath(newPath);
    }

    public String getDataFilePath() {
        return inventory.getFileHandler().getFilePath();
    }
}
