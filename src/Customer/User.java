package Customer;

import java.util.ArrayList;

public class User {
    String customerID;
    String name;
    int password;
    int phone_number;
    String address;
    ArrayList<feedback> userfeedback = new ArrayList<feedback>();
    public User(String customerID, String name, int password, int phone_number, String address) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
        this.address = address;
    }
}
