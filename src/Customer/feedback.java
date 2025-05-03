package Customer;

public class feedback {
    String feedbackID;
    int rating;
    String review;
    User owner;

    public feedback(String feedbackID,String rating,String review,User owner) {
        this.feedbackID = feedbackID;
        this.rating = Integer.parseInt(rating);
        this.review = review;
        this.owner = owner;
    }
}
