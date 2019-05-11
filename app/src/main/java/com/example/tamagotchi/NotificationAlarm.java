package com.example.tamagotchi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class NotificationAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Log.d("CREATOR",String.valueOf(MainActivity.getHunger()));
            createNotification(context);
    }

    public void createNotification(Context context){
        CharSequence name = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            name = "custom channel";
            String description = "ez egy pelda broadcast csatorna";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(name.toString(), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, (name != null ? name.toString(): null))
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Értesítés a Mobil kurzusról")
                        .setContentText(String.valueOf(MainActivity.getFun()))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, builder.build());
    }
}
