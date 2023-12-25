import java.util.ArrayList;
import java.util.List;

public class Rating {
    public String username;
    public int rating;
    public String review;
    public Rating(String username, int rating, String review) {
        this.username = username;
        this.rating = rating;
        this.review = review;
    }
}