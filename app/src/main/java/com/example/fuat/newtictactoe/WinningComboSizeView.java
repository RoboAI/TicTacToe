package com.example.fuat.newtictactoe;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class WinningComboSizeView extends View implements GameController.IWinComboCount, MyTimer.ITimerElapsed {

    class ComboViewBase{
        float x,y;
        float size;
        boolean done;

        ComboViewBase(float x, float y, final float size){
            this.x = x;
            this.y = y;
            this.size = size;
            this.done = false;
        }
    }

    private  class Dot extends ComboViewBase{
        Dot(float x, float y, float size){
            super(x, y, size);
        }
    }

    private  class Line extends ComboViewBase{
        Line(float x, float y, float size){
            super(x, y, size);
        }
    }

    private int iCombinationSize;

    private Rect rcArea;

    private Paint penLowDPI;
    private Paint penBlur;
    private Paint pen;
    private float circleRadius;
    private float lineLength;

    private float center_x;
    private float center_y;
    private float start_x;
    private float strikeStartX;
    private float strikeEndX;

    private MyTimer timer;

    private ArrayList<Dot> dots;
    private Line line;

    public WinningComboSizeView(Context context){
        super(context);

        init();
    }

    public WinningComboSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        iCombinationSize = GameGlobals.DEFAULT_WIN_COMBO;

        penBlur = new Paint();
        penBlur.setAntiAlias(true);
        penBlur.setDither(true);
        penBlur.setStyle(Paint.Style.STROKE);
        penBlur.setStrokeJoin(Paint.Join.ROUND);
        penBlur.setStrokeCap(Paint.Cap.ROUND);
        penBlur.setColor(0xFFFFFFFF);
        penBlur.setStrokeWidth(4.0f);
        penBlur.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));

        pen = new Paint();
        pen.set(penBlur);
        pen.setColor(0xFFFFFFFF);
        pen.setStrokeWidth(3.0f);
        pen.setMaskFilter(null);

        rcArea = new Rect();

        timer = new MyTimer(this, 1, true);

        dots = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            center_x = getWidth() / 2;
            center_y = getHeight() / 2;
            circleRadius = center_y - 15;//TODO: '15' should be calculated based on DPI

            //atleast have a minimum to see something
            if (circleRadius < 3)
                circleRadius = 3;

            setCombinationSize(iCombinationSize, false);
        }
    }

    private float calculateStartX(float x, float radius, int size){
        return x - ((size * (radius * 2)) / 2);
    }

    private float calculateStrikeStartX(float x, float radius, int size){
        //TODO: the +10 is the reason for long strike on low dpi devices
        return x - (((size * (radius * 2)) / 2) + 10 );
    }

    private float calculateStrikeEndX(float x, float radius, int size){
        //TODO: the +10 is the reason for long strike on low dpi devices
        return x + (((size * (radius * 2)) / 2) + 10 );
    }

    private void calculateAllPoints(){
        start_x = calculateStartX(center_x, circleRadius, iCombinationSize);
        strikeStartX = calculateStrikeStartX(center_x, circleRadius, iCombinationSize);
        strikeEndX = calculateStrikeEndX(center_x, circleRadius, iCombinationSize);

        lineLength = strikeEndX - strikeStartX;
    }

    public void setCombinationSize(int dotCount, boolean bAnimate){
        iCombinationSize = dotCount;

        float diameter = circleRadius * 2;

        calculateAllPoints();

        float circle_radius = (bAnimate) ? 0 : circleRadius;
        float line_length = (bAnimate) ? 0 : lineLength;
        float x = start_x + circleRadius;

        dots.clear();
        for(int i=0; i < dotCount; i++) {
            dots.add(new Dot(x, center_y, circle_radius));
            x += diameter;
        }

        line = new Line(strikeStartX, center_y, line_length);

        if(bAnimate)
            timer.start();
    }

    public void setLowDPImode(int mode){
        penLowDPI = new Paint();
        penLowDPI.setAntiAlias(true);
        penLowDPI.setDither(true);
        penLowDPI.setStyle(Paint.Style.STROKE);
        penLowDPI.setStrokeJoin(Paint.Join.ROUND);
        penLowDPI.setStrokeCap(Paint.Cap.ROUND);
        penLowDPI.setStrokeWidth(1.0f);
        penLowDPI.setColor(0xFFFFFFFF);
    }

    @Override
    public void displayWinComboCount(int count) {
        setCombinationSize(count, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(penLowDPI != null) {
            Dot dot;
            for(int i=0; i < dots.size(); i++){
                dot = dots.get(i);
                canvas.drawCircle(dot.x, dot.y, dot.size, penLowDPI);
            }
            canvas.drawLine(line.x, line.y, line.x + line.size, line.y, pen);
        }
        else {
            Dot dot;
            for(int i=0; i < dots.size(); i++){
                dot = dots.get(i);
                canvas.drawCircle(dot.x, dot.y, dot.size, penBlur);
                canvas.drawCircle(dot.x, dot.y, dot.size, pen);
            }
            canvas.drawLine(line.x, line.y, line.x + line.size, line.y, penBlur);
            canvas.drawLine(line.x, line.y, line.x + line.size, line.y, pen);
        }
    }

    @Override
    public void onTimerElapsed(long timeNow) {

        //animate the circles
        Dot dot;
        int counter = 0;
        for(int i=0; i < dots.size(); i++){
            dot = dots.get(i);
            if(!dot.done){
                dot.size += 5;//TODO: '5' should be calculated based on DPI
                if(dot.size >= circleRadius) {
                    dot.size = circleRadius;
                    dot.done = true;
                }
                break;
            }
            counter++;
        }

        //if dots have drawn, now start animating the strike-thru
        if(!line.done) {
            if (dots.size() == counter) {
                line.size += 20;//TODO: '20' should be calculated based on DPI
                if (line.size >= lineLength) {
                    line.size = lineLength;
                    line.done = true;
                }
            }
        }
        else{
            timer.stop();
        }

        invalidate();
    }
}
