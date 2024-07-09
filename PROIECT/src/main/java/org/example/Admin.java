package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Admin extends Staff {
    ContributionExperience contributionExperience = new ContributionExperience();
    RequestExperience requestExperience = new RequestExperience();
    public Admin() {
        super();
    }

    @Override
    public void addActorToSystem(Actor a) {

    }

    @Override
    public void removeActorFromSystem(String name) {

    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    @Override
    public void updateProduction(Production p) {

    }

    @Override
    public void updateActor(Actor a) {

    }


    @Override
    public void addProductionToSystem(Production p) {

    }

    public void displayTerminal(Scanner S, User user){
        List<Request> requests = IMDB.getInstance().getRequests();
        System.out.println("\t1) View productions details");
        System.out.println("\t2) View actors details");
        System.out.println("\t3) View notifications");
        System.out.println("\t4) Search for actor/movie/series");
        System.out.println("\t5) Add/Delete actor/movie/series to/from favorites");
        System.out.println("\t6) Add/Delete actor/movie/series from system");
        System.out.println("\t7) Solve a request");
        System.out.println("\t8) Update Movie/Actor Details");
        System.out.println("\t9) Add/Delete user");
        System.out.println("\t10) See favorites");
        System.out.println("\t11) Logout");
        String option = S.nextLine();
        if (option.equals("1")) {
            List<Production> sortedProductions = IMDB.getInstance().getProductions();
            sortedProductions.sort(Comparator.comparing(Production::getTitle));

            for (Production p : sortedProductions) {
                System.out.println(p.getTitle());
            }
            System.out.println();
            System.out.println("Enter the title of the production you want to view: ");
            String title = S.nextLine();
            while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                System.out.println("Production not found. Please enter a valid title: ");
                title = S.nextLine();
            }
            Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
            p.displayInfo();
            displayTerminal(S,user);
        } else if (option.equals("2")) {
            List<Actor> sortedActors = IMDB.getInstance().getActors();
            sortedActors.sort(Comparator.comparing(Actor::getName));

            for (Actor a : sortedActors) {
                System.out.println(a.getName());
            }
            System.out.println();
            System.out.println("Enter the name of the actor you want to view: ");
            String name = S.nextLine();
            while (IMDB.getInstance().getActorByName(name,sortedActors) == null) {
                System.out.println("Actor not found. Please enter a valid name: ");
                name = S.nextLine();
            }
            Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
            a.displayInfo();
            displayTerminal(S,user);
        } else if (option.equals("3")) {
            for (Object n : notifications) {
                System.out.println(n);
            }
            displayTerminal(S,user);
        } else if (option.equals("4")) {
            System.out.println("\tEnter the name of the actor/movie/series you want to search for: ");
            String name = S.nextLine();
            List<Actor> sortedActors = IMDB.getInstance().getActors();
            sortedActors.sort(Comparator.comparing(Actor::getName));
            List<Production> sortedProductions = IMDB.getInstance().getProductions();
            sortedProductions.sort(Comparator.comparing(Production::getTitle));
            while (IMDB.getInstance().getActorByName(name,sortedActors) == null && IMDB.getInstance().getProductionByTitle(name,sortedProductions) == null) {
                System.out.println("Actor/Movie/Series not found. Please enter a valid name: ");
                name = S.nextLine();
            }
            if (IMDB.getInstance().getActorByName(name,sortedActors) != null){
                Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
                a.displayInfo();
            }
            else if (IMDB.getInstance().getProductionByTitle(name,sortedProductions) != null){
                Production p = IMDB.getInstance().getProductionByTitle(name,sortedProductions);
                p.displayInfo();
            }
            displayTerminal(S,user);
        } else if (option.equals("5")) {
            System.out.println("\t1) Add actor/movie/series to favorites");
            System.out.println("\t2) Delete actor/movie/series from favorites");
            String option2 = S.nextLine();
            if (option2.equals("1")){
                System.out.println("\t1) Add actor to favorites");
                System.out.println("\t2) Add movie to favorites");
                System.out.println("\t3) Add series to favorites");
                String option3 = S.nextLine();
                if (option3.equals("1")){
                    System.out.println("\tEnter the name of the actor you want to add to favorites: ");
                    String name = S.nextLine();
                    List<Actor> sortedActors = IMDB.getInstance().getActors();
                    sortedActors.sort(Comparator.comparing(Actor::getName));
                    while (IMDB.getInstance().getActorByName(name,sortedActors) == null) {
                        System.out.println("Actor not found. Please enter a valid name: ");
                        name = S.nextLine();
                    }
                    Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
                    favorites.add(a);
                    System.out.println("Actor added to favorites!");
                    displayTerminal(S,user);
                }
                else if (option3.equals("2")){
                    System.out.println("\tEnter the title of the movie you want to add to favorites: ");
                    String title = S.nextLine();
                    List<Production> sortedProductions = IMDB.getInstance().getProductions();
                    sortedProductions.sort(Comparator.comparing(Production::getTitle));
                    while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                        System.out.println("Movie not found. Please enter a valid title: ");
                        title = S.nextLine();
                    }
                    Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                    user.favorites.add(p);
                    System.out.println("Movie added to favorites!");
                    displayTerminal(S,user);
                }
                else if (option3.equals("3")){
                    System.out.println("\tEnter the title of the series you want to add to favorites: ");
                    String title = S.nextLine();
                    List<Production> sortedProductions = IMDB.getInstance().getProductions();
                    sortedProductions.sort(Comparator.comparing(Production::getTitle));
                    while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                        System.out.println("Series not found. Please enter a valid title: ");
                        title = S.nextLine();
                    }
                    Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                    user.favorites.add(p);
                    System.out.println("Series added to favorites!");
                    displayTerminal(S,user);
                }
            } else if (option2.equals("2")){
                System.out.println("\t1) Delete actor from favorites");
                System.out.println("\t2) Delete movie from favorites");
                System.out.println("\t3) Delete series from favorites");
                String option3 = S.nextLine();
                if (option3.equals("1")){
                    System.out.println("\tEnter the name of the actor you want to delete from favorites: ");
                    String name = S.nextLine();
                    List<Actor> sortedActors = IMDB.getInstance().getActors();
                    sortedActors.sort(Comparator.comparing(Actor::getName));
                    while (IMDB.getInstance().getActorByName(name,sortedActors) == null) {
                        System.out.println("Actor not found. Please enter a valid name: ");
                        name = S.nextLine();
                    }
                    Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
                    user.favorites.remove(a);
                    System.out.println("Actor deleted from favorites!");
                    displayTerminal(S,user);
                }
                else if (option3.equals("2")){
                    System.out.println("\tEnter the title of the movie you want to delete from favorites: ");
                    String title = S.nextLine();
                    List<Production> sortedProductions = IMDB.getInstance().getProductions();
                    sortedProductions.sort(Comparator.comparing(Production::getTitle));
                    while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                        System.out.println("Movie not found. Please enter a valid title: ");
                        title = S.nextLine();
                    }
                    Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                    user.favorites.remove(p);
                    System.out.println("Movie deleted from favorites!");
                    displayTerminal(S,user);
                }
                else if (option3.equals("3")){
                    System.out.println("\tEnter the title of the series you want to delete from favorites: ");
                    String title = S.nextLine();
                    List<Production> sortedProductions = IMDB.getInstance().getProductions();
                    sortedProductions.sort(Comparator.comparing(Production::getTitle));
                    while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                        System.out.println("Series not found. Please enter a valid title: ");
                        title = S.nextLine();
                    }
                    Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                    user.favorites.remove(p);
                    System.out.println(" Series deleted from favorites!");
                    displayTerminal(S,user);
                }
            }
        } else if (option.equals("6")) {
            System.out.println("Select an option:");
            System.out.println("\t1) Add actor/prodution to system");
            System.out.println("\t2) Delete actor/production from system");
            String option2 = S.nextLine();
            if (option2.equals("1")){
                System.out.println("\t1) Add actor to system");
                System.out.println("\t2) Add production to system");
                String option3 = S.nextLine();
                if (option3.equals("1")){
                    System.out.println("\tEnter the name of the actor you want to add to system: ");
                    String actorName = S.nextLine();
                    System.out.println("\tEnter one performance of the actor: ");
                    String title = S.nextLine();
                    System.out.println("\tEnter the type of the performance: ");
                    String type = S.nextLine();
                    System.out.println("\tEnter the biography of the actor: ");
                    String biography = S.nextLine();
                    Map<String, String> roles = new HashMap<>();
                    roles.put(title,type);
                    Actor a = new Actor(actorName,roles,biography);
                    this.contributions.add(a);
                    addActorToSystem(a);
                    int newExperience = Integer.parseInt(this.getExperience()) + contributionExperience.calculateExperience();
                    this.setExperience(String.valueOf(newExperience));
                    displayTerminal(S,user);
                } else if (option3.equals("2")){
                    System.out.println("\tEnter the title of the production you want to add to system: ");
                    String title = S.nextLine();
                    System.out.println("\tEnter the type of the production: ");
                    String type = S.nextLine();
                    System.out.println("\tEnter the directors of the production: ");
                    // while reading the directors, add them to a list
                    List<String> directors = new ArrayList<>();
                    String director = S.nextLine();
                    while (!director.equals("")){
                        directors.add(director);
                        director = S.nextLine();
                    }
                    System.out.println("\tEnter the actors of the production: ");
                    // while reading the actors, add them to a list
                    List<String> actors = new ArrayList<>();
                    String actor = S.nextLine();
                    while (!actor.equals("")){
                        actors.add(actor);
                        actor = S.nextLine();
                    }
                    System.out.println("\tEnter the genres of the production: ");
                    // while reading the genres, add them to a list
                    List<Genre> genres = new ArrayList<>();
                    String genre = S.nextLine();
                    while (!genre.equals("")){
                        genres.add(Genre.valueOf(genre));
                        genre = S.nextLine();
                    }
                    System.out.println("\tEnter the plot of the production: ");
                    String plot = S.nextLine();
                    System.out.println("\tEnter the average rating of the production: ");
                    String averageRating = S.nextLine();
                    double averageRatingDouble = Double.parseDouble(averageRating);
                    System.out.println("\tEnter the release year of the production: ");
                    String releaseYear = S.nextLine();
                    int releaseYearInt = Integer.parseInt(releaseYear);
                    if (type.equals("Movie")){
                        System.out.println("\tEnter the duration of the movie: ");
                        String duration = S.nextLine();
                        int durationInt = Integer.parseInt(duration);
                        Production p = new Movie(title, directors, actors, genres, null, plot, averageRatingDouble, durationInt, releaseYearInt);
                        this.contributions.add(p);
                        addProductionToSystem(p);
                        displayTerminal(S,user);
                    } else if (type.equals("Series")){
                        System.out.println("\tEnter the number of seasons of the series: ");
                        String numberOfSeasons = S.nextLine();
                        int numberOfSeasonsInt = Integer.parseInt(numberOfSeasons);
                        // while reading the seasons, add them to a list of seasons (if it reads a 2,3,etc go to the next season)
                        Map<String, List<Episode>> seasons = new HashMap<>();
                        for (int i = 0; i < numberOfSeasonsInt; i++){
                            System.out.println("\tEnter the number of episodes of season " + (i+1) + ": ");
                            String numberOfEpisodes = S.nextLine();
                            int numberOfEpisodesInt = Integer.parseInt(numberOfEpisodes);
                            List<Episode> episodes = new ArrayList<>();
                            for (int j = 0; j < numberOfEpisodesInt; j++){
                                System.out.println("\tEnter the title of episode " + (j+1) + ": ");
                                String episodeTitle = S.nextLine();
                                System.out.println("\tEnter the duration of episode " + (j+1) + ": ");
                                String episodeDuration = S.nextLine();
                                int episodeDurationInt = Integer.parseInt(episodeDuration);
                                Episode e = new Episode(episodeTitle, episodeDurationInt);
                                episodes.add(e);
                            }
                            seasons.put("Season " + (i+1),episodes);
                        }
                        Production p = new Series(title, directors, actors, genres, null, plot, averageRatingDouble, releaseYearInt, numberOfSeasonsInt, seasons);
                        this.contributions.add(p);
                        addProductionToSystem(p);
                    }
                    int newExperience = Integer.parseInt(this.getExperience()) + contributionExperience.calculateExperience();
                    this.setExperience(String.valueOf(newExperience));
                    displayTerminal(S,user);
                }
            } else if (option.equals("2")) {
                System.out.println("\t1) Delete actor from system");
                System.out.println("\t2) Delete production from system");
                String option3 = S.nextLine();
                if (option3.equals("1")) {
                    System.out.println("\tEnter the name of the actor you want to delete from system: ");
                    String name = S.nextLine();
                    List<Actor> sortedActors = IMDB.getInstance().getActors();
                    sortedActors.sort(Comparator.comparing(Actor::getName));
                    while (IMDB.getInstance().getActorByName(name,sortedActors) == null) {
                        System.out.println("Actor not found. Please enter a valid name: ");
                        name = S.nextLine();
                    }
                    Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
                    //check if actor was added by current user, check contributions
                    if (IMDB.getInstance().findUserWhoAddedActor(a.name).name.equals(this.name)){
                        this.contributions.remove(a);
                        removeActorFromSystem(a.name);
                    } else {
                        System.out.println("You don't have permission to delete this actor.");
                    }
                    displayTerminal(S,user);
                } else if (option3.equals("2")) {
                    System.out.println("\tEnter the title of the production you want to delete from system: ");
                    String title = S.nextLine();
                    List<Production> sortedProductions = IMDB.getInstance().getProductions();
                    sortedProductions.sort(Comparator.comparing(Production::getTitle));
                    while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                        System.out.println("Production not found. Please enter a valid title: ");
                        title = S.nextLine();
                    }
                    Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                    //check if production was added by current user, check contributions
                    if (IMDB.getInstance().findUserWhoAddedProduction(p.title).name.equals(this.name)){
                        this.contributions.remove(p);
                        removeProductionFromSystem(p.title);
                    } else {
                        System.out.println("You don't have permission to delete this production.");
                    }
                    displayTerminal(S,user);
                }
            }
        } else if (option.equals("7")) {
            List<Request> userRequests = new ArrayList<>(requests.stream()
                    .filter(r -> r.getCreatorUsername().equals(this.getName()))
                    .toList());
            //add to userRequests the requests that have ADMIN as resolver
            List<Request> adminRequests = requests.stream()
                    .filter(r -> r.getResolverUsername().equals("ADMIN"))
                    .toList();

            userRequests.addAll(adminRequests);

            for (int i = 0; i < userRequests.size(); i++) {
                System.out.println((i+1) + ") " + userRequests.get(i).description);
                System.out.println("Select a request: ");
                String requestIndexString = S.nextLine();
                int requestIndex = Integer.parseInt(requestIndexString) - 1;
                System.out.println("\t1) Solve the request");
                System.out.println("\t2) Rejected the request");
                String option2 = S.nextLine();
                if (option2.equals("1")){
                    int newExperience = Integer.parseInt(IMDB.getInstance().findUserWhoAddedRequest(userRequests.get(requestIndex)).getExperience()) + requestExperience.calculateExperience();
                    IMDB.getInstance().findUserWhoAddedRequest(userRequests.get(requestIndex)).setExperience(String.valueOf(newExperience));
                    userRequests.get(requestIndex).notifyObservers("Your request has been solved!");
                    displayTerminal(S,user);
                } else if (option2.equals("2")){
                    displayTerminal(S,user);
                } else {
                    System.out.println("Invalid option. Please enter a valid option: ");
                    displayTerminal(S,user);
                }
            }
        } else if (option.equals("8")) {
            System.out.println("Select an option: ");
            System.out.println("\t1) Create a new user");
            System.out.println("\t2) Delete a user");
            String option2 = S.nextLine();
            if (option2.equals("1")){

            } else if (option2.equals("2")){

            } else {
                System.out.println("Invalid option. Please enter a valid option: ");
                displayTerminal(S,user);
            }
        } else if (option.equals("9")) {
            System.out.println("\t1) Update actor details");
            System.out.println("\t2) Update production details");
            String option2 = S.nextLine();
            if (option2.equals("1")) {
                System.out.println("\tEnter the name of the actor you want to update: ");
                String name = S.nextLine();
                List<Actor> sortedActors = IMDB.getInstance().getActors();
                sortedActors.sort(Comparator.comparing(Actor::getName));
                while (IMDB.getInstance().getActorByName(name,sortedActors) == null) {
                    System.out.println("Actor not found. Please enter a valid name: ");
                    name = S.nextLine();
                }
                Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
                updateActor(a);
                displayTerminal(S,user);
            } else if (option2.equals("2")) {
                System.out.println("\tEnter the title of the production you want to update: ");
                String title = S.nextLine();
                List<Production> sortedProductions = IMDB.getInstance().getProductions();
                sortedProductions.sort(Comparator.comparing(Production::getTitle));
                while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                    System.out.println("Production not found. Please enter a valid title: ");
                    title = S.nextLine();
                }
                Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                updateProduction(p);
                displayTerminal(S,user);
            }
        } else if (option.equals("10")) {
            System.out.println("\t1) View actors");
            System.out.println("\t2) View movies");
            System.out.println("\t3) View series");
            String option2 = S.nextLine();
            if (option2.equals("1")){
                for (Object c : favorites){
                    if (c instanceof Actor a){
                        System.out.println(a.getName());
                    }
                }
                displayTerminal(S,user);
            }
            else if (option2.equals("2")){
                for (Object c : favorites){
                    if (c instanceof Movie m){
                        System.out.println(m.getTitle());
                    }
                }
                displayTerminal(S,user);
            }
            else if (option2.equals("3")){
                for (Object c : favorites){
                    if (c instanceof Series s){
                        System.out.println(s.getTitle());
                    }
                }
                displayTerminal(S,user);
            }
        } else if (option.equals("11")) {
            System.out.println("\tSelect an option: ");
            System.out.println("1) Login");
            System.out.println("2) Close the application");
            int option2 = S.nextInt();
            if (option2 == 1){
                IMDB.getInstance().runTerminal();
            }
            else if (option2 == 2){
                System.exit(0);
            }
        } else {
            System.out.println("Invalid option. Please enter a valid option: ");
            displayTerminal(S,user);
        }
    }
}

