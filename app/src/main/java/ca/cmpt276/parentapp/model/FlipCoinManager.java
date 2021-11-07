package ca.cmpt276.parentapp.model;

import java.util.ArrayList;
import java.util.Random;


public class FlipCoinManager {
    private static FlipCoinManager instance;
    private ArrayList<FlipCoin> flipCoinGameList;
    private int currentIndex;

    private FlipCoinManager(){
        flipCoinGameList = new ArrayList<FlipCoin>();
        currentIndex = -1;
    }

    public static FlipCoinManager getInstance(){
        if (instance == null){
            instance = new FlipCoinManager();
        }
        return instance;
    }

    // numChildren = current number of children saved in the app
    public int getCurrentIndex(int numChildren) {
        if (currentIndex == -1) {
            updateIndex(numChildren);
        }
        return currentIndex;
    }

    public int updateIndex(int numChildren){
        if (currentIndex == -1) {
            Random rand = new Random();
            currentIndex = rand.nextInt(numChildren);
        }
        else {
            currentIndex = (currentIndex + 1) % numChildren;
        }
        return currentIndex;
    }



    public void addGame(FlipCoin flipCoin){
        flipCoinGameList.add(flipCoin);
    }

    public int getNumGames(){
        return flipCoinGameList.size();
    }

    public FlipCoin getGame(int index){
        return flipCoinGameList.get(index);
    }

    public ArrayList<FlipCoin> getListGames(){
        return flipCoinGameList;
    }

}
