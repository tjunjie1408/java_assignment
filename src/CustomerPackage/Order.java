package CustomerPackage;

import java.util.Date;

public class Order {
    private String orderId;
    private String username;
    private String carId;
    private String status;
    private Date orderDate;

    public Order(String orderId, String username, String carId, String status, Date orderDate) {
        this.orderId = orderId;
        this.username = username;
        this.carId = carId;
        this.status = status;
        this.orderDate = orderDate;
    }

    public String getOrderId() { return orderId; }
    public String getUsername() { return username; }
    public String getCarId() { return carId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCSV() {
        return orderId + "," + username + "," + carId + "," + status + "," + orderDate.getTime();
    }

    public static Order fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new Order(parts[0], parts[1], parts[2], parts[3], new Date(Long.parseLong(parts[4])));
    }
}