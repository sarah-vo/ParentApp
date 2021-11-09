package ca.cmpt276.parentapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Model of flip coin activity. Each time user start the activity an object is created which
 * store relative information with it.
 */

public class FlipCoin {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final String localDateTime_formatted = DateTimeFormatter.ofPattern("MMM dd @ HH:mm a")
                                                .format(localDateTime);

    private ArrayList<Child> childrenList;
    private Child picker;
    private CoinSide flipResult;
    private CoinSide pickerChoice;
    private boolean isPickerWinner;

    public enum CoinSide {
        HEADS,
        TAILS
    }

    public FlipCoin(){};

    public FlipCoin(ArrayList<Child> childrenList){
        this.childrenList = childrenList;
    }

    public String getCreatedTime() {return localDateTime_formatted;}

    ///--------------------------Functions to update player Information-------------------------///

    public void setPicker(int index){
        picker = childrenList.get(index);
    }

    public void setPicker(Child picker){
        this.picker = picker;
    }

    public Child getPicker(){
        return picker;
    }

    public void setPickerChoice(CoinSide coinside){
        this.pickerChoice = coinside;
    }

    ///-----------------------Functions updating the flip coin Results-------------------------///

    //Flip the coin and automatically store and update the result
    public CoinSide flipCoin(){
        flipResult = CoinSide.values()[new Random().nextInt(2)];

        if(pickerChoice != null){
            updateResult();
        }
        return flipResult;
    }

    public void setFlipResult(CoinSide flipResult){
        this.flipResult = flipResult;

        if(pickerChoice != null){
            updateResult();
        }
    }

    public CoinSide getFlipResult(){
        if (flipResult == null){
            return null;
        }
        return flipResult;
    }

    public boolean isPickerWinner() {
        return isPickerWinner;
    }

    public void updateResult() {
        isPickerWinner = pickerChoice == flipResult;
    }


}
