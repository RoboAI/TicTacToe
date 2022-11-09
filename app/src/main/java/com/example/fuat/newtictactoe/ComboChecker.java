package com.example.fuat.newtictactoe;

import java.util.ArrayList;

import static com.example.fuat.newtictactoe.GameGlobals.GAME_ENDED_DRAW;
import static com.example.fuat.newtictactoe.GameGlobals.GAME_NOT_ENDED;
import static com.example.fuat.newtictactoe.GameGlobals.GAME_NO_MATCH;
import static com.example.fuat.newtictactoe.GameGlobals.STATE_UNUSED;
import static com.example.fuat.newtictactoe.GameGlobals.convertStateToGameResult;

public class ComboChecker {

    public static GameResult checkCombo(TictactoeBox[][] _cells, int row, int col, int rowMax, int colMax, int rowInc, int colInc, int count) {
        int counter = count;
        ArrayList<TictactoeBox> winning_cells = new ArrayList<>();

        if (counter - 1 <= 0) {
            return new GameResult(GAME_NO_MATCH, null);
        }

        while (row + rowInc < rowMax && col + colInc < colMax && row + rowInc >= 0 && col + colInc >= 0) {
            if (_cells[row][col].state == _cells[row + rowInc][col + colInc].state &&
                    _cells[row][col].state != STATE_UNUSED) {

                winning_cells.add(_cells[row][col]);
                counter--;

                if (counter == 1) {
                    winning_cells.add(_cells[row + rowInc][col + colInc]);
                    TictactoeBox[] cells_won = winning_cells.toArray(new TictactoeBox[winning_cells.size()]);
                    return new GameResult(convertStateToGameResult(_cells[row][col].getState()), cells_won);
                }
            } else {
                winning_cells.clear();
                counter = count;
            }
            row += rowInc;
            col += colInc;
        }

        return new GameResult(GAME_NO_MATCH, null);
    }

    public static GameResult checkIfWon(TictactoeBox[][] cells, int maxRow, int maxCol, int comboCount) {
        GameResult result;

        //check for horizontals
        for (int i = 0; i < maxRow; i++) {
            result = checkCombo(cells, i, 0, maxRow, maxCol, 0, 1, comboCount);
            if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                return result;
            }
        }

        //check for verticals
        for (int i = 0; i < maxCol; i++) {
            result = checkCombo(cells, 0, i, maxRow, maxCol, 1, 0, comboCount);
            if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                return result;
            }
        }

        //check for diagonal (bottom-left to top-left) going right and down
        for (int i = 0; i < maxRow; i++) {
            result = checkCombo(cells, i, 0, maxRow, maxCol, 1, 1, comboCount);
            if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                return result;
            }
        }

        //check for diagonal (top-left to top-right) going right and down
        for (int i = 0; i < maxCol; i++) {
            result = checkCombo(cells, 0, i, maxRow, maxCol, 1, 1, comboCount);
            if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                return result;
            }
        }

        //check for diagonal (top-left to top-right) going down and left
        for (int i = 0; i < maxCol; i++) {
            result = checkCombo(cells, 0, i, maxRow, maxCol, 1, -1, comboCount);
            if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                return result;
            }
        }

        //check for diagonal (top-right to bottom-right) going down and left
        for (int i = 0; i < maxRow; i++) {
            result = checkCombo(cells, i, maxCol - 1, maxRow, maxCol, 1, -1, comboCount);
            if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                return result;
            }
        }

        //if we reached here, then it means its either a draw or game is not finished (there are empty cells)
        //so first check for empty cells
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                if (cells[i][j].getState() == STATE_UNUSED) {
                    return new GameResult(GAME_NOT_ENDED, null);
                }
            }
        }

        //if all checks are false, then it means the game is a Draw
        return new GameResult(GAME_ENDED_DRAW, null);
    }
}

