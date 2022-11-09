package com.example.tictactoe;

import android.view.View;

import java.util.Random;

public class BackgroundChanger implements MyTimer.ITimerElapsed{

    private final int[][] colours = {
    {  0xFF15487B, 0xFF00c1FF, 0xFF15487B, 0xFF15487B },//blue
    {  0xFF66400B, 0xFFFFA500, 0xFF66400B, 0xFF66400B },//orange
    {  0xFF007F00, 0xFF4cbb17, 0xFF007F00, 0xFF007F00 },//green
    {  0xFF15485A, 0xFF0080BB, 0xFF15485A, 0xFF15485A },//dark blue
    {  0xFFFFCC00, 0xFFFFFF00, 0xFFFFCC00, 0xFFFFCC00 },//yellow
    {  0xFF922a31, 0xFFf08080, 0xFF922a31, 0xFF922a31 },//red
    {  0xFF777777, 0xFFffffff, 0xFF777777, 0xFF777777 },//cream
    {  0xFF3091FF, 0xFFCE9908, 0xFF9A0C0C, 0xFF9A0C0C },//blue to red
    {  0xFF9A0C0C, 0xFFCE9908, 0xFF3091FF, 0xFF3091FF },//red to blue
    {  0xFF5C4033, 0xFFFFFF00, 0xFF5C4033, 0xFF5C4033 }};//zingy brown
    //{  0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF },//white
    //{  0xFF000000, 0xFF000000, 0xFF000000, 0xFF000000 }};//black

    private BackgroundGradient backgroundGradient;
    private View view;
    private MyTimer timer;

    public BackgroundChanger(BackgroundGradient backgroundGradient, View view){
        init();

        this.backgroundGradient = backgroundGradient;
        this.view = view;
    }

    private void init(){
        timer = new MyTimer(this, 1, true);
    }

    public void startChangingColours() {
        int index = (new Random()).nextInt(colours.length);
        backgroundGradient.setAnimationColours(colours[index]);
        timer.start();
    }

    @Override
    public void onTimerElapsed(long timeNow) {
        if(backgroundGradient.updateAnimation())
            timer.stop();
        else
            view.invalidate();
    }
}
