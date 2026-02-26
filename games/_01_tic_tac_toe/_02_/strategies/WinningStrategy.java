package games._01_tic_tac_toe._02_;

public 
interface WinningStrategy {
    boolean checkWin(Board board);
    default boolean allSame(Cell arr[]){
        if(arr[0].getSymbol() == Symbol.EMPTY.getSymbol()) return false;

        for(int i=1;i<arr.length;i++)
            if(arr[i].getSymbol() != arr[0].getSymbol())
                return false;

        return true;
    }
}