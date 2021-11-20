package ca.cmpt276.parentapp.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ca.cmpt276.parentapp.R;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {
    String childName;
    String portraitPath;
    Resources resources;

    public Child(String childName) {
        this.childName = childName;
    }

    public Child(String childName, String newPortraitPath, Resources newResources) {
        this.childName = childName;
        portraitPath = newPortraitPath;
        resources = newResources;
    }

    public Bitmap getPortrait() {
            if(portraitPath != null){
                return BitmapFactory.decodeFile(portraitPath);
            }
            else{
                return BitmapFactory.decodeResource(resources, R.drawable.default_portrait);
            }
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
}
