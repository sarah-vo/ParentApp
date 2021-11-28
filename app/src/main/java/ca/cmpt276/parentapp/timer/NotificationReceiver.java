package ca.cmpt276.parentapp.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

/**
 * NotificationClass to stop the alarm when the stop button is pressed from the timer notification
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notification_id = intent.getIntExtra(TimerActivity.NOTIFY_ID,1);

        if (TimerService.alarmSound != null && TimerService.alarmSound.isPlaying()){
            try {
                TimerService.alarmSound.stop();
                TimerService.alarmSound.prepare();
            } catch (IOException e) {
                Toast.makeText(context,"Error in playing alarm",Toast.LENGTH_SHORT).show();
            }
        }

        NotificationManagerCompat notify_manager = NotificationManagerCompat.from(context);
        notify_manager.cancel(notification_id);
    }
}
