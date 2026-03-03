package games._03_mine_sweep;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int nRows=8,nCols=8;
        List<int[]>bombPositions=List.of(
            new int[]{0,1},
            new int[]{0,5},
            new int[]{1,0},
            new int[]{1,1},
            new int[]{1,5},
            new int[]{3,1},
            new int[]{3,2},
            new int[]{3,5},
            new int[]{7,1},
            new int[]{7,7}
        );
        Board board=new Board(nRows, nCols, bombPositions);
        System.out.println(board);

        Game game=new Game(board);

        game.click(5, 0);
        // game.click(7, 1);
        game.click(2, 0);
        game.click(0, 0);
        game.click(0, 3);
        game.click(2, 1);
        game.click(2, 5);

        game.click(3, 3);
        game.click(3, 4);
        game.click(3, 0);
        game.click(7, 0);


    }
}
