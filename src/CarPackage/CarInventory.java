package CarPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarInventory {
    private List<Car> cars;
    private final FileHandling fileHandler;

    public CarInventory(String filePath) {
        this.fileHandler = new FileHandling(filePath);
        try {
            this.cars = fileHandler.loadCarsFile();
        } catch (IOException e) {
            System.err.println("Failed to load cars: " + e.getMessage());
            this.cars = new ArrayList<>();
        }
    }

    private boolean isValidCar(Car car) {
        return car != null &&
                car.getId() != null && !car.getId().isEmpty() &&
                car.getYear() >= 1900 && car.getYear() <= 2100 &&
                car.getPrice() > 0;
    }

    public boolean addCar(Car car) {
        if(!isValidCar(car)) return false;
        for (Car existingCar : cars) {
            if (existingCar.getId().equals(car.getId())) {
                return false;
            }
        }
        return cars.add(car);
    }

    public boolean deleteCar(String id){
        for (int i = 0; i < cars.size(); i++) {
            if(cars.get(i).getId().equals(id)){
                cars.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean updateCar(Car updatedCar) {
        if(updatedCar == null || !isValidCar(updatedCar)) return false;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(updatedCar.getId())) {
                cars.set(i, updatedCar);
                return true;
            }
        }
        return false;
    }

    public Car searchCar(String id){
        for (Car car : cars) {
            if(car.getId().equals(id)){
                return car;
            }
        }
        return null;
    }

    public List<Car> getAllCars(){
        return new ArrayList<>(cars);
    }

    public List<Car> searchCarsByMake(String make){
        List<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if(car.getMake().toLowerCase().contains(make.toLowerCase())){
                results.add(car);
            }
        }
        return results;
    }

    public List<Car> searchCarsByModel(String model){
        List<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if(car.getModel().toLowerCase().contains(model.toLowerCase())){
                results.add(car);
            }
        }
        return results;
    }

    public List<Car> searchCarsByPriceRange(double minPrice, double maxPrice){
        List<Car> results = new ArrayList<>();
        for (Car car : cars){
            double price = car.getPrice();
            if (price >= minPrice && price <= maxPrice) {
                results.add(car);
            }
        }
        return results;
    }

    public List<Car> searchCarsByStatus(String status){
        List<Car> results = new ArrayList<>();
        for(Car car : cars){
            if(car.getStatus().equalsIgnoreCase(status)){
                results.add(car);
            }
        }
        return results;
    }

    public List<Car> searchCarsByYear(int year){
        List<Car> results = new ArrayList<>();
        for(Car car : cars){
            if(car.getYear() == year){
                results.add(car);
            }
        }
        return results;
    }

    public void clearInventory(){
        cars.clear();
    }

    public void setCars(List<Car> cars){
        this.cars = new ArrayList<>(cars);
    }

    public int getInventorySize(){
        return cars.size();
    }
}
