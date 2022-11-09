package com.example.tictactoe;

import java.util.ArrayList;

import static com.example.tictactoe.GameGlobals.STATE_UNUSED;

public class GameSession implements GameController.INewGameStarted,
                                     GameController.ICellInteraction {

    ArrayList<SingleMove> moves;

    public GameSession(){
        init();
    }

    private void init(){
        moves = new ArrayList<>();
    }

    public void saveGameSession(){
        //TODO: save as a text file. e.g
        //String output = new String();
        /*
        0,1,PLAYER_O
        3,2,PLAYER_X
        4,2,PLAYER_O
        4,2,UNDO
        ...
         */
    }

    @Override
    public void cellClicked(SingleMove singleMove) {
        moves.add(new SingleMove(singleMove));
    }

    @Override
    public void undoCell(SingleMove singleMove) {
        SingleMove move = new SingleMove(singleMove);
        move.player = STATE_UNUSED;
        moves.add(move);
    }

    @Override
    public void onNewGameStarted(GameState gameState) {
        moves.clear();
    }
}
