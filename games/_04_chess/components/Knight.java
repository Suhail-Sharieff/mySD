package games._04_chess.components;

import java.util.List;

import games._04_chess.enums.Color;

public class Knight extends Pawn {
    

    public Knight(Color color) {
        super(color);
    }

    @Override
    public List<int[]> getListOfPossibleMoves(Board board,int currX,int currY) {
        int x=currX;
        int y=currY;
        return List.of(
            new int[]{x-2,y-1},
            new int[]{x-2,y+1},

            new int[]{x-1,y-2},
            new int[]{x-1,y+2},

            new int[]{x+1,y-2},
            new int[]{x+1,y+2},

            new int[]{x+2,y-1},
            new int[]{x+2,y+1}
           
        ).stream().filter(pos->isValidPosition(pos[0], pos[1], board)).toList();
    }

    @Override
    public List<int[]> getListOfPossibleAttacks(Board board,int currX,int currY) {
       return getListOfPossibleMoves(board,currX,currY).stream().filter(pos->board.hasPawn(pos[0], pos[1])).toList();
    }

    @Override
    public String toString() {
        return getColor()==Color.Black?"♘":"♞";
    }
    
}
