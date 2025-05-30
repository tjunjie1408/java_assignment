package CarPackage;

public class Car {
    private String carId;
    private String brand;
    private String model;
    private String color;
    private double price;
    private String photoPath;
    private String status; // Added to track car status

    // Constructor
    public Car(String carId, String brand, String model, String color, double price, String photoPath, String status) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.price = price;
        this.photoPath = photoPath;
        this.status = status;
    }

    // Getters and Setters
    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCSV() {
        return carId + "," + brand + "," + model + "," + color + "," + price + "," + photoPath + "," + status;
    }

    public static Car fromCSV(String line) {
        String[] parts = line.split(",");
        String carId = parts[0];
        String brand = parts[1];
        String model = parts[2];
        String color = parts[3];
        double price = Double.parseDouble(parts[4]);
        String photoPath = parts[5];
        String status = parts[6];
        return new Car(carId, brand, model, color, price, photoPath, status);
    }

    @Override
    public String toString() {
        return String.format("""
                Car ID: %s
                Brand: %s
                Model: %s
                Color: %s
                Price: $%.2f
                Photo Path: %s
                Status: %s""",
                carId, brand, model, color, price, photoPath, status);
    }
}