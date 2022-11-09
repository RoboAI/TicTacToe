package com.example.fuat.newtictactoe;

import androidx.annotation.NonNull;

public class SingleMoveAIResult implements Comparable<SingleMoveAIResult>{
    int player;
    int score;
    int row;
    int col;
    TictactoeBox cell;//TODO: this isn't that important, can remove it with a simple modification of
                      //TODO: getNextBestMoves() and checkForWin() in TictactoeAI

    public SingleMoveAIResult(int player, int score, int row, int col, TictactoeBox cell){
        this.player = player;
        this.score = score;
        this.row = row;
        this.col = col;
        this.cell = cell;
    }

    public SingleMoveAIResult(int player, int score, TictactoeBox cell){
        this.player = player;
        this.score = score;
        this.row = cell.row;
        this.col = cell.col;
        this.cell = cell;
    }

    @Override
    public int compareTo(@NonNull SingleMoveAIResult o) {
       // return (o.score > this.score) ? o.score : this.score;
        return this.score - o.score;
    }
}
