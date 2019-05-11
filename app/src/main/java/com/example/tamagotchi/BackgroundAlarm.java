package com.example.tamagotchi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BackgroundAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.passTime();
    }
}
