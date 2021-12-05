package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * A list of Child objects which stores all children configured in the app.
 */

public  class ChildManager {
    private static final int EMPTY_CHILD_LIST = -999;
    private static ChildManager instance;
    private final ArrayList<Child> childrenList;

    TaskManager taskManager = TaskManager.getInstance();
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

    public void addChildren(Child child) {
        childrenList.add(child);
    }

    public void removeChildren(int index) {
        childrenList.remove(index);
        taskManager.updateTaskHistoryList(index);
    }


    public void editChildrenName(String name, int index) {
        childrenList.get(index).childName(name);
    }

    public void editChildrenByteString(String byteString, int position){
        childrenList.get(position).saveBitmapString(byteString);
    }

    public Child getChild(int pos) {
        if (pos >= childrenList.size() || pos < 0) {
            return null;
        }
        return childrenList.get(pos);
    }

    public int getLatestChildPosition() {
        if (!childrenList.isEmpty()) {
            return childrenList.size()-1;
        }
        else{
            return EMPTY_CHILD_LIST;
        }
    }

    public int getNumberOfChildren(){
        return childrenList.size();
    }
}



