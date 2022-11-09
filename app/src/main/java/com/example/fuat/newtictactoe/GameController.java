package com.example.fuat.newtictactoe;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import static com.example.fuat.newtictactoe.GameGlobals.GAME_ENDED_DRAW;
import static com.example.fuat.newtictactoe.GameGlobals.GAME_ENDED_O_WON;
import static com.example.fuat.newtictactoe.GameGlobals.GAME_ENDED_X_WON;
import static com.example.fuat.newtictactoe.GameGlobals.GAME_HUMAN_VS_AI;
import static com.example.fuat.newtictactoe.GameGlobals.GAME_NOT_ENDED;
//import static com.example.fuat.newtictactoe.GameGlobals.PLAYER_AI;
//import static com.example.fuat.newtictactoe.GameGlobals.PLAYER_HUMAN;
import static com.example.fuat.newtictactoe.GameGlobals.STATE_O;
import static com.example.fuat.newtictactoe.GameGlobals.STATE_X;


//TODO: this will get the current cpu time, then call getAiNextMove, then get the elapsed time.
//TODO      then calculate how much time the timer should be set for to give the consistent delay.
//TODO      if more than required time has elapsed, send 0 to timer.start to fire immediately.
public class GameController implements TicTacToePlayer.IPlayerPlayedTurn {

    private ArrayList<IPlayerTurn> IplayerTurn;
    private ArrayList<IWinComboCount> IwinComboCount;
    private ArrayList<IGrid> Igrid;
    private ArrayList<ICellInteraction> IcellInteraction;
    private ArrayList<IOpponent> Iopponent;
    private ArrayList<IGameEndedCelebration> IgameEndedCelebration;
    private ArrayList<IComboChecker> IcomboChecker;
    private ArrayList<IUndoManager> IundoManager;
    private ArrayList<INewGameStarted> InewGameStarted;
    private ArrayList<IGamePauseResume> IgamePauseResume;
    private ArrayList<IUndoCountChanged> IundoCountChanged;
    private ArrayList<IGameIsQuitting> IgameIsQuitting;

    private boolean bOpponentPlaying;

    private GameModel model;
    private int iGameType;

    private boolean bAiActive;

    private LocalAIPlayer playerOpponent;

    public GameController(){
        init();
    }
/*
    public GameController(Context context){
        super(context);

        init();
    }

    public GameController(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }*/

    public interface IPlayerTurn{
        void displayPlayerTurn(int player);
    }

    public interface IWinComboCount{
        void displayWinComboCount(int count);
    }

    public interface IGrid{
        void setupNewGrid(Rect rcArea, int gridSize);
        void gridAreaChanged(Rect newArea);
    }

    public interface ICellInteraction{
        void cellClicked(SingleMove singleMove);
        void undoCell(SingleMove singleMove);
    }

    public interface IOpponent{
        SingleMove getNextMove(GameState gameState);
    }

    public interface IGameEndedCelebration{
        void displayGameEnded(GameResult gameResult);
    }

    public interface IComboChecker{
        GameResult checkCombo(GameState gameState);
    }

    public interface IUndoManager{
        void addMove(int row, int col, int player);
        SingleMove getLastMove(int player);
    }

    public interface INewGameStarted{
        void onNewGameStarted(GameState gameState);
    }

    public interface IGamePauseResume{
        void onGamePaused(GameState gameState);
        void onGameResumed(GameState gameState);
    }

    public interface IUndoCountChanged{
        void onUndoCountChanged(int count);
    }

    public interface IGameIsQuitting{
        void onGameIsQuitting();
    }


    public void addIplayerTurn(IPlayerTurn iplayerTurn) {
        IplayerTurn.add(iplayerTurn);
    }

    public void addIwinComboCount(IWinComboCount iwinComboCount) {
        IwinComboCount.add(iwinComboCount);
    }

    public void addIgrid(IGrid igrid) {
        Igrid.add(igrid);
    }

    public void addIcellInteraction(ICellInteraction icellInteraction) {
        IcellInteraction.add(icellInteraction);
    }

    public void addIopponent(IOpponent iopponent) {
        Iopponent.add(iopponent);
    }

    public void addIgameEndedCelebration(IGameEndedCelebration igameEndedCelebration) {
        IgameEndedCelebration.add(igameEndedCelebration);
    }

