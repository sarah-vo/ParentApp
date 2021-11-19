package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ca.cmpt276.parentapp.configurechildren.ChildConfigurationActivity;
import ca.cmpt276.parentapp.flipcoin.FlipCoinActivity;
import ca.cmpt276.parentapp.timer.TimerActivity;
import ca.cmpt276.parentapp.whoseturn.WhoseTurnActivity;

/**
 * Generate layout for the main screen of the app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchActivityFlipCoin();
        switchActivityTimeoutTimer();
        switchActivitySettings();
        switchActivityWhoseTurn();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void switchActivityFlipCoin(){
        Button flipCoinButton = findViewById(R.id.coinFlipButton);
        flipCoinButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, FlipCoinActivity.class);
            startActivity(intent);
        });
    }

    void switchActivityTimeoutTimer(){
        Button timerButton = findViewById(R.id.timeoutTimerButton);
        timerButton.setOnClickListener(View -> {
            Intent intent = TimerActivity.makeIntent(this);
            startActivity(intent);
        });
    }

    void switchActivitySettings(){
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, ChildConfigurationActivity.class);
            startActivity(intent);
        });
    }

    void switchActivityWhoseTurn() {
        Button btnWhoseTurn = findViewById(R.id.button);
        btnWhoseTurn.setOnClickListener(View -> {
            Intent intent = new Intent(this, WhoseTurnActivity.class);
            startActivity(intent);
        });
    }

}