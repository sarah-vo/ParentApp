package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * A list of Child objects which stores all children configured in the app.
 */

public  class ChildManager {
    private static ChildManager instance;
    private final ArrayList<Child> childrenList;
    public ArrayList<String> childrenName = new ArrayList<>();

    public static ChildManager getInstance(){
        if(instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public static void setInstance(ChildManager manager){
        instance = manager;
    }

    public ChildManager(){
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



