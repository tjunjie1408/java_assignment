package CustomerPackage;

import java.util.Date;

public class Payment {
    private String paymentId;
    private String orderId;
    private double amount;
    private Date paymentDate;

    public Payment(String paymentId, String orderId, double amount, Date paymentDate) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String toCSV() {
        return paymentId + "," + orderId + "," + amount + "," + paymentDate.getTime();
    }
}