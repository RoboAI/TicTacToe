package com.example.fuat.newtictactoe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Fuat on 02/11/2017.
 */

public class TrailsBase {
    public static final int TRAIL_COUNT = 10;
    public static final int MAX_SIZE = 10;
    public static final int MINIMUM_COUNT = 2;

    private boolean visible;
    private  Paint pen;
    private  int colour;
    private  ArrayList<SingleTrail> trail;
    private SingleTrail tempSingle;
    private float x,y;
    private int maxSize;

    class SingleTrail {
        float x;
        float y;
        int size;
    }

    public TrailsBase(){
        super();

        init();
        initialiseTrail();
    }

    private void init() {
        x = 0;
        y = 0;

        maxSize = MAX_SIZE;

        pen = new Paint();
        pen.setColor(Color.WHITE);
        pen.setStrokeWidth(1);
        pen.setStyle(Paint.Style.FILL);

        trail = new ArrayList<>();

        visible = true;
    }

    private void initialiseTrail(){
        trail.clear();

        for(int i=0; i < TRAIL_COUNT; i++){
            SingleTrail single = new SingleTrail();
            single.size = Math.min(TRAIL_COUNT - i, maxSize);
            //tempSingle.size = Math.min((TRAIL_COUNT - i > MAX_SIZE) ? MAX_SIZE : TRAIL_COUNT - i);
            trail.add(single);
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setLowDPImode(int mode) {
        pen = new Paint();
        pen.setColor(Color.WHITE);
        pen.setStrokeWidth(2);
        pen.setStyle(Paint.Style.FILL);

        maxSize = 2;

        initialiseTrail();
    }

    public void setupTrail(float x, float y){
        setX(x);
        setY(y);

        for(int i=0; i < trail.size(); i++) {
            trail.get(i).x = getX();
            trail.get(i).y = getY();
        }
    }

    public void draw(Canvas canvas){
        if(isVisible()) {
            for (int i = 0; i < trail.size(); i++) {
                tempSingle = trail.get(i);
                canvas.drawCircle(tempSingle.x, tempSingle.y, tempSingle.size, pen);
            }
        }
    }

    public void updateTrailXY(float x, float y) {

        SingleTrail single;

        for (int i = trail.size() - 1; i > 0; i--) {
            single = trail.get(i - 1);
            trail.get(i).x = single.x;
            trail.get(i).y = single.y;
        }

        single = trail.get(0);
        single.x = x;
        single.y = y;
    }

    public void endTrailing(float x, float y){
        SingleTrail single;
        for(int i = 0; i < trail.size(); i++){
            single = trail.get(i);
            single.x = x;
            single.y = y;
        }
    }

    public ArrayList<SingleTrail> getTrail() {
        return trail;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
        pen.setColor(colour);
    }

    public Paint getPen(){
        return pen;
    }

    public void setPen(Paint pen){
        this.pen = pen;
    }
}
