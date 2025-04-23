package CarPackage;

import java.io.IOException;
import java.util.List;

public class CarSystem {
    private final CarInventory inventory;
    private final FileHandling dataIO;

    public CarSystem(String dataFilePath) throws IOException {
        this.inventory = new CarInventory();
        this.dataIO = new FileHandling(dataFilePath);
        loadInventoryData();
    }

    private void loadInventoryData() throws IOException {
        List<Car> loadedCars = dataIO.loadCarsFile();
        inventory.setCars(loadedCars);
    }

    public boolean saveInventoryData() {
        return dataIO.saveCarsToFile(inventory.getAllCars());
    }

    public boolean createDataBackup() {
        return dataIO.createBackup();
    }

    public boolean addCar(Car car) {
        boolean result = inventory.addCar(car);
        if (result) {
            saveInventoryData();
        }
        return result;
    }

    public boolean addCar(String id, String make, String model, int year, double price, String color, String status) {
        Car car = new Car(id, make, model, year, price, color, status);
        return addCar(car);
    }

    public boolean deleteCar(String id) {
        boolean result = inventory.deleteCar(id);
        if (result) {
            saveInventoryData();
        }
        return result;
    }

    public Car findCarById(String id) {
        return inventory.searchCar(id);
    }

    public boolean updateCar(Car car) {
        boolean result = inventory.updateCar(car);
        if (result) {
            saveInventoryData();
        }
        return result;
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
        return inventory.getInventorySize();
    }

    public void changeDataFilePath(String newPath) {
        dataIO.setFilePath(newPath);
    }

    public String getDataFilePath() {
        return dataIO.getFilePath();
    }
}
