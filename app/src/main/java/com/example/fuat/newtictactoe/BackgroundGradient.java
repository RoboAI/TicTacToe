package com.example.fuat.newtictactoe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by Fuat on 28/10/2017.
 */

public class BackgroundGradient{
    public static int COLOUR_INC_AMOuNT = 3;

    private Paint pen;

    private int colours[] = {  0xFF15487b, 0xFF00c1FF, 0xFF15487b, 0xFF15487b };//blue
    //int colours[] = {  0xFF66400b, 0xFFFFA500, 0xFF66400b, 0xFF66400b };//orange
    //int colours[] = {  0xFF007F00, 0xFF4cbb17, 0xFF007F00, 0xFF007F00 };//green
    //int colours[] = {  0xFF15487b, 0xFF0080BB, 0xFF15487b, 0xFF222222 };
    //int colours[] = {  0XFF000000, 0XFF000000, 0XFF000000, 0XFF000000 };

    private float positions[] = {0.05f, 0.5f, 0.85f, 0.9f};

    //other good colours
    //0xFF0a0a15 start
    //0xFF00c1ee finish
    //0xFF15487b middle

    private int[] colourAnimateTo;
    private Rect rcArea;

    public BackgroundGradient(Rect area){

        pen = new Paint();
        pen.setDither(true);
        pen.setAntiAlias(true);
        pen.setColor(Color.WHITE);

        setBounds(area);
    }

    public void draw(Canvas canvas) {
        canvas.drawPaint(pen);
    }

    public void setBounds(Rect area) {
        rcArea = new Rect(area);

        LinearGradient gradient = getNewGradient(colours, positions);

        if (pen != null) {
            pen.setShader(gradient);
        }
    }

    public void setAnimationColours(int[] newColours){
        colourAnimateTo = newColours;
    }

    public int getNextUpdatedColour(int colourNow, int colourTo){
        if (colourNow < colourTo) {
            colourNow += COLOUR_INC_AMOuNT;
            if(colourNow > colourTo)
                colourNow = colourTo;
        }
        else if(colourNow > colourTo) {
            colourNow -= COLOUR_INC_AMOuNT;
            if(colourNow < colourTo)
                colourNow = colourTo;
        }

        return colourNow;
    }

    public boolean updateAnimation(){

        boolean bStop = true;
        for(int i=0; i < colourAnimateTo.length; i++) {

            int red = getNextUpdatedColour(Color.red(colours[i]), Color.red(colourAnimateTo[i]));
            int green = getNextUpdatedColour(Color.green(colours[i]), Color.green(colourAnimateTo[i]));
            int blue = getNextUpdatedColour(Color.blue(colours[i]), Color.blue(colourAnimateTo[i]));
            int alpha = getNextUpdatedColour(Color.alpha(colours[i]), Color.alpha(colourAnimateTo[i]));

            colours[i] = Color.argb(alpha, red, green, blue);

            //if a colour still hasn't reached its target then keep going
            if(colours[i] != colourAnimateTo[i])
                bStop = false;
        }

        pen.setShader(getNewGradient(colours, positions));

        return bStop;
    }

    private LinearGradient getNewGradient(int[] newColours, float[] newPositions){
        return new LinearGradient(rcArea.width() / 2, 0, rcArea.width()/2, rcArea.height(),
                newColours, newPositions,
                Shader.TileMode.CLAMP);
    }
}
