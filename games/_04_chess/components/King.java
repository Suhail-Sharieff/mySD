package games._04_chess.components;

import java.util.ArrayList;
import java.util.List;

import games._04_chess.enums.Color;

public class King extends Pawn{
     public King(Color color) {
        super(color);
    }

     @Override
    public List<int[]> getListOfPossibleMoves(Board board,int currX,int currY) {
        int dirs[][]={{-1,-1},{-1,0},{-1,1},{1,1},{1,0},{1,1},{0,-1},{0,1}};
        List<int[]>li=new ArrayList<>();
        for(int dir[]:dirs) if(isValidPosition(currX+dir[0], currY+dir[1], board)) li.add(new int[]{currX+dir[0],currY+dir[1]});
        return li;
    }

    @Override
    public List<int[]> getListOfPossibleAttacks(Board board,int currX,int currY) {
        return getListOfPossibleMoves(board, currX, currY).stream().filter(pos->board.hasPawn(pos[0], pos[1])).toList();
    }


    @Override
    public String toString() {
        return getColor()==Color.Black?"♔":"♚";
    }

}
