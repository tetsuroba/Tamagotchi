package com.example.tamagotchi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>{

    private AlarmManager alarmManager;

    private PendingIntent alarmIntent;

    public static SharedPreferences sharedPref;
    ////// 6 percenként +1%
    private static int hunger;
    private static int fun;

    private static int feedCooldown;
    private static int entertainCooldown;
    private static ProgressBar hungerBar;
    private static ProgressBar funBar;
    private static long lastAlarm;
    private static long missedAlarms;
    private static TextView hungerText;
    private static TextView funText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPref = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);


            this.fun = sharedPref.getInt("fun",0);
            this.hunger = sharedPref.getInt("hunger",0);
            this.feedCooldown = sharedPref.getInt("feedCooldown",0);
            this.entertainCooldown = sharedPref.getInt("entertainCooldown",0);
            this.lastAlarm = sharedPref.getLong("lastAlarm",0);

            missedAlarms = System.currentTimeMillis()-lastAlarm;
            int cooldownResume = (int)missedAlarms/1000;
            Log.d("Utolsó mentés óta:","CREATION: " + Long.toString(missedAlarms));

            double c = ((double)missedAlarms/360000);
            Log.d("MP",String.valueOf(cooldownResume));




            Log.d("C","CREATION: " + Double.toString(c));
            Log.d("Növekedések száma:","CREATION: " + Double.toString((int)c));

            Log.d("CREATOR",String.valueOf(entertainCooldown-cooldownResume));

            setEntertainCooldown(entertainCooldown-cooldownResume);
            setFeedCooldown(feedCooldown-cooldownResume);
            //final int missedIncreases = (int)c;

        makeUpForMissedAlarms((int)c);


        this.hungerBar = findViewById(R.id.hungerBar);
        this.funBar = findViewById(R.id.funBar);

        this.hungerText = findViewById(R.id.hungerText);
        this.funText = findViewById(R.id.funText);

        hungerBar.setProgress(hunger);
        funBar.setProgress(fun);
        hungerText.setText(String.valueOf(this.hunger));
        funText.setText(String.valueOf(this.fun));
        setAlarm();
        saveData();
        setNotificationAlarm();
    }

    private void makeUpForMissedAlarms(final int missedIncreases) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < missedIncreases;i++){
                    passTime();
                }
            }
        });

    }

    public void setAlarm(){
        alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BackgroundAlarm.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, alarmIntent);

    }

    public void setNotificationAlarm(){
        alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationAlarm.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, alarmIntent);

    }





    public static void saveData(){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("hunger", hunger);
        editor.putInt("fun", fun);
        editor.putInt("feedCooldown",feedCooldown);
        editor.putInt("entertainCooldown",entertainCooldown);
        editor.putLong("lastAlarm",System.currentTimeMillis());


        editor.commit();
    }


    public static void updateUI(){
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
    public void onDestroy(){
        saveData();
        super.onDestroy();
    }




    public static void passTime(){
        System.out.println(hunger + " " + fun);
        if(hunger < 100){
            hunger++;
        }
        if(fun > 0){
            fun--;
        }
        if(feedCooldown > 0 && feedCooldown <= 360){
            feedCooldown = 0;
        }else if(feedCooldown > 360){
            feedCooldown = feedCooldown - 360;
        }

        if(entertainCooldown > 0 && entertainCooldown <= 360){
            entertainCooldown = 0;
        }else if(entertainCooldown > 360){
            entertainCooldown = entertainCooldown - 360;
        }

        updateUI();
        saveData();
    }




    public void feed(View view){
        if(this.feedCooldown == 0){
            if(hunger <= 10){
                this.hunger = 0;
            }else if(hunger > 10){
                this.hunger = hunger - 10;
            }
            updateUI();
            this.feedCooldown = 1800;
        }else {

        }
    }

    public void play(View view){
        if(this.entertainCooldown == 0){
            if(fun < 100 && fun >= 90){
                this.fun = 100;
            }else{
                this.fun = fun + 10;
            }
            updateUI();
            this.entertainCooldown = 1800; //1800 mp cooldown
        }else{

        }
    }

    public static int getHunger() {
        return hunger;
    }

    public static void setHunger(int hunger) {
        MainActivity.hunger = hunger;
    }

    public static int getFun() {
        return fun;
    }

    public static void setFun(int fun) {
        MainActivity.fun = fun;
    }

    public static ProgressBar getHungerBar() {
        return hungerBar;
    }

    public static void setHungerBar(ProgressBar hungerBar) {
        MainActivity.hungerBar = hungerBar;
    }

    public static ProgressBar getFunBar() {
        return funBar;
    }

    public static void setFunBar(ProgressBar funBar) {
        MainActivity.funBar = funBar;
    }
    public static void setFeedCooldown(int feedCooldown) {
        if(feedCooldown < 0){
            MainActivity.feedCooldown = 0;
        }else{
            MainActivity.feedCooldown = feedCooldown;
        }

    }

    public static void setEntertainCooldown(int entertainCooldown) {
        if(entertainCooldown < 0){
            MainActivity.entertainCooldown = 0;
        }else{
            MainActivity.entertainCooldown = entertainCooldown;
        }

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
