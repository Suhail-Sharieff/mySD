package games._04_chess.components;


import games._04_chess.actions.Move;
import games._04_chess.enums.Color;

public abstract class Pawn implements Move{
    private final Color color;
    public Color getColor() {
        return color;
    }
    public Pawn(Color color) {
        this.color=color;
    }
    protected boolean isValidPosition(int x,int y,Board board){
        return x<board.getnRows() && y<board.getnCols() && x>=0 && y>=0;
    }
    @Override
    public String toString() {
        return ". ";
    }
}
