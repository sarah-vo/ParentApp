package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {
    String childName;
    String portraitPath;
    boolean isDefaultPicture;

    public Child(String childName) {
        this.childName = childName;
    }

    public Child(String childName, String newPortraitPath, boolean isDefaultPicture) {
        this.childName = childName;
        portraitPath = newPortraitPath;
        this.isDefaultPicture = isDefaultPicture;
    }

    public Bitmap getPortrait() {
        return BitmapFactory.decodeFile(portraitPath);
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortrait(String newPortraitPath) {
        portraitPath = newPortraitPath;
    }

    public String getChildName() {
        return this.childName;
    }

    public void childName(String newName) {
        childName = newName;
    }
    public boolean isDefaultPicture() {
        return isDefaultPicture;
    }

    public void setDefaultPicture(boolean defaultPicture) {
        isDefaultPicture = defaultPicture;
    }
}
