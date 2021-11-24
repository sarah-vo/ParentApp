package ca.cmpt276.parentapp.timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ca.cmpt276.parentapp.R;


public class TimerService extends Service {
    private NotificationManagerCompat notify_manager;
    public static MediaPlayer alarm_sound;

    CountDownTimer timer;

    public static final String NOTIFY_ID = "Notification Channel ID for Timer";

    private final long VIBRATION_TIME = 300;

    public static final String TIME_LEFT_SERVICE_TAG = "TIME_LEFT_SERVICE_TAG =";
    public static final String TIME_INITIAL_SERVICE_TAG = "TIME_INITIAL_SERVICE_TAG";
    public static final String TIMER_PAUSE_SERVICE_TAG = "TIMER_PAUSE_SERVICE_TAG";
    public static final String TIMER_CHANGE_SPEED_SERVICE_TAG = "TIMER_CHANGE_SERVICE_TAG";

    public static final String SERVICE_DESTROY = "DESTROY SERVICE TAG";
    public static final String SERVICE_PAUSE = "PAUSE SERVICE TAG";

    public static final String COUNTDOWN_BR = "COUNTDOWN_BR";
    public static Intent timer_intent = new Intent(COUNTDOWN_BR);

    public static boolean isServiceRunning = false;
    public static boolean isPaused = false;

    int initial_time;
    int time_left;
    float timer_speed_float;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Oncreate", "------------------------------------");
        Log.i("OncreateService called", "------------------------------------");
        timer_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        isServiceRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getBooleanExtra(SERVICE_DESTROY,false)){

            Log.i("went to destroy service", "ok");
            stopForeground(true);
            stopSelf();
            return START_STICKY;
        }

        if (intent.getBooleanExtra(SERVICE_PAUSE,false)){
            Log.i("went to pause service", "ok");
            timer.cancel();
            isPaused = true;
        }

        else if (intent.getBooleanExtra(TIMER_CHANGE_SPEED_SERVICE_TAG,false)){
            timer_speed_float = intent.getFloatExtra(TimerActivity.TIMER_SPEED_TAG,1);
        }

        else{
            isPaused = false;
            timer_intent.putExtra(TIMER_PAUSE_SERVICE_TAG, false);
            initializeAlarmSound();

            initial_time = intent.getIntExtra(TimerActivity.TIME_INITIAL_TAG,1000);
            time_left = intent.getIntExtra(TimerActivity.TIME_LEFT_TAG,1300);
            timer_speed_float = intent.getFloatExtra(TimerActivity.TIMER_SPEED_TAG,1);
            timer_intent.putExtra(TIME_INITIAL_SERVICE_TAG, initial_time);

            Notification timer_running_notification = createTimerRunningNotification();

            startForeground(1,timer_running_notification);
            startTimer();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroyMethod", "---------------------------------------");
        Log.i("onDestroyMethod", "called");
        stopForeground(true);
        stopSelf();

        Log.i("timer null", "before");
        if(timer != null){
            Log.i("timer null", "inside");
            timer.cancel();
        }
        Log.i("timer null", "after");

        isServiceRunning = false;
        Log.i("OnDestroy Service Running", "after");

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimer(){

        int countDownInterval = 100;
        Log.i("timer speed:", timer_speed_float + "");

        timer = new CountDownTimer(time_left,(long)
                (countDownInterval)){
            @Override
            public void onTick(long time_until_finish) {
                if (time_left <= 0){
                    onFinish();
                }
                else{
                    if (! (time_until_finish + (countDownInterval - 1) > initial_time)){
                        time_left -= countDownInterval * timer_speed_float;
                    }

                    timer_intent.putExtra(TIME_LEFT_SERVICE_TAG,time_left);
                    timer_intent.putExtra(TIME_INITIAL_SERVICE_TAG,initial_time);
                    sendBroadcast(timer_intent);
                }
            }

            @Override
            public void onFinish() {
                time_left = 0;

                timer_intent.putExtra(TIME_LEFT_SERVICE_TAG,time_left);
                timer_intent.putExtra(TIME_INITIAL_SERVICE_TAG,initial_time);

                playAlarm();
                sendBroadcast(timer_intent);

                vibrate(VIBRATION_TIME);
                sendToTimerEndedNotificationChannel();
            }
        }.start();
    }

    ///--------------------------Functions for Playing Sounds-------------------------///
    private void initializeAlarmSound(){
        alarm_sound = MediaPlayer.create(TimerService.this,R.raw.alarm_sound);
        alarm_sound.setLooping(false);
    }

    private void playAlarm(){
        if (alarm_sound != null){
            alarm_sound.start();
        }
    }

    private void vibrate(long milliseconds){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE));
    }

    ///--------------------------Functions for Notifications-------------------------///

    private Notification createTimerRunningNotification(){
        Intent intent = new Intent(this, TimerActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this,
                 NotificationClass.NOTIFICATION_TIMER_FOREGROUND_CHANNEL)
                .setContentTitle("Parent App")
                .setContentText("Timer is running.")
                .setSmallIcon(R.drawable.timer)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        return notification;
    }

    private void sendToTimerEndedNotificationChannel(){
        notify_manager = NotificationManagerCompat.from(this);

        int id = 1;
        Intent receive_intent = new Intent(this,NotificationReceiver.class);
        receive_intent.putExtra(NOTIFY_ID,id);
        PendingIntent pending_intent = PendingIntent.getBroadcast(this, 0,
                receive_intent,PendingIntent.FLAG_IMMUTABLE);

        Notification notify= new NotificationCompat.Builder(this,
                NotificationClass.NOTIFICATION_TIMER_ENDED_CHANNEL).
                setSmallIcon(R.drawable.timer).
                setContentTitle("Timer").
                setContentText("Timer Has Ended").
                setPriority(NotificationCompat.PRIORITY_HIGH).
                setCategory(NotificationCompat.CATEGORY_ALARM).
                setAutoCancel(true).
                addAction(R.mipmap.ic_launcher,"Stop",pending_intent).
                build();

        //end service that maintains foreground for timer
        stopService(new Intent(this, TimerService.class));
        notify_manager.notify(id,notify);
    }
}
