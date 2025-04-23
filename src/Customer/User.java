package Customer;

import java.util.ArrayList;

public class User {
    String userID;
    String name;
    int password;
    int phone_number;
    ArrayList<feedback> userfeedback = new ArrayList<feedback>();
    public User(String userID,String name, int password, int phone_number) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
    }
}
