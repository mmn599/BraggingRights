package io.normyle.braggingrights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;

public class BootupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MySQLiteHelper db = new MySQLiteHelper(context);
        List<Goal> goals = db.getAllGoals();
        Notifications.setupAlarms(context,goals);
        db.close();
    }
}
