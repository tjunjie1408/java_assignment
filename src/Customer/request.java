package Customer;

import java.util.ArrayList;

public class request {
    String requestID;
    CarDetails car;
    User owner;
    ArrayList<request> allbooking = new ArrayList<request>();

    public request(String requestID, CarDetails carDetails, User user) {
        this.requestID = requestID;
        this.car = carDetails;
        this.owner = user;
    }
}
