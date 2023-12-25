import java.util.List;
import java.util.Map;

public class Series extends Production {
    int numberOfSeasons;
    int yearOfRelease;
    private Map<String, List<Episode>> seasons;

    public Series(String productionName, List<String> directorNames, List<String> actorNames, List<Genre> genres,
                  List<Rating> ratings, String description, int numberOfSeasons, int yearOfRelease,
                  Map<String, List<Episode>> seasons, Double averageRate, String resposability) {
        this.productionName = productionName;
        this.directorNames = directorNames;
        this.actorNames = actorNames;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.numberOfSeasons = numberOfSeasons;
        this.yearOfRelease = yearOfRelease;
        this.seasons = seasons;
        this.rate = averageRate;
        this.resposability = resposability;
    }

    public void displayInfo() {
        System.out.println("Series: " + this.productionName);
        System.out.println("Directed by: " + this.directorNames);
        System.out.println("Actors: " + this.actorNames);
        System.out.println("Genres: " + this.genres);
        for (Rating rating : this.ratings) {
            System.out.println("Rating: " + rating.username + " " + rating.rating + " " + rating.review);
        }
        System.out.println("Description: " + this.description);
        System.out.println("Rate: " + this.rate);
        System.out.println("Number of seasons: " + this.numberOfSeasons);
        System.out.println("Year of release: " + this.yearOfRelease);
        for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
            System.out.println(entry.getKey());
            for (Episode episode : entry.getValue()) {
                episode.displayInfo();
            }
        }
        System.out.println();
    }
    public String getDisplayInfo() {
        String info = "";
        info += "Series: " + this.productionName + "\n";
        info += "Directed by: " + this.directorNames + "\n";
        info += "Actors: " + this.actorNames + "\n";
        info += "Genres: " + this.genres + "\n";
        for (Rating rating : this.ratings) {
            info += "Rating: " + rating.username + " " + rating.rating + " " + rating.review + "\n";
        }
        info += "Description: " + this.description + "\n";
        info += "Rate: " + this.rate + "\n";
        info += "Number of seasons: " + this.numberOfSeasons + "\n";
        info += "Year of release: " + this.yearOfRelease + "\n";
        for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
            info += entry.getKey() + "\n";
            for (Episode episode : entry.getValue()) {
                info += episode.getDisplayInfo();
            }
        }
        info += "\n";
        return info;
    }
    public Map getSeasons() {
        return this.seasons;
    }
    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }
    public void updateEpisodes(String season, List<Episode> episodes) {
        if (seasons.containsKey(season)) {
            seasons.replace(season, episodes);
        }
    }
}