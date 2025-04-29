package Customer;

import java.util.ArrayList;

public class CarDetails {
    String carID;
    String carBrand;
    String carModel;
    String carStatus;
    int carPrice;
    ArrayList<request> allbooking = new ArrayList<request>();
    ArrayList<order> allorder = new ArrayList<order>();

    public CarDetails(String carID, String carBrand, String carModel, String carStatus, int carPrice) {
        this.carID = carID;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carStatus = carStatus;
        this.carPrice = carPrice;
    }
}
