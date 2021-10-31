package ca.cmpt276.parentapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationClass extends Application {
    public static final String NOTIFICATION_CHANNEL = "Notification Channel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel notify_channel = new NotificationChannel(
                NOTIFICATION_CHANNEL,
                "Timer",
                NotificationManager.IMPORTANCE_HIGH
            );
        notify_channel.setDescription("Timer Notification for Activity");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notify_channel);

    }
}
