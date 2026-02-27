package games._02_snake_ladder;

public class Player{
    private char symbol;

    public Player(char symbol){
        this.symbol=symbol;
    }


    public char getSymbol() {
        return symbol;
    }
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
   
    @Override
    public String toString() {
        return symbol+"";
    }
}

