package io.normyle.braggingrights;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.Calendar;

import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/20/2014.
 */
public class Notifications {

    public static int id = 0;

    /**
     * Days is a boolean array of size 7 to indicate which days to repeat
     * @param days
     * @param hour
     * @param minute
     */
    public static void setRepeatingAlarm(boolean[] days, int hour, int minute) {

    }

    public static void setOneTimeAlarm(Context context, Calendar calendar, String title, String note) {

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, title);
        intent.putExtra(AlarmReceiver.EXTRA_NOTE, note);
        alarmIntent = PendingIntent.getBroadcast(context, id++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);
    }

    //days must be size 8
    public static void setRepeatingAlarm(Context context, Calendar calendar, boolean[] days, String title, String note) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, title);
        intent.putExtra(AlarmReceiver.EXTRA_NOTE, note);
        alarmIntent = PendingIntent.getBroadcast(context, id++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        for(int i=1;i<8;i++) {
            if(days[i]) {
                if(i>=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                    calendar.set(Calendar.DAY_OF_WEEK,i);
                    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY * 7, alarmIntent);
                }
                else {
                    calendar.add(Calendar.WEEK_OF_YEAR,1);
                    calendar.set(Calendar.DAY_OF_WEEK,i);
                    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY * 7, alarmIntent);
                }
            }
        }
    }

}
