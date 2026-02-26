package games._01_tic_tac_toe._02_;

public enum Symbol{
    EMPTY('#');
    private final char symbol;
    Symbol(char c) {
        this.symbol=c;
    }
    public char getSymbol() {
        return symbol;
    }
    
}

