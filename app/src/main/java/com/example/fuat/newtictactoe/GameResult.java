package com.example.fuat.newtictactoe;

public class GameResult{
    int result;
    TictactoeBox[] combo;

    public GameResult(int res, TictactoeBox[] row_col){
        result = res;
        combo = row_col;
    }
}
