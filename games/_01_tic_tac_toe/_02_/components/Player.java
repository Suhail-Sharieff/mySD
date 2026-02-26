package games._01_tic_tac_toe._02_;



public class Player{
    private final int id;
    private final char symbol;
    private int score;
    public Player(int id, char symbol) {
        this.id = id;
        this.symbol = symbol;
        this.score=0;
    }
    public int getId() {
        return id;
    }
    public char getSymbol() {
        return symbol;
    }
    public int getScore(){
        return this.score;
    }
    public void incScore(){
        this.score++;
    }
    public void resetScore(){
        this.score=0;
    }
    @Override
    public String toString() {
        return "Player [id=" + id + ", symbol=" + symbol + ", score=" + score + "]";
    }
    
}
