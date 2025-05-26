package CustomerPackage;

public class Feedback {
    private String feedbackId;
    private String orderId;
    private String customerId;
    private int rating;
    private String comment;

    public Feedback(String feedbackId, String orderId, String customerId, int rating, String comment) {
        this.feedbackId = feedbackId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.rating = rating;
        this.comment = comment;
    }

    public String toCSV() {
        return feedbackId + "," + orderId + "," + customerId + "," + rating + "," + comment;
    }
}