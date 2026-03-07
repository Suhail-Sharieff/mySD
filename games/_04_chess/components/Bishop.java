package games._04_chess.components;

import java.util.ArrayList;
import java.util.List;

import games._04_chess.enums.Color;

public class Bishop extends Pawn{

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public List<int[]> getListOfPossibleAttacks(Board board,int currX,int currY) {
       return getListOfPossibleMoves(board,currX,currY).stream().filter(pos->board.hasPawn(pos[0], pos[1])).toList();
    }

    @Override
    public List<int[]> getListOfPossibleMoves(Board board,int currX,int currY) {
        int x=currX;
        int y=currY;
        List<int[]>li=new ArrayList<>();
        for(int i=x-1,j=y+1;i>=0 && j<board.getnCols();i--,j++) li.add(new int[]{i,j});//i--,j++
        for(int i=x+1,j=y+1;i<board.getnRows() && j<board.getnCols();i++,j++) li.add(new int[]{i,j});//i++,j++
        for(int i=x-1,j=y-1;i>=0 && j>=0;i--,j--) li.add(new int[]{i,j});//i--,j--
        for(int i=x+1,j=y-1;i<board.getnRows() && j>=0;i++,j--) li.add(new int[]{i,j});//i++,j--
        return li;
    }

     @Override
    public String toString() {
        return getColor()==Color.Black?"♗":"♝";
    }
    
}
