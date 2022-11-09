package com.example.fuat.newtictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public class GameView extends ConstraintLayout implements MyTimer.ITimerElapsed,
                                                          GameController.INewGameStarted{
    public static String TAG = "ABCD";

    public interface IMainViewInflated{
        void onLayoutChanged(Rect rcArea);
    }

    private IMainViewInflated callback;
    private Rect rcGridArea;
    private BackgroundGradient backgroundGradient;
    private BackgroundChanger backgroundChanger;

    private MyTimer timer;
    private Rect rcTemp;

    public void setViewInflatedCallback(IMainViewInflated callback) {
        this.callback = callback;
    }

    public GameView(Context context){
        super(context);

        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init(){
        rcGridArea = new Rect();
        rcTemp = new Rect();

        backgroundGradient = new BackgroundGradient(new Rect(0,0,0,0));
        backgroundChanger = new BackgroundChanger(backgroundGradient, this);

        timer = new MyTimer(this, 1, true);
    }

    private void adjustGridArea(){
        View spaceTop = findViewById(R.id.topSection);
        View spaceBottom = findViewById(R.id.bottomSection);
        View winningCombo = findViewById(R.id.textViewWinComboCount);
        Rect rcSpaceTop = Utilities.getViewGlobalRect(spaceTop);
        Rect rcSpaceBottom = Utilities.getViewGlobalRect(spaceBottom);
        Rect rcWinningCombo = Utilities.getViewGlobalRect(winningCombo);
        TictactoeGrid gameGrid = findViewById(R.id.gameGrid);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)gameGrid.getLayoutParams();
        int top =
                gameGrid.getPaddingTop() +
                lp.topMargin;

        rcGridArea.set(
                gameGrid.getPaddingLeft() + lp.leftMargin,
                top,
                getWidth() - (gameGrid.getPaddingRight() + lp.rightMargin),
                (rcSpaceBottom.top - rcSpaceTop.bottom) - (top + gameGrid.getPaddingBottom() + lp.bottomMargin));

        if(callback != null)
            callback.onLayoutChanged(rcGridArea);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.i(TAG, "onLayout: GameView");

        if(callback != null) {
            if(changed) {
                rcTemp.set(left, top, right, bottom);

                adjustGridArea();
                backgroundGradient.setBounds(rcTemp);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backgroundGradient.draw(canvas);
    }

    @Override
    public void onTimerElapsed(long timeNow) {
    }

    public void startColourAnimation(){
        if(backgroundChanger != null)
            backgroundChanger.startChangingColours();
    }

    @Override
    public void onNewGameStarted(GameState gameState) {
        startColourAnimation();
    }
}


/*
    private void adjustGridArea(){
        View spaceTop = findViewById(R.id.topSection);
        View spaceBottom = findViewById(R.id.bottomSection);
        View winningCombo = findViewById(R.id.textViewWinComboCount);
        Rect rcSpaceTop = Utilities.getViewGlobalRect(spaceTop);
        Rect rcSpaceBottom = Utilities.getViewGlobalRect(spaceBottom);
        Rect rcWinningCombo = Utilities.getViewGlobalRect(winningCombo);
        TictactoeGrid gameGrid = findViewById(R.id.gameGrid);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)gameGrid.getLayoutParams();
        int top = gameGrid.getPaddingTop() +
                lp.topMargin +
                rcSpaceTop.height() +
                rcWinningCombo.height();

        rcGridArea.set(
                gameGrid.getPaddingLeft() + lp.leftMargin,
                top,
                getWidth() - (gameGrid.getPaddingRight() + lp.rightMargin),
                (rcSpaceBottom.top - top) - (gameGrid.getPaddingBottom() + lp.bottomMargin));

        if(callback != null)
            callback.onLayoutChanged(rcGridArea);
    }
 */
