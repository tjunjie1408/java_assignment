package CustomerPackage;

import java.util.Date;
public class Payment {
    private String paymentId;
    private String orderId;
    private double amount;
    private Date paymentDate;

    public Payment(String paymentId, String orderId, double amount, Date paymentDate) {
        this.paymentId   = paymentId;
        this.orderId     = orderId;
        this.amount      = amount;
        this.paymentDate = paymentDate;
    }

    // Getters and setters
    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }

    public String toCSV() {
        return String.join(",",
                paymentId,
                orderId,
                String.valueOf(amount),
                String.valueOf(paymentDate.getTime())
        );
    }

    public static Payment fromCSV(String csv) {
        if (csv == null || csv.isEmpty()) return null;
        String[] parts = csv.split(",", 4);
        if (parts.length < 4) return null;
        String pid = parts[0].trim();
        String oid = parts[1].trim();
        double amt;
        try {
            amt = Double.parseDouble(parts[2].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        long ts;
        try {
            ts = Long.parseLong(parts[3].trim());
        } catch (NumberFormatException e) {
            ts = System.currentTimeMillis();
        }
        return new Payment(pid, oid, amt, new Date(ts));
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
