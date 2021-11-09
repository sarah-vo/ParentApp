package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * A list of Child objects which stores all children configured in the app.
 */

public  class childManager {
    private static childManager instance;
    private final ArrayList<Child> childrenList;
    public ArrayList<String> childrenName = new ArrayList<>();

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
        childrenName.add(name);
    }

    public void removeChildren(int index){
        childrenList.remove(index);
        childrenName.remove(index);
    }

    public ArrayList<String> getChildrenNameList(){
        return childrenName;
    }

    public void editChildren(String name, int index){
        childrenList.get(index).editName(name);
        childrenName.set(index, name);
    }

    public String getName(int pos) {
        return childrenName.get(pos);
    }
}



