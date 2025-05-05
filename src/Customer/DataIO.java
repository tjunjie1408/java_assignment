package Customer;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataIO {
    public static ArrayList<User> allCustomer = new ArrayList<User>();
    public static ArrayList<feedback> allfeedback = new ArrayList<feedback>();
    public static ArrayList<CarDetails> allcar = new ArrayList<CarDetails>();
    public static ArrayList<order> allorder = new ArrayList<order>();
    public static void write(){
        try{
            PrintWriter a = new PrintWriter("User.txt");
            for(int i=0; i<allCustomer.size(); i++){
                a.println(allCustomer.get(i).customerID);
                a.println(allCustomer.get(i).name);
                a.println(allCustomer.get(i).password);
                a.println(allCustomer.get(i).phone_number);
                a.println(allCustomer.get(i).address);
                a.println();
            }
            a.close();

            PrintWriter b = new PrintWriter("feedback.txt");
            for(int i=0; i<allfeedback.size(); i++){
                b.println(allfeedback.get(i).feedbackID);
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
                c.println(allcar.get(i).carColour);
                c.println(allcar.get(i).carStatus);
                c.println(allcar.get(i).carPrice);
                c.println();
            }
            c.close();

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

    public static void read() {
        try{
            Scanner s = new Scanner(new File("User.txt"));
            while (s.hasNext()) {
                String userID = s.nextLine();
                String name = s.nextLine();
                int password = Integer.parseInt(s.nextLine());
                int phonenumber = Integer.parseInt(s.nextLine());
                String address = s.nextLine();
                s.nextLine();
                allCustomer.add(new User(userID, name, password, phonenumber,address));
            }

            Scanner t = new Scanner(new File("feedback.txt"));
            while(t.hasNext()){
                String feedbackID = t.nextLine();
                String rating = t.nextLine();
                String review = t.nextLine();
                String owner = t.nextLine();
                t.nextLine();
                allfeedback.add(new feedback(feedbackID,rating,review,searchName(owner)));
            }

            Scanner u = new Scanner(new File("CarDetails.txt"));
            while(u.hasNext()){
                String carID = u.nextLine();
                String carBrand = u.nextLine();
                String carModel = u.nextLine();
                String carColour = u.nextLine();
                String carStatus = u.nextLine();
                int carPrice = Integer.parseInt(u.nextLine());
                u.nextLine();
                allcar.add(new CarDetails(carID,carBrand,carModel,carColour,carStatus,carPrice));
            }

            Scanner w = new Scanner(new File("Order.txt"));
            while(w.hasNext()){
                String orderID = w.nextLine();
                CarDetails car = getNextCarID(w.nextLine());
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
        for(int i=0; i<allCustomer.size(); i++){
            if (name.equals(allCustomer.get(i).name)) {
                return allCustomer.get(i);
            }
        }
        return null;
    }

    public static String getNextUserID() {
        if (allCustomer.isEmpty()) {
            return "U10001";
        }
        String lastUserID = allCustomer.get(allCustomer.size() - 1).customerID;
        int idNumber = Integer.parseInt(lastUserID.substring(1));
        return "U" + (idNumber + 1);
    }

    public static String getNextFeedbackID() {
        if (allfeedback.isEmpty()) {
            return "F10001";
        }
        String lastFeedbackID = allfeedback.get(allfeedback.size() - 1).feedbackID;
        int idNumber = Integer.parseInt(lastFeedbackID.substring(1));
        return "F" + (idNumber + 1);
    }


    public static CarDetails getNextCarID(String selectedCarID) {
        for(int i=0; i<allcar.size(); i++){
            if(selectedCarID.equals(allcar.get(i).carID)){
                return allcar.get(i);
            }
        }
        return null;
    }
}
