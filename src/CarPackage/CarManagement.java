package CarPackage;

import java.io.*;
import java.util.*;

public class CarManagement {
    private static final String CARS_FILE = "cars.txt";
    private final List<Car> cars;

    public CarManagement() {
        cars = new ArrayList<>();
        loadCars();
    }
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    public boolean addCar(Car car) {
        for (Car c : cars) {
            if (c.getCarId().equals(car.getCarId())) {
                System.out.println("Error: car with ID " + car.getCarId() + " already exists.");
                return false;
            }
        }
        cars.add(car);
        saveAllCars();
        System.out.println("Car added successfully!");
        return true;
    }

    public void saveAllCars() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CARS_FILE))) {
            for (Car c : getAllCars()) {
                w.write(c.toCSV());
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Car getCar(String carId) {
        for (Car c : cars) {
            if (c.getCarId().equals(carId)) {
                return c;
            }
        }
        System.out.println("Error: car with ID " + carId + " was not found.");
        return null;
    }

    public boolean updateCar(String carId, Car newCar) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId().equals(carId)) {
                cars.set(i, newCar);
                saveCars();
                System.out.println("Car update successful!");
                return true;
            }
        }
        System.out.println("Error: car with ID " + carId + " was not found.");
        return false;
    }

    public boolean deleteCar(String carId) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId().equals(carId)) {
                cars.remove(i);
                saveCars();
                System.out.println("Car deleted successfully!");
                return true;
            }
        }
        System.out.println("Error: car with ID " + carId + " was not found.");
        return false;
    }

    public List<Car> searchByStatus(String status) {
        List<Car> result = new ArrayList<>();
        for (Car c : cars) {
            if (c.getStatus().equalsIgnoreCase(status)) {
                result.add(c);
            }
        }
        return result;
    }

    public void loadCars() {
        try (BufferedReader br = new BufferedReader(new FileReader("cars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Car car = Car.fromCSV(line);
                cars.add(car);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCars() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CARS_FILE))) {
            for (Car car : cars) {
                writer.write(car.toCSV());
                writer.newLine();
            }
            System.out.println("Car data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving car data: " + e.getMessage());
        }
    }
}
