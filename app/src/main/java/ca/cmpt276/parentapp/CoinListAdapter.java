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
        FlipCoin currentGame = getItem(position);

        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.history_textview, parent, false);
        }

        if (currentGame.getFlipResult() != null){
            // Result:
            TextView gameResult =  itemView.findViewById(R.id.gameResult);
            gameResult.setText(currentGame.printFinalResults());

            // P1 info
            ImageView coinDetails =  itemView.findViewById(R.id.coinDetails);
            coinDetails.setImageResource(currentGame.getFlippedCoin());

            TextView yearText =  itemView.findViewById(R.id.time);
            yearText.setText(currentGame.getTime());
        }

        return itemView;
    }
}
