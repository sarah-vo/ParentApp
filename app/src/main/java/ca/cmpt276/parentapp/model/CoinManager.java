package ca.cmpt276.parentapp.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CoinManager {
    public static CoinManager instance;

    FlipCoin coin = new FlipCoin();
    ArrayList<Child> child_list = new ArrayList<>();

    int current_index = -1;

    public static CoinManager getInstance(){
        if (instance == null){
            instance = new CoinManager();
        }
        return instance;
    }

    public FlipCoin.CoinSide flipCoin(){
        coin.flipCoin();

        //get next index in round-robin if children exist
        if (child_list != null && child_list.size() != 0){
            current_index += 1 % child_list.size();
        }

        return coin.getSide();
    }

    public int getNumChild(){
        return child_list.size();
    }

    public Child getCurrentChild(){

        if(child_list == null){
            return null;
        }
        //if first time flip
        if (current_index == -1){
            current_index = ThreadLocalRandom.current().nextInt(0, getNumChild());
        }

        return child_list.get(current_index);
    }


}
