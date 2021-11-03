package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

public class FlipCoinManager {
    public static FlipCoinManager instance;
    ArrayList<FlipCoin> flipCoin_list = new ArrayList<>();

    //Sets the instance to the provided instance
    public static void setInstance(FlipCoinManager new_instance){
        instance = new_instance;
    }

    public static FlipCoinManager getInstance(){
        if (instance == null){
            instance = new FlipCoinManager();
        }
        return instance;
    }

    public void addGame(FlipCoin flip_coin){
        flipCoin_list.add(flip_coin);
    }

    public void removeGame(int index){
        flipCoin_list.remove(index);
    }

    public void removeGame(FlipCoin flipCoin){
        flipCoin_list.remove(flipCoin);
    }

    public void clearGame(){
        flipCoin_list.clear();
    }

    public int getNumGames(){
        return flipCoin_list.size();
    }

    public FlipCoin getGame(int index){
        return flipCoin_list.get(index);
    }


}
