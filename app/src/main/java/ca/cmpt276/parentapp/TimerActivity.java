package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {

    private static final int MAX_HOUR = 24;
    private static final int MAX_MINUTE = 59;
    private static final int MAX_SECOND = 59;

    private final long VIBRATION_TIME = 300;

    public static final String NOTIFY_ID = "Notification Channel ID for Timer";

    private NotificationManagerCompat notify_manager;
    public static MediaPlayer alarm_sound;
    private CountDownTimer timer;

    private NumberPicker timer_hour, timer_minute, timer_second;
    private TextView hour_text, minute_text, second_text;

    private TextView progress_text;
    private ProgressBar timer_bar;
    private Button pause_resume_button, reset_button, start_button;

    TableLayout default_time_table;
    ArrayList<Integer> default_time_list = new ArrayList<>();

    private int initial_time = 0;
    private int time_left = initial_time;
    private boolean isTimerRunning = false;

    public static Intent makeIntent(Context context){
        return new Intent(context, TimerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        notify_manager = NotificationManagerCompat.from(this);

        initializeTimerScroll();
        initializeProgressView();

        initializeAlarmSound();
        setUpTimerButtons();
        populateDefaultTimeButton();

        updateViewInterface();
    }


    ////Functions for initialization

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

        timer_second.setOnValueChangedListener((numberPicker, old_value, new_value) -> {
            if (timer_hour.getValue() == 0 && timer_minute.getValue() == 0 && new_value == 0){
                timer_second.setValue(1);
            }
        });
    }

    private void initializeProgressView(){
        timer_bar = findViewById(R.id.timer_bar);
        progress_text = findViewById(R.id.timer_progress_text);
    }

    private void initializeAlarmSound(){
        alarm_sound = MediaPlayer.create(TimerActivity.this,R.raw.alarm_sound);
        alarm_sound.setLooping(false);
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

                if(size_left != 0){
                    int time_value = convertToMilliSeconds(default_time_list.get(i));

                    //Set up button
                    CustomButton button = new CustomButton(this);
                    button.setPadding(0,0,0,0);
                    button.setOnClickListener(view -> setPickerValue(time_value));
                    //button.setAutoSizeTextTypeWithDefaults(1);
                    button.setText(getFormatTime(time_value));

                    button.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1.0f
                    ));

                    gridRow.addView(button);

                    size_left--;
                    continue;
                }
                break;

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

    //Functions to update Views

    private void updateViewInterface(){
        if (isTimerRunning){
            setInterface_Running();
        }

        else{
            if (time_left < 1000 || time_left == initial_time){
                setInterface_Choose();
            }
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

    private void setInterface_Running(){
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

    //Functions to update Timers

    private void startTimer(){
        timer_bar.setMax(initial_time);

        timer_bar.setProgress(Math.abs(time_left - initial_time));

        timer = new CountDownTimer(time_left,1000) {
            @Override
            public void onTick(long time_until_finish) {

                if (! (time_until_finish + 999 > initial_time)){
                    time_left -= 1000;
                }

                timer_bar.setProgress(Math.abs(time_left - initial_time));
                updateProgressText();
                updateViewInterface();
            }

            @Override
            public void onFinish() {
                time_left = 0;
                isTimerRunning = false;

                vibrate(VIBRATION_TIME);
                playAlarm();
                sendToNotificationChannel();

                timer_bar.setProgress(initial_time);
                updateProgressText();

                new Handler().postDelayed(()-> updateViewInterface(),1000);

            }
        }.start();

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
        timer.cancel();
        isTimerRunning = false;
    }

    private void resetTimer() {
        timer.cancel();
        isTimerRunning = false;

        time_left = initial_time;

        updateProgressText();
    }

    /////Functions for formatting time

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

    //////Functions for playing Sounds

    private void playAlarm(){
        if (alarm_sound != null){
            alarm_sound.start();
        }
    }

    private void stopAlarm() throws IOException {
        if (alarm_sound != null && alarm_sound.isPlaying()){
            alarm_sound.stop();
            alarm_sound.prepare();
        }
    }

    private void vibrate(long milliseconds){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE));
    }

    ///////Functions for notifications

    private void sendToNotificationChannel(){
        int id = 1;
        Intent receive_intent = new Intent(this,NotificationReceiver.class);
        receive_intent.putExtra(NOTIFY_ID,id);
        PendingIntent pending_intent = PendingIntent.getBroadcast(this, 0,
                receive_intent,PendingIntent.FLAG_IMMUTABLE);

        Notification notify= new NotificationCompat.Builder(this,
                NotificationClass.NOTIFICATION_CHANNEL).
                setSmallIcon(R.drawable.timer).
                setContentTitle("Timer").
                setContentText("Timer Has Ended").
                setPriority(NotificationCompat.PRIORITY_HIGH).
                setCategory(NotificationCompat.CATEGORY_ALARM).
                setAutoCancel(true).
                addAction(R.mipmap.ic_launcher,"Stop",pending_intent).
                build();

        notify_manager.notify(id,notify);

    }

}