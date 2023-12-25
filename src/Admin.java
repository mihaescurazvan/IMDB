import java.util.ArrayList;
import java.util.List;

public class Admin<T extends Comparable<T>> extends Staff<T> {
    static class RequestsHolder {
        public static List<Request> requests = new ArrayList<Request>();
        public static List<Request> getRequests() {
            return requests;
        }
        public static void addRequest(Request request) {
            requests.add(request);
        }
        public static void deleteRequest(Request request) {
            requests.remove(request);
        }
    }
    public Admin(Information information, String username, int experience) {
        super(information, username, experience);
        this.accountType = AccountType.ADMIN;
    }
    public void createRequest(Request request) {
        Admin.RequestsHolder.addRequest(request);
    }
    public void deleteRequest(Request request) {
        Admin.RequestsHolder.deleteRequest(request);
    }
    public void addUser(User user, List<T> users) {
        users.add((T) user);
    }
    public void removeUser(String username, List<T> users) {
        for (T t : users) {
            User user = (User) t;
            if (user.username.equals(username)) {
                users.remove(t);
                break;
            }
        }
    }
}