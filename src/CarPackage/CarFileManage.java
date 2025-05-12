package CarPackage;

import java.io.*;
import java.util.*;

public class CarFileManage {
    private static final String CARS_FILE = "cars.txt";
    private HashMap<String, Car> cars;

    public CarFileManage() {
        cars = new HashMap<>();
        loadCars();
    }

    public boolean addCar(Car car) {
        if (cars.containsKey(car.getCarId())) {
            System.out.println("Error: Car with ID " + car.getCarId() + " already exists.");
            return false;
        }
        cars.put(car.getCarId(), car);
        saveCars();
        System.out.println("Car added successfully!");
        return true;
    }

    // Read - Search cars
    public Car getCar(String carId) {
        return cars.get(carId);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    public List<Car> searchByBrand(String brand) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getBrand().equalsIgnoreCase(brand)) {
                result.add(car);
            }
        }
        return result;
    }

    public List<Car> searchByStatus(String status) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getStatus().equalsIgnoreCase(status)) {
                result.add(car);
            }
        }
        return result;
    }

    public List<Car> searchByPriceRange(double minPrice, double maxPrice) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getPrice() >= minPrice && car.getPrice() <= maxPrice) {
                result.add(car);
            }
        }
        return result;
    }

    public boolean updateCar(String carId, Car newCar) {
        if (!cars.containsKey(carId)) {
            System.out.println("Error: Car with ID " + carId + " not found.");
            return false;
        }
        cars.put(carId, newCar);
        saveCars();
        System.out.println("Car updated successfully!");
        return true;
    }

    public boolean deleteCar(String carId) {
        if (!cars.containsKey(carId)) {
            System.out.println("Error: Car with ID " + carId + " not found.");
            return false;
        }
        cars.remove(carId);
        saveCars();
        System.out.println("Car deleted successfully!");
        return true;
    }

    private void loadCars() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Car car = Car.fromCSV(line);
                    cars.put(car.getCarId(), car);
                } catch (Exception e) {
                    System.out.println("Error parsing car data: " + e.getMessage());
                }
            }
            System.out.println("Cars loaded successfully from file.");
        } catch (FileNotFoundException e) {
            System.out.println("Cars file not found. Creating new file.");
            createFile();
        } catch (IOException e) {
            System.out.println("Error reading cars file: " + e.getMessage());
        }
    }

    private void saveCars() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CARS_FILE))) {
            for (Car car : cars.values()) {
                writer.write(car.toCSV());
                writer.newLine();
            }
            System.out.println("Cars saved successfully to file.");
        } catch (IOException e) {
            System.out.println("Error saving cars to file: " + e.getMessage());
        }
    }

    private void createFile() {
        try {
            File file = new File(CARS_FILE);
            if (file.createNewFile()) {
                System.out.println("Created new cars file.");
            }
        } catch (IOException e) {
            System.out.println("Error creating cars file: " + e.getMessage());
        }
    }

    public void backupCars() {
        String backupDir = "backup_" + System.currentTimeMillis();
        new File(backupDir).mkdir();

        try {
            File source = new File(CARS_FILE);
            if (source.exists()) {
                File dest = new File(backupDir + "/" + CARS_FILE);
                copyFile(source, dest);
                System.out.println("Cars data backed up successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error backing up cars data: " + e.getMessage());
        }
    }

    private void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public void clearAllCars() {
        cars.clear();
        saveCars();
        System.out.println("All cars data cleared.");
    }

    public int getTotalCars() {
        return cars.size();
    }

    public boolean carExists(String carId) {
        return cars.containsKey(carId);
    }

    public Map<String, Integer> getCarsByStatusCount() {
        Map<String, Integer> statusCount = new HashMap<>();
        for (Car car : cars.values()) {
            statusCount.merge(car.getStatus(), 1, Integer::sum);
        }
        return statusCount;
    }

    public Map<String, Integer> getCarsByBrandCount() {
        Map<String, Integer> brandCount = new HashMap<>();
        for (Car car : cars.values()) {
            brandCount.merge(car.getBrand(), 1, Integer::sum);
        }
        return brandCount;
    }
}

