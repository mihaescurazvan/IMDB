public class Contributor<T extends Comparable<T>> extends Staff implements RequestManager {
    public Contributor(Information information, String username, int experience) {
        super(information, username, experience);
        this.accountType = AccountType.CONTRIBUTOR;
    }
    public void createRequest(Request request) {
        requests.add(request);
    }
    public void deleteRequest(Request request) {
        requests.remove(request);
    }
}