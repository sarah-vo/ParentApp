package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * A list of Child objects which stores all children configured in the app.
 */

public  class childManager {
    private static final int EMPTY_CHILD_LIST = -999;
    private static childManager instance;
    private final ArrayList<Child> childrenList;
    //public ArrayList<String> childrenName = new ArrayList<>();

    public static childManager getInstance(){
        if(instance == null){
            instance = new childManager();
        }
        return instance;
    }

    public static void setInstance(childManager manager){
        instance = manager;
    }

    public childManager(){
        childrenList = new ArrayList<>();
    }

    public ArrayList<Child> getChildList(){return childrenList;}

    public void addChildren(String name){
        childrenList.add(new Child(name));
    }

    public void addChildren(String name, String newPhotoPath){
        childrenList.add(new Child(name, newPhotoPath));
    }

    public void removeChildren(int index){
        childrenList.remove(index);
    }


    public void editChildrenName(String name, int index){
        childrenList.get(index).editName(name);
    }

    public void editChildrenPortraitPath(String newPath, int position){
        childrenList.get(position).setPortrait(newPath);
    }

    public Child getChild(int pos){return childrenList.get(pos);}

    public int getChildPosition(){
        if (!childrenList.isEmpty()){
            return childrenList.size()-1;
        }
        else{
            return EMPTY_CHILD_LIST;
        }
    }
}



