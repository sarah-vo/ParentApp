package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {
    String name;
    Bitmap portrait = null;

    public Child(String newName){
        this.name = newName;
    }

    public Child(String newName, Bitmap newPortrait){name = newName; portrait = newPortrait;}

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
