package Customer;

public class User {
    String customerID;
    String name;
    int password;
    int phone_number;
    String address;
    public User(String customerID, String name, int password, int phone_number, String address) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
        this.address = address;
    }
}
