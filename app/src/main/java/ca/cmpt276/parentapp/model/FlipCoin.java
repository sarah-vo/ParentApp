package ca.cmpt276.parentapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ca.cmpt276.parentapp.R;

public class FlipCoin {
    private ArrayList<Child> childrenList;
    private Date flipTime;
    private Child picker;
    private CoinSide flipResult;
    private CoinSide pickerChoice;
    private boolean isPickerWinner;

    public enum CoinSide {
        HEADS,
        TAILS
    }

    public String printFinalResults() {
        String results = flipResult.toString() + " was the results. " + picker.getName();
        if (isPickerWinner) {
            results += " won.";
        } else {
            results += " lost.";
        }
        return results;
    }

    public FlipCoin(){};

    public FlipCoin(ArrayList<Child> childrenList){
        this.flipTime = Calendar.getInstance().getTime();
        Random rand = new Random();
        this.childrenList = childrenList;
    }

    public String getTime() {return flipTime.toString();}

    public int getFlippedCoin() {
        if(flipResult.toString().equals("HEADS")){
            return R.drawable.loonie_heads;
        }
        else{
            return R.drawable.loonie_tails;
        }
    }

    public Date getFlipTime() {
        return flipTime;
    }

    public void setPicker(int index){
        picker = childrenList.get(index);
    }

    public Child getPicker(){
        return picker;
    }

    public CoinSide getFlipResult(){
        if (flipResult == null){
            Log.i("inside", "null");
            return null;
        }

        Log.i("outside", "null");
        return flipResult;
    }

    public boolean isPickerWinner() {
        return isPickerWinner;
    }

    public void setWinner(boolean hasWon){
        isPickerWinner = hasWon;
    }

    public void setPickerChoice(CoinSide coinside){
        this.pickerChoice = coinside;
    }

    public void setFlipResult(CoinSide flipResult){
        this.flipResult = flipResult;
    }


    public void updateResult() {
        isPickerWinner = pickerChoice == flipResult;
    }

    public void addChild(String newName){
        childrenList.add(new Child(newName));
    }
    public void removeChild(int childIndex){
        childrenList.remove(childIndex);
    }

    //For testing
    public CoinSide flipCoin(){
        return CoinSide.values()[new Random().nextInt(2)];
    }


}
