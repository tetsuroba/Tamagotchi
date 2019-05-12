package com.example.tamagotchi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class BackgroundAlarm extends BroadcastReceiver {
    int storedHunger;
    int storedFun;
    int storedPlayCooldown;
    int storedFeedCooldown;
    SharedPreferences sharedPref;


    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPref = context.getSharedPreferences(context.getPackageName() + "_preferences", context.MODE_PRIVATE);

        storedFun = sharedPref.getInt("fun", 40);
        storedHunger = sharedPref.getInt("hunger", 60);

        Log.d("CREATION3", String.valueOf(storedFun) + " " + String.valueOf(storedHunger));

        Log.d("PASSING TIME", "C");
        if (MainActivity.isOn()) {
            MainActivity.passTime();
        } else {
            passTime();
        }

    }

    public void saveData() {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("hunger", storedHunger);
        editor.putInt("fun", storedFun);
        editor.putInt("feedCooldown", storedFeedCooldown);
        editor.putInt("playCooldown", storedPlayCooldown);


        editor.commit();
    }

    public void passTime() {
        System.out.println(storedHunger + " " + storedFun);
        if (storedHunger < 100) {
            storedHunger++;
        }
        if (storedFun > 0) {
            storedFun--;
        }
        if (storedFeedCooldown > 0 && storedFeedCooldown <= 360) {
            storedFeedCooldown = 0;
        } else if (storedFeedCooldown > 360) {
            storedFeedCooldown = storedFeedCooldown - 360;
        }
        if (storedPlayCooldown > 0 && storedPlayCooldown <= 360) {
            storedPlayCooldown = 0;
        } else if (storedPlayCooldown > 360) {
            storedPlayCooldown = storedPlayCooldown - 360;
        }
        try {
            saveData();
        } catch (Exception e) {
            Log.e("SAVE ERROR", "passTime()");
        }
    }


}
