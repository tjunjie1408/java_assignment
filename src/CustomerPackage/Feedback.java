package CustomerPackage;

public class Feedback {
    private final String feedbackId;
    private final String orderId;
    private final String customerId;
    private final int rating;
    private final String comment;

    public Feedback(String feedbackId, String orderId, String customerId, int rating, String comment) {
        this.feedbackId = feedbackId;
        this.orderId    = orderId;
        this.customerId = customerId;
        this.rating     = rating;
        this.comment    = comment != null ? comment : "";
    }
    public String toCSV() {
        String safeComment = comment.replace("\n", " ").replace(",", ";");
        return String.join(",",
                feedbackId,
                orderId,
                customerId,
                String.valueOf(rating),
                safeComment
        );
    }
    public static Feedback fromCSV(String csv) {
        if (csv == null || csv.isEmpty()) return null;
        String[] parts = csv.split(",", 5);
        if (parts.length < 5) return null;
        String id    = parts[0].trim();
        String oid   = parts[1].trim();
        String cid   = parts[2].trim();
        int rate;
        try {
            rate = Integer.parseInt(parts[3].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        String msg   = parts[4].trim().replace(";", ",");
        return new Feedback(id, oid, cid, rate, msg);
    }

    // Getters
    public String getFeedbackId() {
        return feedbackId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId='" + feedbackId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}