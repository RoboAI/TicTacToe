package com.example.fuat.newtictactoe;

public class SingleMove {
    int row;
    int col;
    int player;

    public SingleMove(int row, int col, int player){
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public SingleMove(SingleMove move){
        row = move.row;
        col = move.col;
        player = move.player;
    }
}
