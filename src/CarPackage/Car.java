package CarPackage;

public class Car {
    private String id;
    private String make;
    private String model;
    private int year;
    private double price;
    private String colour;
    private String status;

    public Car(String id, String make, String model, int year, double price, String colour, String status) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.colour = colour;
        this.status = status;
    }



    public Car() {
        this.id = "";
        this.make = "";
        this.model = "";
        this.year = 0;
        this.price = 0.0;
        this.colour = "";
        this.status = "available";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return colour;
    }

    public void setColor(String color) {
        this.colour = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Car [ID=" + id +
                ", Make=" + make +
                ", Model=" + model +
                ", Year=" + year +
                ", Price=$" + price +
                ", Color=" + colour +
                ", Status=" + status + "]";
    }

    public String toFileString() {
        return String.join(",",
                id,
                make,
                model,
                String.valueOf(year),
                String.valueOf(price),
                colour,
                status);
    }

    public static Car fromFileString(String fileString) {
        String[] data = fileString.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        try {
            return new Car(
                    data[0],
                    data[1],
                    data[2],
                    Integer.parseInt(data[3]),
                    Double.parseDouble(data[4]),
                    data[5],
                    data[6]
            );
        } catch (Exception e) {
            System.err.println("Invalid car data: " + fileString);
            return null;
        }
    }
}
