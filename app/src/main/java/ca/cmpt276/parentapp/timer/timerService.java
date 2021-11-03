package ca.cmpt276.parentapp.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import ca.cmpt276.parentapp.R;

public class timerService extends Service {
    String startService = "Service started.";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(startService, startService);

        createNotificationChannel();

        Intent intent1 = new Intent(this, TimerActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification notification = new NotificationCompat.Builder(this, "Channel1Id1")
                .setContentTitle("Parent App")
                .setContentText("Timer is running.")
                .setSmallIcon(R.drawable.timer)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    private void createNotificationChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(
                "Channel1Id1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
