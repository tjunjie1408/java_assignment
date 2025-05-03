package Customer;

public class CarDetails {
    String carID;
    String carBrand;
    String carModel;
    String carColour;
    String carStatus;
    int carPrice;
    public CarDetails(String carID, String carBrand, String carModel,String carColour,String carStatus, int carPrice) {
        this.carID = carID;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carColour = carColour;
        this.carStatus = carStatus;
        this.carPrice = carPrice;
    }
}