    public void addIcomboChecker(IComboChecker icomboChecker) {
        IcomboChecker.add(icomboChecker);
    }

    public void addIundoManager(IUndoManager iundoManager) {
        IundoManager.add(iundoManager);
    }

    public void addInewGameStarted(INewGameStarted inewGameStarted) {
        InewGameStarted.add(inewGameStarted);
    }

    public void addIgamePauseResume(IGamePauseResume igamePauseResume){
        IgamePauseResume.add(igamePauseResume);
    }

    public void addIundoCountChanged(IUndoCountChanged iundoCountChanged){
        IundoCountChanged.add(iundoCountChanged);
    }

    public void addIgameIsQuitting(IGameIsQuitting igameIsQuitting){
        IgameIsQuitting.add(igameIsQuitting);
    }

    public void setModel(GameModel model){
        this.model = model;
    }

    private void init(){
        IplayerTurn = new ArrayList<>();
        IwinComboCount = new ArrayList<>();
        Igrid = new ArrayList<>();
        IcellInteraction = new ArrayList<>();
        Iopponent = new ArrayList<>();
        IgameEndedCelebration = new ArrayList<>();
        IcomboChecker = new ArrayList<>();
        IundoManager = new ArrayList<>();
        InewGameStarted = new ArrayList<>();
        IgamePauseResume = new ArrayList<>();
        IundoCountChanged = new ArrayList<>();
        IgameIsQuitting = new ArrayList<>();

        playerOpponent = new LocalAIPlayer();
        //playerOpponent.setPlayerType(PLAYER_AI);
        playerOpponent.setPlayer(GameGlobals.PLAYER_X);
        playerOpponent.setCallbackPlayTurn(this);

        iGameType = GAME_HUMAN_VS_AI;

        bAiActive = false;
    }

    public void onCLick_Settings(){

    }

    public void onClick_ToggleAI(){

    }

    public void onClick_Undo(){
        undoCell();
    }

    public void onClick_Reset(){

    }

    public void onClick_Back(){

    }

    private void onStartNewGame(){
        GameState gs = model.getGameState();

        if(InewGameStarted != null) {
            for (INewGameStarted c : InewGameStarted) {
                c.onNewGameStarted(gs);
            }
        }
    }

    private void onWinComboCount(){
        GameState gs = model.getGameState();

        if(IwinComboCount != null) {
            for (IWinComboCount c : IwinComboCount) {
                c.displayWinComboCount(gs.winComboCount);
            }
        }
    }

    private void onCellClicked(SingleMove singleMove){
        if(IcellInteraction != null){
            for (ICellInteraction c : IcellInteraction) {
                c.cellClicked(singleMove);
            }
        }
    }

    private void onUndoCell(SingleMove singleMove){
        if(IcellInteraction != null){
            for (ICellInteraction c : IcellInteraction) {
                c.undoCell(singleMove);
            }
        }
    }

    private void onPlayerTurn(int player){
        if(IplayerTurn != null){
            for (IPlayerTurn c : IplayerTurn) {
                c.displayPlayerTurn(player);
            }
        }
    }

    private void onGameEndedCelebration(GameResult gameResult){
        if(IgameEndedCelebration != null){
            for (IGameEndedCelebration c : IgameEndedCelebration) {
                c.displayGameEnded(gameResult);
            }
        }
    }

    private void onGamePause(GameState gameResult){
        if(IgamePauseResume != null){
            for (IGamePauseResume c : IgamePauseResume) {
                c.onGamePaused(gameResult);
            }
        }
    }

    private void onGameResume(GameState gameResult){
        if(IgamePauseResume != null){
            for (IGamePauseResume c : IgamePauseResume) {
                c.onGameResumed(gameResult);
            }
        }
    }

    private void onUndoCountChanged(int count){
        if(IundoCountChanged != null){
            for (IUndoCountChanged c : IundoCountChanged) {
                c.onUndoCountChanged(count);
            }
        }
    }

    private void onGameIsQuitting(){
        if(IgameIsQuitting != null){
            for(IGameIsQuitting c : IgameIsQuitting){
                c.onGameIsQuitting();
            }
        }
    }

    public void startNewGame(){
        startNewGame(model.getGridSize(), model.getWinComboCount(), model.getiAILevel());
    }

