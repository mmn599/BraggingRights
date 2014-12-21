package io.normyle.braggingrights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,AlarmReceiverScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
