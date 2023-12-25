import java.util.ArrayList;
import java.util.List;

abstract class Production implements Comparable<Production>, Subject {
    public String productionName;
    public List<String> directorNames;
    public List<String> actorNames;
    public List<Genre> genres;
    public List<Rating> ratings;
    public String description;
    public Double rate;
    String resposability;
    public abstract void displayInfo();
    public abstract String getDisplayInfo();
    public int compareTo(Production p) {
        return this.productionName.compareTo(p.productionName);
    }
    public List<Observer> observers = new ArrayList<Observer>();
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObservers(String notification, int type) {
        for (Observer observer : observers) {
            if (type == 1) {
                if (observer instanceof User) {
                    if (((User) observer).username.equals(resposability) ||
                            (observer instanceof Admin && resposability.equals("Admin"))) {
                        observer.update(notification);
                    }
                }
            } else {
                if (observer instanceof User) {
                    if (!(((User) observer).username.equals(resposability) ||
                            (observer instanceof Admin && resposability.equals("Admin")))) {
                        observer.update(notification);
                    }
                }
            }
        }
    }
}