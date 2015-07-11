package io.normyle.braggingrights;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;

/*
 * AlarmReceiver class picks up on system alarms set by goal reminder
 */
public class AlarmReceiver extends BroadcastReceiver {

    //Keys for bundle communication
    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_NOTE = "NOTE";
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_REPEATING = "REP";

    public static final int GOAL_NOTIFCATION_ID = 1;

    public AlarmReceiver() {

    }

    /**
     * When notification is received, create android system notification
     * @param context
     * @param intent intent contains a bundle with information related to the goal that created this reminder/alarm
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String title = (String) bundle.get(EXTRA_TITLE);
            String note = (String) bundle.get(EXTRA_NOTE);
            int goal_id = (int) bundle.get(EXTRA_ID);
            boolean repeating = bundle.getBoolean(EXTRA_REPEATING);
            if(repeating) {
                //If the reminder is repeating, make it occur at the same time next week
                //TODO: make sure reminder's turn off properly
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                Notifications.repeatSetAlarm(context, title, note, goal_id, calendar);
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.tree)
                            .setContentTitle(title)
                            .setContentText(note);
            int mNotificationId = GOAL_NOTIFCATION_ID;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //sets up intent to be used when user clicks on goal notification
            //this intent opens GoalViewActivity for proper goal
            Intent notificationIntent = new Intent(context, GoalViewActivity.class);
            notificationIntent.putExtra("GOAL_ID", goal_id);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mBuilder.setContentIntent(PendingIntent.getActivity(context, 0,
                    notificationIntent, 0));
            Notification notification = mBuilder.build();
            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            mNotifyMgr.notify(mNotificationId, notification);
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error setting up goal notification");
        }
    }


}
