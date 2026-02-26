package games._01_tic_tac_toe._02_.strategies;

import games._01_tic_tac_toe._02_.components.Board;
import games._01_tic_tac_toe._02_.components.Symbol;

public 
class ValidPositionStrategy implements PostionStrategy{

    @Override
    public boolean valid(Board board, int i, int j) {
        if(i>=board.getSize() || j>=board.getSize() || i<0 || j<0) return false;
        return board.getCell(i, j).getSymbol()==Symbol.EMPTY.getSymbol();
    }
    
}