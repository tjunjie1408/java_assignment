package CarPackage;

public class Car {
    private String carId;
    private String brand;
    private String model;
    private String year;
    private double price;
    private String color;
    private String status;
    private String description;
    private String mileage;
    private String transmission;
    private String fuelType;

    public Car(String carId, String brand, String model, String year, double price,
               String color, String status, String description, String mileage,
               String transmission, String fuelType) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.status = status;
        this.description = description;
        this.mileage = mileage;
        this.transmission = transmission;
        this.fuelType = fuelType;
    }

    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMileage() { return mileage; }
    public void setMileage(String mileage) { this.mileage = mileage; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String toCSV() {
        return carId + "," + brand + "," + model + "," + year + "," + price + "," +
                color + "," + status + "," + description + "," + mileage + "," +
                transmission + "," + fuelType;
    }

    public static Car fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new Car(
                parts[0], // carID
                parts[1], // brand
                parts[2], // model
                parts[3], // year
                Double.parseDouble(parts[4]), // price
                parts[5], // color
                parts[6], // status
                parts[7], // description
                parts[8], // mileage
                parts[9], // transmission
                parts[10] // fuelType
        );
    }

    @Override
    public String toString() {
        return String.format("""
                        Car ID: %s
                        Brand: %s
                        Model: %s
                        Year: %s
                        Price: $%.2f
                        Color: %s
                        Status: %s
                        Description: %s
                        Mileage: %s
                        Transmission: %s
                        Fuel Type: %s""",
                carId, brand, model, year, price, color, status,
                description, mileage, transmission, fuelType);
    }
}