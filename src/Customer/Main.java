package Customer;

public class Main {
    public static UserPage1 first;
    public static UserPage2 second;
    public static User loginUser = null;
    public static void main(String[] args) {
        DataIO.read();
        first = new UserPage1();
        second = new UserPage2("Guest");
    }
}
