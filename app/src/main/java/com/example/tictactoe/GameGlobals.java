package com.example.tictactoe;

import java.util.Random;

public class GameGlobals {
    public static boolean UI_IS_LOW_DPI = false;

    public final static int AI_VERY_HARD = 2;
    public final static int AI_HARD = 1;
    public final static int AI_MEDIUM = 0;

    static final int DEFAULT_GRID_SIZE = 6;
    static final int DEFAULT_WIN_COMBO = 4;
    static final int DEFAULT_AI_DIFFICULTY = AI_MEDIUM;

    static final int DEFAULT_AI_TURN_DELAY = 750;

    static final int DEFAULT_MAX_UNDO_MOVES = 50;

    static final int STATE_UNUSED = 0;
    static final int STATE_X = 1;
    static final int STATE_O = 2;

    static final int PLAYER_NONE = 0;
    static final int PLAYER_X = 1;
    static final int PLAYER_O = 2;

    static final int GAME_NOT_ENDED = 10;
    static final int GAME_ENDED_X_WON = 11;
    static final int GAME_ENDED_O_WON = 12;
    static final int GAME_ENDED_DRAW = 13;
    static final int GAME_NO_MATCH = 14;

    //static final int PLAYER_HUMAN = PLAYER_O;
    //static final int PLAYER_AI = PLAYER_X;
    //static final int PLAYER_ONLINE = PLAYER_NONE;

    static final int GAME_HUMAN_VS_HUMAN = 200;
    static final int GAME_HUMAN_VS_AI = 201;
    static final int GAME_HUMAN_VS_ONLINE = 202;

    public static int convertStateToGameResult(int state){
        switch (state){
            case STATE_O: return GAME_ENDED_O_WON;
            case STATE_X: return GAME_ENDED_X_WON;
            case STATE_UNUSED: return GAME_NOT_ENDED;
            default: return GAME_NOT_ENDED;
        }
    }

    public static int invertPlayer(int player){
        return (player == STATE_X) ? STATE_O : STATE_X;
    }

    /*TODO: this should setup an array of PLAYER_X and PLAYER_0 and
            use array.size() instead of '2' (see below). Because currently, if
            PLAYER_X or PLAYER_O index changes then this function wouldn't return
            the correct value.
    */
    public static int getRandomPlayer(){
        Random random = new Random();
        return random.nextInt(2) + 1;
    }
}
