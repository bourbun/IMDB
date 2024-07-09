package org.example;

public interface Sortable extends Comparable<Sortable> {
    String getSortableIdentifier();

    void addObserver(Observer o);

	void removeObserver(Observer o);

	void notifyObservers(String message,User user);
}


