import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public abstract class User<T extends Comparable<T>> implements Observer {
    public static class InformationBuilder {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char sex;
        private LocalDate dateOfBirth;

        public InformationBuilder() {

        }
        public InformationBuilder setCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }
        public InformationBuilder setName(String name) {
            this.name = name;
            return this;
        }
        public InformationBuilder setCountry(String country) {
            this.country = country;
            return this;
        }
        public InformationBuilder setAge(int age) {
            this.age = age;
            return this;
        }
        public InformationBuilder setSex(char sex) {
            this.sex = sex;
            return this;
        }
        public InformationBuilder setDateOfBirth(String dateOfBirth) throws DateTimeParseException {
            try {
                this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
            return this;
        }
        public User.Information build(User user) {
            return user.new Information(this);
        }
    }
    public class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char sex;
        private LocalDate dateOfBirth;
        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.sex = builder.sex;
            this.dateOfBirth = builder.dateOfBirth;
        }
        public Credentials getCredentials() {
            return credentials;
        }
        public String getName() {
            return name;
        }
        public String getCountry() {
            return country;
        }
        public int getAge() {
            return age;
        }
        public char getSex() {
            return sex;
        }
        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }
        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }
    public AccountType accountType;
    public String username;
    public int experience;
    public List<String> notifications = new ArrayList<String>();
    Comparator<Object> comparator = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Movie && o2 instanceof Movie) {
                return ((Movie) o1).compareTo((Movie) o2);
            }
            if (o1 instanceof Series && o2 instanceof Series) {
                return ((Series) o1).compareTo((Series) o2);
            }
            if (o1 instanceof Actor && o2 instanceof Actor) {
                return ((Actor) o1).compareTo((Actor) o2);
            }
            if (o1 instanceof Movie && o2 instanceof Series) {
                return ((Movie) o1).productionName.compareTo(((Series) o2).productionName);
            }
            if (o1 instanceof Series && o2 instanceof Movie) {
                return ((Series) o1).productionName.compareTo(((Movie) o2).productionName);
            }
            if (o1 instanceof Movie && o2 instanceof Actor) {
                return ((Movie) o1).productionName.compareTo(((Actor) o2).name);
            }
            if (o1 instanceof Actor && o2 instanceof Movie) {
                return ((Actor) o1).name.compareTo(((Movie) o2).productionName);
            }
            if (o1 instanceof Series && o2 instanceof Actor) {
                return ((Series) o1).productionName.compareTo(((Actor) o2).name);
            }
            if (o1 instanceof Actor && o2 instanceof Series) {
                return ((Actor) o1).name.compareTo(((Series) o2).productionName);
            }
            return 0;
        }
    };
    public SortedSet<T> preferences = new TreeSet<T>(comparator);

    public Information information;

    private boolean isLogged = false;

    public User(Information information, String username, int experience) {
        this.information = information;
        this.username = username;
        this.experience = experience;
    }

    public void addMovieToPreferences(Movie movie) {
        preferences.add((T) movie);
    }

    public void addSeriesToPreferences(Series series) {
        preferences.add((T) series);
    }

    public void addActorToPreferences(Actor actor) {
        preferences.add((T) actor);
    }

    public void deleteMovieFromPreferences(Movie movie) {
        preferences.remove(movie);
    }

    public void deleteSeriesFromPreferences(Series series) {
        preferences.remove(series);
    }

    public void deleteActorFromPreferences(Actor actor) {
        preferences.remove(actor);
    }

    public void logout() {
        isLogged = false;
    }
    public void login() {
        isLogged = true;
    }

    public boolean isLogged() {
        return isLogged;
    }
    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public void deleteNotification(String notification) {
        notifications.remove(notification);
    }

    public void updateExperience(ExperienceStrategy experienceStrategy) {
        experience += experienceStrategy.calculateExperience();
    }

    public void update (String notification) {
        addNotification(notification);
    }

    public List<String> ratedProductions = new ArrayList<String>();
    public List<String> ratedAndDeletedProductions = new ArrayList<String>();

    public void displayPreferences() {
        System.out.println();
        System.out.println("Your favorites are: ");
        for (T t : preferences) {
            if (t instanceof Movie) {
                System.out.println("Movie: ");
                ((Movie) t).displayInfo();
            }
            if (t instanceof Series) {
                System.out.println("Series: ");
                ((Series) t).displayInfo();
            }
            if (t instanceof Actor) {
                System.out.println("Actor: ");
                ((Actor) t).displayInfo();
            }
        }
    }
    public List<T> getFavorites() {
        List<T> favorites = new ArrayList<T>();
        for (T t : preferences) {
            favorites.add(t);
        }
        return favorites;
    }
}