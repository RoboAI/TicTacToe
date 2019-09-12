package com.example.fuat.newtictactoe;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static com.example.fuat.newtictactoe.GameGlobals.STATE_O;
import static com.example.fuat.newtictactoe.GameGlobals.STATE_UNUSED;

public class PlayerTurnDots22 extends View implements GameController.IPlayerTurn,
                                                    MyTimer.ITimerElapsed{
    private int player;
    private boolean bFirstTime;
    private Rect rcArea;

    private float playerO_x;
    private float playerX_x;
    private float dot_y;
    private float anim_x;
    private float moveSpeed;

    private Paint pen;

    private MyTimer timer;
    private boolean bGoingTowardsO;

    public PlayerTurnDots22(Context context) {
        super(context);

        init();
    }

    public PlayerTurnDots22(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        player = STATE_UNUSED;
        bFirstTime = true;

        rcArea = new Rect();

        playerO_x = 0;
        playerX_x = 0;
        dot_y = 0;
        anim_x = 0;

        timer = new MyTimer(this, 1, true);

        pen = new Paint();
        pen.setAntiAlias(true);
        pen.setDither(true);
        pen.setStyle(Paint.Style.FILL);
        pen.setStrokeJoin(Paint.Join.ROUND);
        pen.setStrokeCap(Paint.Cap.ROUND);
        pen.setColor(0xFFFFFFFF);
        pen.setStrokeWidth(1.0f);
        pen.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));
    }

    public void setArea(Rect area) {
        rcArea.set(area);
    }

    public void setPlayerLocations(float playerO_x, float playerX_x, float y){
        this.playerO_x = playerO_x;
        this.playerX_x = playerX_x;
        this.dot_y = y;

        moveSpeed = (this.playerX_x - this.playerO_x) / 10;

        setDotLocationToMiddle();

        invalidate();
    }

    public void setDotLocationToMiddle(){
        anim_x = playerX_x - playerO_x;
    }

    public void setPlayerTurn(final int player) {
        this.player = player;

        if (player == STATE_O) {
            bGoingTowardsO = true;
            anim_x = playerX_x;
        } else {
            bGoingTowardsO = false;
            anim_x = playerO_x;
        }

        timer.start();
    }

    private void updateAnimation(){
        if(bGoingTowardsO){
            anim_x -= moveSpeed;
        }
        else{
            anim_x += moveSpeed;
        }

        if(anim_x >= playerX_x) {
            anim_x = playerX_x;
            timer.stop();
        }
        else if(anim_x <= playerO_x) {
            anim_x = playerO_x;
            timer.stop();
        }

        invalidate();
    }

    @Override
    public void onTimerElapsed(long timeNow) {
        updateAnimation();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(changed){
            ViewGroup parent = ((ViewGroup)(getParent()));
            View playerO = parent.findViewById(R.id.playerOBottomView);
            View playerX = parent.findViewById(R.id.playerXBottomView);
            float o_x = playerO.getWidth() / 2;
            float x_x = parent.getWidth() - (playerX.getWidth() / 2);
            float y = getHeight() / 2;

            setPlayerLocations((int)o_x, x_x, (int)y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(anim_x, dot_y, 15, pen);
    }

    @Override
    public void displayPlayerTurn(int player) {
        setPlayerTurn(player);
    }
}
