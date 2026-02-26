package games._01_tic_tac_toe._02_;

public
class ColWinningStrategy implements WinningStrategy{
    @Override
    public boolean checkWin(Board board) {
        for(int i=0;i<board.getSize();i++) if(allSame(board.getColumn(i))) return true;
        return false;
    }
}