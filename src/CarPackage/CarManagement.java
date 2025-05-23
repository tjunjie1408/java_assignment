package CarPackage;

import java.io.*;
import java.util.*;

public class CarManagement {
    private static final String CARS_FILE = "cars.txt";
    private List<Car> cars;

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
        saveCars();
        System.out.println("Car added successfully!");
        return true;
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

    public List<Car> searchByBrand(String brand) {
        List<Car> result = new ArrayList<>();
        for (Car c : cars) {
            if (c.getBrand().equalsIgnoreCase(brand)) {
                result.add(c);
            }
        }
        return result;
    }

    // 按状态搜索
    public List<Car> searchByStatus(String status) {
        List<Car> result = new ArrayList<>();
        for (Car c : cars) {
            if (c.getStatus().equalsIgnoreCase(status)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Car> searchByPriceRange(double minPrice, double maxPrice) {
        List<Car> result = new ArrayList<>();
        for (Car c : cars) {
            if (c.getPrice() >= minPrice && c.getPrice() <= maxPrice) {
                result.add(c);
            }
        }
        return result;
    }

    public boolean updateCarStatus(String carId, String newStatus) {
        Car car = getCar(carId);
        if (car == null) {
            return false;
        }
        car.setStatus(newStatus);
        saveCars();
        System.out.println("Car status has been updated to " + newStatus);
        return true;
    }

    public List<Car> getAvailableCars() {
        return searchByStatus("available");
    }

    public void loadCars() {
        try (BufferedReader br = new BufferedReader(new FileReader("cars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Car car = Car.fromCSV(line); // 假设有一个 fromCSV 方法
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

    private void createFile() {
        try {
            File file = new File(CARS_FILE);
            if (file.createNewFile()) {
                System.out.println("A new car file has been created.");
            }
        } catch (IOException e) {
            System.out.println("Error creating car file: "+ e.getMessage());
        }
    }
}
