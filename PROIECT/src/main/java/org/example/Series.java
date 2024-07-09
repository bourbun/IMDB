package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Series extends Production {
    private int releaseYear;
    private int numberOfSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, Double averageRating, int releaseYear, int numberOfSeasons, Map<String, List<Episode>> seasons) {
        super(title, directors, actors, genres, ratings, description, averageRating);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    @Override
    public void displayInfo() {
        System.out.println("Release year: " + this.releaseYear);
        System.out.println("Number of seasons: " + this.numberOfSeasons);
    }

    //returneaza sezoanele
    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }
    //returneaza episoadele
    public List<Episode> getEpisodes(String season) {
        return seasons.get(season);
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

    public void setReleaseYear(int i) {
        this.releaseYear = i;
    }

    public void setNumberOfSeasons(int i) {
        this.numberOfSeasons = i;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
	}

    public void addEpisode(String season, Episode newEpisode) {
        this.seasons.get(season).add(newEpisode);
    }

    public void deleteEpisode(String season, String episode) {
        // get the episodes from the season
        List<Episode> episodes = this.seasons.get(season);
        // iterate through the episodes
        for (Episode e : episodes) {
            // if the episode title matches the one we want to delete
            if (e.getTitle().equals(episode)) {
                // remove it
                episodes.remove(e);
                break;
            }
        }
    }

    @Override
    public void notifyObservers(String message, User user) {

    }
}

