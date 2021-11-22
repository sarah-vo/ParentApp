package ca.cmpt276.parentapp;

import static ca.cmpt276.parentapp.configurechildren.ChildConfigurationActivity.CHILD_LIST;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;

import ca.cmpt276.parentapp.configurechildren.ChildConfigurationActivity;
import ca.cmpt276.parentapp.flipcoin.FlipCoinActivity;
import ca.cmpt276.parentapp.model.ChildManager;
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

        getChildrenListFromSharedPreferences();
        switchActivityFlipCoin();
        switchActivityTimeoutTimer();
        switchActivitySettings();
        switchActivityWhoseTurn();
    }

    private void getChildrenListFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("Child List",null);

        ChildManager childrenManager = gson.fromJson(json, ChildManager.class);
        ChildManager.setInstance(childrenManager);

        if(childrenManager == null){
            childrenManager = ChildManager.getInstance();
        }
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
        Button whoseTurnButton = findViewById(R.id.taskButton);
        whoseTurnButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, WhoseTurnActivity.class);
            startActivity(intent);
        });
    }

}