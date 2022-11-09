package com.example.tictactoe;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyTimer.ITimerElapsed{
    TextView tv;

    @Override
    public void onTimerElapsed(long timeNow) {
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
    }

    public void onClick_Start(View view){
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }

    public void onClick_Stop(View view){
    }
}
