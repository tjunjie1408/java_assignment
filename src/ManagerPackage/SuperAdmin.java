package ManagerPackage;

public class SuperAdmin extends Manager{
    public SuperAdmin(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber);
        this.setAccessLevel("SUPER_ADMIN");
    }
}
