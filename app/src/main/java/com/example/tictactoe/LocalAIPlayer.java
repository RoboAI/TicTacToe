package com.example.tictactoe;

import android.os.SystemClock;

public class LocalAIPlayer extends TicTacToePlayer implements MyTimer.ITimerElapsed{
    static final int AI_PLAY_DELAY = 800;

    MyTimer timer;//this is used to play it in a delayed manner to give it a more realistic feel
    SingleMoveAIResult aiMove;

    public LocalAIPlayer(){
        aiMove = null;
        timer = new MyTimer(this, AI_PLAY_DELAY, false);
        playerType = GameGlobals.STATE_X;
    }

    @Override
    public void abortMoveStarted(){
        timer.stop();
    }
    
    @Override
    public SingleMove getNextMove(GameState gameState) {
        SingleMoveAIResult aiResult = getNextAIMove(gameState);
        return new SingleMove(aiResult.row, aiResult.col, aiResult.player);
    }

    public SingleMoveAIResult getNextAIMove(GameState gameState){
        timer.stop();

        long startTime = SystemClock.uptimeMillis();

        aiMove = TictactoeAI.getNextBestMove(
                    gameState.cells,
                    gameState.difficulty,
                    GameGlobals.STATE_X,
                    gameState.gridSize, gameState.gridSize,
                    gameState.winComboCount);

        long timeSpent = SystemClock.uptimeMillis() - startTime;
        long timerDelay = Math.max(AI_PLAY_DELAY - timeSpent, 0);

        timer.start(timerDelay);

        //caller can make use of this return or wait for timer to send it
        return aiMove;
    }

    @Override
    public void onTimerElapsed(long timeNow) {
        if(callbackPlayTurn != null)
            callbackPlayTurn.onPlayerPlayedTurn(aiMove);
    }
}
