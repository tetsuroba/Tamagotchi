package com.example.tamagotchi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class NotificationAlarm extends BroadcastReceiver {
    int storedFun;
    int storedHunger;
    SharedPreferences sharedPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPref = context.getSharedPreferences(context.getPackageName() + "_preferences", context.MODE_PRIVATE);

        storedFun = sharedPref.getInt("fun", 40);
        storedHunger = sharedPref.getInt("hunger", 60);

        Log.d("CREATION2", String.valueOf(storedFun) + " " + String.valueOf(storedHunger));

        if (storedHunger >= 50 && storedFun <= 50) {
            createNotification(context, context.getString(R.string.notification_boredandhungry), context.getString(R.string.hunger_level)
                    + " " + storedHunger + " "
                    + context.getString(R.string.fun_level)
                    + " " + storedFun);
        } else if (storedHunger >= 50) {
            createNotification(context, context.getString(R.string.notifcation_hungry), context.getString(R.string.hunger_level)
                    + " " + storedHunger);
        } else if (storedFun <= 50) {
            createNotification(context, context.getString(R.string.notification_bored), context.getString(R.string.fun_level)
                    + " " + storedFun);
        }

        Log.d("HUNGER_ALARM", String.valueOf(storedFun));
    }

    public void createNotification(Context context, String title, String text) {

        CharSequence name = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            name = "StatusChannel";
            String description = "Status broadcast channel";
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
                new NotificationCompat.Builder(context, (name != null ? name.toString() : null))
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, builder.build());
    }
}
