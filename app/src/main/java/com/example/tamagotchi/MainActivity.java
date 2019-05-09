package com.example.tamagotchi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


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
        Log.d("Utolsó mentés óta:","CREATION: " + Long.toString(missedAlarms));

            double c = ((double)(System.currentTimeMillis()-lastAlarm)/360000);

            Log.d("C","CREATION: " + Double.toString(c));
            Log.d("Növekedések száma:","CREATION: " + Double.toString((int)c));


            //final int missedIncreases = (int)c;

        makeUpForMissedAlarms(0);


        this.hungerBar = findViewById(R.id.hungerBar);
        this.funBar = findViewById(R.id.funBar);

        hungerBar.setProgress(hunger);
        funBar.setProgress(fun);
        setAlarm();
        saveData();
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



    public static void saveData(){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("hunger", hunger);
        editor.putInt("fun", fun);
        editor.putInt("feedCooldown",0);
        editor.putInt("entertainCooldown",0);
        editor.putLong("lastAlarm",System.currentTimeMillis());



        editor.commit();
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
        hungerBar.setProgress(hunger);
        funBar.setProgress(fun);
        saveData();
    }




    public void feed(View view){
        if(this.feedCooldown == 0){
            if(hunger <= 10){
                this.hunger = 0;
            }else if(hunger > 10){
                this.hunger = hunger - 10;
            }
            hungerBar.setProgress(hunger);
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
            funBar.setProgress(fun);
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
