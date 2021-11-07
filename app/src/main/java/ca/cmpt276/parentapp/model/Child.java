package ca.cmpt276.parentapp.model;

public class Child {
    String name;

    public Child(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void editName(String newName){
        name = newName;
    }
}
