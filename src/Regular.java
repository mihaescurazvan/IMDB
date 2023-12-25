import java.util.ArrayList;
import java.util.List;

public class Regular<T extends Comparable<T>> extends User implements RequestManager {
    public List<Request> requests;
    public Regular (Information information, String username, int experience) {
        super(information, username, experience);
        this.accountType = AccountType.REGULAR;
        this.requests = new ArrayList<Request>();
    }
    public void createRequest(Request request) {
        requests.add(request);
    }
    public void deleteRequest(Request request) {
        requests.remove(request);
    }
    public void addRating (Production p, Rating rating) {
        p.ratings.add(rating);
    }
}