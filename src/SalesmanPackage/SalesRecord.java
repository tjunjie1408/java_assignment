package SalesmanPackage;

class SalesRecord {
    private String recordId;
    private String orderId;
    private String salesmanId;
    private String comment;

    public SalesRecord(String recordId, String orderId, String salesmanId, String comment) {
        this.recordId = recordId;
        this.orderId = orderId;
        this.salesmanId = salesmanId;
        this.comment = comment;
    }

    public String toCSV() {
        return recordId + "," + orderId + "," + salesmanId + "," + comment;
    }
}