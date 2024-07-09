package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Staff extends User implements StaffInterface{

    public void addProductionToSystem(Production p) {
        List<Production> productions = IMDB.getInstance().getProductions();
        if (productions.contains(p)) {
            System.out.println("Production already exists in the system");
        } else {
            productions.add(p);
            System.out.println("Production added successfully");
        }
    }

    public void removeProductionFromSystem(String name) {
        List<Production> productions = IMDB.getInstance().getProductions();
        Production productionToRemove = IMDB.getInstance().getProductionByTitle(name, productions);

        // Check if the production exists in the system
        if (productionToRemove == null) {
            System.out.println("Production not found in the system.");
            return;
        }
        productions.remove(productionToRemove);
        System.out.println("Production removed from the system: " + name);
    }

    public void addActorToSystem(Actor a) {
        List<Actor> actors = IMDB.getInstance().getActors();
        if (actors.contains(a)) {
            System.out.println("Actor already exists in the system");
        } else {
            actors.add(a);
            System.out.println("Actor added successfully");
        }
    }

    public void removeActorFromSystem(String name) {
        List<Actor> actors = IMDB.getInstance().getActors();
        Actor actorToRemove = IMDB.getInstance().getActorByName(name, actors);

        // Check if the actor exists in the system
        if (actorToRemove == null) {
            System.out.println("Actor not found in the system.");
            return;
        }
        actors.remove(actorToRemove);
        System.out.println("Actor removed from the system: " + name);
    }

    public void updateProduction(Production p) {
        if (IMDB.getInstance().findUserWhoAddedProduction(p.title).name.equals(this.name)){
            Scanner S = new Scanner(System.in);
            System.out.println("Updating details for production: " + p.getTitle());
            System.out.println("\tWould you like to update the directors? (y/n)");
            String updateDirectors = S.nextLine();
            if (updateDirectors.equals("Y") || updateDirectors.equals("y")){
                System.out.println("1) Add directors");
                System.out.println("2) Delete a director");
                String option3 = S.nextLine();

                if (option3.equals("1")){
                    System.out.println("\tEnter the new directors of the production: ");
                    // while reading the directors, add them to a list
                    String director = S.nextLine();
                    while (!director.isEmpty()){
                        p.directors.add(director);
                        director = S.nextLine();
                    }
                } else if (option3.equals("2")){
                    System.out.println("\tEnter the director you want to delete: ");
                    String director = S.nextLine();
                    for (String d : p.getDirectors()){
                        if (d.equals(director)){
                            p.directors.remove(director);
                        } else {
                            System.out.println("Director not found.");
                        }
                    }
                }
            } else if (updateDirectors.equals("N") || updateDirectors.equals("n")) {
                System.out.println();
            } else {
                System.out.println("Invalid option. Please enter a valid option: ");
                return;
            }
            System.out.println("\tWould you like to update the actors? (y/n)");
            String updateActors = S.nextLine();
            if (updateActors.equals("Y") || updateActors.equals("y")){
                System.out.println("1) Add actors");
                System.out.println("2) Delete an actor");
                String option3 = S.nextLine();

                if (option3.equals("1")){
                    System.out.println("\tEnter the new actors of the production: ");
                    String actor = S.nextLine();
                    while (!actor.isEmpty()){
                        p.actors.add(actor);
                        actor = S.nextLine();
                    }
                } else if (option3.equals("2")){
                    System.out.println("\tEnter the actor you want to delete: ");
                    String actor = S.nextLine();
                    for (String a : p.getActors()){
                        if (a.equals(actor)){
                            p.actors.remove(actor);
                        } else {
                            System.out.println("Actor not found.");
                        }
                    }
                }
            } else if (updateActors.equals("N") || updateActors.equals("n")) {
                System.out.println();
            } else {
                System.out.println("Invalid option. Please enter a valid option: ");
                return;
            }
            System.out.println("\tWould you like to update the genres? (y/n)");
            String updateGenres = S.nextLine();
            if (updateGenres.equals("Y") || updateGenres.equals("y")){
                System.out.println("1) Add genres");
                System.out.println("2) Delete a genre");
                String option3 = S.nextLine();

                if (option3.equals("1")){
                    System.out.println("\tEnter the new genres of the production: ");
                    // while reading the genres, add them to a list
                    String genre = S.nextLine();
                    while (!genre.isEmpty()){
                        p.genres.add(Genre.valueOf(genre));
                        genre = S.nextLine();
                    }
                } else if (option3.equals("2")){
                    System.out.println("\tEnter the genre you want to delete: ");
                    String genre = S.nextLine();
                    for (Genre g : p.getGenres()){
                        if (g.name().equals(genre)){
                            p.genres.remove(Genre.valueOf(genre));
                        } else {
                            System.out.println("Genre not found.");
                        }
                    }
                }
            } else if (updateGenres.equals("N") || updateGenres.equals("n")) {
                System.out.println();
            } else {
                System.out.println("Invalid option. Please enter a valid option: ");
                return;
            }

                System.out.println("\tWould you like to update the plot? (y/n)");
                String updatePlot = S.nextLine();
                if (updatePlot.equals("Y") || updatePlot.equals("y")){
                    System.out.println("\tEnter the new plot of the production: ");
                    p.description = S.nextLine();
                } else if (updatePlot.equals("N") || updatePlot.equals("n")) {
                    System.out.println();
                } else {
                    System.out.println("Invalid option. Please enter a valid option: ");
                    return;
                }
                System.out.println("\tWould you like to update the average rating? (y/n)");
                String updateAverageRating = S.nextLine();
                if (updateAverageRating.equals("Y") || updateAverageRating.equals("y")) {
                    System.out.println("\tEnter the new average rating of the production: ");
                    String averageRating = S.nextLine();
                    p.averageRating = Double.parseDouble(averageRating);
                } else if (updateAverageRating.equals("N") || updateAverageRating.equals("n")) {
                    System.out.println();
                } else {
                    System.out.println("Invalid option. Please enter a valid option: ");
                    return;
                }
            if (p.title.equals("MOVIE")){
                int ok = 0;
                int check = 0;
                int releaseYearInt = 0;
                int durationInt = 0;
                System.out.println("\tWould you like to update the release year? (y/n)");
                String updateReleaseYear = S.nextLine();
                if (updateReleaseYear.equals("Y") || updateReleaseYear.equals("y")) {
                    System.out.println("\tEnter the new release year of the production: ");
                    String releaseYear = S.nextLine();
                    releaseYearInt = Integer.parseInt(releaseYear);
                    ok= 1;
                } else if (updateReleaseYear.equals("N") || updateReleaseYear.equals("n")) {
                    System.out.println();
                } else {
                    System.out.println("Invalid option. Please enter a valid option: ");
                    return;
                }
                System.out.println("\tWould you like to update the duration? (y/n)");
                String updateDuration = S.nextLine();
                if (updateDuration.equals("Y") || updateDuration.equals("y")) {
                    System.out.println("\tEnter the new duration of the movie: ");
                    String duration = S.nextLine();
                    durationInt = Integer.parseInt(duration);
                    check = 1;
                } else if (updateDuration.equals("N") || updateDuration.equals("n")) {
                    System.out.println();
                } else {
                    System.out.println("Invalid option. Please enter a valid option: ");
                    return;
                }
                if (ok == 1 && check == 1){
                    Production movie = new Movie(p.title, p.directors, p.actors, p.genres, p.ratings, p.description, p.averageRating, releaseYearInt, durationInt);
                } else if (ok == 1){
                    Production movie = new Movie(p.title, p.directors, p.actors, p.genres, p.ratings, p.description, p.averageRating, releaseYearInt, ((Movie) p).duration);
                } else if (check == 1){
                    Production movie = new Movie(p.title, p.directors, p.actors, p.genres, p.ratings, p.description, p.averageRating, ((Movie) p).releaseYear, durationInt);
                } else {
                    Production movie = new Movie(p.title, p.directors, p.actors, p.genres, p.ratings, p.description, p.averageRating, ((Movie) p).releaseYear, ((Movie) p).duration);
                }
            }
        } else {
            System.out.println("You don't have permission to delete this production.");
            return;
        }
    }
    public void updateActor(Actor a){
        Scanner S = new Scanner(System.in);
        if (IMDB.getInstance().findUserWhoAddedActor(a.name).name.equals(this.name)){
            System.out.println("Updating details for actor: " + a.getName());
            System.out.println("\tWould you like to update the biography? (y/n)");
            String updateBiography = S.nextLine();
            if (updateBiography.equals("Y") || updateBiography.equals("y")){
                System.out.println("\tEnter the new biography of the actor: ");
                a.setBiography(S.nextLine());
            } else if (updateBiography.equals("N") || updateBiography.equals("n")) {
                System.out.println();
            } else {
                System.out.println("Invalid option. Please enter a valid option: ");
                return;
            }
            System.out.println("Would you like to update performances? (y/n)");
            String updatePerformances = S.nextLine();
            if (updatePerformances.equals("y") || updatePerformances.equals("Y")){
                System.out.println("\t1) Add a performance");
                System.out.println("\t2) Delete a performance");
                System.out.println("\t3) Update a performance");
                String option3 = S.nextLine();
                if (option3.equals("1")){
                    System.out.println("\tEnter the title of the performance you want to add: ");
                    String title = S.nextLine();
                    System.out.println("\tEnter the type of the performance you want to add: ");
                    String type = S.nextLine();
                    Map<String, String> roles = new HashMap<>();
                    roles.put(title,type);
                    a.setRoles(roles);
                    return;
                } else if (option3.equals("2")){
                    System.out.println("\tEnter the title of the performance you want to delete: ");
                    String title = S.nextLine();
                    a.deletePerformance(title);
                    System.out.println("Performance deleted!");
                    return;
                } else if (option3.equals("3")){
                    System.out.println("\tEnter the title of the performance you want to update: ");
                    String title = S.nextLine();
                    for (Map.Entry<String, String> entry : a.getRoles().entrySet()) {
                        if (entry.getKey().equals(title)){
                            System.out.println("\tEnter the new title of the performance: ");
                            String newTitle = S.nextLine();
                            System.out.println("\tEnter the new type of the performance: ");
                            String newType = S.nextLine();
                            a.deletePerformance(title);
                            a.addPerformance(newTitle, newType);
                            System.out.println("Performance updated!");
                            return;
                        }
                    }
                }
            }
        } else {
            System.out.println("You don't have permission to delete this actor.");
        }

    }

    public abstract int compareTo(@NotNull Object o);
}

