import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;
public class Movie extends Production {
    public String duration;
    public int yearProduced;
    public Movie(String productionName, List<String> directorNames, List<String> actorNames, List<Genre> genres,
                 List<Rating> ratings, String description, String duration, int yearProduced, Double averageRate,
                 String resposability) {
        this.productionName = productionName;
        this.directorNames = directorNames;
        this.actorNames = actorNames;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.duration = duration;
        this.yearProduced = yearProduced;
        this.rate = averageRate;
        this.resposability = resposability;
    }
    public void displayInfo() {
        System.out.println("Movie: " + this.productionName);
        System.out.println("Directed by: " + this.directorNames);
        System.out.println("Actors: " + this.actorNames);
        System.out.println("Genres: " + this.genres);
        for (Rating rating : this.ratings) {
            System.out.println("Rating: " + rating.username + " " + rating.rating + " " + rating.review);
        }
        System.out.println("Description: " + this.description);
        System.out.println("Rate: " + this.rate);
        System.out.println("Duration: " + this.duration);
        System.out.println("Year produced: " + this.yearProduced);
        System.out.println();
    }
    public String getDisplayInfo() {
        String info = "";
        info += "Movie: " + this.productionName + "\n";
        info += "Directed by: " + this.directorNames + "\n";
        info += "Actors: " + this.actorNames + "\n";
        info += "Genres: " + this.genres + "\n";
        for (Rating rating : this.ratings) {
            info += "Rating: " + rating.username + " " + rating.rating + " " + rating.review + "\n";
        }
        info += "Description: " + this.description + "\n";
        info += "Rate: " + this.rate + "\n";
        info += "Duration: " + this.duration + "\n";
        info += "Year produced: " + this.yearProduced + "\n";
        info += "\n";
        return info;
    }
}