package Customer;

public class CustomerRequest {
    String requestID;
    CarDetails car;
    User owner;
    public CustomerRequest(String requestID, CarDetails car, User owner) {
        this.requestID = requestID;
        this.car = car;
        this.owner = owner;
    }
}

