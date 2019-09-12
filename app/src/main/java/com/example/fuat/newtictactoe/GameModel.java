package com.example.fuat.newtictactoe;

import static com.example.fuat.newtictactoe.GameGlobals.GAME_NOT_ENDED;
import static com.example.fuat.newtictactoe.GameGlobals.STATE_UNUSED;
import static com.example.fuat.newtictactoe.GameGlobals.getRandomPlayer;

public class GameModel {
    private TictactoeBox[][] cells;
    private boolean bGameEnded;
    private int gameWinComboCount;
    private int gameGridSize;
    private int moveCount;

    private int playerTurn;
    private int iAILevel;
    private boolean bOpponentPlaying;

    UndoManager undoManager;

    public GameModel(){
        init();
    }

    private void init() {
        cells = null;//this is setup in startNewGame

        bGameEnded = true;
        gameWinComboCount = GameGlobals.DEFAULT_WIN_COMBO;
        gameGridSize = GameGlobals.DEFAULT_GRID_SIZE;
        moveCount = 0;

        playerTurn = GameGlobals.STATE_O;
        iAILevel = GameGlobals.DEFAULT_AI_DIFFICULTY;
        bOpponentPlaying = false;

        undoManager = new UndoManager();
    }

    public TictactoeBox[][] getCells(){
        return cells;
    }

    public int getGridSize(){
        return gameGridSize;
    }

    public int getWinComboCount(){
        return gameWinComboCount;
    }

    public int getCurrentPlayer(){
        return playerTurn;
    }

    public int getiAILevel(){
        return iAILevel;
    }

    public void setiAILevel(int level){
        iAILevel = level;
    }

    public int getMoveCount(){
        return moveCount;
    }

    public void resetGame() {
        //TODO: timer.stop();

        undoManager.reset();

        if(cells != null) {
            for (TictactoeBox[] cell : cells) {
                for (int j = 0; j < cell.length; j++) {
                    cell[j].reset();
                    cell[j].setStateBlank();
                }
            }

            setPlayerTurn(getRandomPlayer());
        }

        bOpponentPlaying = false;

        bGameEnded = false;
    }

    public void startNewGame(){
        startNewGame(getGridSize(), getWinComboCount(), iAILevel);
    }

    public void startNewGame(int gridSize, int numberOfWinBoxes, int aiLevel) {
        resetGame();

        iAILevel = aiLevel;

        gameGridSize = gridSize;
        gameWinComboCount = numberOfWinBoxes;

        moveCount = 0;

        setupArray(gridSize, gridSize);

        TictactoeAI.initialiseGridString(gameGridSize, gameGridSize);

        //TODO:
        /*
        if (buttonAISwitch.isChecked() && bPlayerTurn == PLAYER_X) {
            startAIMove();
        }
        */
    }

    private void setupArray(int row_count, int col_count){
        cells = new TictactoeBox[row_count][col_count];

        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                TictactoeBox cell = new TictactoeBox(i, j);
                cell.setStateBlank();
                cells[i][j] = cell;
            }
        }
    }

    public GameState getGameState(){
        GameState gs = new GameState();
        gs.cells = cells;
        gs.difficulty = iAILevel;
        gs.gridSize = getGridSize();
        gs.winComboCount = getWinComboCount();
        gs.moveCount = getMoveCount();
        return gs;
    }

    public boolean hasGameStarted(){
        return (moveCount > 0 && !bGameEnded);
    }

    public SingleUndoMove undoCell(){
        if(!bGameEnded) {
            SingleUndoMove moves = undoManager.getLastMoves();
            if (moves.move1 != null) {
                cells[moves.move1.row][moves.move1.col].setStateBlank();

                //since the move is undone, it should be that players turn again
                playerTurn = moves.move1.player;

                //decrease moveCount as we went back once
                moveCount--;
            }

            if (moves.move2 != null) {
                cells[moves.move2.row][moves.move2.col].setStateBlank();

                //since the move is undone, it should be that players turn again
                playerTurn = moves.move2.player;

                //decrease moveCount as we went back once
                moveCount--;
            }

            return moves;
        }

        return null;
    }

    public GameResult playCell(int row, int col, int player) {
        if (!bGameEnded) {
            if (cells[row][col].getState() == STATE_UNUSED) {

                storeLastCellPlayed(row, col, player);

                cells[row][col].btnClicked(player);

                moveCount++;

                GameResult result = ComboChecker.checkIfWon(cells, getGridSize(), getGridSize(), getWinComboCount());

                if (result.result != GAME_NOT_ENDED) {
                    gameEnded();
                }
                else{
                    //TODO: caller should call nextPlayerTurn() to update playerTurn to next player
                }

                return result;
            }
        }

        return null;
    }

    private void gameEnded(){
        bGameEnded = true;
    }

    private void storeLastCellPlayed(int row, int col, int player){
        undoManager.addMove(row, col, player);
    }

    public void nextPlayerTurn(){
        setPlayerTurn(GameGlobals.invertPlayer(playerTurn));
    }

    private void setPlayerTurn(int newPlayer){
        playerTurn = newPlayer;
    }
}
