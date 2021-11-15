package ca.cmpt276.parentapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Store a list of FlipCoin objects. Supports singleton access and includes methods to
 * edit and retrieve elements from the list. Also store a currentIndex variable use to
 * indicate the index of the picking child.
 */
public class FlipCoinManager {
    private static FlipCoinManager instance;
    private final ArrayList<FlipCoin> flipCoinGameList;
    private ArrayList<Child> childrenList;

    private boolean epoch;
    private boolean override_default_empty;

    private FlipCoinManager(){
        flipCoinGameList = new ArrayList<FlipCoin>();
        override_default_empty = false;
    }

    ///--------------------------Functions for instances-------------------------///
    public static void setInstance(FlipCoinManager new_instance){
        instance = new_instance;
    }

    public static FlipCoinManager getInstance(){
        if (instance == null){
            instance = new FlipCoinManager();
        }
        return instance;
    }

    ///--------------------------Functions for updating Queue-------------------------///

    public void setPlayerList(ArrayList<Child> childrenList){
        this.childrenList = childrenList;
    }

    public ArrayList<Child> getPlayerList(){
        return childrenList;
    }

    public int getNumPlayer(){
        return childrenList.size();
    }

    public boolean isEmpty(){
        return childrenList.size() == 0;
    }

    //numChildren = current number of children saved in the app
    public Child getCurrentPlayer() {
        if(childrenList == null || override_default_empty){
            return null;
        }
        return childrenList.get(0);
    }

    public void updateQueue(){
        if (childrenList != null && childrenList.size() > 0) {
            if (override_default_empty){
                override_default_empty = false;
                return;
            }
            Child child = childrenList.get(0);
            childrenList.remove(0);
            childrenList.add(child);
        }
    }

    public void overrideDefault(int index){

        if(childrenList == null || index < 0 || index >= childrenList.size()){
            throw new IndexOutOfBoundsException();
        }

        Child child = childrenList.get(index);
        childrenList.remove(index);
        childrenList.add(0, child);
    }

    public void overrideDefault(Child child){
        if (!childrenList.contains(child)){
            throw new IllegalArgumentException("Child does not exist!");
        }
        childrenList.remove(child);
        childrenList.add(0, child);
    }

    public void shufflePlayer(){
        if (childrenList != null){
            Collections.shuffle(childrenList);
        }
    }

    public void setDefaultEmpty(){
        override_default_empty = true;
    }

    public boolean isOverrideDefaultEmpty(){
        return override_default_empty;
    }

    public void resetEpoch(){
        epoch = true;
    }

    public void updateEpoch(){
        epoch = false;
    }

    public boolean isNewEpoch(){
        return epoch;
    }

    ///--------------------------Functions to update Game-------------------------///

    public void addGame(FlipCoin flipCoin){
        flipCoinGameList.add(flipCoin);
    }

    public FlipCoin getGame(int index){
        return flipCoinGameList.get(index);
    }

    public ArrayList<FlipCoin> getListGames(){
        return flipCoinGameList;
    }

    public int getNumGames(){
        return flipCoinGameList.size();
    }

}
