import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Random;


public class IMDB<T> {
    private static IMDB instance = null;
    private IMDB() { }
    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }
    public List<User> users = new ArrayList<User>();
    public List<Actor> actors = new ArrayList<Actor>();
    public List<Request> requests = new ArrayList<Request>();
    public List<Production> productions = new ArrayList<Production>();

    public Actor findActor(String name) {
        for (Actor a : actors) {
            if (a.name.equals(name)) {
                return a;
            }
        }
        return null;
    }

    public Production findProduction(String name) {
        for (Production p : productions) {
            if (p.productionName.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public String createRandomStrongPassword() {
        String password = "";
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*_=+-/.?<>)";
        String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            password += combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

    public void addDeleteActorsProductionToFromSystem (User user, Scanner scanner) throws InvalidCommandException {
        System.out.println("Choose one of the following options:");
        System.out.println("\t1. Add production to system");
        System.out.println("\t2. Delete production from system");
        System.out.println("\t3. Add actor to system");
        System.out.println("\t4. Delete actor from system");
        int option2 = 0;
        try {
            option2 = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 < 1 || option2 > 4) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 == 1) {
            System.out.println("Enter the name of the production:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String productionName = scanner.nextLine();
            System.out.println();
            for (Production p : productions) {
                if (p.productionName.equals(productionName)) {
                    System.out.println("Production already exists!");
                    return;
                }
            }
            System.out.println("Enter the type of the production:");
            String type = scanner.nextLine();
            System.out.println();
            System.out.println("Enter the description of the production:");
            String description = scanner.nextLine();
            System.out.println();
            System.out.println("Enter the directors of the production:");
            String directorsString = scanner.nextLine();
            System.out.println();
            List<String> directors = Arrays.asList(directorsString.split(","));
            System.out.println("Enter the actors of the production:");
            String actorsString = scanner.nextLine();
            System.out.println();
            List<String> actors = Arrays.asList(actorsString.split(","));
            System.out.println("Enter the genres of the production:");
            String genresString = scanner.nextLine();
            System.out.println();
            List<Genre> genres = new ArrayList<Genre>();
            for (String genre : genresString.split(",")) {
                try {
                    genres.add(Genre.valueOf(genre));
                } catch (IllegalArgumentException e) {
                    throw new InvalidCommandException("Invalid command!");
                }
            }
            List<Rating> ratings = new ArrayList<Rating>();
            Double averageRate = 0.0;
            if (type.equals("Movie")) {
                System.out.println("Enter the duration of the movie:");
                // read everthing including spaces until the end of the line
                String duration = scanner.nextLine();
                System.out.println();
                System.out.println("Enter the release year of the movie:");
                int releaseYear = scanner.nextInt();
                System.out.println();
                productions.add(
                        new Movie(
                                productionName,
                                directors,
                                actors,
                                genres,
                                ratings,
                                description,
                                duration,
                                releaseYear,
                                averageRate,
                                user.username
                        )
                );
                ((Staff) user).addProductionSystem(productions.get(productions.size() - 1));
                for (String actor : actors) {
                    boolean ok = false;
                    for (Actor a : this.actors) {
                        if (a.name.equals(actor)) {
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        Actor newActor = new Actor(actor, new ArrayList<Pair>(), null, user.username);
                        newActor.movies.add(new Pair(productionName, type));
                        this.actors.add(newActor);
                        ((Staff) user).addActorSystem(newActor);
                    }
                }
                productions.get(productions.size() - 1).addObserver((User) user);
            } else if (type.equals("Series")) {
                System.out.println("Enter the release year of the series:");
                int releaseYear = 0;
                try {
                    releaseYear = scanner.nextInt();
                } catch (InputMismatchException e) {
                    throw new InvalidCommandException("Invalid command!");
                }
                System.out.println();
                System.out.println("Enter the number of seasons of the series:");
                int nrSeasons = 0;
                try {
                    nrSeasons = scanner.nextInt();
                } catch (InputMismatchException e) {
                    throw new InvalidCommandException("Invalid command!");
                }
                System.out.println();
                Map<String, List<Episode>> seasons = new HashMap<String, List<Episode>>();
                for (int i = 1; i <= nrSeasons; i++) {
                    System.out.println("Enter the number of episodes of season " + i + ":");
                    int nrEpisodes = 0;
                    try {
                        nrEpisodes = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                    System.out.println();
                    List<Episode> episodes = new ArrayList<Episode>();
                    for (int j = 1; j <= nrEpisodes; j++) {
                        System.out.println("Enter the name of episode " + j + ":");
                        // read everthing including spaces until the end of the line
                        scanner.nextLine();
                        String episodeName = scanner.nextLine();
                        System.out.println();
                        System.out.println("Enter the duration of episode " + j + ":");
                        String duration = scanner.nextLine();
                        System.out.println();
                        episodes.add(new Episode(episodeName, duration));
                    }
                    seasons.put("Season " + i, episodes);
                }
                productions.add(
                        new Series(
                                productionName,
                                directors,
                                actors,
                                genres,
                                ratings,
                                description,
                                nrSeasons,
                                releaseYear,
                                seasons,
                                averageRate,
                                user.username
                        )
                );
                ((Staff) user).addProductionSystem(productions.get(productions.size() - 1));
                for (String actor : actors) {
                    boolean ok = false;
                    for (Actor a : this.actors) {
                        if (a.name.equals(actor)) {
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        Actor newActor = new Actor(actor, new ArrayList<Pair>(), null, user.username);
                        newActor.movies.add(new Pair(productionName, type));
                        this.actors.add(newActor);
                        ((Staff) user).addActorSystem(newActor);
                    }
                }
                productions.get(productions.size() - 1).addObserver((User) user);
            }
            for (Object deleted : ((Staff) user).deletedProductionsActors) {
                String deletedName = (String) deleted;
                if (deletedName.equals(productionName)) {
                    return;
                }
            }
            user.updateExperience(new AddProductionActor());
        }
        if (option2 == 3) {
            System.out.println("Enter the name of the actor:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String actorName = scanner.nextLine();
            System.out.println();
            for (Actor a : actors) {
                if (a.name.equals(actorName)) {
                    System.out.println("Actor already exists!");
                    return;
                }
            }
            System.out.println("Enter the biography of the actor:");
            String description = scanner.nextLine();
            System.out.println();
            List <Pair> movies = new ArrayList<>();
            System.out.println("Enter the movies of the actor:");
            String moviesString = scanner.nextLine();
            System.out.println();
            for (String movie : moviesString.split(",")) {
                String[] movieInfo = movie.split("-");
                movies.add(new Pair(movieInfo[0], movieInfo[1]));
            }
            actors.add(new Actor(actorName, movies, description, user.username));
            ((Staff) user).addActorSystem(actors.get(actors.size() - 1));
            for (Object deleted : ((Staff) user).deletedProductionsActors) {
                String deletedName = (String) deleted;
                if (deletedName.equals(actorName)) {
                    return;
                }
            }
            user.updateExperience(new AddProductionActor());
        }
        if (option2 == 2) {
            System.out.println("Enter the name of the production:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String productionName = scanner.nextLine();
            System.out.println();

            boolean productionExists = false;
            for (Production p : productions) {
                if (p.productionName.equals(productionName)) {
                    if (p.resposability.equals(user.username) ||
                            (user instanceof Admin && p.resposability.equals("Admin"))) {
                        productionExists = true;
                        productions.remove(p);
                        ((Staff) user).removeProductionSystem(productionName);
                        ((Staff) user).deletedProductionsActors.add(productionName);
                        break;
                    } else {
                        System.out.println("You are not responsible for this production!");
                        return;
                    }
                }
            }
            if (!productionExists) {
                System.out.println("No production with this name!");
            }
        }
        if (option2 == 4) {
            System.out.println("Enter the name of the actor:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String actorName = scanner.nextLine();
            System.out.println();
            boolean actorExists = false;
            for (Actor a : actors) {
                if (a.name.equals(actorName)) {
                    if (a.responsability.equals(user.username) ||
                            (user instanceof Admin && a.responsability.equals("Admin"))) {
                        actorExists = true;
                        actors.remove(a);
                        ((Staff) user).removeActorSystem(actorName);
                        ((Staff) user).deletedProductionsActors.add(actorName);
                        break;
                    } else {
                        System.out.println("You are not responsible for this actor!");
                        return;
                    }
                }
            }
            if (!actorExists) {
                System.out.println("No actor with this name!");
            }
        }
    }

    public void viewResolveRequests(User user, Scanner scanner) throws InvalidCommandException {
        Map<Integer, Request> map = new LinkedHashMap<>();

        System.out.println("This are your requests:");
        int i = 1;
        for(Object requestObj : ((Staff) user).requests) {
            Request request = (Request) requestObj;
            map.put(i, request);
            System.out.println(i + ". " + request);
            i++;
        }
        if (map.isEmpty()) {
            System.out.println("You don't have any requests!");
            return;
        }
        System.out.println("Select one of the following options:");
        System.out.println("\t1. Mark request as solved/rejected");
        System.out.println("\t2. Back");
        int option2 = 0;
        try {
            option2 = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 < 1 || option2 > 2) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 == 2) {
            return;
        }
        System.out.println("Enter the id of the request you want to solve:");
        int id = 0;
        try {
            id = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (id < 1 || id > map.size()) {
            throw new InvalidCommandException("Invalid command!");
        }
        Request request = map.get(id);
        System.out.println("Select one of the following options:");
        System.out.println("\t1. Accept request");
        System.out.println("\t2. Reject request");
        int option3 = 0;
        try {
            option3 = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option3 < 1 || option3 > 2) {
            throw new InvalidCommandException("Invalid command!");
        }
        requests.remove(request);
        ((Staff) user).resolveRequest(request);
        if (option3 == 1) {
            for (Object userObj : users) {
                if (userObj instanceof User) {
                    User user2 = (User) userObj;
                    if (user2.username.equals(request.username)) {
                        if (request.getRequestType().equals(RequestType.MOVIE_ISSUE) ||
                                request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                            user2.updateExperience(new CreatedRequest());
                        }
                        if (request.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                            request.notifyObservers("Your request regarding the movie " + request.movieName +
                                    " has been accepted!", 2);
                        }
                        if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                            request.notifyObservers("Your request regarding the actor " + request.actorName +
                                    " has been accepted!", 2);
                        }
                        if (request.getRequestType().equals(RequestType.OTHERS)) {
                            request.notifyObservers("Your request regarding " + request.description +
                                    " has been accepted!", 2);
                        }
                        break;
                    }
                }
            }
        } else {
            for (Object userObj : users) {
                if (userObj instanceof User) {
                    User user2 = (User) userObj;
                    if (user2.username.equals(request.username)) {
                        if (request.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                            request.notifyObservers("Your request regarding the movie " + request.movieName +
                                    " has been rejected!", 2);
                        }
                        if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                            request.notifyObservers("Your request regarding the actor " + request.actorName +
                                    " has been rejected!", 2);
                        }
                        if (request.getRequestType().equals(RequestType.OTHERS)) {
                            request.notifyObservers("Your request regarding " + request.description +
                                    " has been rejected!", 2);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void updateProductionsActors (User user, Scanner scanner) throws InvalidCommandException {
        System.out.println("Select one of the following options:");
        System.out.println("\t1. Update production details");
        System.out.println("\t2. Update actor details");
        int option2 = 0;
        try {
            option2 = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 < 1 || option2 > 2) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 == 1) {
            System.out.println("Enter the name of the production:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String productionName = scanner.nextLine();
            System.out.println();
            Production production = findProduction(productionName);
            if (production == null) {
                System.out.println("No production with this name!");
                return;
            }
            if (!(production.resposability.equals(user.username) ||
                    (user instanceof Admin && production.resposability.equals("Admin")))) {
                System.out.println("You are not responsible for this production!");
                return;
            }
            System.out.println("Select one of the following options:");
            System.out.println("\t1. Update description");
            System.out.println("\t2. Update directors");
            System.out.println("\t3. Update actors");
            System.out.println("\t4. Update genres");
            System.out.println("\t5. Update release year");
            if (production instanceof Movie) {
                System.out.println("\t6. Update duration");
            } else {
                System.out.println("\t6. Update number of seasons");
                System.out.println("\t7. Update seasons");
            }
            int option3 = 0;
            try {
                option3 = scanner.nextInt();
            } catch (InputMismatchException e) {
                throw new InvalidCommandException("Invalid command!");
            }
            if (option3 < 1 || option3 > 7) {
                throw new InvalidCommandException("Invalid command!");
            }
            if (option3 == 1) {
                System.out.println("Enter the new description:");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String description = scanner.nextLine();
                System.out.println();
                production.description = description;
            }
            if (option3 == 2) {
                System.out.println("Enter the new directors:");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String directorsString = scanner.nextLine();
                System.out.println();
                List<String> directors = Arrays.asList(directorsString.split(","));
                production.directorNames = directors;
            }
            if (option3 == 3) {
                System.out.println("Enter the new actors:");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String actorsString = scanner.nextLine();
                System.out.println();
                List<String> actors = Arrays.asList(actorsString.split(","));
                production.actorNames = actors;
            }
            if (option3 == 4) {
                System.out.println("Enter the new genres:");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String genresString = scanner.nextLine();
                System.out.println();
                List<Genre> genres = new ArrayList<Genre>();
                for (String genre : genresString.split(",")) {
                    try {
                        genres.add(Genre.valueOf(genre));
                    } catch (IllegalArgumentException e) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                }
                production.genres = genres;
            }
            if (option3 == 5) {
                System.out.println("Enter the new release year:");
                int releaseYear = 0;
                try {
                    releaseYear = scanner.nextInt();
                } catch (InputMismatchException e) {
                    throw new InvalidCommandException("Invalid command!");
                }
                System.out.println();
                if (production instanceof Movie) {
                    ((Movie) production).yearProduced = releaseYear;
                } else {
                    ((Series) production).yearOfRelease = releaseYear;
                }
            }
            if (option3 == 6) {
                if (production instanceof Movie) {
                    System.out.println("Enter the new duration:");
                    // read everthing including spaces until the end of the line
                    scanner.nextLine();
                    String duration = scanner.nextLine();
                    System.out.println();
                    ((Movie) production).duration = duration;
                } else {
                    System.out.println("Enter the new number of seasons:");
                    int nrSeasons = 0;
                    try {
                        nrSeasons = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                    System.out.println();
                    ((Series) production).numberOfSeasons = nrSeasons;
                }
            }
            if (production instanceof Series && option3 == 7) {
                System.out.println("Enter the season you want to update:");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String season = scanner.nextLine();
                System.out.println();
                System.out.println("Enter the new episodes:");
                System.out.println("Please enter the episodes in the following format:");
                System.out.println("episodeName1-duration1,episodeName2-duration2,...");
                // read everthing including spaces until the end of the line
                String episodesString = scanner.nextLine();
                System.out.println();
                List<Episode> episodes = new ArrayList<Episode>();
                for (String episode : episodesString.split(",")) {
                    String[] episodeInfo = episode.split("-");
                    episodes.add(new Episode(episodeInfo[0], episodeInfo[1]));
                }
                ((Series) production).updateEpisodes(season, episodes);
            }
            ((Staff) user).updateProduction(production);
        }
        if (option2 == 2) {
            System.out.println("Enter the name of the actor:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String actorName = scanner.nextLine();
            System.out.println();
            Actor actor = findActor(actorName);
            if (actor == null) {
                System.out.println("No actor with this name!");
                return;
            }
            if (!(actor.responsability.equals(user.username) ||
                    (user instanceof Admin && actor.responsability.equals("Admin")))) {
                System.out.println("You are not responsible for this actor!");
                return;
            }
            System.out.println("Select one of the following options:");
            System.out.println("\t1. Update movies");
            System.out.println("\t2. Update biography");
            int option3 = 0;
            try {
                option3 = scanner.nextInt();
            } catch (InputMismatchException e) {
                throw new InvalidCommandException("Invalid command!");
            }
            if (option3 < 1 || option3 > 2) {
                throw new InvalidCommandException("Invalid command!");
            }
            if (option3 == 1) {
                System.out.println("Enter the correct movies:");
                System.out.println("Please enter the movies in the following format:");
                System.out.println("movieName1-type1,movieName2-type2,...");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String moviesString = scanner.nextLine();
                System.out.println();
                List<Pair> movies = new ArrayList<>();
                for (String movie : moviesString.split(",")) {
                    String[] movieInfo = movie.split("-");
                    movies.add(new Pair(movieInfo[0], movieInfo[1]));
                }
                actor.movies = movies;
            }
            if (option3 == 2) {
                System.out.println("Enter the new biography:");
                // read everthing including spaces until the end of the line
                scanner.nextLine();
                String biography = scanner.nextLine();
                System.out.println();
                actor.biography = biography;
            }
            ((Staff) user).updateActor(actor);
        }
    }
    public void addDeleteRequests(User user, Scanner scanner) throws InvalidCommandException {
        System.out.println("Select one of the following options:");
        System.out.println("\t1.Create a request");
        System.out.println("\t2.Delete a request");
        int option2 = 0;
        try {
            option2 = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 < 1 || option2 > 2) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (option2 == 1) {
            System.out.println("Select one of the following options:");
            System.out.println("\t1. Delete account");
            System.out.println("\t2. Actor issue");
            System.out.println("\t3. Movie issue");
            System.out.println("\t4. Other");
            int option3 = 0;
            try {
                option3 = scanner.nextInt();
            } catch (InputMismatchException e) {
                throw new InvalidCommandException("Invalid command!");
            }
            if (option3 < 1 || option3 > 4) {
                throw new InvalidCommandException("Invalid command!");
            }
            System.out.println("Enter the description of the request:");
            // read everthing including spaces until the end of the line
            scanner.nextLine();
            String description = scanner.nextLine();
            System.out.println();
            if (option3 == 1) {
                Request request = new Request(RequestType.DELETE_ACCOUNT, user.username, description,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                        null, null);
                Admin.RequestsHolder.addRequest(request);
                for (User u : users) {
                    if (u instanceof Admin) {
                        request.addObserver(u);
                    }
                }
                requests.add(request);
                request.notifyObservers("The user " + user.username +
                        " wants to delete his account!", 1);
            }
            if (option3 == 4) {
                Request request = new Request(RequestType.OTHERS, user.username, description,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                        null, null);
                request.addObserver(user);
                Admin.RequestsHolder.addRequest(request);
                for (User u : users) {
                    if (u instanceof Admin) {
                        request.addObserver(u);
                    }
                }
                requests.add(request);
                request.notifyObservers("The user " + user.username + " has a request!", 1);
            }
            if (option3 == 2) {
                System.out.println("Enter the name of the actor:");
                // read everthing including spaces until the end of the line
                String actorName = scanner.nextLine();
                System.out.println();
                for (Actor a : actors) {
                    if (a.name.equals(actorName)) {
                        Request request = new Request(RequestType.ACTOR_ISSUE, user.username, description,
                                a.responsability,
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                                null, actorName);

                        request.addObserver(user);
                        boolean ok = false;
                        for (User u : users) {
                            if (u.username.equals(a.responsability)) {
                                ((Staff) u).requests.add(request);
                                request.addObserver(u);
                                ok = true;
                                break;
                            }
                        }
                        if (!ok) {
                            Admin.RequestsHolder.addRequest(request);
                            for (User u : users) {
                                if (u instanceof Admin) {
                                    request.addObserver(u);
                                }
                            }
                        }
                        requests.add(request);
                        request.notifyObservers("The user " + user.username +
                                " has a request regarding actor " + actorName + "!", 1);
                        break;
                    }
                }
            }
            if (option3 == 3) {
                System.out.println("Enter the name of the movie:");
                // read everthing including spaces until the end of the line
                String movieName = scanner.nextLine();
                System.out.println();
                for (Production p : productions) {
                    if (p.productionName.equals(movieName)) {
                        Request request = new Request(RequestType.MOVIE_ISSUE, user.username, description,
                                p.resposability,
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                                movieName, null);

                        request.addObserver(user);
                        boolean ok = false;
                        for (User u : users) {
                            if (u.username.equals(p.resposability)) {
                                ((Staff) u).requests.add(request);
                                request.addObserver(u);
                                ok = true;
                                break;
                            }
                        }
                        if (!ok) {
                            Admin.RequestsHolder.addRequest(request);
                            for (User u : users) {
                                if (u instanceof Admin) {
                                    request.addObserver(u);
                                }
                            }
                        }
                        requests.add(request);
                        request.notifyObservers("The user " + user.username +
                                " has a request regarding movie " + movieName + "!", 1);
                        break;
                    }
                }
            }
        }
        if (option2 == 2) {
            Map <Integer, Request> map = new LinkedHashMap<>();

            System.out.println("This are your requests, please enter the id of " +
                    "the request you want to delete:");
            int i = 1;
            for (Request request : requests) {
                if (request.username.equals(user.username)) {
                    map.put(i, request);
                    System.out.println(i + ". " + request);
                    i++;
                }
            }
            if (map.isEmpty()) {
                System.out.println("You don't have any requests!");
                return;
            }
            int id = 0;
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                throw new InvalidCommandException("Invalid command!");
            }
            if (id < 1 || id > map.size()) {
                throw new InvalidCommandException("Invalid command!");
            }
            Request request = map.get(id);
            requests.remove(request);
            if (request.responsableUsername.equals("Admin")) {
                Admin.RequestsHolder.deleteRequest(request);
            } else {
                for (User u : users) {
                    if (u.username.equals(request.responsableUsername)) {
                        ((Staff) u).requests.remove(request);
                        break;
                    }
                }
            }
        }
    }
    public void parseActors() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\mihae\\IdeaProjects\\IMDB\\src\\actors.json"));
            JSONArray actorsArray = (JSONArray) obj;

            for (Object actor : actorsArray) {
                JSONObject actorObject = (JSONObject) actor;
                String name = (String) actorObject.get("name");
                String biography = (String) actorObject.get("biography");
                JSONArray movies = (JSONArray) actorObject.get("performances");
                List<Pair> moviesList = new ArrayList<Pair>();
                for (Object movie : movies) {
                    JSONObject movieObject = (JSONObject) movie;
                    String movieName = (String) movieObject.get("title");
                    String movieType = (String) movieObject.get("type");
                    moviesList.add(new Pair(movieName, movieType));
                }
                actors.add(new Actor(name, moviesList, biography, "Admin"));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (ParseException e) {
            System.out.println("ParseException!");
        }
    }
    public void parseRequests() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\mihae\\IdeaProjects\\IMDB\\src\\requests.json"));
            JSONArray requestsArray = (JSONArray) obj;
            for (Object req : requestsArray) {
                JSONObject reqObject = (JSONObject) req;
                String type = (String) reqObject.get("type");
                String createdDate = (String) reqObject.get("createdDate");
                String username = (String) reqObject.get("username");
                String to = (String) reqObject.get("to");
                String description = (String) reqObject.get("description");
                if (type.equals("DELETE_ACCOUNT")) {
                    if (to.equals("ADMIN")) {
                        Admin.RequestsHolder.addRequest(new Request(RequestType.DELETE_ACCOUNT, username, description,
                                createdDate, null, null));
                        requests.add(new Request(RequestType.DELETE_ACCOUNT, username, description, createdDate,
                                null, null));
                    }
                } else if (type.equals("ACTOR_ISSUE")) {
                    if (to.equals("ADMIN")) {
                        String actorName = (String) reqObject.get("actorName");
                        Admin.RequestsHolder.addRequest(new Request(RequestType.ACTOR_ISSUE, username, description,
                                createdDate, null, actorName));
                        requests.add(new Request(RequestType.ACTOR_ISSUE, username, description, createdDate,
                                null, actorName));
                    } else {
                        String actorName = (String) reqObject.get("actorName");
                        requests.add(new Request(RequestType.ACTOR_ISSUE, username, description, to, createdDate,
                                null, actorName));
                        for (Object userObj : users) {
                            if (userObj instanceof Staff<?>) {
                                Staff<?> user = (Staff<?>) userObj;
                                if (user.username.equals(to)) {
                                    user.requests.add(new Request(RequestType.ACTOR_ISSUE, username, description,
                                            to, createdDate, null, actorName));
                                    break;
                                }
                            }
                        }
                    }
                } else if (type.equals("MOVIE_ISSUE")) {
                    if (to.equals("ADMIN")) {
                        String movieName = (String) reqObject.get("movieTitle");
                        Admin.RequestsHolder.addRequest(new Request(RequestType.MOVIE_ISSUE, username, description,
                                createdDate, movieName, null));
                        requests.add(new Request(RequestType.MOVIE_ISSUE, username, description, createdDate,
                                movieName, null));
                    } else {
                        String movieName = (String) reqObject.get("movieTitle");
                        requests.add(new Request(RequestType.MOVIE_ISSUE, username, description, to, createdDate,
                                movieName, null));
                        for (Object userObj : users) {
                            if (userObj instanceof Staff<?>) {
                                Staff<?> user = (Staff<?>) userObj;
                                if (user.username.equals(to)) {
                                    user.requests.add(new Request(RequestType.MOVIE_ISSUE, username, description,
                                            to, createdDate, movieName, null));
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if (to.equals("ADMIN")) {
                        Admin.RequestsHolder.addRequest(new Request(RequestType.OTHERS, username, description,
                                createdDate, null, null));
                        requests.add(new Request(RequestType.OTHERS, username, description, createdDate, null,
                                null));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void parseProductions() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\mihae\\IdeaProjects\\IMDB\\src\\production.json"));
            JSONArray productionsArray = (JSONArray) obj;
            for (Object production : productionsArray) {
                JSONObject productionObject = (JSONObject) production;
                String productionName = (String) productionObject.get("title");
                String type = (String) productionObject.get("type");
                JSONArray directors = (JSONArray) productionObject.get("directors");
                List<String> directorsList = new ArrayList<String>();
                for (Object director : directors) {
                    directorsList.add((String) director);
                }
                JSONArray actors = (JSONArray) productionObject.get("actors");
                List<String> actorsList = new ArrayList<String>();
                for (Object actor : actors) {
                    actorsList.add((String) actor);
                }
                JSONArray genres = (JSONArray) productionObject.get("genres");
                List<Genre> genresList = new ArrayList<Genre>();
                for (Object genre : genres) {
                    genresList.add(Genre.valueOf((String) genre));
                }
                JSONArray ratings = (JSONArray) productionObject.get("ratings");
                List<Rating> ratingsList = new ArrayList<Rating>();
                for (Object rating : ratings) {
                    JSONObject ratingObject = (JSONObject) rating;
                    String username = (String) ratingObject.get("username");
                    Long value = (Long) ratingObject.get("rating");
                    String comment = (String) ratingObject.get("comment");
                    int intValue = 0;
                    if (value != null) {
                        intValue = value.intValue();
                    }
                    ratingsList.add(new Rating(username, intValue, comment));
                    for (User user : users) {
                        if (user.username.equals(username)) {
                            user.ratedProductions.add(productionName);
                            user.ratedAndDeletedProductions.add(productionName);
                            break;
                        }
                    }
                }
                String description = (String) productionObject.get("plot");
                Double averageRate = (Double) productionObject.get("averageRating");
                if (type.equals("Movie")) {
                    String duration = (String) productionObject.get("duration");
                    Long releaseYear = (Long) productionObject.get("releaseYear");
                    int intReleaseYear = 0;
                    if (releaseYear != null) {
                        intReleaseYear = releaseYear.intValue();
                    }
                    productions.add(
                      new Movie(
                              productionName,
                              directorsList,
                              actorsList,
                              genresList,
                              ratingsList,
                              description,
                              duration,
                              intReleaseYear,
                              averageRate,
                              "Admin"
                      )
                    );
                } else if (type.equals("Series")) {
                    Long releaseYear = (Long) productionObject.get("releaseYear");
                    int intReleaseYear = 0;
                    if (releaseYear != null) {
                        intReleaseYear = releaseYear.intValue();
                    }
                    Long nrSeasons = (Long) productionObject.get("numSeasons");
                    int intNrSeasons = 0;
                    if (nrSeasons != null) {
                        intNrSeasons = nrSeasons.intValue();
                    }
                    Object seasonsObject = productionObject.get("seasons");
                    JSONObject seasons = (JSONObject) seasonsObject;
                    Map<String, List<Episode>> seasonsMap = new HashMap<String, List<Episode>>();
                    for (int i = 1; i <= intNrSeasons; i++) {
                        JSONArray episodes = (JSONArray) seasons.get("Season " + i);
                        List<Episode> episodesList = new ArrayList<Episode>();
                        for (Object episode : episodes) {
                            JSONObject episodeObject = (JSONObject) episode;
                            String episodeName = (String) episodeObject.get("episodeName");
                            String duration = (String) episodeObject.get("duration");
                            episodesList.add(new Episode(episodeName, duration));
                        }
                        seasonsMap.put("Season " + i, episodesList);
                    }
                    productions.add(
                      new Series(
                              productionName,
                              directorsList,
                              actorsList,
                              genresList,
                              ratingsList,
                              description,
                              intNrSeasons,
                              intReleaseYear,
                              seasonsMap,
                              averageRate,
                              "Admin"
                      )
                    );
                }
                for (String actor : actorsList) {
                    boolean ok = false;
                    for (Actor a : this.actors) {
                        if (a.name.equals(actor)) {
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        Actor newActor = new Actor(actor, new ArrayList<Pair>(), null, "Admin");
                        newActor.movies.add(new Pair(productionName, type));
                        this.actors.add(newActor);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (ParseException e) {
            System.out.println("ParseException!");
        }
    }
    public void parseAccounts() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\mihae\\IdeaProjects\\IMDB\\src\\accounts.json"));
            JSONArray accountsArray = (JSONArray) obj;
            for (Object account : accountsArray) {
                JSONObject accountObject = (JSONObject) account;
                String userType = (String) accountObject.get("userType");
                String username = (String) accountObject.get("username");
                String experience = (String) accountObject.get("experience");
                // transform experience to int
                int exp = 0;
                if (experience != null) {
                    exp = Integer.parseInt(experience);
                }
                Object infoObject = accountObject.get("information");
                JSONObject info = (JSONObject) infoObject;
                Object credentialsObject = info.get("credentials");
                JSONObject credentials = (JSONObject) credentialsObject;
                String password = (String) credentials.get("password");
                String email = (String) credentials.get("email");
                String name = (String) info.get("name");
                String country = (String) info.get("country");
                Long longAge = (Long) info.get("age");
                int age = longAge.intValue();
                char sex = ((String) info.get("gender")).charAt(0);
                String birthDate = (String) info.get("birthDate");
                JSONArray productionContributions = (JSONArray)accountObject.get("productionsContribution");
                JSONArray actorContributions = (JSONArray)accountObject.get("actorsContribution");
                JSONArray favoriteProductions = (JSONArray)accountObject.get("favoriteProductions");
                JSONArray favoriteActors = (JSONArray)accountObject.get("favoriteActors");
                JSONArray notifications = (JSONArray)accountObject.get("notifications");
                User.InformationBuilder informationBuilder = new User.InformationBuilder();
                User auxUser = UserFactory.factory(
                        userType,
                        null,
                        username,
                        exp
                );
                if (email == null || password == null || name == null) {
                    throw new InformationIncompleteException("Information incomplete!");
                }
                auxUser.information = informationBuilder
                        .setCredentials(new Credentials(email, password))
                        .setName(name)
                        .setCountry(country)
                        .setAge(age)
                        .setDateOfBirth(birthDate)
                        .setSex(sex)
                        .build(auxUser);
                if (favoriteProductions != null) {
                    for (Object production : favoriteProductions) {
                        String productionString = (String) production;
                        for (Production p : productions) {
                            if (p.productionName.equals(productionString)) {
                                if (p instanceof Movie) {
                                    auxUser.addMovieToPreferences((Movie) p);
                                } else {
                                    auxUser.addSeriesToPreferences((Series) p);
                                }
                                break;
                            }
                        }
                    }
                }
                if (favoriteActors != null) {
                    for (Object actor : favoriteActors) {
                        String actorString = (String) actor;
                        for (Actor a : actors) {
                            if (a.name.equals(actorString)) {
                                auxUser.addActorToPreferences(a);
                                break;
                            }

                        }
                    }
                }
                if (notifications != null) {
                    for (Object notification : notifications) {
                        String notificationString = (String) notification;
                        auxUser.notifications.add(notificationString);
                    }
                }
                if (auxUser.accountType.equals(AccountType.ADMIN) ||
                        auxUser.accountType.equals(AccountType.CONTRIBUTOR)) {
                    if (productionContributions != null) {
                        for (Object production : productionContributions) {
                            String productionString = (String) production;
                            for (Production p : productions) {
                                if (p.productionName.equals(productionString)) {
                                    p.resposability = auxUser.username;
                                    ((Staff) auxUser).addProductionSystem(p);
                                    break;
                                }
                            }
                        }
                    }
                    if (actorContributions != null) {
                        for (Object actor : actorContributions) {
                            String actorString = (String) actor;
                            for (Actor a : actors) {
                                if (a.name.equals(actorString)) {
                                    a.responsability = auxUser.username;
                                    ((Staff) auxUser).addActorSystem(a);
                                    break;
                                }
                            }
                        }
                    }
                }
                users.add((User) auxUser);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (ParseException e) {
            System.out.println("ParseException!");
        } catch (InformationIncompleteException e) {
            System.out.println("Information incomplete!");
        }
    }
    public void run() throws InformationIncompleteException, InvalidCommandException {
        IMDB imdb = IMDB.getInstance();
        imdb.parseActors();
        imdb.parseProductions();
        imdb.parseAccounts();
        imdb.parseRequests();

        int usage = 0;
        System.out.println("Welcome to IMDB!");
        System.out.println("Select how you want to use the application:");
        System.out.println();

        System.out.println("\t1. Terminal");
        System.out.println("\t2. GUI");
        Scanner scanner = new Scanner(System.in);
        // throw exception if the user enters something else than 1 or 2
        try {
            usage = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Invalid command!");
        }
        if (usage != 1 && usage != 2) {
            throw new InvalidCommandException("Invalid command!");
        }
        while (usage == 1) {
            String email = null;
            String password = null;
            System.out.println("Welcome back! Enter your credentials:");
            User user = null;
            while (user == null) {
                System.out.println();
                System.out.print("\tEmail: ");
                email = scanner.next();
                System.out.print("\tPassword: ");
                password = scanner.next();
                System.out.println();
                for (User u : users) {
                    if (u.information.getCredentials().getEmail().equals(email) &&
                            u.information.getCredentials().getPassword().equals(password)) {
                        user = u;
                        user.login();
                        break;
                    }
                }
                if (user == null) {
                    System.out.println("Wrong credentials! Try again!");
                }
            }
            System.out.println("Welcome back user " + user.username + "!");


            while (user.isLogged()) {
                System.out.println("Username: " + user.username);
                if (user.accountType.equals(AccountType.ADMIN)) {
                    System.out.println("Account type: Admin");
                    System.out.println("Experience: -");
                    System.out.println();

                    System.out.println("Choose an option:");
                    System.out.println("\t1. View productions details");
                    System.out.println("\t2. View actors details");
                    System.out.println("\t3. View notifications");
                    System.out.println("\t4. Search actor/movie/series");
                    System.out.println("\t5. Add/Delete actor/movie/series to/from favorites");
                    System.out.println("\t6. Add/Delete actor/movie/series to/from system");
                    System.out.println("\t7. View/Solve requests");
                    System.out.println("\t8. Update Production/Actor details");
                    System.out.println("\t9. Add/Delete user from system");
                    System.out.println("\t10. Logout");
                } else if (user.accountType.equals(AccountType.CONTRIBUTOR)) {
                    System.out.println("Account type: Contributor");
                    System.out.println("Experience: " + user.experience);
                    System.out.println();

                    System.out.println("Choose an option:");
                    System.out.println("\t1. View productions details");
                    System.out.println("\t2. View actors details");
                    System.out.println("\t3. View notifications");
                    System.out.println("\t4. Search actor/movie/series");
                    System.out.println("\t5. Add/Delete actor/production to/from favorites");
                    System.out.println("\t6. Create/Delete a request");
                    System.out.println("\t7. Add/Delete actor/production to/from system");
                    System.out.println("\t8. View/Solve requests");
                    System.out.println("\t9. Update Production/Actor details");
                    System.out.println("\t10. Logout");
                } else {
                    System.out.println("Account type: User");
                    System.out.println("Experience: " + user.experience);
                    System.out.println();

                    System.out.println("Choose an option:");
                    System.out.println("\t1. View productions details");
                    System.out.println("\t2. View actors details");
                    System.out.println("\t3. View notifications");
                    System.out.println("\t4. Search actor/movie/series");
                    System.out.println("\t5. Add/Delete actor/production to/from favorites");
                    System.out.println("\t6. Create/Delete a request");
                    System.out.println("\t7. Add/Delete rating to/from production");
                    System.out.println("\t8. Logout");
                }
                System.out.println();
                int option = 0;
                try {
                    option = scanner.nextInt();
                } catch (InputMismatchException e) {
                    throw new InvalidCommandException("Invalid command!");
                }
                if (option < 1 || option > 10) {
                    throw new InvalidCommandException("Invalid command!");
                }
                if (option == 1) {
                    for (Production p : productions) {
                        p.displayInfo();
                    }
                }
                if (option == 2) {
                    for (Actor a : actors) {
                        a.displayInfo();
                    }
                }
                if (option == 3) {
                    System.out.println();
                    System.out.println("Notifications:");
                    for (Object notificationObj : user.notifications) {
                        String notification = (String) notificationObj;
                        System.out.println(notification);
                    }
                    System.out.println();
                }
                if (option == 4) {
                    System.out.println("Enter the name of the actor/movie/series you want to search:");
                    // read everthing including spaces until the end of the line
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    System.out.println();
                    Actor actor = findActor(name);
                    if (actor != null) {
                        actor.displayInfo();
                        continue;
                    } else {
                        Production production = findProduction(name);
                        if (production != null) {
                            production.displayInfo();
                            continue;
                        }
                    }
                    System.out.println("No actor/movie/series with this name!");
                }
                if (option == 5) {
                    while (true) {
                        System.out.println("Select one of the following options:");
                        System.out.println("\t1. Add actor/production to favorites");
                        System.out.println("\t2. Delete actor/production from favorites");
                        System.out.println("\t3. View your favorites");
                        System.out.println("\t4. Back");
                        int option2 = 0;
                        try {
                            option2 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 < 1 || option2 > 4) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 == 3) {
                            user.displayPreferences();
                        }
                        if (option2 == 4) {
                            break;
                        }
                        if (option2 == 1) {
                            System.out.println("Enter the name of the actor/production you want to add to favorites:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String name = scanner.nextLine();
                            System.out.println();
                            boolean ok = false;
                            for (Actor a : actors) {
                                if (a.name.equals(name)) {
                                    user.addActorToPreferences(a);
                                    ok = true;
                                    break;
                                }
                            }
                            if (!ok) {
                                for (Production p : productions) {
                                    if (p.productionName.equals(name)) {
                                        if (p instanceof Movie) {
                                            user.addMovieToPreferences((Movie) p);
                                        } else {
                                            user.addSeriesToPreferences((Series) p);
                                        }
                                        ok = true;
                                        break;
                                    }
                                }
                            }
                            if (!ok) {
                                System.out.println("No actor/production with this name!");
                            }
                        }
                        if (option2 == 2) {
                            System.out.println("Enter the name of the actor/production you want to delete from favorites:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String name = scanner.nextLine();
                            System.out.println();
                            boolean ok = false;
                            for (Actor a : actors) {
                                if (a.name.equals(name)) {
                                    user.deleteActorFromPreferences(a);
                                    ok = true;
                                    break;
                                }
                            }
                            if (!ok) {
                                for (Production p : productions) {
                                    if (p.productionName.equals(name)) {
                                        if (p instanceof Movie) {
                                            user.deleteMovieFromPreferences((Movie) p);
                                        } else {
                                            user.deleteSeriesFromPreferences((Series) p);
                                        }
                                        ok = true;
                                        break;
                                    }
                                }
                            }
                            if (!ok) {
                                System.out.println("No actor/production with this name!");
                            }
                        }
                    }
                }
                if (user.accountType.equals(AccountType.REGULAR)) {
                    if (option == 6) {
                        addDeleteRequests(user, scanner);
                    }
                    if (option == 7) {
                        System.out.println("Select one of the following options:");
                        System.out.println("\t1. Add rating to production");
                        System.out.println("\t2. Delete rating from production");
                        int option2 = 0;
                        try {
                            option2 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 < 1 || option2 > 2) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 == 1) {
                            System.out.println("Enter the name of the production:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String productionName = scanner.nextLine();
                            System.out.println();
                            System.out.println("Enter the rating:");
                            int rating = 0;
                            try {
                                rating = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (rating < 1 || rating > 10) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            System.out.println();
                            System.out.println("Enter the comment:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String comment = scanner.nextLine();
                            System.out.println();
                            boolean productionExists = false;
                            for (Object prodNameObj : user.ratedAndDeletedProductions) {
                                String prodName = (String) prodNameObj;
                                if (prodName.equals(productionName)) {
                                    System.out.println("You already rated this production!");
                                    productionExists = true;
                                    break;
                                }
                            }
                            if (productionExists) {
                                continue;
                            }
                            for (Production p : productions) {
                                if (p.productionName.equals(productionName)) {
                                    p.ratings.add(new Rating(user.username, rating, comment));
                                    Double averageRate = 0.0;
                                    for (Rating r : p.ratings) {
                                        averageRate += r.rating;
                                    }
                                    averageRate /= p.ratings.size();
                                    p.rate = averageRate;
                                    p.notifyObservers("The production you added in the system " +
                                            p.productionName + " received a new rating from the user " +
                                            user.username + " -> " + rating, 1);
                                    p.notifyObservers("The production you also rated " +
                                            p.productionName + " received a new rating from the user " +
                                            user.username + " -> " + rating, 1);
                                    p.addObserver(user);
                                    break;
                                }
                            }
                            boolean ok = false;
                            for (Object prodNameObj : user.ratedProductions) {
                                String prodName = (String) prodNameObj;
                                if (prodName.equals(productionName)) {
                                    ok = true;
                                    break;
                                }
                            }
                            if (!ok) {
                                user.updateExperience(new RateProduction());
                                user.ratedProductions.add(productionName);
                                user.ratedAndDeletedProductions.add(productionName);
                            }
                        }
                        if (option2 == 2) {
                            System.out.println("Enter the name of the production:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String productionName = scanner.nextLine();
                            boolean productionExists = false;
                            System.out.println();
                            for (Production p : productions) {
                                if (p.productionName.equals(productionName)) {
                                    productionExists = true;
                                    boolean ratingExists = false;
                                    for (Rating r : p.ratings) {
                                        if (r.username.equals(user.username)) {
                                            ratingExists = true;
                                            p.ratings.remove(r);
                                            p.removeObserver(user);
                                            user.ratedAndDeletedProductions.remove(productionName);
                                            Double averageRate = 0.0;
                                            for (Rating rating : p.ratings) {
                                                averageRate += rating.rating;
                                            }
                                            averageRate /= p.ratings.size();
                                            p.rate = averageRate;
                                            break;
                                        }
                                    }
                                    if (!ratingExists) {
                                        System.out.println("You didn't rate this production!");
                                    }
                                    break;
                                }
                            }
                            if (!productionExists) {
                                System.out.println("No production with this name!");
                            }
                        }
                    }
                    if (option > 8) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                } else if (user.accountType.equals(AccountType.CONTRIBUTOR)) {
                    if (option == 6) {
                        addDeleteRequests(user, scanner);
                    }
                    if (option == 7) {
                        addDeleteActorsProductionToFromSystem(user, scanner);
                    }
                    if (option == 8) {
                        viewResolveRequests(user, scanner);
                    }
                    if (option == 9) {
                        updateProductionsActors(user, scanner);
                    }
                    if (option > 10) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                } else {
                    if (option == 6) {
                        addDeleteActorsProductionToFromSystem(user, scanner);
                    }
                    if (option == 7) {
                        System.out.println("Select one of the following options:");
                        System.out.println("\t1. View/Solve your personal requests");
                        System.out.println("\t2. View/Solve admin requests");
                        int option2 = 0;
                        try {
                            option2 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 < 1 || option2 > 2) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 == 1) {
                            viewResolveRequests(user, scanner);
                        } else {
                            Map<Integer, Request> map = new LinkedHashMap<>();

                            System.out.println("This are the admin requests:");
                            int i = 1;
                            for (Request request : Admin.RequestsHolder.requests) {
                                map.put(i, request);
                                System.out.println(i + ". " + request);
                                i++;
                            }
                            if (map.isEmpty()) {
                                System.out.println("There are no requests!");
                                continue;
                            }
                            System.out.println("Select one of the following options:");
                            System.out.println("\t1. Mark request as solved/rejected");
                            System.out.println("\t2. Back");
                            int option3 = 0;
                            try {
                                option3 = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (option3 < 1 || option3 > 2) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (option3 == 2) {
                                continue;
                            }
                            System.out.println("Enter the id of the request you want to solve:");
                            int id = 0;
                            try {
                                id = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (id < 1 || id > map.size()) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            Request request = map.get(id);
                            System.out.println("Select one of the following options:");
                            System.out.println("\t1. Mark request as solved");
                            System.out.println("\t2. Mark request as rejected");
                            int option4 = 0;
                            try {
                                option4 = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (option4 < 1 || option4 > 2) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (option4 == 1) {
                                Admin.RequestsHolder.deleteRequest(request);
                                requests.remove(request);
                                for (User u : users) {
                                    if (u.username.equals(request.username)) {
                                        if (request.getRequestType().equals(RequestType.MOVIE_ISSUE) ||
                                                request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                                            u.updateExperience(new CreatedRequest());
                                        }
                                        if (request.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                                            request.notifyObservers("Your request regarding the movie "
                                                    + request.movieName + " has been accepted!", 2);
                                        }
                                        if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                                            request.notifyObservers("Your request regarding the actor " +
                                                    request.actorName + " has been accepted!", 2);
                                        }
                                        if (request.getRequestType().equals(RequestType.OTHERS)) {
                                            request.notifyObservers("Your request regarding " +
                                                    request.description + " has been accepted!", 2);
                                        }
                                        break;
                                    }
                                }
                            } else {
                                Admin.RequestsHolder.deleteRequest(request);
                                requests.remove(request);
                                for (User u : users) {
                                    if (u.username.equals(request.username)) {
                                        if (request.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                                            request.notifyObservers("Your request regarding the movie "
                                                    + request.movieName + " has been rejected!", 2);
                                        }
                                        if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                                            request.notifyObservers("Your request regarding the actor "
                                                    + request.actorName + " has been rejected!", 2);
                                        }
                                        if (request.getRequestType().equals(RequestType.OTHERS)) {
                                            request.notifyObservers("Your request regarding "
                                                    + request.description + " has been rejected!", 2);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (option == 8) {
                        updateProductionsActors(user, scanner);
                    }
                    if (option == 9) {
                        System.out.println("Select one of the following options:");
                        System.out.println("\t1. Add user from system");
                        System.out.println("\t2. Delete user from system");
                        int option2 = 0;
                        try {
                            option2 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 < 1 || option2 > 2) {
                            throw new InvalidCommandException("Invalid command!");
                        }
                        if (option2 == 1) {
                            User.InformationBuilder informationBuilder = new User.InformationBuilder();
                            System.out.println("Enter the name of the user:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String name = scanner.nextLine();
                            System.out.println();
                            informationBuilder.setName(name);
                            System.out.println("Enter the country of the user:");
                            String country = scanner.nextLine();
                            System.out.println();
                            informationBuilder.setCountry(country);
                            System.out.println("Enter the age of the user:");
                            int age = 0;
                            try {
                                age = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (age < 0) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            System.out.println();
                            informationBuilder.setAge(age);
                            System.out.println("Enter the date of birth of the user:");
                            // read everthing including spaces until the end of the line
                            scanner.nextLine();
                            String dateOfBirth = scanner.nextLine();
                            System.out.println();
                            informationBuilder.setDateOfBirth(dateOfBirth);
                            System.out.println("Enter the email of the user:");
                            String emailOfNewUser = scanner.nextLine();
                            System.out.println();
                            String passwordOfNewUser = createRandomStrongPassword();
                            informationBuilder.setCredentials(new Credentials(emailOfNewUser, passwordOfNewUser));

                            String usernameOfNewUser = "";
                            List<String> names = new ArrayList<>();
                            names = Arrays.asList(name.split(" "));
                            for (String n : names) {
                                usernameOfNewUser += n;
                            }
                            // add a number to the username if it already exists
                            while (true) {
                                boolean ok = true;
                                int random = (int) (Math.random() * 900 + 100);
                                for (User u : users) {
                                    if (u.username.equals(usernameOfNewUser + "_" + random)) {
                                        ok = false;
                                        break;
                                    }
                                }
                                if (ok) {
                                    usernameOfNewUser += "_" + random;
                                    break;
                                }
                            }
                            System.out.println("Enter the type of the new user:");
                            String type = scanner.nextLine();
                            System.out.println();
                            if (!type.equals("Regular") && !type.equals("Contributor") && !type.equals("Admin")) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            User newUser = UserFactory.factory(type, null, usernameOfNewUser, 0);
                            if (name == null || emailOfNewUser == null || passwordOfNewUser == null) {
                                throw new InformationIncompleteException("Information incomplete!");
                            }
                            newUser.information = informationBuilder.build(newUser);
                            users.add(newUser);
                            System.out.println("The new user has been added to the system with the " +
                                    "following credentials:");
                            System.out.println("\tUsername: " + usernameOfNewUser);
                            System.out.println("\tPassword: " + passwordOfNewUser);
                        } else {
                            System.out.println("These are the users in the system (select one to delete):");
                            Map<Integer, User> map = new LinkedHashMap<>();
                            int i = 1;
                            for (User u : users) {
                                map.put(i, u);
                                System.out.println(i + ". " + u.username);
                                i++;
                            }
                            if (map.isEmpty()) {
                                System.out.println("There are no users in the system!");
                                continue;
                            }
                            int id = 0;
                            try {
                                id = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            if (id < 1 || id > map.size()) {
                                throw new InvalidCommandException("Invalid command!");
                            }
                            User userToDelete = map.get(id);
                            if (userToDelete instanceof Staff) {
                                // put his productions and actors he is responsible for in the system to the admin list
                                for (Production p : productions) {
                                    if (p.resposability.equals(userToDelete.username)) {
                                        p.resposability = "Admin";
                                        for (User u : users) {
                                            if (u instanceof Admin) {
                                                ((Admin) u).addProductionSystem(p);
                                                break;
                                            }
                                        }
                                    }
                                }
                                for (Actor a : actors) {
                                    if (a.responsability.equals(userToDelete.username)) {
                                        a.responsability = "Admin";
                                        for (User u : users) {
                                            if (u instanceof Admin) {
                                                ((Admin) u).addActorSystem(a);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            users.remove(userToDelete);
                        }
                    }
                    if (option > 10) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                }
                if ((option == 8 && user.accountType.equals(AccountType.REGULAR)) ||
                        (option == 10 && user.accountType.equals(AccountType.CONTRIBUTOR)) ||
                        (option == 10 && user.accountType.equals(AccountType.ADMIN))) {
                    user.logout();
                    System.out.println("You have been logged out!");
                    System.out.println("Select one of the following options:");
                    System.out.println("\t1. Login again");
                    System.out.println("\t2. Exit");
                    int option2 = 0;
                    try {
                        option2 = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                    if (option2 < 1 || option2 > 2) {
                        throw new InvalidCommandException("Invalid command!");
                    }
                    if (option2 == 2) {
                        usage = 0;
                        break;
                    }
                    break;
                }
            }
        }
        if (usage == 2) {
            GUI gui = new GUI();
            gui.start();
        }
    }

    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        try {
            imdb.run();
        } catch (InvalidCommandException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (InformationIncompleteException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}