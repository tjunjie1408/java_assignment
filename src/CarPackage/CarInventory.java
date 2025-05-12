package CarPackage;

import java.io.*;
import java.util.*;

public class CarInventory {
    private HashMap<String, Car> cars;
    private static final String FILE_NAME = "cars.txt";

    public CarInventory() {
        cars = new HashMap<>();
        loadCars();
    }

    public boolean addCar(Car car) {
        if (cars.containsKey(car.getCarId())) {
            return false; // Car ID already exists
        }
        cars.put(car.getCarId(), car);
        saveCars();
        return true;
    }

    public Car searchCar(String carId) {
        return cars.get(carId);
    }

    public List<Car> searchCarsByBrand(String brand) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getBrand().equalsIgnoreCase(brand)) {
                result.add(car);
            }
        }
        return result;
    }

    public List<Car> searchCarsByStatus(String status) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getStatus().equalsIgnoreCase(status)) {
                result.add(car);
            }
        }
        return result;
    }

    public boolean updateCar(String carId, Car newCar) {
        if (!cars.containsKey(carId)) {
            return false; // Car not found
        }
        cars.put(carId, newCar);
        saveCars();
        return true;
    }

    public boolean deleteCar(String carId) {
        if (!cars.containsKey(carId)) {
            return false; // Car not found
        }
        cars.remove(carId);
        saveCars();
        return true;
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    private void loadCars() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Car car = Car.fromCSV(line);
                cars.put(car.getCarId(), car);
            }
        } catch (IOException e) {
            System.out.println("Error loading cars: " + e.getMessage());
        }
    }

    private void saveCars() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Car car : cars.values()) {
                writer.write(car.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving cars: " + e.getMessage());
        }
    }
}
