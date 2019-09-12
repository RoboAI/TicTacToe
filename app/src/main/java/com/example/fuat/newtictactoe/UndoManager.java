package com.example.fuat.newtictactoe;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.example.fuat.newtictactoe.GameGlobals.DEFAULT_MAX_UNDO_MOVES;

public class UndoManager {

    Deque<SingleMove> playerMoves;

    int iMaxMoves;
    int movesRetrievalCounter;

    public UndoManager(){
        init();
    }

    private void init(){
        playerMoves = new ArrayDeque<>();
        iMaxMoves = DEFAULT_MAX_UNDO_MOVES;
        movesRetrievalCounter = 0;
    }

    public void reset(){
        playerMoves.clear();
        movesRetrievalCounter = 0;
    }

    public void setMaxUndoMoves(int max){
        iMaxMoves = max;
    }

    public int getMovesRemaining(){
        return iMaxMoves - movesRetrievalCounter;
    }

    public void addMove(int row, int col, int player){
        playerMoves.push(new SingleMove(row, col, player));
    }

    public SingleUndoMove getLastMoves(){
        if(movesRetrievalCounter < iMaxMoves) {
            SingleMove move1 = playerMoves.pollFirst();
            SingleMove move2 = playerMoves.pollFirst();

            movesRetrievalCounter++;

            return new SingleUndoMove(move1, move2);
        }

        return new SingleUndoMove(null, null);
    }

    public SingleMove peekLastMove(){
        return playerMoves.peek();
    }
}
