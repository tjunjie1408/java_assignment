package Customer;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataIO {
    public static ArrayList<User> allUser = new ArrayList<User>();
    public static ArrayList<feedback> allfeedback = new ArrayList<feedback>();
    public static ArrayList<CarDetails> allcar = new ArrayList<CarDetails>();
    public static ArrayList<booking> allbooking = new ArrayList<booking>();
    public static ArrayList<order> allorder = new ArrayList<order>();
    public static void write(){
        try{
            PrintWriter a = new PrintWriter("User.txt");
            for(int i=0; i<allUser.size(); i++){
                a.println(allUser.get(i).userID);
                a.println(allUser.get(i).name);
                a.println(allUser.get(i).password);
                a.println(allUser.get(i).phone_number);
                a.println();
            }
            a.close();

            PrintWriter b = new PrintWriter("feedback.txt");
            for(int i=0; i<allfeedback.size(); i++){
                b.println(allfeedback.get(i).number);
                b.println(allfeedback.get(i).rating);
                b.println(allfeedback.get(i).review);
                b.println(allfeedback.get(i).owner.name);
                b.println();
            }
            b.close();

            PrintWriter c = new PrintWriter("CarDetails.txt");
            for(int i=0; i<allcar.size(); i++){
                c.println(allcar.get(i).carID);
                c.println(allcar.get(i).carBrand);
                c.println(allcar.get(i).carModel);
                c.println(allcar.get(i).carStatus);
                c.println(allcar.get(i).carPrice);
                c.println();
            }
            c.close();

            PrintWriter d = new PrintWriter("Booking.txt");
            for(int i=0; i<allbooking.size(); i++){
                d.println(allbooking.get(i).bookingID);
                d.println(allbooking.get(i).car);
                d.println(allbooking.get(i).owner.name);
                d.println();
            }
            d.close();

            PrintWriter e = new PrintWriter("Order.txt");
            for(int i=0; i<allorder.size(); i++){
                e.println(allorder.get(i).orderID);
                e.println(allorder.get(i).car);
                e.println(allorder.get(i).owner.name);
                e.println();
            }
            e.close();

        }catch(Exception e){
            System.out.println("Error in write......");
        }
    }

    public static CarDetails searchCar(String carID) {
        if (carID == null) return null;
        for (int i = 0; i < allcar.size(); i++) {
            if (carID.equals(allcar.get(i).carID)) {
                return allcar.get(i);
            }
        }
        return null;
    }

    public static void read() {
        try{
            Scanner s = new Scanner(new File("User.txt"));
            while (s.hasNext()) {
                String userID = s.nextLine();
                String name = s.nextLine();
                int password = Integer.parseInt(s.nextLine());
                int phonenumber = Integer.parseInt(s.nextLine());
                s.nextLine();
                allUser.add(new User(userID, name, password, phonenumber));
            }

            Scanner t = new Scanner(new File("feedback.txt"));
            while(t.hasNext()){
                int number = Integer.parseInt(t.nextLine());
                String rating = t.nextLine();
                String review = t.nextLine();
                String owner = t.nextLine();
                t.nextLine();
                allfeedback.add(new feedback(number,rating,review,searchName(owner)));
            }

            Scanner u = new Scanner(new File("CarDetails.txt"));
            while(u.hasNext()){
                String carID = u.nextLine();
                String carBrand = u.nextLine();
                String carModel = u.nextLine();
                String carStatus = u.nextLine();
                int carPrice = Integer.parseInt(u.nextLine());
                u.nextLine();
                allcar.add(new CarDetails(carID,carBrand,carModel,carStatus,carPrice));
            }

            Scanner v = new Scanner(new File("Booking.txt"));
            while(v.hasNext()){
                String bookingID = v.nextLine();
                CarDetails car = searchCar(v.nextLine());
                User owner = searchName(v.nextLine());
                v.nextLine();
                allbooking.add(new booking(bookingID,car,owner));
            }

            Scanner w = new Scanner(new File("Order.txt"));
            while(w.hasNext()){
                String orderID = w.nextLine();
                CarDetails car = searchCar(w.nextLine());
                User owner = searchName(w.nextLine());
                w.nextLine();
                allorder.add(new order(orderID,car,owner));
            }

        }catch(Exception e){
            System.out.println("Error in read......");
        }
    }

    public static User searchName(String name) {
        if (name == null) return null;
        for(int i=0; i<allUser.size(); i++){
            if (name.equals(allUser.get(i).name)) {
                return allUser.get(i);
            }
        }
        return null;
    }

    public static String getNextUserID() {
        if (allUser.isEmpty()) {
            return "U10001";
        }
        String lastUserID = allUser.get(allUser.size() - 1).userID;
        int idNumber = Integer.parseInt(lastUserID.substring(1));
        return "U" + (idNumber + 1);
    }

}
