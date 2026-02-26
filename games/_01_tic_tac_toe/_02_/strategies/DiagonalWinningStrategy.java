package games._01_tic_tac_toe._02_;

public 
class DiagonalWinningStrategy implements WinningStrategy{

    @Override
    public boolean checkWin(Board board) {
        Cell[]diag1=new Cell[board.getSize()];
        Cell[]diag2=new Cell[board.getSize()];
        int k1=0,k2=0;
        int i=0,j1=0,j2=board.getSize()-1;
        while (i<board.getSize() && j1<board.getSize() && j2>=0) {
            diag1[k1++]=board.getCell(i, j1++);
            diag2[k2++]=board.getCell(i, j2--);
            i++;
        }
        return allSame(diag1) || allSame(diag2);
    }
    
}