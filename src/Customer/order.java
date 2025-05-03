package Customer;

public class order {
    String orderID;
    CarDetails car;
    User owner;
    public order(String orderID, CarDetails car, User owner) {
        this.orderID = orderID;
        this.car = car;
        this.owner = owner;
    }
}
