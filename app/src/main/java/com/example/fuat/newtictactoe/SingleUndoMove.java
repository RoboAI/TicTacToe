package com.example.fuat.newtictactoe;

public class SingleUndoMove {
    SingleMove move1;
    SingleMove move2;

    public SingleUndoMove(){}

    public SingleUndoMove(SingleMove move1, SingleMove move2){
        this.move1 = move1;
        this.move2 = move2;
    }
}
