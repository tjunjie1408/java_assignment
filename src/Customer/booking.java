package Customer;

import java.util.ArrayList;

public class booking {
    String bookingID;
    CarDetails car;
    User owner;
    ArrayList<booking> allbooking = new ArrayList<booking>();
    public booking(String bookingID, CarDetails car, User owner) {
        this.bookingID = bookingID;
        this.car = car;
        this.owner = owner;
    }
}
