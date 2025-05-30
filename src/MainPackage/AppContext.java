package MainPackage;
import CarPackage.*;
import CustomerPackage.*;
import ManagerPackage.*;
import SalesmanPackage.*;

public class AppContext {
    private CarManagement carManagement;
    private SalesmanManagement salesmanManagement;
    private CustomerManagement customerManagement;
    private ManagerManagement managerManagement;
    private String currentManagerId;
    private String carId;

    public AppContext() {
        this.carManagement = new CarManagement();
        this.salesmanManagement = new SalesmanManagement();
        this.customerManagement = new CustomerManagement();
        this.managerManagement = new ManagerManagement();
        this.salesmanManagement.setCustomerManagement(customerManagement);
        this.salesmanManagement.setCarManagement(carManagement);
        this.customerManagement.setCarManagement(carManagement);
    }
    public CarManagement getCarManagement() {
        return carManagement;
    }
    public SalesmanManagement getSalesmanManagement() {return salesmanManagement;}
    public CustomerManagement getCustomerManagement() {
        return customerManagement;
    }
    public ManagerManagement getManagerManagement() {
        return managerManagement;
    }
    public void setCurrentManagerId(String id) {
        this.currentManagerId = id;
    }
    public String getCurrentManagerId() {
        return currentManagerId;
    }
    public void setCarId(String id) {this.carId = id;}
    public String getCarId() {return carId;}
}