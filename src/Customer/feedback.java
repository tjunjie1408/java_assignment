package Customer;

import java.util.ArrayList;

public class feedback {
    int number;
    int rating;
    String review;
    User owner;

    public feedback(int number,String rating,String review,User owner) {
        this.number = number;
        this.rating = Integer.parseInt(rating);
        this.review = review;
        this.owner = owner;
    }
}
