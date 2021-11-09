package ca.cmpt276.parentapp.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    ConstraintLayout timerLayout;

    private NumberPicker timer_hour, timer_minute, timer_second;
    private TextView hour_text, minute_text, second_text;

    private TextView progress_text;
    private ProgressBar timer_bar;
    private Button pause_resume_button, reset_button, start_button;

    TableLayout default_time_table;
    ArrayList<Integer> default_time_list = new ArrayList<>();

    Intent service_intent;

    Toolbar toolbar;

    private int initial_time = 0;
    private int time_left = initial_time;
    private boolean isTimerRunning;

    public static Intent makeIntent(Context context){
        return  new Intent(context, TimerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerLayout = findViewById(R.id.timerLayout);
        initializeTimerScroll();
        initializeProgressView();
        setUpTimerButtons();

        populateDefaultTimeButton();
        updateViewInterface();
        this.setTitle("Timeout Timer");

        //Check if there a timer service exist and on pause state
        if(isTimerServiceRunning() && TimerService.isPaused){
            time_left = TimerService.timer_intent.getIntExtra(TimerService.TIME_LEFT_SERVICE_TAG,3000);
            initial_time = TimerService.timer_intent.getIntExtra(TimerService.TIME_INITIAL_SERVICE_TAG,9000);
            isTimerRunning = false;

            pause_resume_button.setText(getString(R.string.resume));

            timer_bar.setMax(initial_time);
            timer_bar.setProgress(Math.abs(time_left - initial_time));

            updateProgressText();
            updateViewInterface();
        }
    }

    //Create a broadcast receiver
    private final BroadcastReceiver b_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
            isTimerRunning = true;
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(b_receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(b_receiver,new IntentFilter(TimerService.COUNTDOWN_BR));
    }

    ///--------------------------Functions for initialization-------------------------///

    private void initializeTimerScroll(){
        hour_text = findViewById(R.id.text_hour);
        minute_text = findViewById(R.id.text_minute);
        second_text = findViewById(R.id.text_second);

        timer_hour = findViewById(R.id.timer_hour);
        timer_minute = findViewById(R.id.timer_minute);
        timer_second = findViewById(R.id.timer_second);

        //Set the min and max values
        timer_hour.setMinValue(0);
        timer_hour.setMaxValue(MAX_HOUR);

        timer_minute.setMinValue(0);
        timer_minute.setMaxValue(MAX_MINUTE);

        timer_second.setMinValue(0);
        timer_second.setMaxValue(MAX_SECOND);

        //Initialize all initial values
        timer_hour.setValue(0);
        timer_minute.setValue(0);
        timer_second.setValue(1);

        timer_second.setOnValueChangedListener((numberPicker, old_value, new_value) ->
                checkNewScrollValue(new_value));

        timer_minute.setOnValueChangedListener((numberPicker, old_value, new_value) ->
                checkNewScrollValue(new_value));

        timer_hour.setOnValueChangedListener((numberPicker, old_value, new_value) ->
                checkNewScrollValue(new_value));
    }

    private void initializeProgressView(){
        timer_bar = findViewById(R.id.timer_bar);
        progress_text = findViewById(R.id.timer_progress_text);
    }

    private void setUpTimerButtons(){
        start_button = findViewById(R.id.timer_start);
        pause_resume_button = findViewById(R.id.timer_pause_resume);
        reset_button = findViewById(R.id.timer_reset);

        start_button.setOnClickListener(view ->{

            setTime(getValueFromPicker());
            startTimer();
            pause_resume_button.setText(R.string.pause);

            updateViewInterface();
        });

        pause_resume_button.setOnClickListener(view -> {
            if (time_left != 0){
                if(!isTimerRunning){
                    startTimer();
                    pause_resume_button.setText(R.string.pause);
                }

                else{
                    pauseTimer();
                    pause_resume_button.setText(R.string.resume);
                }
            }
            else{
                Toast.makeText(this,"Timer finished!",Toast.LENGTH_SHORT).show();
            }
        });

        reset_button.setOnClickListener(view -> {
            resetTimer();
            updateProgressText();
            updateViewInterface();
        });

    }

    private void populateDefaultTimeButton() {
        default_time_table = findViewById(R.id.default_table);

        for (Integer value : getResources().getIntArray(R.array.default_time)){
            default_time_list.add(value);
        }

        int max_col = 3;
        int size_left = default_time_list.size();

        for (int row = 0; row < size_left; row++){
            TableRow gridRow = new TableRow(this);
            gridRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            default_time_table.addView(gridRow);

            for (int col = 0; col < max_col; col++){
                //Get corresponding index for the default_time_list
                int i = row * max_col + col;
                int time_value;

                CustomButton button = new CustomButton(this);


                if (size_left != 0){
                    time_value = convertToMilliSeconds(default_time_list.get(i));
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
        int hour = timer_hour.getValue();
        int minute = timer_minute.getValue();
        int second = timer_second.getValue();

        int time_sec =  (hour * 60 * 60) + (minute * 60) + second;

        return convertToMilliSeconds(time_sec);
    }

    ///--------------------------Functions to update Views-------------------------///

    private void updateViewInterface(){
        if (time_left != 0){
            setInterface_Running();
        }

        else{
            setInterface_Choose();
        }
    }

    private void updateProgressText(){
        int hour = getHour(time_left);
        int minute = getMinute(time_left);
        int second = getSecond(time_left);
        progress_text.setText(getString(R.string.format_time,hour,minute,second));
    }

    private void setPickerValue(int time){
        int hour = getHour(time);
        int minute = getMinute(time);
        int second = getSecond(time);

        timer_hour.setValue(hour);
        timer_minute.setValue(minute);
        timer_second.setValue(second);
    }

    private void checkNewScrollValue(int new_value){
        if (timer_hour.getValue() == 0 && timer_minute.getValue() == 0 && new_value == 0){
            timer_second.setValue(1);
        }
    }

    private void setInterface_Running(){
        timerLayout.setBackgroundResource(R.drawable.calming_green_scenery);

        start_button.setVisibility(View.GONE);

        timer_bar.setVisibility(View.VISIBLE);
        progress_text.setVisibility(View.VISIBLE);

        pause_resume_button.setVisibility(View.VISIBLE);
        reset_button.setVisibility(View.VISIBLE);

        timer_hour.setVisibility(View.GONE);
        timer_minute.setVisibility(View.GONE);
        timer_second.setVisibility(View.GONE);

        hour_text.setVisibility(View.GONE);
        minute_text.setVisibility(View.GONE);
        second_text.setVisibility(View.GONE);

        default_time_table.setVisibility(View.GONE);
    }

    private void setInterface_Choose(){
        timerLayout.setBackgroundResource(R.drawable.timer_background);

        start_button.setVisibility(View.VISIBLE);

        timer_bar.setVisibility(View.GONE);
        progress_text.setVisibility(View.GONE);

        pause_resume_button.setVisibility(View.GONE);
        reset_button.setVisibility(View.GONE);

        timer_hour.setVisibility(View.VISIBLE);
        timer_minute.setVisibility(View.VISIBLE);
        timer_second.setVisibility(View.VISIBLE);

        hour_text.setVisibility(View.VISIBLE);
        minute_text.setVisibility(View.VISIBLE);
        second_text.setVisibility(View.VISIBLE);

        default_time_table.setVisibility(View.VISIBLE);
    }

    private void updateGUI(Intent intent){
        time_left = intent.getIntExtra(TimerService.TIME_LEFT_SERVICE_TAG,10);
        initial_time = intent.getIntExtra(TimerService.TIME_INITIAL_SERVICE_TAG,900);

        timer_bar.setMax(initial_time);
        timer_bar.setProgress(Math.abs(time_left - initial_time));

        updateProgressText();
        updateViewInterface();
    }

    ///--------------------------Functions to update timers from Service-------------------------///

    private void startTimer(){
        timer_bar.setMax(initial_time);
        timer_bar.setProgress(Math.abs(time_left - initial_time));

        service_intent = new Intent(this, TimerService.class);
        service_intent.putExtra(TIME_INITIAL_TAG,initial_time);
        service_intent.putExtra(TIME_LEFT_TAG,time_left);

        startForegroundService(service_intent);

        isTimerRunning = true;
        updateProgressText();
        updateViewInterface();
    }

    private void setTime(int ms){
        initial_time = ms;
        time_left = initial_time;

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
        time_left = initial_time;
        isTimerRunning = false;

        pause_resume_button.setText(getString(R.string.resume));

        timer_bar.setProgress(0);
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