package ca.cmpt276.parentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.cmpt276.parentapp.model.FlipCoin;

public class CoinListAdapter extends ArrayAdapter<FlipCoin> {

    public CoinListAdapter(Context context, ArrayList<FlipCoin> coinList){
        super(context, R.layout.activity_flip_coin_history, coinList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FlipCoin currentGame = getItem(getCount() - 1 - position);

        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.history_textview, parent, false);
        }

        if (currentGame.getFlipResult() != null){
            // Result:
            TextView gameResult =  itemView.findViewById(R.id.gameResult);
            gameResult.setText(formatResult(currentGame));

            // P1 info
            ImageView coinDetails =  itemView.findViewById(R.id.coinDetails);
            coinDetails.setImageResource(getCoinImageId(currentGame));

            TextView creationTime =  itemView.findViewById(R.id.time);
            creationTime.setText(currentGame.getCreatedTime());

            ImageView result_img = itemView.findViewById(R.id.result_img);
            result_img.setImageResource(getResultIconId(currentGame));
        }

        return itemView;
    }

    private String formatResult(FlipCoin currentGame) {

        return "Result: " + currentGame.getFlipResult().toString()+
                         "\nPicker: " + currentGame.getPicker().getName();
    }

    private int getCoinImageId(FlipCoin currentGame){
        if(currentGame.getFlipResult() == FlipCoin.CoinSide.HEADS){
            return R.drawable.loonie_heads;
        }
        else{
            return R.drawable.loonie_tails;
        }
    }

    private int getResultIconId(FlipCoin currentGame){
        if(currentGame.isPickerWinner()){
            return R.drawable.tick_icon;
        }
        else{
            return R.drawable.x_icon;
        }

    }
}
