package ca.cmpt276.parentapp.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import ca.cmpt276.parentapp.CustomButton;
import ca.cmpt276.parentapp.R;

/**
 *Timer layout that will update its layout based on the value of the Timer Service
 */
public class TimerActivity extends AppCompatActivity {

    private static final int MAX_HOUR = 24;
    private static final int MAX_MINUTE = 59;
    private static final int MAX_SECOND = 59;

    public static final String NOTIFY_ID = "Notification Channel ID for Timer";
    public static final String TIME_INITIAL_TAG = "INITIAL TIME TAG";
    public static final String TIME_LEFT_TAG = "INITIAL LEFT TAG";
    public static final String TIMER_SPEED_TAG = "TIMER SPEED TAG";

    ConstraintLayout timerLayout;

    private NumberPicker timerHour, timerMinute, timerSecond;
    private TextView hourText, minuteText, secondText;

    private TextView progressText, timerSpeedText;
    private ProgressBar timerBar;
    private Button pauseResumeButton, resetButton, startButton;

    TableLayout defaultTimeTable;
    ArrayList<Integer> defaultTimeList = new ArrayList<>();

    Intent serviceIntent;

    ArrayList<Integer> timerSpeedList;

    private int initialTime = 0;
    private int timeLeft = initialTime;
    private boolean isTimerRunning;
    private float timerSpeedFloat;

    private boolean menuHidden;

