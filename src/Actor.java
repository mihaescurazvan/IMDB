import java.util.List;

public class Actor implements Comparable<Actor> {
    public String name;
    public List<Pair> movies;
    public String biography;
    public String responsability;
    public Actor(String name, List<Pair> movies, String biography, String responsability) {
        this.name = name;
        this.movies = movies;
        this.biography = biography;
        this.responsability = responsability;
    }
    public int compareTo(Actor a) {
        return this.name.compareTo(a.name);
    }
    public void displayInfo() {
        System.out.println();
        System.out.println("Name: " + this.name);
        if (this.biography != null)
            System.out.println("Biography: " + this.biography);
        if (this.movies.size() > 0)
        {
            System.out.println("Movies: ");
            for (Pair p : this.movies) {
                if (p.getName() != null && p.getType() != null)
                    System.out.println("\t" + p.getName() + " - " + p.getType());
            }
        }
        System.out.println();
    }
    public String getDisplayInfo() {
        String info = "";
        info += "Name: " + this.name + "\n";
        if (this.biography != null)
            info += "Biography: " + this.biography + "\n";
        if (this.movies.size() > 0)
        {
            info += "Movies: " + "\n";
            for (Pair p : this.movies) {
                if (p.getName() != null && p.getType() != null)
                    info += "\t" + p.getName() + " - " + p.getType() + "\n";
            }
        }
        info += "\n";
        return info;
    }
}