package com.example.fuat.newtictactoe;

public class GameState {
    TictactoeBox[][] cells;
    int difficulty;
    int gridSize;
    int winComboCount;
    int moveCount;

    public void reset(){
        cells = null;
        difficulty = 0;
        gridSize = 0;
        winComboCount = 0;
        moveCount = 0;
    }
}