package com.example.tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static com.example.tictactoe.GameGlobals.AI_HARD;
import static com.example.tictactoe.GameGlobals.AI_VERY_HARD;
import static com.example.tictactoe.GameGlobals.AI_MEDIUM;
import static com.example.tictactoe.GameGlobals.GAME_NOT_ENDED;
import static com.example.tictactoe.GameGlobals.GAME_NO_MATCH;
import static com.example.tictactoe.GameGlobals.STATE_UNUSED;
import static com.example.tictactoe.GameGlobals.invertPlayer;

public class TictactoeAI implements GameController.INewGameStarted{

    public void onNewGameStarted(GameState gameState) {
        initialiseGridString(gameState.gridSize, gameState.gridSize);
    }

    public static int counter_1 = 0;

    public static String[][] stringMap = initialiseGridString(
            GameGlobals.DEFAULT_GRID_SIZE,
            GameGlobals.DEFAULT_GRID_SIZE);

    public static SingleMoveAIResult getNextBestMove(TictactoeBox[][] cells, int difficulty, int player, int rowMax, int colMax, int count) {
        ArrayList<SingleMoveAIResult> bestMoves;
        ArrayList<TictactoeBox> unusedCells;
        SingleMoveAIResult theOne;

        unusedCells = getUnusedCells(cells);

        bestMoves = getNextBestMoves(cells, player, rowMax, colMax, count);

        theOne = getBestMoveByDifficulty(bestMoves, difficulty);

        return theOne;
    }

    public static ArrayList<TictactoeBox> getUnusedCells(TictactoeBox[][] cells) {
        ArrayList<TictactoeBox> emptyCells = new ArrayList();
        for (TictactoeBox[] cell : cells) {
            for (int j = 0; j < cell.length; j++) {
                if (cell[j].getState() == GameGlobals.STATE_UNUSED) {
                    emptyCells.add(cell[j]);
                }
            }
        }

        return emptyCells;
    }

    public static String[][] initialiseGridString(int row, int col){
        String[][] array = new String[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                array[i][j] = String.valueOf(i) + "," + String.valueOf(j);
            }
        }

