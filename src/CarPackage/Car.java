package CarPackage;

public class Car {
    private String id;
    private String model;
    private int year;
    private double price;
    private String color;
    private String status;

    public Car(String id, String model, int year, double price, String color, String status) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.status = status;
    }



    public Car() {
        this("",  "", 0, 0.0, "", "available");
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getModel() {return model;}
    public void setModel(String model) {this.model = model;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return String.format("Car[id=%s, model=%s, year=%d, price=%.2f, color=%s, status=%s]",
                id, model, year, price, color, status);
    }

    public String toFileString() {
        return String.join(",", id, model,
                String.valueOf(year), String.valueOf(price), color, status);
    }

    public static Car fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 7) return null;
        try {
            return new Car( parts[0], parts[1],
                    Integer.parseInt(parts[2]),
                    Double.parseDouble(parts[3]),
                    parts[4], parts[5]);
        } catch (Exception e) {
            return null;
        }
    }
}
