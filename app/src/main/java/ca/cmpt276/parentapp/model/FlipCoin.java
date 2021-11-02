package ca.cmpt276.parentapp.model;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FlipCoin {
    enum CoinSide {
        HEAD,
        TAIL
    }
    CoinSide pickerChoice;

    public FlipCoin() { }

    public CoinSide flipCoin(){
        int flip_output = ThreadLocalRandom.current().nextInt(1, 3);

        if (flip_output == 1){
            pickerChoice = CoinSide.HEAD;
        }
        else{
            pickerChoice = CoinSide.TAIL;
        }

        return pickerChoice;
    }

    public CoinSide getSide(){
        return pickerChoice;
    }
}
