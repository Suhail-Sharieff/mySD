package games._01_tic_tac_toe._02_;

public 
class RowWinningStrategy implements WinningStrategy{
    @Override
    public boolean checkWin(Board board) {
        for(int i=0;i<board.getSize();i++) if(allSame(board.getRow(i))) return true;
        return false;
    }
}