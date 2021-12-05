package ca.cmpt276.parentapp.model;

public class History {

    private int childIndex = -1;
    private String lastTurnDate;

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
