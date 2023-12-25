public class UserFactory {
    public static User factory (String type, User.Information information, String username, int experience) {
        if (type.equals("Regular")) {
            return new Regular(information, username, experience);
        } else if (type.equals(("Contributor"))) {
            return new Contributor(information, username, experience);
        } else if (type.equals("Admin")) {
            return new Admin(information, username, experience);
        }
        return null;
    }

}
