package com.example.fuat.newtictactoe;

public interface CellInterface {
    void reset();
    int getState();
    void setRowCol(int x, int y);
    void setStateBlank();
    void setStateX();
    void setStateO();
    void btnClicked(int player);
}
