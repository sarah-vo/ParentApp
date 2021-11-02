package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        switchActivityFlipCoin();
        switchActivityTimeoutTimer();
//        switchActivitySettings();
    }
/*
    void switchActivityFlipCoin(){
        Button flipCoinButton = findViewById(R.id.coinFlipButton);
        /*flipCoinButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, flipCoin.class);//TODO: ADD CLASS HERE
            startActivity(intent);
        });*/

    void switchActivityTimeoutTimer(){
        Button flipCoinButton = findViewById(R.id.timeoutTimerButton);
        flipCoinButton.setOnClickListener(View -> {
            Intent intent = TimerActivity.makeIntent(this);
            startActivity(intent);
        });
    }

    void switchActivitySettings(){
        /*Button flipCoinButton = findViewById(R.id.settingsButton);
        flipCoinButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, settings.class);//TODO: ADD CLASS HERE
            startActivity(intent);
        });*/
    }

}