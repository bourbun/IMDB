package org.example;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Regular extends User implements RequestsManager {

    private ReviewExperience reviewExperience = new ReviewExperience();
    private RequestExperience requestExperience = new RequestExperience();


    public Regular() {
        super();
        this.type = AccountType.Regular;
    }

    @Override
    public void createRequest(Request r) {
        IMDB.RequestsHolder.addRequest(r);
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.RequestsHolder.removeRequest(r);
    }



    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    public void displayTerminal(Scanner S,User user){
//        IMDB.getInstance().loadData();
        List<Request> requests = IMDB.getInstance().getRequests();
        List<Production> productions = IMDB.getInstance().getProductions();
        System.out.println("\t1) View productions details");
        System.out.println("\t2) View actors details");
        System.out.println("\t3) View notifications");
        System.out.println("\t4) Search for actor/movie/series");
        System.out.println("\t5) Add/Delete actor/movie/series to/from favorites");
        System.out.println("\t6) Add/Delete a request");
        System.out.println("\t7) Add/Delete a review for a production");
        System.out.println("\t8) See favorites");
        System.out.println("\t9) Logout");
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
            if (user.notifications.isEmpty()) {
                System.out.println("You have no notifications.");
            } else {
                System.out.println("Notifications:");
                for (String notification : user.notifications) {
                    System.out.println(notification);
                }
            }
            displayTerminal(S,user);
        } else if (option.equals("4")) {
            System.out.println("\t1) Enter the name of the actor/movie/series you want to search for: ");
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
                    user.addFavorite(p);
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
                    user.addFavorite(p);
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
                    user.removeFavorite(p);
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
                    user.removeFavorite(p);
                    System.out.println(" Series deleted from favorites!");
                    displayTerminal(S,user);
                }
            }
        } else if (option.equals("6")) {
            System.out.println("\t1) Add a request");
            System.out.println("\t2) Delete a request");
            String option2 = S.nextLine();
            if (option2.equals("1")) {
                System.out.println("Enter the type of the request you want to add: ");
                System.out.println("\t1) DELETE_ACCOUNT");
                System.out.println("\t2) ACTOR_ISSUE");
                System.out.println("\t3) MOVIE_ISSUE");
                System.out.println("\t4) OTHERS");
                String option3 = S.nextLine();
                if (option3.equals("1")){
                    System.out.println("\tEnter the description of the request: ");
                    String description = S.nextLine();
                    LocalDateTime creationDate = LocalDateTime.now();
                    String resolverUsername = "ADMIN"; //all others requests are solved by admins
                    Request r = new Request(RequestTypes.DELETE_ACCOUNT, creationDate,null,null,description,this.getName(),resolverUsername);
                    createRequest(r);
                    System.out.println("Request added!");
                    displayTerminal(S,user);
                } else if (option3.equals("2")) {
                    System.out.println("\tEnter the name of the actor you want to add a request for: ");
                    String name = S.nextLine();
                    List<Actor> sortedActors = IMDB.getInstance().getActors();
                    sortedActors.sort(Comparator.comparing(Actor::getName));
                    while (IMDB.getInstance().getActorByName(name,sortedActors) == null) {
                        System.out.println("Actor not found. Please enter a valid name: ");
                        name = S.nextLine();
                    }
                    Actor a = IMDB.getInstance().getActorByName(name,sortedActors);
                    System.out.println("\tEnter the description of the request: ");
                    String description = S.nextLine();
                    LocalDateTime creationDate = LocalDateTime.now();
                    // search in actors contributions through each user, and the user who add it is the solver
                    String resolverUsername = IMDB.getInstance().findUserWhoAddedActor(a.name).name;
                    Request r = new Request(RequestTypes.ACTOR_ISSUE, creationDate,null,a.name,description,this.getName(),resolverUsername);
                    createRequest(r);
                    r.addObserver(this);
                    System.out.println("Request added!");
                    displayTerminal(S,user);
                } else if (option3.equals("3")) {
                    System.out.println("\tEnter the title of the production you want to add a request for: ");
                    String title = S.nextLine();
                    List<Production> sortedProductions = IMDB.getInstance().getProductions();
                    sortedProductions.sort(Comparator.comparing(Production::getTitle));
                    while (IMDB.getInstance().getProductionByTitle(title,sortedProductions) == null) {
                        System.out.println("Production not found. Please enter a valid title: ");
                        title = S.nextLine();
                    }
                    Production p = IMDB.getInstance().getProductionByTitle(title,sortedProductions);
                    System.out.println("\tEnter the description of the request: ");
                    String description = S.nextLine();
                    LocalDateTime creationDate = LocalDateTime.now();
                    // search in productions contributions through each user, and the user who add it is the solver
                    String resolverUsername = IMDB.getInstance().findUserWhoAddedProduction(p.title).name;
                    Request r = new Request(RequestTypes.MOVIE_ISSUE, creationDate,p.title,null,description,this.getName(),resolverUsername);
                    createRequest(r);
                    r.addObserver(this);
                    System.out.println("Request added!");
                    displayTerminal(S,user);
                } else if (option3.equals("4")) {
                    System.out.println("\tEnter the description of the request: ");
                    String description = S.nextLine();
                    LocalDateTime creationDate = LocalDateTime.now();
                    String resolverUsername = "ADMIN"; //all others requests are solved by admins
                    Request r = new Request(RequestTypes.OTHERS, creationDate,null,null,description,this.getName(),resolverUsername);
                    createRequest(r);
                    System.out.println("Request added!");
                    displayTerminal(S,user);
                }
            } else if (option2.equals("2")) {
                System.out.println("Choose a request to delete:");
                List<Request> userRequests = requests.stream()
                        .filter(r -> r.getCreatorUsername().equals(this.getName()))
                        .toList();

                for (int i = 0; i < userRequests.size(); i++) {
                    System.out.println((i+1) + ") " + userRequests.get(i).description);
                }

                System.out.println("Enter the number of the request to delete:");
                String requestIndexString = S.nextLine();
                int requestIndex = Integer.parseInt(requestIndexString) - 1;
                if (requestIndex >= 0 && requestIndex < userRequests.size()) {
                    removeRequest(userRequests.get(requestIndex));
                    System.out.println("Request deleted successfully.");
                } else {
                    System.out.println("Invalid request number.");
                }
                displayTerminal(S, user);
            }
        } else if (option.equals("7")) {
            System.out.println("\t1) Add a review");
            System.out.println("\t2) Delete a review");
            String option2 = S.nextLine();
            if (option2.equals("1")) {
                System.out.println("\tEnter the title of the production you want to add a review to: ");
                String title = S.nextLine();
                Production p = IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions());
                while (p == null) {
                    System.out.println("Production not found. Please enter a valid title: ");
                    title = S.nextLine();
                    p = IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions());
                }
                Rating existingRating = p.getRatingByUser(this.getName());
                if (existingRating != null) {
                    System.out.println("You have already added a review for this production. Do you want to edit it? (Y/N)");
                    String option3 = S.nextLine();
                    if (option3.equals("Y") || option3.equals("y")) {
                        System.out.println("Enter your comments: ");
                        String comments = S.nextLine();
                        System.out.println("Enter your rating (1-10): ");
                        String scoreString = S.nextLine();
                        int score = Integer.parseInt(scoreString);
                        Rating rating = new Rating(this.getName(), score, comments);
                        p.ratings.remove(existingRating);
                        p.ratings.add(rating);
                        System.out.println("Review added!");
                        displayTerminal(S,user);
                    } else if (option3.equals("N") || option3.equals("n")) {
                        displayTerminal(S,user);
                    } else {
                        System.out.println("Invalid option. Please enter a valid option: ");
                        displayTerminal(S,user);
                    }
                } else {
                    System.out.println("Enter your comments: ");
                    String comments = S.nextLine();
                    System.out.println("Enter your rating (1-10): ");
                    String scoreString = S.nextLine();
                    int score = Integer.parseInt(scoreString);
                    Rating rating = new Rating(this.getName(), score, comments);
                    p.ratings.add(rating);
                    System.out.println("Review added!");
                    int newExperience = Integer.parseInt(this.getExperience()) + reviewExperience.calculateExperience();
                    this.setExperience(String.valueOf(newExperience));
                    displayTerminal(S,user);
                }
            } else if (option2.equals("2")) {
                System.out.println("\tEnter the title of the production you want to delete a review from: ");
                String title = S.nextLine();
                Production p = IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions());
                while (p == null) {
                    System.out.println("Production not found. Please enter a valid title: ");
                    title = S.nextLine();
                    p = IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions());
                }
                Rating existingRating = p.getRatingByUser(this.getName());
                if (existingRating != null) {
                    p.ratings.remove(existingRating);
                    System.out.println("Review deleted!");
                    displayTerminal(S,user);
                } else {
                    System.out.println("You have not added a review for this production.");
                    displayTerminal(S,user);
                }
            }
        } else if (option.equals("8")) {
            System.out.println("\t1) View actors");
            System.out.println("\t2) View movies");
            System.out.println("\t3) View series");
            String option2 = S.nextLine();

            switch (option2) {
                case "1":
                    System.out.println("Favorite Actors:");
                    for (Comparable favorite : user.favorites) {
                        if (favorite instanceof Actor actor) {
                            System.out.println(actor.getName());
                        }
                    }
                    break;
                case "2":
                    System.out.println("Favorite Movies:");
                    for (Comparable favorite : user.favorites) {
                        if (favorite instanceof Movie movie) {
                            System.out.println(movie.getTitle());
                        }
                    }
                    break;
                case "3":
                    System.out.println("Favorite Series:");
                    for (Comparable favorite : user.favorites) {
                        if (favorite instanceof Series series) {
                            System.out.println(series.getTitle());
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option: ");
                    break;
            }
            displayTerminal(S,user);

        } else if (option.equals("9")) {
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

