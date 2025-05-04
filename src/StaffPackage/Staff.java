package StaffPackage;

public abstract class Staff {
    private String name,gender, phoneNumber, email, staffId;
    private int age;

    public Staff(String name, int age, String gender, String phoneNumber, String email, String staffId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.staffId = staffId;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getStaffId() {return staffId;}
    public void setStaffId(String staffId) {this.staffId = staffId;}

    @Override
    public String toString() {
        return String.format("Name: %s\nAge: %d\nGender: %s\nPhone Number: %s\nEmail: %s\nStaff ID: %s",
                this.getClass().getSimpleName(), age, gender, phoneNumber, email, staffId);
    }
}