    public static Intent makeIntent(Context context){
        return  new Intent(context, TimerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timerLayout = findViewById(R.id.timerLayout);

        timerSpeedFloat = 1;
        timerSpeedText = findViewById(R.id.timer_speed_text);
        timerSpeedText.setText(getString(R.string.timer_speed, timerSpeedFloat));

        initializeTimerScroll();
        initializeProgressView();
        setUpTimerButtons();

        populateDefaultTimeButton();
        updateViewInterface();
        this.setTitle("Timeout Timer");

        timerSpeedList = new ArrayList<>();
        for (Integer speed : getResources().getIntArray(R.array.timer_speed)){
            timerSpeedList.add(speed);
        }

        menuHidden = true;

        //Check if a timer service exist and on pause state
        if (isTimerServiceRunning() && TimerService.isPaused){
            timeLeft = TimerService.timerIntent.getIntExtra(TimerService.TIME_LEFT_SERVICE_TAG,3000);
            initialTime = TimerService.timerIntent.getIntExtra(TimerService.TIME_INITIAL_SERVICE_TAG,9000);
            isTimerRunning = false;

            pauseResumeButton.setText(getString(R.string.resume));

            timerBar.setMax(initialTime);
            timerBar.setProgress(Math.abs(timeLeft - initialTime));

            updateProgressText();
            updateViewInterface();
        }
    }

    //Create a broadcast receiver
    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isTimerServiceRunning() || TimerService.willServiceDestroy){
                isTimerRunning = false;
                pauseResumeButton.setText(getString(R.string.resume));
                timeLeft = initialTime;
                timerBar.setProgress(0);
                updateProgressText();
            }
            else{
                updateGUI(intent);
                isTimerRunning = true;
            }
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (!menuHidden){
            if (timerSpeedList != null){
                for (int i = 0; i < timerSpeedList.size(); i++){
                    menu.add(0,i,Menu.NONE, timerSpeedList.get(i) + "%");
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        for (int i = 0; i < timerSpeedList.size(); i++){
            if (item.getItemId() == i){
                timerSpeedFloat = (float) timerSpeedList.get(i)/100;
                timerSpeedText.setText(getString(R.string.timer_speed, timerSpeedFloat));

                serviceIntent = new Intent(this, TimerService.class);
                serviceIntent.putExtra(TimerService.TIMER_CHANGE_SPEED_SERVICE_TAG, true);
                serviceIntent.putExtra(TIMER_SPEED_TAG, timerSpeedFloat);
                startForegroundService(serviceIntent);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(bReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(bReceiver,new IntentFilter(TimerService.COUNTDOWN_BR));
    }

    ///--------------------------Functions for initialization-------------------------///

    private void initializeTimerScroll(){
        hourText = findViewById(R.id.text_hour);
        minuteText = findViewById(R.id.text_minute);
        secondText = findViewById(R.id.text_second);

        timerHour = findViewById(R.id.timer_hour);
        timerMinute = findViewById(R.id.timer_minute);
        timerSecond = findViewById(R.id.timer_second);

        //Set the min and max values
        timerHour.setMinValue(0);
        timerHour.setMaxValue(MAX_HOUR);

        timerMinute.setMinValue(0);
        timerMinute.setMaxValue(MAX_MINUTE);

        timerSecond.setMinValue(0);
        timerSecond.setMaxValue(MAX_SECOND);

        //Initialize all initial values
        timerHour.setValue(0);
        timerMinute.setValue(0);
        timerSecond.setValue(1);

        timerSecond.setOnValueChangedListener((numberPicker, old_value, new_value) ->
                checkNewScrollValue(new_value));

        timerMinute.setOnValueChangedListener((numberPicker, old_value, new_value) ->
                checkNewScrollValue(new_value));

        timerHour.setOnValueChangedListener((numberPicker, old_value, new_value) ->
                checkNewScrollValue(new_value));
    }

    private void initializeProgressView(){
        timerBar = findViewById(R.id.timer_bar);
        progressText = findViewById(R.id.timer_progress_text);
    }

    private void setUpTimerButtons(){
        startButton = findViewById(R.id.timer_start);
        pauseResumeButton = findViewById(R.id.timer_pause_resume);
        resetButton = findViewById(R.id.timer_reset);

        startButton.setOnClickListener(view ->{
            setTime(getValueFromPicker());
            startTimer();
            pauseResumeButton.setText(R.string.pause);

            updateViewInterface();
        });

        pauseResumeButton.setOnClickListener(view -> {
            if (timeLeft != 0){
                if(!isTimerRunning){
                    Log.i("do start", "ok");
                    startTimer();
                    pauseResumeButton.setText(R.string.pause);
                }

                else{
                    Log.i("do pause", "ok");
                    pauseTimer();
                    pauseResumeButton.setText(R.string.resume);
                }
            }
            else{
                Toast.makeText(this,"Timer finished!",Toast.LENGTH_SHORT).show();
            }
        });

        resetButton.setOnClickListener(view -> {
            resetTimer();
            updateProgressText();
            updateViewInterface();
        });

    }

    private void populateDefaultTimeButton() {
        defaultTimeTable = findViewById(R.id.default_table);

        for (Integer value : getResources().getIntArray(R.array.default_time)){
            defaultTimeList.add(value);
        }

        int max_col = 3;
        int size_left = defaultTimeList.size();

        for (int row = 0; row < size_left; row++){
            TableRow gridRow = new TableRow(this);
            gridRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            defaultTimeTable.addView(gridRow);

            for (int col = 0; col < max_col; col++){
                //Get corresponding index for the default_time_list
                int i = row * max_col + col;
                int time_value;

                CustomButton button = new CustomButton(this);

                if (size_left != 0){
                    time_value = convertToMilliSeconds(defaultTimeList.get(i));
                }

                else{
                    time_value = 0;
                    button.setVisibility(View.INVISIBLE);
                    button.setClickable(false);
                }

                //Set up button
                button.setPadding(0,0,0,0);
                button.setOnClickListener(view -> setPickerValue(time_value));
                button.setBackgroundResource(R.drawable.default_button_img);
                button.setText(getFormatTime(time_value));

                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0F
                ));
                gridRow.addView(button);

                size_left--;
            }
        }
    }

    private int getValueFromPicker(){
        int hour = timerHour.getValue();
        int minute = timerMinute.getValue();
        int second = timerSecond.getValue();

        int time_sec =  (hour * 60 * 60) + (minute * 60) + second;

        return convertToMilliSeconds(time_sec);
    }

    ///--------------------------Functions to update Views-------------------------///

    private void updateViewInterface(){
        if (timeLeft != 0){
            setInterface_Running();
        }

        else{
            setInterface_Choose();
        }
    }

    private void updateProgressText(){
        int hour = getHour(timeLeft);
        int minute = getMinute(timeLeft);
        int second = getSecond(timeLeft);
        progressText.setText(getString(R.string.format_time,hour,minute,second));
    }

    private void setPickerValue(int time){
        int hour = getHour(time);
        int minute = getMinute(time);
        int second = getSecond(time);

        timerHour.setValue(hour);
        timerMinute.setValue(minute);
        timerSecond.setValue(second);
    }

    private void checkNewScrollValue(int new_value){
        if (timerHour.getValue() == 0 && timerMinute.getValue() == 0 && new_value == 0){
            timerSecond.setValue(1);
        }
    }

    private void setInterface_Running(){
        timerLayout.setBackgroundResource(R.drawable.calming_green_scenery);

        startButton.setVisibility(View.GONE);

        timerBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        timerSpeedText.setVisibility(View.VISIBLE);

        pauseResumeButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);

        timerHour.setVisibility(View.GONE);
        timerMinute.setVisibility(View.GONE);
        timerSecond.setVisibility(View.GONE);

        hourText.setVisibility(View.GONE);
        minuteText.setVisibility(View.GONE);
        secondText.setVisibility(View.GONE);

        defaultTimeTable.setVisibility(View.GONE);

        menuHidden = false;
        invalidateOptionsMenu();
    }

    private void setInterface_Choose(){
        timerLayout.setBackgroundResource(R.drawable.timer_background);

        startButton.setVisibility(View.VISIBLE);

        timerBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        timerSpeedText.setVisibility(View.GONE);

        pauseResumeButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);

        timerHour.setVisibility(View.VISIBLE);
        timerMinute.setVisibility(View.VISIBLE);
        timerSecond.setVisibility(View.VISIBLE);

        hourText.setVisibility(View.VISIBLE);
        minuteText.setVisibility(View.VISIBLE);
        secondText.setVisibility(View.VISIBLE);

        defaultTimeTable.setVisibility(View.VISIBLE);

        menuHidden = true;
        invalidateOptionsMenu();
    }

    private void updateGUI(Intent intent){
        timeLeft = intent.getIntExtra(TimerService.TIME_LEFT_SERVICE_TAG,10);
        initialTime = intent.getIntExtra(TimerService.TIME_INITIAL_SERVICE_TAG,900);

        timerBar.setMax(initialTime);
        timerBar.setProgress(Math.abs(timeLeft - initialTime));

        updateProgressText();
        updateViewInterface();
    }

    ///--------------------------Functions to update timers from Service-------------------------///

    private void startTimer(){
        timerBar.setMax(initialTime);
        timerBar.setProgress(Math.abs(timeLeft - initialTime));

        timerSpeedFloat = 1;
        timerSpeedText.setText(getString(R.string.timer_speed, timerSpeedFloat));

        serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(TIME_INITIAL_TAG, initialTime);
        serviceIntent.putExtra(TIME_LEFT_TAG, timeLeft);
        serviceIntent.putExtra(TIMER_SPEED_TAG, timerSpeedFloat);

        startForegroundService(serviceIntent);

        isTimerRunning = true;
        updateProgressText();
        updateViewInterface();
    }

    private void setTime(int ms){
        initialTime = ms;
        timeLeft = initialTime;

        updateProgressText();
    }

    private void pauseTimer() {
        if (isTimerServiceRunning()){
            Intent intent = new Intent(this, TimerService.class);
            intent.putExtra(TimerService.SERVICE_PAUSE,true);
            startForegroundService(intent);
        }
        isTimerRunning = false;
    }

    private void resetTimer() {
        if (!isTimerServiceRunning()){
            return;
        }

        else{
            Intent intent = new Intent(this, TimerService.class);
            intent.putExtra(TimerService.SERVICE_DESTROY,true);
            startForegroundService(intent);

        }
        timeLeft = initialTime;
        isTimerRunning = false;

        pauseResumeButton.setText(getString(R.string.resume));
        timerBar.setProgress(0);

        updateProgressText();
    }

    ///--------------------------Functions for formatting time -------------------------///

    private int getHour(int time){
        return  (time/1000) /3600;
    }

    private int getMinute(int time){
        return  ((time/1000) % 3600) /60;
    }

    private int getSecond(int time){
        return  (time/1000)%60;
    }

    private String getFormatTime(int time){
        int hour = getHour(time);
        int minute = getMinute(time);
        int second = getSecond(time);

        return getString(R.string.format_time,hour,minute,second);
    }

    private int convertToMilliSeconds(int second){
        return second * 1000;
    }

    ///--------------------------Functions regarding Service -------------------------///

    public boolean isTimerServiceRunning (){
        return TimerService.isServiceRunning;
    }

}