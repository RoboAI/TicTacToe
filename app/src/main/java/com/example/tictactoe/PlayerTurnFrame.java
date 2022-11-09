package com.example.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Toast;

import static com.example.tictactoe.GameGlobals.PLAYER_O;
import static com.example.tictactoe.GameGlobals.PLAYER_X;
import static com.example.tictactoe.GameGlobals.STATE_UNUSED;

public class PlayerTurnFrame extends FrameLayout implements GameController.IPlayerTurn{
    public static final float GLOW_DIM = 0.0f;
    public static final float BOTTOM_DIM = 0.6f;

    private int player;
    private boolean bFirstTime;
    private Rect rcArea;

    private Rect rcImageView_O_bottom;
    private Rect rcImageView_O_glow;
    private AppCompatImageView imageView_O_bottom;
    private AppCompatImageView imageView_O_glow;

    private Rect rcImageView_X_bottom;
    private Rect rcImageView_X_glow;
    private AppCompatImageView imageView_X_bottom;
    private AppCompatImageView imageView_X_glow;

    private Paint pen;

    public PlayerTurnFrame(Context context) {
        super(context);

        init();
    }

    public PlayerTurnFrame(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        player = STATE_UNUSED;
        bFirstTime = true;

        imageView_O_bottom = null;
        imageView_O_glow = null;
        rcImageView_O_bottom = new Rect();
        rcImageView_O_glow = new Rect();

        imageView_X_bottom = null;
        imageView_X_glow = null;
        rcImageView_X_bottom = new Rect();
        rcImageView_X_glow = new Rect();

        rcArea = new Rect();

        //setPlayerTurn(bPlayerTurn);

        pen = new Paint();
        pen.setStrokeWidth(5);
        pen.setColor(Color.RED);
        pen.setStyle(Paint.Style.STROKE);
    }

    public void setArea(Rect area) {
        rcArea.set(area);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setPlayerTurn(final int player) {
        this.player = player;

        if (imageView_O_bottom == null ||
                imageView_X_bottom == null ||
                imageView_O_glow == null ||
                imageView_X_glow == null)
            return;

        AppCompatImageView viewComingGlow;
        AppCompatImageView viewComingBottom;
        AppCompatImageView viewGoingGlow;
        AppCompatImageView viewGoingBottom;

        if (player == PLAYER_O) {
            viewComingBottom = imageView_O_bottom;
            viewComingGlow = imageView_O_glow;
            viewGoingGlow = imageView_X_glow;
            viewGoingBottom = imageView_X_bottom;
        } else if (player == PLAYER_X){
            viewComingBottom = imageView_X_bottom;
            viewComingGlow = imageView_X_glow;
            viewGoingGlow = imageView_O_glow;
            viewGoingBottom = imageView_O_bottom;
        }else{
            viewComingBottom = imageView_X_bottom;
            viewComingGlow = imageView_X_glow;
            viewGoingGlow = imageView_O_glow;
            viewGoingBottom = imageView_O_bottom;
        }

        viewComingBottom.animate().alpha(1.0f);
        viewComingGlow.animate().alpha(1.0f);
        viewGoingBottom.animate().alpha(BOTTOM_DIM);
        viewGoingGlow.animate().alpha(GLOW_DIM);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        rcArea = Utilities.getViewLocalRect(this);

        imageView_O_bottom = findViewById(R.id.playerOBottomView);
        imageView_O_glow = findViewById(R.id.playerOGlowView);
        rcImageView_O_bottom = Utilities.getViewLocalRect(imageView_O_bottom);
        rcImageView_O_glow = Utilities.getViewLocalRect(imageView_O_glow);

        imageView_X_bottom = findViewById(R.id.playerXBottomView);
        imageView_X_glow = findViewById(R.id.playerXGlowView);
        rcImageView_X_bottom = Utilities.getViewLocalRect(imageView_X_bottom);
        rcImageView_X_glow = Utilities.getViewLocalRect(imageView_X_glow);

        if(bFirstTime){
            imageView_O_bottom.setAlpha(BOTTOM_DIM);
            imageView_X_bottom.setAlpha(BOTTOM_DIM);
            imageView_O_glow.setAlpha(GLOW_DIM);
            imageView_X_glow.setAlpha(GLOW_DIM);
            bFirstTime = false;
        }

        if(changed)
            setPlayerTurn(player);
    }

    @Override
    public void displayPlayerTurn(int player) {
        setPlayerTurn(player);
    }

    public void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
