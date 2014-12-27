package io.normyle.braggingrights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;

public class BootupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MySQLiteHelper db = new MySQLiteHelper(context);

        List<Goal> goals = db.getAllGoals();
        for(Goal goal : goals) {
            int i = 0;
            for(Date date : goal.getReminderDateList()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String note = "";
                try {
                    note = goal.getReminderNotes().get(i);
                } catch (Exception e) {
                    note = "Do it!";
                }
                Notifications.setOneTimeAlarm(context,calendar,goal.getTitle(),note);
                Toast.makeText(context,calendar.toString(),Toast.LENGTH_LONG).show();
                i++;
            }
        }

        db.close();
    }
}
