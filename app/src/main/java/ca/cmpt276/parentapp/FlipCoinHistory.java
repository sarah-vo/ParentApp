package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    FlipCoinManager manager;
    Toolbar toolbar;

    public static Intent makeIntent(Context context){
        return new Intent(context, FlipCoinHistory.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = FlipCoinManager.getInstance();
        this.setTitle("Coin Flips History");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager.getListGames() == null || manager.getNumGames() == 0){
            setContentView(R.layout.flip_coin_empty_history);
        }

        else {
            setContentView(R.layout.activity_flip_coin_history);
            populateGameList();
        }

        /*toolbar = findViewById(R.id.coin_history_toolbar);
        toolbar.setTitle("Flip Coin History");
        setSupportActionBar(toolbar);*/

    }

    private void populateGameList(){
        //Building adapter

        ArrayAdapter<FlipCoin> adapter = new CoinListAdapter(this,
                manager.getListGames());
        ListView list = findViewById(R.id.gameListView);
        list.setAdapter(adapter);
    }

}