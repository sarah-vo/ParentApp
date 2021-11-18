package ca.cmpt276.parentapp.model;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {
    String name;

    public Child(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void editName(String newName) {
        name = newName;
    }
}
