package org.example;

public interface StaffInterface {
    void addProductionToSystem(Production p);
    void addActorToSystem(Actor a);
    void removeProductionFromSystem(String name);
    void removeActorFromSystem(String name);
    void updateProduction(Production p);
    void updateActor(Actor a);
}
