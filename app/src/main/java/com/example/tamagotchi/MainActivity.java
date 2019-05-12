package com.example.tamagotchi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private AlarmManager alarmManager;

    private PendingIntent alarmIntent;

    public static SharedPreferences sharedPref;
    ////// 6 percenként +1%
    ///// 30 perces cooldown az etetésen/szórakoztatáson
    private static int hunger;
    private static int fun;

    public static boolean isOn() {
        return on;
    }

    private static boolean on = false;

    private static int feedCooldown;
    private static int entertainCooldown;
    private static ProgressBar hungerBar;
    private static ProgressBar funBar;
    private static TextView hungerText;
    private static TextView funText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATION1", String.valueOf(fun) + " " + String.valueOf(hunger));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        this.fun = sharedPref.getInt("fun", 40);
        this.hunger = sharedPref.getInt("hunger", 60);
        this.feedCooldown = sharedPref.getInt("feedCooldown", 0);
        this.entertainCooldown = sharedPref.getInt("playCooldown", 0);
        this.on = true;

        Log.d("CREATION1", String.valueOf(fun) + " " + String.valueOf(hunger));


        this.hungerBar = findViewById(R.id.hungerBar);
        this.funBar = findViewById(R.id.funBar);

        this.hungerText = findViewById(R.id.hungerText);
        this.funText = findViewById(R.id.funText);


        updateUI();
        setBackgroundAlarm();
        setNotificationAlarm();
        saveData();
    }

    public void setBackgroundAlarm() {
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BackgroundAlarm.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 6, alarmIntent);
    }

    public void setNotificationAlarm() {
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationAlarm.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 30, alarmIntent);

    }


    public static void saveData() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("hunger", hunger);
        editor.putInt("fun", fun);
        editor.putInt("feedCooldown", feedCooldown);
        editor.putInt("playCooldown", entertainCooldown);


        editor.commit();
    }


    public static void updateUI() {
        hungerBar.setProgress(hunger);
        funBar.setProgress(fun);
        hungerText.setText(String.valueOf(hunger));
        funText.setText(String.valueOf(fun));
    }


    @Override
    public void onPause() {
        saveData();
        super.onPause();
    }

    @Override
    public void onStop() {
        saveData();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        saveData();
        super.onDestroy();
    }


    public static void passTime() {
        System.out.println(hunger + " " + fun);
        if (hunger < 100) {
            hunger++;
        }
        if (fun > 0) {
            fun--;
        }
        if (feedCooldown > 0 && feedCooldown <= 360) {
            feedCooldown = 0;
        } else if (feedCooldown > 360) {
            feedCooldown = feedCooldown - 360;
        }

        if (entertainCooldown > 0 && entertainCooldown <= 360) {
            entertainCooldown = 0;
        } else if (entertainCooldown > 360) {
            entertainCooldown = entertainCooldown - 360;
        }
        try {
            updateUI();
            saveData();
        } catch (Exception e) {
            Log.e("SAVE ERROR", "passTime()");
        }
    }


    public void feed(View view) {
        if (this.feedCooldown == 0) {
            if (hunger <= 10) {
                this.hunger = 0;
            } else if (hunger > 10) {
                this.hunger = hunger - 10;
            }
            updateUI();
            this.feedCooldown = 1800;
        } else {
            Toast.makeText(this, this.getString(R.string.tryagain) + " " + feedCooldown / 60 + " " + this.getString(R.string.minutes_later), Toast.LENGTH_LONG).show();
        }
    }

    public void play(View view) {
        if (this.entertainCooldown == 0) {
            if (fun < 100 && fun >= 90) {
                this.fun = 100;
            } else {
                this.fun = fun + 10;
            }
            updateUI();
            this.entertainCooldown = 1800; //1800 mp cooldown
        } else {
            Toast.makeText(this, this.getString(R.string.tryagain) + " " + entertainCooldown / 60 + " " + this.getString(R.string.minutes_later), Toast.LENGTH_LONG).show();
        }
    }

    public void resetSharedPref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();

        editor.commit();
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
