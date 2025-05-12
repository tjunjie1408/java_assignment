package CarPackage;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class CarSystem {
    private CarFileManage carManager;
    private List<CarSystemListener> listeners;

    public CarSystem() {
        carManager = new CarFileManage();
        listeners = new ArrayList<>();
    }

    // Listener interface for GUI updates
    public interface CarSystemListener {
        void onCarAdded(Car car);
        void onCarUpdated(Car car);
        void onCarDeleted(String carId);
        void onError(String message);
        void onDataLoaded(List<Car> cars);
    }

    // Add listener for GUI updates
    public void addListener(CarSystemListener listener) {
        listeners.add(listener);
    }

    // Remove listener
    public void removeListener(CarSystemListener listener) {
        listeners.remove(listener);
    }

    // Notify all listeners
    private void notifyListeners(String event, Object data) {
        for (CarSystemListener listener : listeners) {
            try {
                switch (event) {
                    case "ADD":
                        listener.onCarAdded((Car) data);
                        break;
                    case "UPDATE":
                        listener.onCarUpdated((Car) data);
                        break;
                    case "DELETE":
                        listener.onCarDeleted((String) data);
                        break;
                    case "ERROR":
                        listener.onError((String) data);
                        break;
                    case "LOAD":
                        listener.onDataLoaded((List<Car>) data);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    // CRUD Operations

    // Create
    public boolean addCar(Car car) {
        try {
            if (carManager.addCar(car)) {
                notifyListeners("ADD", car);
                return true;
            }
        } catch (Exception e) {
            notifyListeners("ERROR", "Error adding car: " + e.getMessage());
        }
        return false;
    }

    // Read
    public Car getCar(String carId) {
        try {
            return carManager.getCar(carId);
        } catch (Exception e) {
            notifyListeners("ERROR", "Error getting car: " + e.getMessage());
            return null;
        }
    }

    public List<Car> getAllCars() {
        try {
            List<Car> cars = carManager.getAllCars();
            notifyListeners("LOAD", cars);
            return cars;
        } catch (Exception e) {
            notifyListeners("ERROR", "Error getting all cars: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Car> searchByBrand(String brand) {
        try {
            return carManager.searchByBrand(brand);
        } catch (Exception e) {
            notifyListeners("ERROR", "Error searching by brand: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Car> searchByStatus(String status) {
        try {
            return carManager.searchByStatus(status);
        } catch (Exception e) {
            notifyListeners("ERROR", "Error searching by status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Car> searchByPriceRange(double minPrice, double maxPrice) {
        try {
            return carManager.searchByPriceRange(minPrice, maxPrice);
        } catch (Exception e) {
            notifyListeners("ERROR", "Error searching by price range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Update
    public boolean updateCar(String carId, Car newCar) {
        try {
            if (carManager.updateCar(carId, newCar)) {
                notifyListeners("UPDATE", newCar);
                return true;
            }
        } catch (Exception e) {
            notifyListeners("ERROR", "Error updating car: " + e.getMessage());
        }
        return false;
    }

    // Delete
    public boolean deleteCar(String carId) {
        try {
            if (carManager.deleteCar(carId)) {
                notifyListeners("DELETE", carId);
                return true;
            }
        } catch (Exception e) {
            notifyListeners("ERROR", "Error deleting car: " + e.getMessage());
        }
        return false;
    }

    // Statistics and Reports
    public Map<String, Integer> getCarsByStatusCount() {
        try {
            return carManager.getCarsByStatusCount();
        } catch (Exception e) {
            notifyListeners("ERROR", "Error getting status count: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public Map<String, Integer> getCarsByBrandCount() {
        try {
            return carManager.getCarsByBrandCount();
        } catch (Exception e) {
            notifyListeners("ERROR", "Error getting brand count: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public int getTotalCars() {
        return carManager.getTotalCars();
    }

    // Backup and Maintenance
    public void backupData() {
        try {
            carManager.backupCars();
        } catch (Exception e) {
            notifyListeners("ERROR", "Error backing up data: " + e.getMessage());
        }
    }

    public void clearAllCars() {
        try {
            carManager.clearAllCars();
            notifyListeners("LOAD", new ArrayList<>());
        } catch (Exception e) {
            notifyListeners("ERROR", "Error clearing cars: " + e.getMessage());
        }
    }
}
