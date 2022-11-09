package com.example.fuat.newtictactoe;

public class TicTacToePlayer {
    public interface IPlayerPlayedTurn {
        void onPlayerPlayedTurn(SingleMoveAIResult aiResult);
    }

    int playerType;
    int player;
    int playerID;
    int score;
    boolean ready;

    IPlayerPlayedTurn callbackPlayTurn;

    public TicTacToePlayer(){
        init();
    }

    public TicTacToePlayer(int player){
        init();

        this.player = player;
    }

    private void init(){
        callbackPlayTurn = null;
        player = GameGlobals.PLAYER_NONE;
        playerType = GameGlobals.PLAYER_NONE;
        score = 0;
        ready = false;
    }

    public IPlayerPlayedTurn getCallbackPlayTurn() {
        return callbackPlayTurn;
    }

    public void setCallbackPlayTurn(IPlayerPlayedTurn callbackPlayTurn) {
        this.callbackPlayTurn = callbackPlayTurn;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer(){
        return player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public SingleMove getNextMove(GameState gameState){
        if(callbackPlayTurn != null)
            callbackPlayTurn.onPlayerPlayedTurn(null);
        return null;
    }

    public void abortMoveStarted(){}
}
