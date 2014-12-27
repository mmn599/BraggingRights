package io.normyle.braggingrights;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import io.matthew.braggingrights.R;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_NOTE = "NOTE";

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title, note = "";
        try {
            Bundle bundle = intent.getExtras();
            title = (String) bundle.get(EXTRA_TITLE);
            note = (String) bundle.get(EXTRA_NOTE);
        } catch (Exception e) {
            title = "Unknown Goal";
            note = "Unknown Note";
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.tree)
                        .setContentTitle(title)
                        .setContentText(note);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
