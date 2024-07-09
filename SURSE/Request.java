package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Request implements Subject {
    private RequestTypes requestType;
    LocalDateTime creationDate;
    String productionTitle;
    String actorName;
    String description;
    private String creatorUsername;
    private String resolverUsername;

    public Request(RequestTypes requestType, LocalDateTime createdDate, String productionTitle, String actorName, String description, String creatorUsername, String resolverUsername) {
        this.requestType = requestType;
        this.creationDate = createdDate;
        this.productionTitle = productionTitle;
        this.actorName = actorName;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.resolverUsername = resolverUsername;
    }

    public RequestTypes getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypes requestType) {
        this.requestType = requestType;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getResolverUsername() {
        return resolverUsername;
    }

    public int getCreationDate() {
        return creationDate.getDayOfMonth();
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

    public String getDescription() {
        return description;
    }

    //toString method
    @Override
    public String toString() {
        return "Request Type: " + requestType + "\n" +
                "Creation Date: " + creationDate + "\n" +
                "Production Title: '" + productionTitle + "'\n" +
                "Actor Name: '" + actorName + "'\n" +
                "Description: '" + description + "'\n" +
                "Creator Username: '" + creatorUsername + "'\n" +
                "Resolver Username: '" + resolverUsername + "'";
    }

}
