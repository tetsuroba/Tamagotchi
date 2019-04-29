package com.example.tamagotchi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    // 6 percenként +1%
    private int hunger = 30;
    private int fun = 70;
    private int growthStage;
    private int growthStatus;

    private ProgressBar hungerBar;
    private ProgressBar funBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
            this.hunger = savedInstanceState.getInt("hunger");
            this.fun = savedInstanceState.getInt("fun");
            this.growthStage = savedInstanceState.getInt("growthStage");
            this.growthStatus = savedInstanceState.getInt("growthStatus");
        }

        this.hungerBar = findViewById(R.id.hungerBar);
        this.funBar = findViewById(R.id.funBar);
        hungerBar.setProgress(hunger);
        funBar.setProgress(fun);
    }


    public void feed(View view){
        if(hunger <= 10){
            this.hunger = 0;
        }else if(hunger > 10){
            this.hunger = hunger - 10;
        }
        hungerBar.setProgress(hunger);
    }

    public void play(View view){
        if(fun < 100 && fun >= 90){
            this.fun = 100;
        }else{
            this.fun = fun + 10;
        }
        funBar.setProgress(fun);
    }





}
