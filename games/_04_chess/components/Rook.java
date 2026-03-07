package games._04_chess.components;

import java.util.ArrayList;
import java.util.List;

import games._04_chess.enums.Color;

public class Rook extends Pawn {

    public Rook(Color color) {
        super(color);
    }

    @Override
    public List<int[]> getListOfPossibleMoves(Board board,int currX,int currY) {
       List<int[]>li=new ArrayList<>();
       for(int i=currX-1;i>=0;i--)  li.add(new int[]{i,currY});
       for(int i=currX+1;i<board.getnRows();i++) li.add(new int[]{i,currY});
       for(int j=currY-1;j>=0;j--) li.add(new int[]{currX,j});
       for(int j=currY+1;j<board.getnCols();j++) li.add(new int[]{currX,j});
       return li;
    }

   @Override
    public List<int[]> getListOfPossibleAttacks(Board board,int currX,int currY) {
       return getListOfPossibleMoves(board,currX,currY).stream().filter(pos->board.hasPawn(pos[0], pos[1])).toList();
    }


    @Override
    public String toString() {
        return getColor()==Color.Black?"♖":"♜";
    }
    
}