/*
////////////////////////////////////////////////////////

    public static int[][] getHorizontalLoopHelperArray(int maxRow, int maxCol) {
        int[][] comboHelper = new int[][]{
                {0, 0, 1},
                {0, 1, 1},
                {maxCol - 1, 1, -1}};

        return comboHelper;
    }

    public static int[][] getVerticalLoopHelperArray(int maxRow, int maxCol) {
        int[][] comboHelper = new int[][]{
                {0, 1, 0},
                {0, 1, 1},
                {0, 1, -1}};

        return comboHelper;
    }

    //TODO:this function should have an in-parameter current_row, and current_col to have more control
    //TODO:of the checking
    public static GameResult checkIfWon2(TictactoeCell[][] cells, int row, int col, int maxRow, int maxCol, int comboCount) {
        GameResult result;
        int[][] loopHelper;

        //check for horizontals
        loopHelper = getHorizontalLoopHelperArray(maxRow, maxCol);
        for (int i = 0; i < loopHelper.length; i++) {
            for (int j = 0; j < maxRow; j++) {
                result = checkCombo(cells, j, loopHelper[i][0], maxRow, maxCol, loopHelper[i][1], loopHelper[i][2], comboCount);
                if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                    return result;
                }
            }
        }

        //check for verticals
        loopHelper = getVerticalLoopHelperArray(maxRow, maxCol);
        for (int i = 0; i < loopHelper.length; i++) {
            for (int j = 0; j < maxCol; j++) {
                result = checkCombo(cells, loopHelper[i][0], j, maxRow, maxCol, loopHelper[i][1], loopHelper[i][2], comboCount);
                if (result.result != GAME_NOT_ENDED && result.result != GAME_NO_MATCH) {
                    return result;
                }
            }
        }

        //if we reached here, then it means its either a draw or game is not finished (there are empty cells)
        //so first check for 'empty cells'
        boolean bDraw = true;
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                if (cells[i][j].getState() == STATE_UNUSED) {
                    return new GameResult(GAME_NOT_ENDED, null);
                }
            }
        }

        //if all checks are false, then it means the game is a 'Draw'
        return new GameResult(GAME_ENDED_DRAW, null);
    }


////////////////////////////////////////////////////////
*/


/*

    public int checkCombo(Connect3Cell[] buttons){
        if ((buttons[0].getState() == buttons[1].getState()) &&
                (buttons[0].getState() == buttons[2].getState()) &&
                (buttons[0].getState() != Connect3Cell.STATE_UNUSED)) {

            if (buttons[0].getState() == Connect3Cell.STATE_X)
                return Connect3Cell.GAME_ENDED_X_WON;
            else
                return Connect3Cell.GAME_ENDED_O_WON;
        }
        return Connect3Cell.GAME_NOT_ENDED;
    }

    public GameResult checkIfWon(){
        int result;
        Connect3Cell[] combo;
        boolean isAnyUnusedCell = true;

        //check rows and columns
        for(int i=0; i < 3; i++) {
            combo = cells[i];
            if((result = checkCombo(combo)) != Connect3Cell.GAME_NOT_ENDED)
                return new GameResult(result, combo);

            combo = new Connect3Cell[]{cells[0][i], cells[1][i], cells[2][i]};
            if((result = checkCombo(combo)) != Connect3Cell.GAME_NOT_ENDED)
                return new GameResult(result, combo);
        }

        //check diagonal top-left to bottom-right
        combo = new Connect3Cell[]{cells[0][0], cells[1][1], cells[2][2]};
        if((result = checkCombo(combo)) != Connect3Cell.GAME_NOT_ENDED)
            return new GameResult(result, combo);

        //check diagonal top-right to bottom-left
        combo = new Connect3Cell[]{cells[0][2], cells[1][1], cells[2][0]};
        if((result = checkCombo(combo)) != Connect3Cell.GAME_NOT_ENDED)
            return new GameResult(result, combo);

        //check for draw
        isAnyUnusedCell = false;
        for(int i = 0; i < 3 && !isAnyUnusedCell; i++){
            for(int j = 0; j < 3; j++){
                if(cells[i][j].getState() == Connect3Cell.STATE_UNUSED) {
                    isAnyUnusedCell = true;
                    break;
                }
            }
        }

        //if we have reached here, then it means there are no winners (maybe still an empty box).
        //So check for an empty box

        if(isAnyUnusedCell) {//if there is an empty box, then carry on the game
            return new GameResult(Connect3Cell.GAME_NOT_ENDED, null);
        }
        else {//else the game is a draw
            return new GameResult(Connect3Cell.GAME_ENDED_DRAW, null);
        }
    }

    public int checkCombo2(Connect3Cell[][] _cells, int counter, int row, int col, int rowInc, int colInc, int rowMax, int colMax){
        if(counter <= 0)
            return Connect3Cell.GAME_NOT_ENDED;

        if(row > rowMax || col > colMax || row + rowInc > rowMax || col + colInc > colMax)
            return Connect3Cell.STATE_UNUSED;

        if(_cells[row][col].state == _cells[row + rowInc][col + colInc].state &&
                _cells[row][col].state != Connect3Cell.STATE_UNUSED){

            counter--;
            if(counter == 1) {
                if(_cells[row][col].state == Connect3Cell.STATE_X)
                    return Connect3Cell.GAME_ENDED_X_WON;
                else
                    return Connect3Cell.GAME_ENDED_O_WON;
            }

            return checkCombo2(_cells, counter, row + rowInc, col + colInc, rowInc, colInc, rowMax, colMax);
            //TODO: this function should return an array (ArrayList maybe?) of the winning cells
            //      perhaps fill the array with Connect3Cells as it goes along and then if a 'win'
            //      occurs then return the array, else discard
        }

        return Connect3Cell.STATE_UNUSED;

    }
  }

 */
