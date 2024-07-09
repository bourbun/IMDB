package org.example;

import java.util.List;
import java.util.Map;

public class Actor implements Sortable {

    public String name;
    private Map<String, String> roles; // Key: Nume film/serial, Value: Tip (Movie/Series)
    private String biography;

    public Actor(String name, Map<String, String> roles, String biography) {
        this.name = name;
        this.roles = roles;
        this.biography = biography;
    }


    @Override
    public String getSortableIdentifier() {
        return this.name;
    }

    @Override
    public int compareTo(Sortable o) {
        return this.getSortableIdentifier().compareTo(o.getSortableIdentifier());
    }
    // Getteri și setteri
    public String getName() {
        return name;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }


    public void displayInfo() {
        System.out.println("Name: " + this.name);
        System.out.println("Biography: " + this.biography);
        System.out.println("Roles: ");
        for (Map.Entry<String, String> entry : roles.entrySet()) {
            System.out.println("\t" + entry.getKey() + " - " + entry.getValue());
        }
    }

    public void addPerformance(String title, String type) {
        this.roles.put(title, type);
    }

    public void deletePerformance(String title) {
        this.roles.remove(title);
    }

    public void updatePerformance(String title, String newTitle, String newType) {
        this.roles.remove(title);
        this.roles.put(newTitle, newType);
    }

    private List<Observer> observers = IMDB.getInstance().getObservers();
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

	public String getRolesAsString() {
        String rolesAsString = "";
        for (Map.Entry<String, String> entry : roles.entrySet()) {
            rolesAsString += entry.getKey() + " - " + entry.getValue() + "\n";
        }
        return rolesAsString;
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message, User user) {
        for (Observer observer : observers) {
            observer.update(message);
        }
	}

    @Override
    public String toString() {
        return this.name;
    }

    public String getDetails() {
        return this.biography;
    }

    public void addRole(Map<String, String> roles) {
        this.roles = roles;
    }

    public void deleteRole(String role) {
        this.roles.remove(role);
    }
}

