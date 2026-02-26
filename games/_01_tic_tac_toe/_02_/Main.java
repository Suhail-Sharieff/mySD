package games._01_tic_tac_toe._02_;



import java.util.Arrays;
import java.util.List;

import games._01_tic_tac_toe._02_.components.Board;
import games._01_tic_tac_toe._02_.components.Game;
import games._01_tic_tac_toe._02_.components.Player;
import games._01_tic_tac_toe._02_.strategies.ColWinningStrategy;
import games._01_tic_tac_toe._02_.strategies.DiagonalWinningStrategy;
import games._01_tic_tac_toe._02_.strategies.DrawStrategy;
import games._01_tic_tac_toe._02_.strategies.DrawWinningStrategy;
import games._01_tic_tac_toe._02_.strategies.PostionStrategy;
import games._01_tic_tac_toe._02_.strategies.RowWinningStrategy;
import games._01_tic_tac_toe._02_.strategies.ValidPositionStrategy;
import games._01_tic_tac_toe._02_.strategies.WinningStrategy;

public class Main {
    public static void main(String[] args) {
        List<Player>players=Arrays.asList(new Player(1, 'X'),new Player(2, 'O'));
        List<PostionStrategy>positionRules=List.of(new ValidPositionStrategy());
        List<WinningStrategy>winningRules=List.of(new RowWinningStrategy(),new ColWinningStrategy(),new DiagonalWinningStrategy());
        List<DrawStrategy>drawRules=List.of(new DrawWinningStrategy());

        int sz=3;
        Game game=new Game(new Board(sz), players, positionRules, winningRules, drawRules);
        game.play(0, 0); // X
        game.play(1, 0); // O
        game.play(0, 1); // X
        game.play(1, 1); // O
        game.play(0, 2); // X -> should win (row win)

        System.out.println("\nTrying move after game over:");
        game.play(2, 2); // Should say game over

        System.out.println("\nResetting game...");
        game.resetGame();

        // Simulate draw
        game.play(0, 0); // X
        game.play(0, 1); // O
        game.play(0, 2); // X
        game.play(1, 1); // O
        game.play(1, 0); // X
        game.play(1, 2); // O
        game.play(2, 1); // X
        game.play(2, 0); // O
        game.play(2, 2); // X -> should be DRAW



    }
}







