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

    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
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

    private void loadCars() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Car car = Car.fromCSV(line);
                    cars.add(car);
                } catch (Exception e) {
                    System.out.println("Error parsing car data:" + e.getMessage());
                }
            }
            System.out.println("Car data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Car file not found, creating new file.");
            createFile();
        } catch (IOException e) {
            System.out.println("Error reading car file: " + e.getMessage());
        }
    }

    // 保存汽车数据到文件
    private void saveCars() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CARS_FILE))) {
            for (Car car : cars) {
                writer.write(car.toCSV());
                writer.newLine();
            }
            System.out.println("汽车数据保存成功。");
        } catch (IOException e) {
            System.out.println("保存汽车数据出错：" + e.getMessage());
        }
    }

    // 创建新文件
    private void createFile() {
        try {
            File file = new File(CARS_FILE);
            if (file.createNewFile()) {
                System.out.println("已创建新的汽车文件。");
            }
        } catch (IOException e) {
            System.out.println("创建汽车文件出错：" + e.getMessage());
        }
    }
}
