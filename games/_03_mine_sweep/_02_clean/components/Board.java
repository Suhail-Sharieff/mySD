package games._03_mine_sweep._02_clean.components;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int nRows;
    private final int nCols;
    private final Cell[][] grid;
    private final int totalBombs;

    public Board(int nRows, int nCols, List<int[]> bombPositions) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.grid = new Cell[nRows][nCols];
        this.totalBombs = bombPositions.size();
        
        initializeGrid();
        placeBombs(bombPositions);
    }

    private void initializeGrid() {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    private void placeBombs(List<int[]> bombPositions) {
        for (int[] pos : bombPositions) {
            int r = pos[0];
            int c = pos[1];
            if (isValidPosition(r, c) && !grid[r][c].isMine()) {
                grid[r][c].setMine(true);
                updateAdjacentCounts(r, c);
            }
        }
    }

    private void updateAdjacentCounts(int mineRow, int mineCol) {
        for (Cell neighbor : getNeighbors(grid[mineRow][mineCol])) {
            if (!neighbor.isMine()) {
                neighbor.incrementAdjacentBombs();
            }
        }
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dRow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dCol = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = cell.getRow() + dRow[i];
            int newCol = cell.getCol() + dCol[i];
            if (isValidPosition(newRow, newCol)) {
                neighbors.add(grid[newRow][newCol]);
            }
        }
        return neighbors;
    }

    private boolean isValidPosition(int r, int c) {
        return r >= 0 && c >= 0 && r < nRows && c < nCols;
    }

    public Cell getCell(int r, int c) {
        if (!isValidPosition(r, c)) throw new IllegalArgumentException("Out of bounds");
        return grid[r][c];
    }

    public int getRows() { return nRows; }
    public int getCols() { return nCols; }
    public int getTotalBombs() { return totalBombs; }
}