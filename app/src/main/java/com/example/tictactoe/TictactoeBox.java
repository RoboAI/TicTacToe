package com.example.tictactoe;

import static com.example.tictactoe.GameGlobals.STATE_UNUSED;

public class TictactoeBox implements CellInterface{
    protected int state;
    protected int row, col;

    public TictactoeBox(int row, int col){
        setRowCol(row, col);
    }

    @Override
    public void reset() {
        setStateBlank();
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setRowCol(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public void setStateBlank() {
        state = GameGlobals.STATE_UNUSED;
    }

    @Override
    public void setStateX() {
        state = GameGlobals.STATE_X;
    }

    @Override
    public void setStateO() {
        state = GameGlobals.STATE_O;
    }

    @Override
    public void btnClicked(int player){
        if(getState() == STATE_UNUSED) {
            if (player == GameGlobals.STATE_X) {
                setStateX();
            } else if (player == GameGlobals.STATE_O){
                setStateO();
            }
        }
    }
}