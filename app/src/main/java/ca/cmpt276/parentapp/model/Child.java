package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Represent children in the app. Each child is an object.
 */

public class Child {

    String childName;
    String bytePhoto;

    public Child(){}

    public Child(String childName) {
        this.childName = childName;
    }

    public void setName(String name){
        this.childName = name;
    }

    public Bitmap getPortrait() {
        try {
            byte [] encodeByte= Base64.decode(bytePhoto,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String getPortraitString() {
        return bytePhoto;
    }

    public void saveBitmapString(Bitmap bitmap){
        ByteArrayOutputStream byteOutput = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteOutput);
        byte [] b = byteOutput.toByteArray();
        this.bytePhoto =  Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void saveBitmapString(String bytePhoto){
        this.bytePhoto =  bytePhoto;
    }

    public String getChildName() {
        return this.childName;
    }

    public void childName(String newName) {
        childName = newName;
    }

}
