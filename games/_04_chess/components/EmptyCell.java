package games._04_chess.components;

import java.util.List;

import games._04_chess.enums.Color;

public class EmptyCell extends Pawn{
    public EmptyCell(Color color) {
        super(Color.None);

    }

    private static EmptyCell instance;
    
    static Pawn getEmptyCell(){
        if(instance==null) instance=new EmptyCell(Color.None);
        return instance;
    }

    @Override
    public List<int[]> getListOfPossibleMoves(Board board, int currX, int currY) {
       return List.of();
    }

    @Override
    public List<int[]> getListOfPossibleAttacks(Board board, int currX, int currY) {
        return List.of();
    }
}
