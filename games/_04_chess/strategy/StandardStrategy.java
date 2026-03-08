package games._04_chess.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games._04_chess.components.Board;
import games._04_chess.components.Pawn;
import games._04_chess.enums.Color;

public class StandardStrategy implements GameStrategy{

    @Override
    public boolean canResultInCheckForMe(Color myColor, int fromX, int fromY, int toX, int toY, Board board) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean inCheck(Color myColor, Board board) {
        Color otherColor=(myColor==Color.Black)?Color.White:Color.Black;
        HashMap<Pawn,int[]>otherColorPawns=board.getPawnsOfColor(otherColor);
        for(Map.Entry<Pawn,int[]>entry:otherColorPawns.entrySet()){
            Pawn p=entry.getKey();
            int pos[]=entry.getValue();
            int currX=pos[0];
            int currY=pos[1];
            List<int[]>myAttakingPositions=p.getListOfPossibleAttacks(board, currX, currY);
            for(int e[]:myAttakingPositions){
                if(board.hasKing(myColor, e[0], e[1])) return true;
            }
        }
        return false;
    }

    @Override
    public boolean inCheckmate(Color myColor, Board board) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean inStalemate(Color myColor, Board board) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
