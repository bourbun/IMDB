package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Production implements Sortable, Subject {
    protected String title;
    protected List<String> directors;

    protected List<String> actors;
    protected List<Genre> genres;
    protected List<Rating> ratings;
    protected String description;
    protected Double averageRating;



    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, Double averageRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.averageRating = averageRating;
    }



    public abstract void displayInfo();


    @Override
    public String getSortableIdentifier() {
        return this.title;
    }

    @Override
    public int compareTo(Sortable o) {
        return this.getSortableIdentifier().compareTo(o.getSortableIdentifier());
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return description;
    }

    public Rating getRatingByUser(String name) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(name)) {
                return rating;
            }
        }
        return null;
    }

    public Collection<? extends Rating> getRatings() {
        return ratings;
    }
    private List<Observer> observers = IMDB.getInstance().getObservers();

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

    public void addRating(Rating rating) {
        // ... adăugare rating ...
        String message = "Filmul \"" + this.getTitle() + "\" a primit un nou review.";
        notifyObservers(message);
    }

    public void deleteDirector(String director) {
        this.directors.remove(director);
    }

    public void deleteActor(String actor) {
        this.actors.remove(actor);
    }

    public List<Genre> deleteGenre(String genre) {
        for (Genre g : this.genres) {
            if (g.getName().equals(genre)) {
                this.genres.remove(g);
                return this.genres;
            }
        }
        return null;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public String[] getDirectors() {
        return directors.toArray(new String[0]);
    }
    @Override
    public String toString() {
        return this.getTitle(); // Presupunând că există o metodă getTitle() care returnează titlul producției.
    }
}

