package games._01_tic_tac_toe._02_.components;

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

