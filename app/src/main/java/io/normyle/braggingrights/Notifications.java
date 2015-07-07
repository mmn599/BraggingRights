package io.normyle.braggingrights;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/20/2014.
 */
public class Notifications {

    public static int id = 0;

    public static void setAlarm(Context context, Goal goal, Goal.Reminder reminder) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, goal.getTitle());
        intent.putExtra(AlarmReceiver.EXTRA_NOTE, reminder.note);
        intent.putExtra(AlarmReceiver.EXTRA_ID, goal.getId());
        alarmIntent = PendingIntent.getBroadcast(context, id++, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(reminder.repeating) {
            for(int i=1;i<8;i++) {
                reminder.calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                if(reminder.days[(i-1)]) {
                    Calendar alarmCal = GregorianCalendar.getInstance();
                    alarmCal.setTime(reminder.calendar.getTime());
                    alarmCal.setFirstDayOfWeek(Calendar.SUNDAY);
                    alarmCal.set(Calendar.MINUTE, 0);
                    if(i>=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                        alarmCal.set(Calendar.DAY_OF_WEEK,i);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY * 7, alarmIntent);
//                        alarmMgr.set(AlarmManager.RTC_WAKEUP,
//                                alarmCal.getTimeInMillis(), alarmIntent);
                    }
                    else {
                        alarmCal.add(Calendar.WEEK_OF_YEAR,1);
                        alarmCal.set(Calendar.DAY_OF_WEEK, i);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY * 7, alarmIntent);
//                        alarmMgr.set(AlarmManager.RTC_WAKEUP,
//                                alarmCal.getTimeInMillis(), alarmIntent);
                    }
                }
            }
        }
        else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    reminder.calendar.getTimeInMillis(), alarmIntent);
        }
    }

    public static void setupAlarms(Context context, List<Goal> goalList) {
        for(Goal goal : goalList) {
            for(Goal.Reminder reminder : goal.getReminder()) {
                Notifications.setAlarm(context, goal, reminder);
            }
        }
    }

}
