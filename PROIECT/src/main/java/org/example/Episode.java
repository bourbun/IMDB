package org.example;

public class Episode {
    private String name;
    private int duration;

    public Episode(String episodeName, int duration) {
        this.name = episodeName;
        this.duration = duration;
    }

	public String getTitle() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	// Alte detalii
}

