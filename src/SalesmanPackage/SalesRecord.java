package SalesmanPackage;

public class SalesRecord {
    private final String recordId;
    private final String orderId;
    private final String salesmanId;
    private final String comment;
    public SalesRecord(String recordId, String orderId, String salesmanId, String comment) {
        this.recordId = recordId;
        this.orderId = orderId;
        this.salesmanId = salesmanId;
        this.comment = comment;
    }
    public static SalesRecord fromCSV(String csv) {
        if (csv == null || csv.isEmpty()) return null;
        String[] parts = csv.split(",", 4);
        if (parts.length < 4) return null;
        return new SalesRecord(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim()
        );
    }

    public String toCSV() {
        String safeComment = comment.replace("\n", " ").replace(",", ";");
        return String.join(",", recordId, orderId, salesmanId, safeComment);
    }
    // Getters
    public String getRecordId() {
        return recordId;
    }
    public String getOrderId() {
        return orderId;
    }
    public String getSalesmanId() {
        return salesmanId;
    }
    public String getComment() {
        return comment;
    }
    @Override
    public String toString() {
        return "SalesRecord{" +
                "recordId='" + recordId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", salesmanId='" + salesmanId + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}