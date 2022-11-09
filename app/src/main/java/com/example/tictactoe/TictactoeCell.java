package com.example.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import static com.example.tictactoe.GameGlobals.STATE_O;
import static com.example.tictactoe.GameGlobals.STATE_UNUSED;
import static com.example.tictactoe.GameGlobals.STATE_X;

public class TictactoeCell extends AppCompatImageView implements CellInterface{
    private int state;
    private int row, col;

    private Paint pen;
    private Rect rcArea;

    private Bitmap bmpO;
    private Bitmap bmpX;
    private int colourBlank;
    private int colourWonX;
    private int colourWonO;

    public TictactoeCell(Context context, int row, int col, Bitmap bmp0, Bitmap bmpX){
        super(context, null, R.style.CustomStyleButton);

        init();

        this.bmpO = bmp0;
        this.bmpX = bmpX;
        this.row = row;
        this.col = col;
    }

    public TictactoeCell(Context context){
        super(context, null, R.style.CustomStyleButton);

        init();
    }

    public TictactoeCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TictactoeCell,
                0, 0
        );

        try{
            row = a.getInteger(R.styleable.TictactoeCell_row, 0);
            col = a.getInteger(R.styleable.TictactoeCell_col, 0);
        } finally{
            a.recycle();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        rcArea = Utilities.getViewLocalRect(this);
    }

    void init() {
        row = 0;
        col = 0;
        state = STATE_UNUSED;

        colourBlank = ContextCompat.getColor(getContext(), R.color.colorCellBlank);
        colourWonX = ContextCompat.getColor(getContext(), R.color.colorWonX);
        colourWonO = ContextCompat.getColor(getContext(), R.color.colorWonO);

        pen = new Paint();
        pen.setColor(Color.GREEN);
        pen.setStrokeWidth(5);
        pen.setStyle(Paint.Style.STROKE);

        setClickable(true);
    }

    @Override
    public void reset() {
        setStateBlank();
    }

    @Override
    public int getState() {
        return state;
    }

    public void setRowCol(int x, int y){
        row = x;
        col = y;
    }

    public void setCellWon(){
        if(state == GameGlobals.STATE_X)
            setBackgroundColor(colourWonX);
        else
            setBackgroundColor(colourWonO);
    }

    @Override
    public void setStateBlank(){
        state = STATE_UNUSED;
        setBackgroundColor(colourBlank);
        this.setImageBitmap(null);
    }

    @Override
    public void setStateX(){
        state = STATE_X;
        setScaleX(0.0f);
        setScaleY(0.0f);
        setAlpha(0.0f);
        setImageBitmap(bmpX);
        animate().scaleX(1.0f).alpha(1.0f).setDuration(150).start();
        animate().scaleY(1.0f).alpha(1.0f).setDuration(150).start();
    }

    @Override
    public void setStateO(){
        state = STATE_O;
        setScaleX(0.0f);
        setScaleY(0.0f);
        setAlpha(0.0f);
        setImageBitmap(bmpO);
        animate().scaleX(1.0f).alpha(1.0f).setDuration(100).start();
        animate().scaleY(1.0f).alpha(1.0f).setDuration(100).start();
    }

    @Override
    public void btnClicked(int player){
        if(getState() == STATE_UNUSED) {
            if (player == GameGlobals.STATE_X) {
                setStateX();
            } else if (player == GameGlobals.STATE_O){
                setStateO();
            }
            else {
                Log.i("ABCDE", "Error: btnClicked: state=" + getState());
            }
        }
        else {
            Log.i("ABCDE", "Error: btnClicked: state=" + getState());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawRect(rcArea, pen);
        //canvas.drawArc();
    }
}