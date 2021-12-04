package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;

public class History {
    private Child child;
    private String lastTurnDate;

    public History(Child child, String lastTurnDate) {
        this.child = child;
        this.lastTurnDate = lastTurnDate;
    }

    public Child getChild() {
        return child;
    }
}
