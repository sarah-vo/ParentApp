package ca.cmpt276.parentapp.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FlipCoin {
    public enum CoinSide {
        HEADS,
        TAILS
    }

    public static FlipCoin instance;
    ArrayList<Child> child_list = new ArrayList<>();

    Child winner;
    int current_index = -1;

    public FlipCoin(ArrayList<Child> child_list){
        this.child_list = child_list;
        updateIndex();
    }

    public CoinSide flipCoin(){
        Random rand = new Random();
        return CoinSide.values()[rand.nextInt(2)];
    }

    public void updateIndex(){
        if(child_list == null || getNumChild() == 0){
            throw new NullPointerException("No children list found");
        }

        //if first time flip
        if (current_index == -1){
            current_index = ThreadLocalRandom.current().nextInt(0, getNumChild());
        }

        else {
            //get next index in round-robin if children exist
            current_index += 1 % child_list.size();
        }
    }

    public boolean isChildExist(Child child){
        return child_list.contains(child);
    }

    public int getNumChild(){
        return child_list.size();
    }

    public Child getCurrentChild(){
        if(child_list == null){
            return null;
        }

        return child_list.get(current_index);
    }

    public void setWinner(Child child){
        if (!isChildExist(child)){
            throw new IllegalArgumentException("Child does not Exist!");
        }

        winner = child;
    }

    public Child getWinner(){
        if (winner == null){
            throw new NullPointerException("No winner yet!");
        }

        return winner;
    }
}
