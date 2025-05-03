package Customer;

public class Main {
    public static CustomerPage1 first;
    public static CustomerPage2 second;
    public static User loginCustomer = null;
    public static CarDetails showcar = null;
    public static void main(String[] args) {
        DataIO.read();
        first = new CustomerPage1();
        second = new CustomerPage2("Guest");
    }
}
