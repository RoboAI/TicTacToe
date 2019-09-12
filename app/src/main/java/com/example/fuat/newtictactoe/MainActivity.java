package com.example.fuat.newtictactoe;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyTimer.ITimerElapsed{
    MyTimer timer;
    TextView tv;
    int counter;

    @Override
    public void onTimerElapsed(long timeNow) {
        counter++;
        tv.setText(String.valueOf(counter));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.requestFullScreen(this);
        Utilities.requestPortrait(this);

        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.hide();

        setContentView(R.layout.activity_main);

        //startActivity(new Intent(getApplicationContext(), GameActivity.class));
        tv = findViewById(R.id.textTimer);
        timer = new MyTimer(this, 1000, true);
    }

    public void onClick_Start(View view){
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
        //timer.start();
    }

    public void onClick_Stop(View view){
        //startActivity(new Intent(getApplicationContext(), GameActivity.class));
        //timer.stop();
    }
}
