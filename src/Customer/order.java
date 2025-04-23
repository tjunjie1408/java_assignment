package Customer;

import java.util.ArrayList;

public class order {
    String orderID;
    CarDetails car;
    User owner;
    ArrayList<order> allorder = new ArrayList<order>();
    public order(String orderID, CarDetails car, User owner) {
        this.orderID = orderID;
        this.car = car;
        this.owner = owner;
    }
}
