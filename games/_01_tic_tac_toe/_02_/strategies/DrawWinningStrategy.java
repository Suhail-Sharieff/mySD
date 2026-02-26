package games._01_tic_tac_toe._02_.strategies;

import games._01_tic_tac_toe._02_.components.Board;
import games._01_tic_tac_toe._02_.components.Symbol;

public 
class DrawWinningStrategy implements DrawStrategy{
    @Override
    public boolean checkDraw(Board board) {
        for(int i=0;i<board.getSize();i++) for(int j=0;j<board.getSize();j++) if(board.getCell(i, j).getSymbol()==Symbol.EMPTY.getSymbol()) return false;
        return true;
    }
}