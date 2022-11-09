package com.example.tictactoe;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static com.example.tictactoe.GameActivity.TAG;
import static com.example.tictactoe.GameGlobals.PLAYER_NONE;
import static com.example.tictactoe.GameGlobals.PLAYER_O;
import static com.example.tictactoe.GameGlobals.PLAYER_X;

public class PlayerTurnDots extends View implements GameController.IPlayerTurn,
                                                    GameController.IGameEndedCelebration,
                                                    MyTimer.ITimerElapsed{

    private final static int ANIM_DOT_DURATION = 250;
    private final static int ANIM_ENDGAME_DURATION = 1500;

    private int player;
    private Rect rcArea;

    private float playerO_x;
    private float playerX_x;
    private float dot_y;
    private float dot_x;

    private TrailsBase trails;
    private MyTimer timer;
    private Paint pen;
    private float circleRadius;

    public PlayerTurnDots(Context context) {
        super(context);

        init();
    }

    public PlayerTurnDots(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        player = PLAYER_NONE;

        rcArea = new Rect();

        timer = new MyTimer(this, 20, true);

        playerO_x = 0;
        playerX_x = 0;
        dot_y = 0;
        dot_x = 0;
        circleRadius = 10.0f;

        pen = new Paint();
        pen.setAntiAlias(true);
        pen.setDither(true);
        pen.setStyle(Paint.Style.FILL);
        pen.setStrokeJoin(Paint.Join.ROUND);
        pen.setStrokeCap(Paint.Cap.ROUND);
        pen.setColor(0xFFFFFFFF);
        pen.setStrokeWidth(1.0f);
        pen.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));

        trails = new TrailsBase();
        trails.setColour(Color.WHITE);
    }

    public void setLowDPImode(int mode) {
        pen = new Paint();
        pen.setAntiAlias(true);
        pen.setDither(true);
        pen.setStyle(Paint.Style.FILL);
        pen.setStrokeJoin(Paint.Join.ROUND);
        pen.setStrokeCap(Paint.Cap.ROUND);
        pen.setColor(0xFFFFFFFF);
        pen.setStrokeWidth(1.0f);

        circleRadius = 2.0f;

        trails.setLowDPImode(0);
    }

    public void setArea(Rect area) {
        rcArea.set(area);
    }

    public void setPlayerLocations(float playerO_x, float playerX_x, float y){
        this.playerO_x = playerO_x;
        this.playerX_x = playerX_x;
        this.dot_y = y;
    }

    public float getCenterPoint(){
        return playerO_x + ((playerX_x - playerO_x) / 2);
    }

    public void setPlayerTurn(final int player) {
        this.player = player;

        float goingTo;
        long duration = ANIM_DOT_DURATION;

        if (player == PLAYER_O) {
            goingTo = playerO_x;
        }
        else if(player == PLAYER_X){
            goingTo = playerX_x;
        }
        else{
            goingTo = getCenterPoint();
            duration = ANIM_ENDGAME_DURATION;
        }

        trails.setupTrail(dot_x, dot_y);
        animateDot(dot_x, goingTo, duration);
    }

    private void animateDot(float x, float goingTo, long duration){
        ValueAnimator vl = ValueAnimator.ofFloat(x, goingTo);
        vl.setDuration(duration);
        vl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dot_x = (float)animation.getAnimatedValue();
                trails.updateTrailXY(dot_x, dot_y);
                invalidate();
            }
        });
        vl.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //start timer so that trails would carry on animating until finished
                timer.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        vl.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(changed){

            Log.i(TAG, "onLayout: PlayerDots");

            ViewGroup parent = ((ViewGroup)(getParent()));
            View playerO = parent.findViewById(R.id.playerOBottomView);
            View playerX = parent.findViewById(R.id.playerXBottomView);
            float height = getHeight();
            float width = getWidth();

            float o_x = playerO.getWidth() / 2;
            float x_x = width - (playerX.getWidth() / 2);
            float y = height / 2;

            if(height > 4)
                circleRadius = height / 2;
            else
                circleRadius = 2;

            setPlayerLocations((int)o_x, x_x, (int)y);
            setPlayerTurn(player);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        trails.draw(canvas);
        canvas.drawCircle(dot_x, dot_y, circleRadius, pen);
    }

    @Override
    public void displayPlayerTurn(int player) {

        Log.i(TAG, "displayPlayerTurn: PlayerDots");
        setPlayerTurn(player);
    }

    @Override
    public void displayGameEnded(GameResult gameResult) {
        setPlayerTurn(PLAYER_NONE);
    }

    @Override
    public void onTimerElapsed(long timeNow) {
        trails.updateTrailXY(dot_x, dot_y);

        //if last trail-dot has reached the first one, then stop the trails
        if(trails.getTrail().size() > 0) {
            if (trails.getTrail().get(0).x <= dot_x &&
                    trails.getTrail().get(trails.getTrail().size() - 1).x == dot_x) {
                trails.endTrailing(dot_x, dot_y);
                timer.stop();
            }
        }

        invalidate();
    }
}
