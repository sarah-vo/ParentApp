package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

public  class childManager {
    private static childManager instance;
    private ArrayList<Child> childrenList = new ArrayList<>();
    public static childManager getInstance(){
        if(instance == null){
            instance = new childManager();
        }
            return instance;
    }


    public ArrayList<Child> getChildList(){return childrenList;}

    public void addChildren(String name){
        childrenList.add(new Child(name));
    }

    public void removeChildren(int index){
        childrenList.remove(index);
    }

    public void editChildren(String name, int index){
        childrenList.get(index).editName(name);
    }
}



