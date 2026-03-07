package games._04_chess.components;

import java.util.ArrayList;
import java.util.List;

import games._04_chess.enums.Color;

public class Soldier extends Pawn {

    public Soldier(Color color) {
        super(color);
    }

    @Override
    public List<int[]> getListOfPossibleMoves(Board board, int currX, int currY) {
        List<int[]> li = new ArrayList<>();
        int nx = (super.getColor() == Color.Black) ? currX + 1 : currX - 1;
        int ny = currY;
        if (isValidPosition(nx, ny, board))
            li.add(new int[] { nx, ny });
        return li;
    }

    @Override
    public List<int[]> getListOfPossibleAttacks(Board board, int currX, int currY) {
        List<int[]> li = new ArrayList<>();
        int nx = (super.getColor() == Color.Black) ? currX + 1 : currX - 1;
        int ny = currY - 1;
        if (isValidPosition(nx, ny, board))
            li.add(new int[] { nx, ny });
        nx = (super.getColor() == Color.Black) ? currX + 1 : currX - 1;
        ;
        ny = currY + 1;
        if (isValidPosition(nx, ny, board))
            li.add(new int[] { nx, ny });
        return li.stream().filter(
                pos -> board.hasPawn(pos[0], pos[1]) && board.getPawn(pos[0], pos[1]).getColor() != this.getColor())
                .toList();
    }

    @Override
    public String toString() {
        return getColor() == Color.Black ? "♙" : "♟";
    }

}
