package com.example.tamagotchi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class BackgroundAlarm extends BroadcastReceiver {
    public static boolean alreadyTriggered = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.passTime();
        if(MainActivity.getHunger() > 50 && !alreadyTriggered){
            Log.d("Itt egy notifcationnek kéne lennie","CREATOR: ");
            showNotification(context);
            alreadyTriggered = true;
        }
    }

    private void showNotification(Context context){
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Éhség")
                .setContentText("Éhes");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