        stringMap = array;
        return array;
    }

    public static SingleMoveAIResult getBestMoveByDifficulty(ArrayList<SingleMoveAIResult> moves, int difficulty) {
        if(moves == null || moves.size() == 0){
            return null;
        }

        if (difficulty == AI_VERY_HARD) {
            return moves.get(0);
        }
        else if (difficulty == AI_HARD) {
           /* ArrayList<ArrayList<BestMoveAIResult>> levels = new ArrayList<>();
            levels.add(new ArrayList<BestMoveAIResult>());
            levels.get(0).add(moves.get(0));
            for (int i = 1; i < moves.size(); i++) {
                if (levels.get(i - 1).get(0).score != moves.get(i).score) {
                    ArrayList<BestMoveAIResult> temp = new ArrayList<>();
                    temp.add(moves.get(i));
                    levels.add(temp);
                } else {
                    levels.get(levels.size() - 1).add(moves.get(i));
                }
            }*/

            Random random = new Random();
            int index = moves.size() / 2 + 2;
            if(index > 0) {
                index = random.nextInt(index);
                if(index >= moves.size())
                    index = moves.size() - 1;
                SingleMoveAIResult abc = moves.get(index);
                return moves.get(index);
            }
            else {
                return moves.get(0);
            }
        }
        else if (difficulty == AI_MEDIUM) {
            Random random = new Random();
            int qqq = moves.size() / 2;
            if(qqq > 0) {
                int index = random.nextInt(qqq) + qqq;
                if (index >= moves.size())
                    index = moves.size() - 1;

                if (index > 0)
                    return moves.get(index);
                else
                    return moves.get(0);
            }
            else
                return moves.get(0);
        }

        return null;
    }

    public static ArrayList<SingleMoveAIResult> getNextBestMoves(TictactoeBox[][] cells, int player, int rowMax, int colMax, int count) {
        ArrayList<SingleMoveAIResult> bestMoves;
        TreeMap<String, SingleMoveAIResult> tmap = new TreeMap();

        GameResult result;
        int opponent = invertPlayer(player);

        SingleMoveAIResult nextMove;

        counter_1 = 0;

        //check if AI can win
        bestMoves = checkForWin(cells, player, rowMax, colMax, count);
        if (bestMoves.size() > 0) {
            return bestMoves;
        }

        counter_1 = 0;

        //check if opponent is about to win
        bestMoves = checkForWin(cells, opponent, rowMax, colMax, count);
        if (bestMoves.size() > 0) {
            return bestMoves;
        }

        counter_1 = 0;

        //opponent cant win now, but check if AI can make a move and have a two-way win
        bestMoves = checkFor2WayWin(tmap, cells, player, rowMax, colMax, count);
        if(bestMoves != null){
            if(bestMoves.size() > 0){
                return bestMoves;
            }
        }

        counter_1 = 0;

        //opponent cant win now, but check if opponent can make a move and have a two-way win
        bestMoves = checkFor2WayWin(tmap, cells, opponent, rowMax, colMax, count);
        if(bestMoves != null){
            if(bestMoves.size() > 0){
                return bestMoves;
            }
        }

        //TODO: return a move that's one-cell around one of the opponent moves.


        //if no good moves found, just return a random empty cell
        if(bestMoves == null) {
            bestMoves = new ArrayList<>();
            bestMoves.add(new SingleMoveAIResult(player, 1, getRandomEmptyCell(cells)));
            return bestMoves;
        }

        //return bestMoves.toArray(new BestMoveAIResult[bestMoves.size()]);
        return bestMoves;
    }

    public static ArrayList<SingleMoveAIResult> checkFor2WayWin(TreeMap<String, SingleMoveAIResult> tMap, TictactoeBox[][] cells, int player, int rowMax, int colMax, int count){
        ArrayList<SingleMoveAIResult> bestMoves = null;
        TreeMap<String, SingleMoveAIResult> tmap;

        if(tMap != null) {
            tmap = tMap;
        }
        else {
            tmap = new TreeMap<>();
        }

        traverseMoves(tmap, cells, player, rowMax, colMax, count);
        if (tmap.size() > 0) {
            bestMoves = new ArrayList<>();
            for(Map.Entry<String, SingleMoveAIResult> entry : tmap.entrySet()){
                bestMoves.add(entry.getValue());
            }

            Collections.sort(bestMoves, Collections.<SingleMoveAIResult>reverseOrder());
        }

        return bestMoves;
    }

    public static ArrayList<SingleMoveAIResult> traverseMoves(TreeMap<String, SingleMoveAIResult> tmap, TictactoeBox[][] cells, int player, int rowMax, int colMax, int count){
        ArrayList<SingleMoveAIResult> bestMoves = new ArrayList<>();
        ArrayList<SingleMoveAIResult> temp;
        int opponent = GameGlobals.invertPlayer(player);

        counter_1++;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getState() == STATE_UNUSED) {

                    cells[i][j].state = player;
                    temp = checkForWin(cells, player, rowMax, colMax, count);

                    //if there is a win(ners)..
                    if (temp.size() > 0) {

                        for(int z = 0; z < temp.size(); z++) {

                            SingleMoveAIResult move = temp.get(z);
                            move.player = player;

                            //if cell is not in the list, then add it
                            if (!tmap.containsKey(stringMap[move.row][move.col])) {
                                tmap.put(stringMap[move.row][move.col], move);
                                bestMoves.add(move);
                            }
                            else {//else update it
                                move = tmap.get(stringMap[move.row][move.col]);
                                if(move.player == player) {
                                    move.score += temp.size();
                                }
                            }
                        }
                    }
                    else{
                        //if(counter_1 < 1)
                        if(rowMax <= 3)
                            traverseMoves(tmap, cells, opponent, rowMax, colMax, count);
                        else{
                            if(counter_1 < 1){
                                traverseMoves(tmap, cells, player, rowMax, colMax, count);
                            }
                        }
                    }

                    cells[i][j].state = STATE_UNUSED;
                }
            }
        }

        return bestMoves;
    }


    public static ArrayList<SingleMoveAIResult> checkForWin(TictactoeBox[][] cells, int player, int rowMax, int colMax, int count){
        ArrayList<SingleMoveAIResult> winningMoves = new ArrayList<>();
        GameResult result;

        for(int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getState() == STATE_UNUSED) {

                    cells[i][j].state = player;
                    result = ComboChecker.checkIfWon(cells, rowMax, colMax, count);
                    cells[i][j].state = STATE_UNUSED;

                    if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                        //TODO:this also adds if the move is a draw, which we kinda dont need atm
                        winningMoves.add(new SingleMoveAIResult(player, 1, cells[i][j]));
                    }
                }
            }
        }

        return winningMoves;
    }

    public static SingleMoveAIResult getBestOfTheBest(ArrayList<SingleMoveAIResult> bestMoves, boolean bRandomIfSame){
        boolean bAllSame = true;
        SingleMoveAIResult theOne = null;

        if (bestMoves.size() > 0) {
            theOne = bestMoves.get(0);
            for (int i = 1; i < bestMoves.size(); i++) {
                if (bestMoves.get(i).score > theOne.score) {
                    theOne = bestMoves.get(i);
                    bAllSame = false;
                }
            }

            if(bAllSame && bRandomIfSame)
                return getRandomMove(bestMoves);
            else
                return bestMoves.get(0);
        }

        return theOne;
    }

    public static SingleMoveAIResult getBestOfTheBest(TreeMap<String, SingleMoveAIResult> tmap) {
        SingleMoveAIResult best = new SingleMoveAIResult(STATE_UNUSED, 0, 0, 0, null);

        if(tmap != null) {
            for (Map.Entry<String, SingleMoveAIResult> entry : tmap.entrySet()) {
                if (entry.getValue().score > best.score) {
                    best = entry.getValue();
                }
            }
        }

        return best;
    }

    public static SingleMoveAIResult getRandomMove(ArrayList<SingleMoveAIResult> moves){
        SingleMoveAIResult chosenMove = null;

        if(moves != null) {
            Random random = new Random();
            chosenMove = moves.get(random.nextInt(moves.size()));
        }

        return chosenMove;
    }

    public static TictactoeBox getRandomEmptyCell(TictactoeBox[][] cells) {
        ArrayList<TictactoeBox> empty_cells = getUnusedCells(cells);

        if(empty_cells != null){
            if(empty_cells.size() > 1) {
                Random random = new Random();
                return empty_cells.get(random.nextInt(empty_cells.size()));
            }else
                return empty_cells.get(0);
        }

        return null;
    }

    public static int[][] getCellsClone(TictactoeBox[][] cells){
        int[][] new_cells = new int[cells.length][cells[0].length];

        for(int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                new_cells[row][col] = cells[row][col].getState();
            }
        }
        return new_cells;
    }
}
