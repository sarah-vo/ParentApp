package ca.cmpt276.parentapp.timer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationClass extends Application {
    public static final String NOTIFICATION_TIMER_ENDED_CHANNEL = "Timer Ended Channel";
    public static final String NOTIFICATION_TIMER_FOREGROUND_CHANNEL = "Timer Foreground Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel notify_timer_ended = new NotificationChannel(
                NOTIFICATION_TIMER_ENDED_CHANNEL,
                "Timer",
                NotificationManager.IMPORTANCE_HIGH
            );
        notify_timer_ended.setDescription("Timer Notification for Activity");

        NotificationChannel notify_timer_running = new NotificationChannel(
                NOTIFICATION_TIMER_FOREGROUND_CHANNEL,
                "Foreground notification",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notify_timer_ended);
        manager.createNotificationChannel(notify_timer_running);

    }
}
