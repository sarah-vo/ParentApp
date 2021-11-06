package ca.cmpt276.parentapp.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ca.cmpt276.parentapp.R;

public class FlipCoin {
    public String printFinalResults() {
        String results = flipResult.toString() + " was the results. " + picker.getName();
        if (isPickerWinner) {
            results += " won.";
        } else {
            results += " lost.";
        }
        return results;
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

    public enum CoinSide {
        HEADS,
        TAILS
    }
    private ArrayList<Child> childrenList;
    private Date flipTime;
    private Child picker;
    private CoinSide flipResult;
    private boolean isPickerWinner;

    public FlipCoin(ArrayList<Child> childrenList){
        this.flipTime = Calendar.getInstance().getTime();
        Random rand = new Random();
        this.flipResult = CoinSide.values()[rand.nextInt(2)];
        this.childrenList = childrenList;
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
        return flipResult;
    }

    public boolean isPickerWinner() {
        return isPickerWinner;
    }

    public void setIsPickerWinner(CoinSide pickerChoice) {
        isPickerWinner = flipResult == pickerChoice;
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
