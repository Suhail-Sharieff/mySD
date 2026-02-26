package games._01_tic_tac_toe._02_;

public 
class Cell{
    private char symbol;
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
    public Cell(char symbol){
        this.symbol=symbol;
    }
    public char getSymbol() {
        return symbol;
    }
    @Override
    public String toString() {
        return symbol+" ";
    }
    
}
