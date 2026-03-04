package games._03_mine_sweep._02_clean.game_engine;


import java.util.LinkedList;
import java.util.Queue;

import games._03_mine_sweep._02_clean.components.Board;
import games._03_mine_sweep._02_clean.components.Cell;
import games._03_mine_sweep._02_clean.game_state.GameState;

public class Game {
    private final Board board;
    private GameState state;
    private int revealedCellsCount;

    public Game(Board board) {
        this.board = board;
        this.state = GameState.PLAYING;
        this.revealedCellsCount = 0;
    }

    public GameState click(int r, int c) {
        if (state != GameState.PLAYING) return state;

        Cell cell = board.getCell(r, c);

        // Can't reveal a flagged or already revealed cell
        if (cell.isFlagged() || cell.isRevealed()) return state;

        if (cell.isMine()) {
            cell.setRevealed(true);
            state = GameState.LOST;
            return state;
        }

        revealCellCascade(cell);
        checkWinCondition();
        
        return state;
    }

    public void flag(int r, int c) {
        if (state != GameState.PLAYING) return;
        
        Cell cell = board.getCell(r, c);
        if (!cell.isRevealed()) {
            cell.toggleFlag();
        }
    }

    private void revealCellCascade(Cell startCell) {
        Queue<Cell> queue = new LinkedList<>();
        
        // Fix: Mark as revealed BEFORE adding to queue to prevent duplicates
        startCell.setRevealed(true);
        revealedCellsCount++;
        queue.offer(startCell);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            // Only cascade if the current cell has 0 adjacent mines
            if (current.getAdjacentMines() == 0) {
                for (Cell neighbor : board.getNeighbors(current)) {
                    if (!neighbor.isRevealed() && !neighbor.isFlagged() && !neighbor.isMine()) {
                        neighbor.setRevealed(true);
                        revealedCellsCount++;
                        queue.offer(neighbor);
                    }
                }
            }
        }
    }

    private void checkWinCondition() {
        int totalCells = board.getRows() * board.getCols();
        int safeCells = totalCells - board.getTotalBombs();
        
        if (revealedCellsCount == safeCells) {
            state = GameState.WON;
        }
    }

    public GameState getState() { return state; }
    public Board getBoard() { return board; }
}