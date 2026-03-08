package games._04_chess.strategy;

import games._04_chess.components.Board;
import games._04_chess.enums.Color;

public interface GameStrategy {
    boolean inCheckmate(Color myColor,Board board);
    boolean inStalemate(Color myColor,Board board);
    boolean canResultInCheckForMe(Color myColor,int fromX,int fromY,int toX,int toY,Board board);
    boolean inCheck(Color myColor,Board board);
}
