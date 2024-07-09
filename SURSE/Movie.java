package org.example;

import java.util.List;

public class Movie extends Production {
    int duration;
    int releaseYear;

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, Double averageRating, int duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, description, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }




    @Override
    public void displayInfo() {
        System.out.println("Duration: " + this.duration + " minutes");
        System.out.println("Release year: " + this.releaseYear);
        //print all ratings
        System.out.println("Ratings: ");
        for (Rating rating : ratings) {
            System.out.println(rating.getUsername() + " " + rating.getScore() + " " + rating.getComments());
        }
    }


	public void setTitle(String newTitle) {
        this.title = newTitle;
	}

    public void setDirectors(String[] directors) {
        this.directors = List.of(directors);
    }

    public void setActors(String[] actors) {
        this.actors = List.of(actors);
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public void setReleaseYear(int i) {
        this.releaseYear = i;
    }

    @Override
    public void notifyObservers(String message, User user) {
    }
}

