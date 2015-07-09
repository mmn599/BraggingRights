package io.normyle.braggingrights;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.matthew.braggingrights.R;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_NOTE = "NOTE";
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_REPEATING = "REP";

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String title, note = "";
            int goal_id = 0;
            Bundle bundle = intent.getExtras();
            title = (String) bundle.get(EXTRA_TITLE);
            note = (String) bundle.get(EXTRA_NOTE);
            goal_id = (int) bundle.get(EXTRA_ID);
            boolean repeating = bundle.getBoolean(EXTRA_REPEATING);
            if(repeating) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                Notifications.repeatSetAlarm(context, title, note, goal_id, calendar);
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.tree)
                            .setContentTitle(title)
                            .setContentText(note);
            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(context, GoalViewActivity.class);
            notificationIntent.putExtra("GOAL_ID", goal_id);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pintent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);
            mBuilder.setContentIntent(pintent);
            Notification notification = mBuilder.build();
            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            mNotifyMgr.notify(mNotificationId, notification);
        } catch (Exception e) {

        }
    }


}
