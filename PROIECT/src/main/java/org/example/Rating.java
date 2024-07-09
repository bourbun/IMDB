package org.example;

public class Rating {
    private String username;
    private int score;
    private String comments;

    public Rating(String username, int score, String comments) {
        this.username = username;
        this.score = score;
        this.comments = comments;
    }

    public Object getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public Object getComments() {
        return comments;
    }

    public void setScore(int rating) {
        this.score = rating;
    }

    public void setComments(String comment) {
        this.comments = comment;
    }

	public Rating returnRating() {
        return this;
    }
}


