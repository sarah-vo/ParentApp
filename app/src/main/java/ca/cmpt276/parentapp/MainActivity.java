package ca.cmpt276.parentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.gson.Gson;

import ca.cmpt276.parentapp.breath.breathActivity;
import ca.cmpt276.parentapp.flipcoin.FlipCoinActivity;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.child_config.ConfigActivity;
import ca.cmpt276.parentapp.takebreath.TakeBreath;
import ca.cmpt276.parentapp.model.TaskManager;
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
        switchActivityTakeBreath();
    }

    private void getChildrenListFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("Child List",null);

        ChildManager childrenManager = gson.fromJson(json, ChildManager.class);
        ChildManager.setInstance(childrenManager);
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
        Button flipCoinButton = findViewById(R.id.settingsButton);
        flipCoinButton.setOnClickListener(View -> {
            Intent intent = new Intent(this, ConfigActivity.class);
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

    private void switchActivityTakeBreath() {
        Button takeBreathButton = findViewById(R.id.takeBreathButton);
        takeBreathButton.setOnClickListener(View -> {
            //Intent intent = new Intent(this, TakeBreath.class);
            Intent intent = new Intent(this, breathActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_button:
                Intent intent = new Intent(this, HelpScreenActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
