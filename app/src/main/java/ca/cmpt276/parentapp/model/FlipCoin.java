package ca.cmpt276.parentapp.model;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FlipCoin {
    int lastPickChildIndex;
    private static FlipCoin instance;
    public enum CoinSide {
        HEADS,
        TAILS
    }
    CoinSide pickerChoice;

    private FlipCoin() {
        lastPickChildIndex = -1;
    }

    public static FlipCoin getInstance() {
        if (instance == null) {
            instance = new FlipCoin();
        }
        return instance;
    }

    public int getLastPickChildIndex() {
        return lastPickChildIndex;
    }

    public CoinSide getPickerChoice() {
        return pickerChoice;
    }

    public void setPickerChoice(CoinSide pickerChoice) {
        this.pickerChoice = pickerChoice;
    }

    public String generateCurrentPickChild(List<String> childrenList) {
        int numChildren = childrenList.size();
        int currentPickChildIndex;
        if (lastPickChildIndex == -1) { // First time flip
            Random rand = new Random();
            currentPickChildIndex = rand.nextInt(numChildren);
        }
        else {
            currentPickChildIndex = lastPickChildIndex + 1;
            if (currentPickChildIndex >= childrenList.size()){
                currentPickChildIndex = 0;
            }
        }

        lastPickChildIndex = currentPickChildIndex;

        return childrenList.get(currentPickChildIndex);
    }

    public CoinSide generateFlipResult() {
        Random rand = new Random();
        return CoinSide.values()[rand.nextInt(2)];
    }
}
