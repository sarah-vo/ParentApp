package ca.cmpt276.parentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.parentapp.flipcoin.FlipCoinActivity;
import ca.cmpt276.parentapp.newConfig.configActivity;
import ca.cmpt276.parentapp.timer.TimerActivity;

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
        Button flipCoinButton = findViewById(R.id.timeoutTimerButton);
        flipCoinButton.setOnClickListener(View -> {
            Intent intent = TimerActivity.makeIntent(this);
            startActivity(intent);
        });
    }

    void switchActivitySettings(){
        Button flipCoinButton = findViewById(R.id.settingsButton);
        flipCoinButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, configActivity.class);
            startActivity(intent);
        });
    }

}