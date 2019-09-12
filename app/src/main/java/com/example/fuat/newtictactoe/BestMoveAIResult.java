package com.example.fuat.newtictactoe;

import android.support.annotation.NonNull;

public class BestMoveAIResult implements Comparable<BestMoveAIResult>{
    int player;
    int score;
    int row;
    int col;
    TictactoeBox cell;

    public BestMoveAIResult(int player, int score, int row, int col, TictactoeBox cell){
        this.player = player;
        this.score = score;
        this.row = row;
        this.col = col;
        this.cell = cell;
    }

    public BestMoveAIResult(int player, int score, TictactoeBox cell){
        this.player = player;
        this.score = score;
        this.row = cell.row;
        this.col = cell.col;
        this.cell = cell;
    }

    @Override
    public int compareTo(@NonNull BestMoveAIResult o) {
       // return (o.score > this.score) ? o.score : this.score;
        return this.score - o.score;
    }
}
