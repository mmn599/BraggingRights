package io.normyle.braggingrights;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import io.matthew.braggingrights.R;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
      /*  Intent i = new Intent(context,AlarmReceiverScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
