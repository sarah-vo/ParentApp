package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {
    String name;
    Bitmap portrait;

    public Child(String name){
        this.name = name;
    }

    public Bitmap getPortrait(){return portrait;}

    public void setPortrait(Bitmap newPortrait){portrait = newPortrait;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void editName(String newName){
        name = newName;
    }
}
