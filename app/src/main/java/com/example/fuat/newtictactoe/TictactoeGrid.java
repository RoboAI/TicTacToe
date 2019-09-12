package com.example.fuat.newtictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import static com.example.fuat.newtictactoe.GameGlobals.DEFAULT_GRID_SIZE;

public class TictactoeGrid extends TableLayout
        implements GameController.INewGameStarted,
        GameController.ICellInteraction,
        GameController.IGameEndedCelebration{


    public final static int CELL_MARGIN = 5;

    private TictactoeCell cells[][];
    private int gameGridSize;
    private Bitmap bmpPlayerO;
    private Bitmap bmpPlayerX;
    private float cellWidth;
    private float cellHeight;

    private Rect rcGridArea;
    private boolean bControlVarsInitialised;

    private OnClickListener clickListener;

    private Paint pen;

    private GameController control;

    public void setController(GameController control){
        this.control = control;
    }

    public TictactoeGrid(Context context){
        super(context);

        init();
    }

    public TictactoeGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        cells = null;
        rcGridArea = new Rect();
        cellWidth = 0 ;
        cellHeight = 0;
        control = null;

        if(bmpPlayerO == null)
            bmpPlayerO = BitmapFactory.decodeResource(getResources(), R.drawable.player_yellow_all);
        if(bmpPlayerX == null)
            bmpPlayerX = BitmapFactory.decodeResource(getResources(), R.drawable.player_red_all);

        gameGridSize = DEFAULT_GRID_SIZE;

        bControlVarsInitialised = false;

        //listener for cells. All the cell-buttons will use this
        clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_cell(v);
            }
        };

        pen = new Paint();
        pen.setStrokeWidth(5);
        pen.setColor(getResources().getColor(R.color.colorGridLines));
        pen.setStyle(Paint.Style.STROKE);
    }

    private void resetCellArray(){
        if(cells != null) {
            for (TictactoeCell[] cell : cells) {
                for (int j = 0; j < cell.length; j++) {
                    cell[j].reset();
                }
            }
        }
    }

    public int getGridSize(){
        return gameGridSize;
    }

    public void initialiseControls(){
    }

    protected float getAvailableHeightForGrid(){
        return rcGridArea.height();
    }

    protected float getAvailableWidthForGrid(){
        return getWidth();
    }

    protected float getAvailableHeightForGridCell(int row_count){
        float height = rcGridArea.height();
        height = height - (row_count * CELL_MARGIN);
        height /= row_count;
        height -= CELL_MARGIN;
        return height;
    }

    protected float getAvailableWidthForGridCell(int col_count){
        float width = rcGridArea.width();

        width = width - (col_count * CELL_MARGIN);
        width /= col_count;
        width -= CELL_MARGIN;
        return width;
    }

    private void adjustGridDimensions(){
        float width = getAvailableWidthForGrid();
        float height = getAvailableHeightForGrid();

        adjustGridDimensions(new Rect(0, 0, (int)width, (int)height));
    }

    private void adjustGridDimensions(Rect area){
        if(area.height() > area.width()) {
            rcGridArea.set(0, 0, area.left + area.width(), area.top + area.width());
        }else{
            rcGridArea.set(0, 0, area.left + area.height(), area.top + area.height());
        }
    }

    private void adjustGridCellsDimensions(){
        TableLayout tableLayout = this;
        ViewGroup.LayoutParams params;
        TableRow row;
        TictactoeCell cell;

        int height = (int) getAvailableHeightForGridCell(getGridSize());
        int width = (int) getAvailableWidthForGridCell(getGridSize());

        cellWidth = width;
        cellHeight = height;

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            row = (TableRow)tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                cell = (TictactoeCell)row.getChildAt(j);
                params = cell.getLayoutParams();
                params.height = height;
                params.width = width;
                cell.setMaxHeight(height);
                cell.setMaxWidth(width);
                cell.setLayoutParams(params);
            }
        }
    }

    private void generateGrid(int row_count, int col_count){
        resetCellArray();
        cells = new TictactoeCell[row_count][col_count];

        removeAllViews();

        LayoutParams tableParams = new LayoutParams(getLayoutParams());
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.height = 0;
        params.width = 0;
        params.setMargins(CELL_MARGIN, CELL_MARGIN, CELL_MARGIN, CELL_MARGIN);

        ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(), R.style.CustomStyleButton);

        for (int i = 0; i < row_count; i++) {
            TableRow row = new TableRow(getContext());
            for (int j = 0; j < col_count; j++) {
                TictactoeCell cell = new TictactoeCell(newContext, i, j, bmpPlayerO, bmpPlayerX);
                cell.setStateBlank();
                cell.setOnClickListener(clickListener);
                row.addView(cell, params);
                cells[i][j] = cell;
            }
            addView(row, tableParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //make sure we only call this once
        if (!bControlVarsInitialised) {
            initialiseControls();
            bControlVarsInitialised = true;
        }

        //if parent size has changed, then resize cells accordingly
        if(changed) {
            adjustGridDimensions();
            adjustGridCellsDimensions();
        }
    }

    public void setupNewGrid(Rect rcArea, int gridSize) {
        this.gameGridSize = gridSize;
        generateGrid(getGridSize(), getGridSize());
        adjustGridDimensions(rcArea);
        adjustGridCellsDimensions();
    }

    public void gridAreaChanged(Rect newArea){
        adjustGridDimensions(newArea);
        adjustGridCellsDimensions();
    }

    public void onClick_cell(View v) {
        //we will only notify Controller of the onClick, then hopefully the controller would route it
        //back to this class. just trying to follow the MVC.
        if(control != null)
            control.cellClicked(((TictactoeCell)v).getRow(), ((TictactoeCell)v).getCol());
    }

    public void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewGameStarted(GameState gameState) {
        setAlpha(0.0f);
        setupNewGrid(rcGridArea, gameState.gridSize);
        animate().alpha(1.0f).setDuration(250).start();
    }

    @Override
    public void cellClicked(SingleMove singleMove) {
        cells[singleMove.row][singleMove.col].btnClicked(singleMove.player);
    }

    @Override
    public void undoCell(SingleMove singleMove) {
        if(singleMove !=null)
            cells[singleMove.row][singleMove.col].setStateBlank();
    }

    @Override
    public void displayGameEnded(GameResult gameResult) {
        if(gameResult.combo != null) {
            for (TictactoeBox box : gameResult.combo) {
                cells[box.row][box.col].setCellWon();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas, 0, 0, rcGridArea.width(), rcGridArea.height(), cellWidth, CELL_MARGIN, pen);
        canvas.drawRect(rcGridArea, pen);
    }

    private void drawGrid(Canvas c, float xOffset, float yOffset, float width, float height, float spacing, float padding, Paint p){
        int counter = 0;

        spacing += padding * 2;
        xOffset = spacing;

        if(xOffset < 0)
            xOffset = (xOffset % spacing) + spacing;

        for(float i = xOffset; i < width; i += spacing, counter++) {
            c.drawLine(i, 0, i, height, p);
        }
        for(float i = xOffset - spacing; i > 0; i -= spacing, counter++) {
            c.drawLine(i, 0, i, height, p);
        }

        for(float j = yOffset; j > 0; j -= spacing, counter++){
            c.drawLine(0, j, width, j, p);
        }
        for(float j = (int)yOffset + spacing; j < height; j += spacing, counter++){
            c.drawLine(0, j, width, j, p);
        }
    }

    public void setCellBlankColour(int colour){
        for(int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].setBackgroundColor(colour);
            }
        }
    }
}
