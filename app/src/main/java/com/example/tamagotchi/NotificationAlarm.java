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
            if(MainActivity.getHunger() >= 50 && MainActivity.getFun() <= 50){
                    createNotification(context,context.getString(R.string.notification_boredandhungry),context.getString(R.string.hunger_level)
                            + MainActivity.getHunger()
                            + context.getString(R.string.fun_level)
                            + MainActivity.getFun());
            }else if(MainActivity.getHunger() >= 50){
                    createNotification(context,context.getString(R.string.notifcation_hungry),context.getString(R.string.hunger_level)
                            + MainActivity.getHunger());
            }else if(MainActivity.getFun() <= 50){
                    createNotification(context,context.getString(R.string.notification_bored),context.getString(R.string.fun_level)
                            + MainActivity.getFun());
            }



    }

    public void createNotification(Context context,String title,String text){
        CharSequence name = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                new NotificationCompat.Builder(context, (name != null ? name.toString(): null))
                        .setSmallIcon(R.drawable.ic_launcher_background)
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
