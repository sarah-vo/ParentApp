package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.cmpt276.parentapp.model.FlipCoin;
import ca.cmpt276.parentapp.model.FlipCoinManager;
import ca.cmpt276.parentapp.timer.TimerActivity;

public class FlipCoinHistory extends AppCompatActivity {
    ArrayList<FlipCoin> gameList = new ArrayList<>();
    FlipCoinManager manager = FlipCoinManager.getInstance();

    public static Intent makeIntent(Context context){
        return  new Intent(context, FlipCoinHistory.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_history);

        populateGameList();
    }

    private void populateGameList(){
        for(int i = 0; i < manager.getNumGames(); i++){
            FlipCoin tempGame = manager.getGame(i);
            gameList.add(tempGame);
        }

        //Building adapter
        ArrayAdapter<FlipCoin> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.gameListView);
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<FlipCoin> {
        public MyListAdapter(){
            super(FlipCoinHistory.this, R.layout.activity_flip_coin_history, gameList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.history_textview, parent, false);
            }
            FlipCoin currentGame = gameList.get(position);

            // Result:
            TextView gameResult =  itemView.findViewById(R.id.gameResult);
            gameResult.setText(currentGame.printFinalResults());

            // Coin Image
            ImageView coinDetails =  itemView.findViewById(R.id.coinDetails);
            coinDetails.setImageResource(currentGame.getFlippedCoin());

            // time:
            TextView yearText =  itemView.findViewById(R.id.time);
            yearText.setText(currentGame.getTime());

            return itemView;
        }
    }
}