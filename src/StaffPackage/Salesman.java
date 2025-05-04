package StaffPackage;

public class Salesman extends Staff{
    public Salesman(String name, int age, String gender, String phoneNumber, String email, String staffId) {
        super(name, age, gender, phoneNumber, email, staffId);
    }
    public String toCsv() {
        return String.join(",",
                getName(),
                String.valueOf(getAge()),
                getGender(),
                getPhoneNumber(),
                getEmail(),
                getStaffId()
        );
    }

    /** 从 CSV 行反序列化得到 Salesman 对象 */
    public static Salesman fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length != 6) return null;
        return new Salesman(
                parts[0],
                Integer.parseInt(parts[1]),
                parts[2],
                parts[3],
                parts[4],
                parts[5]
        );
    }
}
