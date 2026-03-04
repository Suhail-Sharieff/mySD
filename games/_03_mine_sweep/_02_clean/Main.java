package games._03_mine_sweep._02_clean;


import java.util.List;

import games._03_mine_sweep._02_clean.components.Board;
import games._03_mine_sweep._02_clean.components.Cell;
import games._03_mine_sweep._02_clean.game_engine.Game;
import games._03_mine_sweep._02_clean.game_state.GameState;

public class Main {
    public static void main(String[] args) {
        int nRows = 8, nCols = 8;
        List<int[]> bombPositions = List.of(
            new int[]{0, 1}, new int[]{0, 5}, new int[]{1, 0}, new int[]{1, 1},
            new int[]{1, 5}, new int[]{3, 1}, new int[]{3, 2}, new int[]{3, 5},
            new int[]{7, 1}, new int[]{7, 7}
        );

        Board board = new Board(nRows, nCols, bombPositions);
        Game game = new Game(board);

        // Simulate playing the game
        playMove(game, 5, 0);
        playMove(game, 2, 0);
        playMove(game, 0, 0);
        
        // Try flagging a cell we suspect is a mine
        System.out.println("Flagging (0, 1)...\n");
        game.flag(0, 1);
        
        playMove(game, 0, 3);
        // ... add more moves to test win/loss
    }

    private static void playMove(Game game, int r, int c) {
        System.out.println("Clicking (" + r + ", " + c + ")...\n");
        GameState state = game.click(r, c);
        
        printBoard(game.getBoard(), state);
        
        if (state == GameState.WON) {
            System.out.println("\n🎉 You won the game!");
        } else if (state == GameState.LOST) {
            System.out.println("\n💥 Boom! You clicked on a bomb!");
        }
        System.out.println("------------------------------");
    }

    private static void printBoard(Board board, GameState state) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                Cell cell = board.getCell(i, j);
                
                // If game is over, reveal the unflagged mines
                if (state == GameState.LOST && cell.isMine() && !cell.isFlagged()) {
                    System.out.print("B ");
                } else if (cell.isFlagged()) {
                    System.out.print("F ");
                } else if (cell.isRevealed()) {
                    System.out.print(cell.getAdjacentMines() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}