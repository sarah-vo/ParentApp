package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {
    String name;
    String portraitPath;

    public Child(String newName){
        this.name = newName;
    }

    public Child(String newName, String newPortraitPath){
        name = newName;
        portraitPath = newPortraitPath;
    }

    public Bitmap getPortrait(){
        return BitmapFactory.decodeFile(portraitPath);}

    public String getPortraitPath(){return portraitPath;}

    public void setPortrait(String newPortraitPath){portraitPath = newPortraitPath;}

    public String getName(){
        return this.name;
    }

    public void editName(String newName){
        name = newName;
    }
}
