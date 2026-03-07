package games._04_chess.actions;

import java.util.List;

import games._04_chess.components.Board;
import games._04_chess.components.Pawn;

public interface Move {
    List<int[]> getListOfPossibleMoves(Board board,int currX,int currY);
    List<int[]> getListOfPossibleAttacks(Board board,int currX,int currY);
}
