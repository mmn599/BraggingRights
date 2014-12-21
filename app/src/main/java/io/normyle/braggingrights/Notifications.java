package io.normyle.braggingrights;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by MatthewNew on 12/20/2014.
 */
public class Notifications {

    public static int id = 0;

    //TODO: IMPORTANT: implement repeating alarms

    public static class AlarmSpecs {

        private boolean repeating;

        //if repeating is true
        private boolean[] days = new boolean[7];

        private Calendar calendar;

        public AlarmSpecs(Calendar calendar, boolean repeating) {
            this.repeating = repeating;
            this.calendar = calendar;
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }

    /**
     * Days is a boolean array of size 7 to indicate which days to repeat
     * @param days
     * @param hour
     * @param minute
     */
    public static void setRepeatingAlarm(boolean[] days, int hour, int minute) {

    }

    public static void setOneTimeAlarm(Context context, Calendar calendar) {

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, id++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);

    }

}
