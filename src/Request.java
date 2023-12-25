import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject{
    private RequestType requestType;
    private LocalDateTime date;
    public String actorName;
    public String movieName;
    public String description;
    public String username;
    public String responsableUsername;
    public Request(RequestType requestType, String username, String description, String date, String movieName,
                   String actorName) {
        this.requestType = requestType;
        this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.username = username;
        this.description = description;
        this.username = username;
        if (actorName != null) {
            this.actorName = actorName;
        }
        if (movieName != null) {
            this.movieName = movieName;
        }
        this.responsableUsername = "Admin";
    }
    public Request(RequestType requestType, String username, String description, String responsableUsername,
                   String date, String movieName, String actorName) {
        this.requestType = requestType;
        this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.username = username;
        this.description = description;
        this.username = username;
        this.responsableUsername = responsableUsername;
        if (actorName != null) {
            this.actorName = actorName;
        }
        if (movieName != null) {
            this.movieName = movieName;
        }
    }
    public RequestType getRequestType() {
        return requestType;
    }
    public LocalDateTime getDate() {
        return date;
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
                    if (((User) observer).username.equals(responsableUsername) ||
                            (observer instanceof Admin && responsableUsername.equals("Admin"))) {
                        observer.update(notification);
                    }
                }
            } else {
                if (observer instanceof User) {
                    if (((User) observer).username.equals(username)) {
                        observer.update(notification);
                    }
                }
            }
        }
    }
    public String toString() {
        String s = "";
        s += "RequestType: " + requestType + "\n";
        s += "Date: " + date + "\n";
        s += "Username: " + username + "\n";
        s += "Description: " + description + "\n";
        s += "ResponsableUsername: " + responsableUsername + "\n";
        if (actorName != null) {
            s += "ActorName: " + actorName + "\n";
        }
        if (movieName != null) {
            s += "MovieName: " + movieName + "\n";
        }
        return s;
    }
}