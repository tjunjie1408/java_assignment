package CarPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CarInventory {
    private final CarFileManage fileHandler;
    private final List<Car> cars;

    public CarInventory(CarFileManage fileHandler) throws IOException {
        this.fileHandler = Objects.requireNonNull(fileHandler);
        this.cars = new ArrayList<>(fileHandler.loadCarsFile());
    }

    private boolean isValid(Car car) {
        return car != null
                && car.getId() != null && !car.getId().isEmpty()
                && car.getYear() >= 1900 && car.getYear() <= 2100
                && car.getPrice() > 0;
    }

    public boolean addCar(Car car) {
        if (!isValid(car)) return false;
        if (cars.stream().anyMatch(c -> c.getId().equals(car.getId()))) return false;
        cars.add(car);
        return true;
    }

    public boolean deleteCar(String id) {
        return cars.removeIf(c -> c.getId().equals(id));
    }

    public boolean updateCar(Car updated) {
        if (!isValid(updated)) return false;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(updated.getId())) {
                cars.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public Car searchCar(String id) {
        return cars.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    public int size() {
        return cars.size();
    }

    public CarFileManage getFileHandler() {
        return fileHandler;
    }

    public void setCars(List<Car> newCars) {
        this.cars.clear();
        this.cars.addAll(newCars);
    }


    public List<Car> searchCarsByModel(String model) {
        return cars.stream()
                .filter(c -> c.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<Car> searchCarsByPriceRange(double min, double max) {
        return cars.stream()
                .filter(c -> c.getPrice() >= min && c.getPrice() <= max)
                .collect(Collectors.toList());
    }

    public List<Car> searchCarsByStatus(String status) {
        return cars.stream()
                .filter(c -> c.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<Car> searchCarsByYear(int year) {
        return cars.stream()
                .filter(c -> c.getYear() == year)
                .collect(Collectors.toList());
    }
}
