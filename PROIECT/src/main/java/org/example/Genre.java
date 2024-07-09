package org.example;

public enum Genre {
    Action, Adventure, Comedy,Cooking, Drama, Horror, SF, Fantasy, Romance, Mystery, Thriller, Crime, Biography, War;

    public Object getName() {
        return this.name();
    }

    // make a list of all genres
    public static Genre[] getGenres() {
        return Genre.values();
    }
}
