package com.example.fuat.newtictactoe;

public class PlayerAI {
    public static int AI_MOVE_DELAY = 1200;
    public static int AI_DEFAULT_PLAYER = GameGlobals.STATE_X;

    int iWhichPlayerAmI;

    public PlayerAI(){
        init();
    }

    public PlayerAI(int player){
        init();

        iWhichPlayerAmI = player;
    }

    private void init(){
        iWhichPlayerAmI = AI_DEFAULT_PLAYER;
    }

    public SingleMove getAiNextMove(GameState gameState){
        //TictactoeAI.getN
        return null;
    }
}
