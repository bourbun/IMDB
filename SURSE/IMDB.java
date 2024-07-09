package org.example;
import com.fasterxml.jackson.core.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;

public class IMDB implements Subject{
    private List<Observer> observers = new ArrayList<>();
    public static IMDB instance;
    private List<Actor> actors;
    private List<Request> requests;
    public List<Production> productions;
    private List<User> users;

    public IMDB() {
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public void runTerminal() {
        loadData();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome back! Enter your credentials!\n");
        User user = null;
        while (user == null) {
            System.out.print("\tEmail: ");
            String email = scanner.nextLine().trim();

            System.out.print("\tPassword: ");
            String password = scanner.nextLine().trim();

            user = authenticateUser(email, password);
            if (user == null) {
                System.out.println("Authentication failed. Please try again.");
            }
        }
        System.out.println("Welcome back user " + user.getName() + "!");
        System.out.println("Username: " + user.getName());
        if (user.getExperience() == null) {
            System.out.println("Experience: -");
        } else {
            System.out.println("Experience: " + user.getExperience());
        }
        System.out.println("Choose action: ");
        // check what type of user is logged in
        if (user instanceof Admin admin) {
            admin.displayTerminal(scanner, admin);
        } else if (user instanceof Contributor contributor) {
            contributor.displayTerminal(scanner, contributor);
        } else {
            Regular regular = (Regular) user;
            regular.displayTerminal(scanner, regular);
        }
    }

    public void runGUI() {
            loadData();
            startApplicationFlow();
    }

    void loadData() {
        JSONParser parserUser = new JSONParser();
        JSONParser parserActors = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\src\\main\\java\\org\\example\\actors.json")) {
            JSONArray actorList = (JSONArray) parserActors.parse(reader);
            for (Object actorObj : actorList) {
                JSONObject actorJSON = (JSONObject) actorObj;
                String name = (String) actorJSON.get("name");
                JSONArray performancesJSON = (JSONArray) actorJSON.get("performances");
                Map<String, String> roles = new HashMap<>();
                for (Object performanceObj : performancesJSON) {
                    JSONObject performanceJSON = (JSONObject) performanceObj;
                    String title = (String) performanceJSON.get("title");
                    String type = (String) performanceJSON.get("type");
                    roles.put(title, type);
                }
                String biography = (String) actorJSON.get("biography");
                // check if actor already exists
                boolean actorExists = false;
                for (Actor actor : actors) {
                    if (actor.getName().equals(name)) {
                        actorExists = true;
                        break;
                    }
                }
                if (!actorExists) {
                    Actor actor = new Actor(name, roles, biography);
                    actors.add(actor);
                }
//                Actor actor = new Actor(name, roles, biography);
//                actors.add(actor);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        try (FileReader reader = new FileReader("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\src\\main\\java\\org\\example\\production.json")) {
            JSONArray productionList = (JSONArray) new JSONParser().parse(reader);
            for (Object productionObj : productionList) {
                JSONObject productionJSON = (JSONObject) productionObj;
                String title = (String) productionJSON.get("title");
                String type = (String) productionJSON.get("type");
                JSONArray directorsJSON = (JSONArray) productionJSON.get("directors");
                List<String> directors = new ArrayList<>();
                for (Object directorObj : directorsJSON) {
                    directors.add((String) directorObj);
                }
                JSONArray actorsJSON = (JSONArray) productionJSON.get("actors");
                for (Object actorObj : actorsJSON) {
                    String actorName = (String) actorObj;
                    boolean actorExists = false;
                    for (Actor actor : actors) {
                        if (actor.getName().equals(actorName)) {
                            actorExists = true;
                            break;
                        }
                    }
                    if (!actorExists) {
                        Map<String, String> roles = new HashMap<>();
                        roles.put(title, type); // Assuming title and type are from the current production
                        Actor newActor = new Actor(actorName, roles, ""); // Empty biography
                        actors.add(newActor);
                    }
                }
                List<String> actors = new ArrayList<>();
                for (Object actorObj : actorsJSON) {
                    actors.add((String) actorObj);
                }

                JSONArray genresJSON = (JSONArray) productionJSON.get("genres");
                List<Genre> genres = new ArrayList<>();
                for(Object genreObj : genresJSON) {
                    String genreString = (String) genreObj;
                    Genre genre = Genre.valueOf(genreString);
                    genres.add(genre);
                }


                JSONArray ratingsJSON = (JSONArray) productionJSON.get("ratings");
                List<Rating> ratings = new ArrayList<>();
                for (Object ratingObj : ratingsJSON) {
                    JSONObject ratingJSON = (JSONObject) ratingObj;
                    String username = (String) ratingJSON.get("username");
                    long ratingValue = (long) ratingJSON.get("rating");
                    String comment = (String) ratingJSON.get("comment");
                    Rating rating = new Rating(username, (int) ratingValue, comment);
                    ratings.add(rating);
                }



                if ("Series".equals(type)) {
                    String plot = (String) productionJSON.get("plot");
                    Double averageRating = (Double) productionJSON.get("averageRating");
                    int releaseYear = ((Long) productionJSON.get("releaseYear")).intValue();
                    int numSeasons = ((Long) productionJSON.get("numSeasons")).intValue();
                    JSONObject seasonsJSON = (JSONObject) productionJSON.get("seasons");
                    Map<String, List<Episode>> seasons = new HashMap<>();

                    for (Object seasonKey : seasonsJSON.keySet()) {
                        String seasonName = (String) seasonKey;
                        JSONArray episodesJSON = (JSONArray) seasonsJSON.get(seasonName);
                        List<Episode> episodes = new ArrayList<>();

                        for (Object episodeObj : episodesJSON) {
                            JSONObject episodeJSON = (JSONObject) episodeObj;
                            String episodeName = (String) episodeJSON.get("episodeName");
                            String durationStr  = (String) episodeJSON.get("duration");
                            int duration = Integer.parseInt(durationStr.split(" ")[0]); // Assuming format "XX minutes"

                            Episode episode = new Episode(episodeName, duration);
                            episodes.add(episode);
                        }

                        seasons.put(seasonName, episodes);
                    }
                    Series series = new Series(title, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons);
                    productions.add(series);
                }
                else if ("Movie".equals(type)) {
                    String plot = (String) productionJSON.get("plot");
                    Double averageRating = (Double) productionJSON.get("averageRating");
                    String durationStr  = (String) productionJSON.get("duration");
                    int duration = Integer.parseInt(durationStr.split(" ")[0]);
                    int releaseYear = ((Number) productionJSON.getOrDefault("releaseYear", 0)).intValue();
                    Movie movie = new Movie(title, directors, actors, genres, ratings, plot, averageRating, duration, releaseYear);
                    productions.add(movie);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        try (FileReader reader = new FileReader("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\src\\main\\java\\org\\example\\accounts.json"))
        {
            JSONArray userList = (JSONArray) parserUser.parse(reader);
            for (Object userObj : userList) {
                JSONObject userJSON = (JSONObject) userObj;
                String username = (String) userJSON.get("username");
                String experience = (String) userJSON.get("experience");
                JSONObject infoJSON = (JSONObject) userJSON.get("information");
                JSONObject credsJSON = (JSONObject) infoJSON.get("credentials");
                User.Information info = new User.Information.Builder((String) credsJSON.get("email"), (String) credsJSON.get("password"))
                        .name((String) infoJSON.get("name"))
                        .country((String) infoJSON.get("country"))
                        .age(String.valueOf(((Long) infoJSON.get("age")).intValue()))
                        .gender((String) infoJSON.get("gender"))
                        .birthday(LocalDate.parse((String) infoJSON.get("birthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay())
                        .build();

                String accountType = (String) userJSON.get("userType");
                User user = UserFactory.createUser(AccountType.valueOf(accountType), username, experience, info);
                user.setInformation(info);
                user.setName(username);
                user.setExperience(experience);


                if (user instanceof Admin || user instanceof Contributor) {
                    JSONArray productionsContribution = (JSONArray) userJSON.getOrDefault("productionsContribution", new JSONArray());
                    JSONArray actorsContribution = (JSONArray) userJSON.getOrDefault("actorsContribution", new JSONArray());
                    if (productionsContribution != null) {
                        for (Object productionObj : productionsContribution) {
                            String productionTitle = (String) productionObj;
                            Production production = getProductionByTitle(productionTitle, productions);
                            if (production != null) {
                                user.addContributionProduction(production);
                                production.addObserver(user);
                            }
                        }
                    }
                    if (actorsContribution != null) {
                        for (Object actorObj : actorsContribution) {
                            String actorName = (String) actorObj;
                            Actor actor = getActorByName(actorName, actors);
                            if (actor != null) {
                                user.addContributionActor(actor);
                                actor.addObserver(user);
                            }
                        }
                    }
                }

                JSONArray favoriteProductions = (JSONArray) userJSON.getOrDefault("favoriteProductions", new JSONArray());
                JSONArray favoriteActors = (JSONArray) userJSON.getOrDefault("favoriteActors", new JSONArray());
                for (Object productionObj : favoriteProductions) {
                    String productionTitle = (String) productionObj;
                    Production production = getProductionByTitle(productionTitle, productions);
                    if (production != null) {
                        user.favorites.add(production);
                        production.addObserver(user);
                    }
                }
                for (Object actorObj : favoriteActors) {
                    String actorName = (String) actorObj;
                    Actor actor = getActorByName(actorName, actors);
                    if (actor != null) {
                        user.favorites.add(actor);
                    }
                }


                JSONArray notifications = (JSONArray) userJSON.getOrDefault("notifications", new JSONArray());
                List<String> notificationsJSON = new ArrayList<>();
                if (notificationsJSON != null) {
                    for (Object notificationObj : notificationsJSON) {
                        String notification = (String) notificationObj;
                        notifications.add(notification);
                    }
                }
                user.setNotifications(notifications);

                users.add(user);

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try (FileReader reader = new FileReader("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\src\\main\\java\\org\\example\\requests.json")) {
            JSONArray requestList = (JSONArray)  new JSONParser().parse(reader);
            for (Object requestObj : requestList) {
                JSONObject requestJSON = (JSONObject) requestObj;
                RequestTypes type = RequestTypes.valueOf((String) requestJSON.get("type"));
                LocalDateTime createdDate = LocalDateTime.parse((String) requestJSON.get("createdDate"));
                String username = (String) requestJSON.get("username");
                String to = (String) requestJSON.get("to");
                String description = (String) requestJSON.get("description");
                String actorName = (String) requestJSON.getOrDefault("actorName", null);
                String productionTitle = (String) requestJSON.getOrDefault("productionTitle", null);
                Request request = new Request(type, createdDate, productionTitle, actorName, description, username, to);
                //add request to requests list only if doesn't exist already
                requests.add(request);
                User user = getUserByUsername(username);
                addObserver(user);

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public List<Production> getProductions() {
        return productions;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public User authenticateUser(String email, String password) {
        for (User user : users) {
            if (user.getInformation().getEmail().equals(email) &&
                    user.getInformation().getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void startApplicationFlow() {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
    public Production getProductionByTitle(String title, List<Production> productions) {
        for (Production production : productions) {
            if (production.getTitle().trim().equals(title.trim())) {
                return production;
            }
        }
        return null;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public Actor getActorByName(String name, List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getName().trim().equals(name.trim())) {
                return actor;
            }
        }
        return null;
    }

    public User findUserWhoAddedActor(String actorName) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user instanceof Admin || user instanceof Contributor) {
                for (Sortable item : user.contributions) {
                    if (item instanceof Actor && ((Actor) item).getName().equalsIgnoreCase(actorName)) {
                        return user; // User found who added this actor
                    }
                }
            }
        }
        return null; // No user found who added this actor
    }

    public List<User> getUsers() {
        return users;
    }

    public User findUserWhoAddedProduction(String title) {
        for (User user : users) {
            if (user instanceof Admin || user instanceof Contributor) {
                for (Sortable item : user.contributions) {
                    if (item instanceof Production && ((Production) item).getTitle().equalsIgnoreCase(title)) {
                        return user; // User found who added this production
                    }
                }
            }
        }
        return null; // No user found who added this production
    }

    public List<Rating> getRatings() {
        List<Rating> ratings = new ArrayList<>();
        for (Production production : productions) {
            ratings.addAll(production.getRatings());
        }
        return ratings;
    }

    public User findUserWhoAddedRequest(Request request) {
        for (User user : users) {
            if (user instanceof Admin || user instanceof Contributor) {
                for (Sortable item : user.contributions) {
                    if (item instanceof Request && ((Request) item).equals(request)) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    public List<Actor> searchActors(String searchText) {
        List<Actor> results = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.getName().toLowerCase().contains(searchText.toLowerCase())) {
                results.add(actor);
            }
        }
        return results;
    }

    public List<Production> searchProductions(String searchText) {
        List<Production> results = new ArrayList<>();
        for (Production production : productions) {
            if (production.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                results.add(production);
            }
        }
        return results;
    }

    public List<Rating> getUserRatings(String name) {
        List<Rating> ratings = new ArrayList<>();
        for (Production production : productions) {
            Rating rating = production.getRatingByUser(name);
            if (rating != null) {
                ratings.add(rating);
            }
        }
        return ratings;
    }

    public List<Request> getUserRequests(String name) {
        List<Request> userRequests = new ArrayList<>();
        for (Request request : requests) {
            if (request.getCreatorUsername().equals(name)) {
                userRequests.add(request);
            }
        }
        return userRequests;
    }

    public List<Request> getRequestsForUser(String name) {
        List<Request> userRequests = new ArrayList<>();
        for (Request request : requests) {
            if (request.getResolverUsername().equals(name)) {
                userRequests.add(request);
            }
        }
        return userRequests;
    }

    public User getUserByUsername(String selectedUsername) {
        for (User user : users) {
            if (user.getName().equals(selectedUsername)) {
                return user;
            }
        }
        return null;
    }

    public List<Request> getRequestsForUserIncludingAdmin(String name) {
        List<Request> userRequests = new ArrayList<>();
        for (Request request : requests) {
            if (request.getResolverUsername().equals("ADMIN") || request.getResolverUsername().equals(name)) {
                userRequests.add(request);
            }
        }
        return userRequests;
    }

    public Object getDataToSave() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("actors", this.actors);
        dataMap.put("requests", this.requests);
        dataMap.put("productions", this.productions);
        dataMap.put("users", this.users);
        // Add other data you want to save similarly...
        return dataMap;
    }

    public static class RequestsHolder {
        private static final List<Request> allRequests = IMDB.getInstance().getRequests();

        public static void addRequest(Request request) {
            allRequests.add(request);
        }

        public static void removeRequest(Request request) {
            allRequests.remove(request);
        }

        public static List<Request> getAllRequests() {
            return new ArrayList<>(allRequests);
        }
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
    public void saveToJsonFile(String filename) throws IOException {
        File file = new File(filename);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // To beautify the JSON output

        Object dataToSave = getDataToSave(); // Get the data to save

        // Check if the file already exists
        if (file.exists() && file.length() != 0) {
            // Read the existing content of the file
            TypeReference<HashMap<String,Object>> typeRef
                    = new TypeReference<HashMap<String,Object>>() {};
            HashMap<String, Object> existingData = mapper.readValue(file, typeRef);

            // Compare existing data with the data to save
            if (existingData.equals(dataToSave)) {
                // If data is the same, do not write to file to avoid duplicates
                System.out.println("No changes detected. Skipping save operation.");
                return;
            }
        }

        // If new data or changes are detected, write to the file
        mapper.writeValue(file, dataToSave);
    }
}
