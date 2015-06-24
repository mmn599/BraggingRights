package io.normyle.braggingrights;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import java.security.acl.NotOwnerException;
import java.util.Calendar;
import java.util.List;

import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/20/2014.
 */
public class Notifications {

    public static int id = 0;

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

    //TODO: make sure this shit works
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
            if(days[(i-1)]) {
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


    public static void setupAlarms(Context context, List<Goal> goalList) {
        for(Goal goal : goalList) {
            for(Goal.Reminder reminder : goal.getReminder()) {
                if(reminder.repeating) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR, reminder.hour);
                    calendar.set(Calendar.MINUTE, reminder.minute);
                    Notifications.setRepeatingAlarm(context,
                            calendar, reminder.days, goal.getTitle(), reminder.note);
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_YEAR, reminder.day_of_year);
                    calendar.set(Calendar.HOUR, reminder.hour);
                    calendar.set(Calendar.MINUTE, reminder.minute);
                    calendar.set(Calendar.YEAR, reminder.year);
                    Notifications.setOneTimeAlarm(context, calendar, goal.getTitle(), reminder.note);
                }
            }
        }
    }

}