    public void startNewGame(int gridSize, int winComboCount, int difficulty){
        playerOpponent.abortMoveStarted();
        bOpponentPlaying = false;

        if(model != null){
            model.startNewGame(gridSize, winComboCount, difficulty);

            //callbacks
            onStartNewGame();
            onWinComboCount();
            onPlayerTurn(model.getPlayerTurn());

            //TODO: maybe this can go in a new class 'players controller'
            if(model.getPlayerTurn() == STATE_X){
                startAiMove();
            }
        }
    }

    public void setAiLevel(int level){
        if(model != null){
            model.setiAILevel(level);
        }
    }

    private void cell_clicked(int row, int col, int player){
        if(model != null){
            GameResult result = model.playCell(row, col, player);
            if(result != null) {

                //notify others of cell clicked and accepted
                onCellClicked(new SingleMove(row, col, model.getPlayerTurn()));

                //check the state of the game
                switch (result.result) {
                    case GAME_NOT_ENDED:

                        //game hasn't ended so it's next player's turn
                        nextPlayerTurn();

                        //TODO: this part should be in PlayerAi. nextPlayerTurn should notify PlayerAi of next player,
                        //TODO  then PlayerAi will catch this message, and call startAiMove.
                        //if next player is AI, then startAiMove()
                        if(model.getPlayerTurn() == STATE_X && iGameType == GAME_HUMAN_VS_AI){
                            startAiMove();
                        }
                        else if(model.getPlayerTurn() == STATE_O) {
                            //TODO: cant think of anything yet!!
                        }

                        break;
                    case GAME_ENDED_X_WON:
                        xWon(result);
                        break;
                    case GAME_ENDED_O_WON:
                        oWon(result);
                        break;
                    case GAME_ENDED_DRAW:
                        gameDraw(result);
                        break;
                    default:
                        //displayError("Error on playCell() : default");
                        break;
                }
            }
        }
    }

    //called from View (currently TictactoeGrid)
    public void cellClicked(int row, int col){
        cellClicked(row, col, model.getPlayerTurn());
    }

    //called for both human and AI click
    public void cellClicked(int row, int col, int player){
        if(player == STATE_X){
            if(!bAiActive)
                cell_clicked(row, col, STATE_X);
        }
        else if(player == STATE_O && !bOpponentPlaying){
            cell_clicked(row, col, STATE_O);
        }
    }

    private void nextPlayerTurn(){
        //update model
        model.nextPlayerTurn();

        //let others know about player turn
        onPlayerTurn(model.getPlayerTurn());
    }

    @Override
    public void onPlayerPlayedTurn(SingleMoveAIResult aiResult) {
        if(aiResult != null) {
           // Log.i("ABCDEF", "onPlayerPlayedTurn: " + aiResult.player);
            cell_clicked(aiResult.row, aiResult.col, STATE_X);
        }else
            Log.i("ABCDEF", "onPlayerPlayedTurn: aiResult is null");
        bOpponentPlaying = false;
    }

    //TODO: this function should be in PlayerAi
    private void startAiMove(){
        if(!bOpponentPlaying && bAiActive) {
            bOpponentPlaying = true;
            playerOpponent.getNextAIMove(model.getGameState());
        }
    }

    //TODO: find a better way for model.moveCount() < 2
    public boolean undoCell(){
        if(bOpponentPlaying || model.getMoveCount() < 2)
            return false;

        SingleUndoMove moves = model.undoCell();

        if(moves != null) {
            onUndoCell(moves.move1);
            onUndoCell(moves.move2);

            onPlayerTurn(model.getPlayerTurn());

            onUndoCountChanged(model.undoManager.getMovesRemaining());

            if(model.getPlayerTurn() == STATE_X){
                startAiMove();
            }

            return true;
        }

        return false;
    }

    public void resumeGame(){
        if(!model.hasGameStarted()){
            startNewGame();
        }
    }

    private void xWon(GameResult combo){
        onGameEndedCelebration(combo);
    }

    private void oWon(GameResult combo){
        onGameEndedCelebration(combo);
    }

    private void gameDraw(GameResult combo){
        onGameEndedCelebration(combo);
    }

    public void setAiActive(boolean state){
        bAiActive = state;
    }

    public void exitGame(){
        playerOpponent.abortMoveStarted();
        onGameIsQuitting();
    }
}