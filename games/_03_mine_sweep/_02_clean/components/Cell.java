package games._03_mine_sweep._02_clean.components;


public class Cell {
    private final int row;
    private final int col;
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Getters and Setters
    public int getRow() { return row; }
    public int getCol() { return col; }
    
    public boolean isMine() { return isMine; }
    public void setMine(boolean mine) { isMine = mine; }
    
    public boolean isRevealed() { return isRevealed; }
    public void setRevealed(boolean revealed) { isRevealed = revealed; }
    
    public boolean isFlagged() { return isFlagged; }
    public void toggleFlag() { this.isFlagged = !this.isFlagged; }
    
    public int getAdjacentMines() { return adjacentMines; }
    public void setAdjacentMines(int adjacentMines) { this.adjacentMines = adjacentMines; }
    public void incrementAdjacentBombs() { this.adjacentMines++; }
}