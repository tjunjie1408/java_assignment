package Customer;

public class request {
    String requestID;
    CarDetails car;
    User owner;

    public request(String requestID, CarDetails carDetails, User user) {
        this.requestID = requestID;
        this.car = carDetails;
        this.owner = user;
    }
}
