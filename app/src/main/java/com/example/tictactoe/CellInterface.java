package com.example.tictactoe;

public interface CellInterface {
    void reset();
    int getState();
    void setRowCol(int x, int y);
    void setStateBlank();
    void setStateX();
    void setStateO();
    void btnClicked(int player);
}
