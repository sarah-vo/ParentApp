package ca.cmpt276.parentapp.model;

/**
 * Represent history of a task in the app. Each history is an object.
 */

public class History {

    private int childIndex;
    private final String lastTurnDate;

    public History(int childIndex, String lastTurnDate) {
        this.childIndex = childIndex;
        this.lastTurnDate = lastTurnDate;
    }

    public int getChildIndex() {
        return this.childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

    public String getLastTurnDate() {
        return lastTurnDate;
    }
}
