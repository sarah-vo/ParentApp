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

    private int currentIndex;

    private FlipCoinManager(){
        flipCoinGameList = new ArrayList<FlipCoin>();
        currentIndex = -1;
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


    //numChildren = current number of children saved in the app
    public Child getCurrentPlayer() {
        return childrenList.get(0);
    }

    public void updateQueue(){
        if (childrenList != null && childrenList.size() > 0) {
            Child child = childrenList.get(0);
            childrenList.remove(0);
            childrenList.add(child);
        }
    }

    public void shufflePlayer(){
        if (childrenList != null){
            Collections.shuffle(childrenList);
        }
    }

    /*public int updateIndex(int numChildren){
        if (currentIndex == -1) {
            Random rand = new Random();
            currentIndex = rand.nextInt(numChildren);
        }
        else {
            currentIndex = (currentIndex + 1) % numChildren;
        }
        return currentIndex;
    }*/

    public void resetIndex(){
        currentIndex = -1;
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
