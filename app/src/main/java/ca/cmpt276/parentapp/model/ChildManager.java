package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * A list of Child objects which stores all children configured in the app.
 */

public  class ChildManager {
    private static final int EMPTY_CHILD_LIST = -999;
    private static ChildManager instance;
    private final ArrayList<Child> childrenList;

    public static ChildManager getInstance() {
        if(instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public static void setInstance(ChildManager manager) {
        instance = manager;
    }

    public ChildManager() {
        childrenList = new ArrayList<>();
    }

    public ArrayList<Child> getChildList() {
        return childrenList;
    }


    public void addChildren(String name, String newPhotoPath, boolean isDefaultPicture) {
        childrenList.add(new Child(name, newPhotoPath, isDefaultPicture));
    }

    public void removeChildren(int index){
        childrenList.remove(index);
    }


    public void editChildrenName(String name, int index) {
        childrenList.get(index).childName(name);
    }

    public void editChildrenPortraitPath(String newPath, int position) {
        childrenList.get(position).setPortrait(newPath);
    }

    public Child getChild(int pos) {
        return childrenList.get(pos);
    }

    public int getChildPosition() {
        if (!childrenList.isEmpty()) {
            return childrenList.size()-1;
        }
        else{
            return EMPTY_CHILD_LIST;
        }
    }

//    public boolean isDefaultPicture(int pos){
//        return childrenList.get(pos).isDefaultPicture()]
//    }
//
//    public void setDefaultPicture(int pos, boolean newBoolean){
//        childrenList.get(pos).setDefaultPicture(newBoolean);
//    }
}



