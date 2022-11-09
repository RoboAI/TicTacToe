package com.example.fuat.newtictactoe;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static com.example.fuat.newtictactoe.GameView.TAG;

public class BackgroundAux extends View {

    private Rect rcArea;

    private Paint circle1;
    private Paint circle2;
    private Paint cross1;
    private Paint cross2;

    private Path pathCircle;
    private Path pathCross;

    private PointF ptCircle;

    private PointF ptCross;
    private float cross_arm_length;
    private float crossWidth;

    public BackgroundAux(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        init();
    }

    public BackgroundAux(Context context){
        super(context);

        init();
    }

    private void init(){
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        circle1 = new Paint();
        circle1.setAntiAlias(true);
        circle1.setDither(true);
        circle1.setStyle(Paint.Style.STROKE);
        circle1.setStrokeJoin(Paint.Join.ROUND);
        circle1.setStrokeCap(Paint.Cap.ROUND);
        circle1.setColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundCircle1));
        circle1.setStrokeWidth(100.0f);
        circle1.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));

        circle2 = new Paint();
        circle2.set(circle1);
        circle2.setColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundCircle2));
        circle2.setStrokeWidth(40.0f);
        circle2.setMaskFilter(new BlurMaskFilter(40, BlurMaskFilter.Blur.NORMAL));

        cross1 = new Paint();
        cross1.set(circle1);
        cross1.setColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundCross1));

        cross2 = new Paint();
        cross2.set(circle2);
        cross2.setColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundCross2));

        pathCircle = new Path();
        pathCross = new Path();

        rcArea = new Rect();

        ptCircle = new PointF();
        ptCross = new PointF();
    }

    private float[][] createCross(){
        float[][] points = new float[13][2];
        points[0][0] = ptCross.x - crossWidth;
        points[0][1] = ptCross.y - cross_arm_length;

        points[1][0] = ptCross.x - crossWidth;
        points[1][1] = ptCross.y - crossWidth;

        points[2][0] = ptCross.x - cross_arm_length;
        points[2][1] = ptCross.y - crossWidth;

        points[3][0] = ptCross.x - cross_arm_length;
        points[3][1] = ptCross.y + crossWidth;

        points[4][0] = ptCross.x - crossWidth;
        points[4][1] = ptCross.y + crossWidth;

        points[5][0] = ptCross.x - crossWidth;
        points[5][1] = ptCross.y + cross_arm_length;

        points[6][0] = ptCross.x + crossWidth;
        points[6][1] = ptCross.y + cross_arm_length;

        points[7][0] = ptCross.x + crossWidth;
        points[7][1] = ptCross.y + crossWidth;

        points[8][0] = ptCross.x + cross_arm_length;
        points[8][1] = ptCross.y + crossWidth;

        points[9][0] = ptCross.x + cross_arm_length;
        points[9][1] = ptCross.y - crossWidth;

        points[10][0] = ptCross.x + crossWidth;
        points[10][1] = ptCross.y - crossWidth;

        points[11][0] = ptCross.x + crossWidth;
        points[11][1] = ptCross.y - cross_arm_length;

        points[12][0] = ptCross.x - crossWidth;
        points[12][1] = ptCross.y - cross_arm_length;

        return points;
    }

    private float[][] rotatePoints(float[][] pts, float angle){
        double s = Math.sin(Math.toRadians(angle));
        double c = Math.cos(Math.toRadians(angle));

        for(int i=0; i < pts.length; i++) {

            double x1 = pts[i][0] - ptCross.x;
            double y1 = pts[i][1] - ptCross.y;

            double x2 = x1 * c - y1 * s;
            double y2 = x1 * s + y1 * c;

            pts[i][0] = (float)(x2 + ptCross.x);
            pts[i][1] = (float)(y2 + ptCross.y);
        }

        return pts;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.i(TAG, "onLayout: BackgroundAux");

        if(changed){

            Log.i(TAG, "onLayout: BackgroundAux changed");

            float circle_radius;
            float[][] points;

            rcArea.set(left, top, right, bottom);

            pathCircle.reset();
            circle_radius = rcArea.width() / 2.5f;
            ptCircle.y = rcArea.height() / 2.5f;
            ptCircle.x = rcArea.width() / 4 * 3;
            pathCircle.addCircle(ptCircle.x, ptCircle.y, circle_radius, Path.Direction.CW);

            cross_arm_length = rcArea.width() / 2f;
            crossWidth = 10;
            ptCross.x = rcArea.width() / 4;
            ptCross.y = rcArea.height() / 5 * 3;

            pathCross.reset();
            points = createCross();
            points = rotatePoints(points, 45);

            if(points != null) {
                pathCross.moveTo(points[0][0], points[0][1]);
                for (float[] point : points) {
                    pathCross.lineTo(point[0], point[1]);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawPath(pathCross, cross1);
        canvas.drawPath(pathCross, cross2);

        canvas.drawPath(pathCircle, circle1);
        canvas.drawPath(pathCircle, circle2);

        Log.i(TAG, "onDraw: X" + String.valueOf(ptCross.x));
        Log.i(TAG, "onDraw: Y" + String.valueOf(ptCross.y));

        Log.i(TAG, "onDraw: BackgroundAux");
    }
}