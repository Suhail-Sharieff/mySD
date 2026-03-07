package games._04_chess.components;

import java.util.Arrays;

public class Board {
    private final int nRows;
    private final int nCols;
    private Pawn mat[][];
    public Board(int nRows,int nCols){
        this.nRows=nRows;
        this.nCols=nCols;
        mat=new Pawn[nRows][nCols];
        mat=new Pawn[nRows][nCols];
        for(var row:mat) Arrays.fill(row,EmptyCell.getEmptyCell());
    }
    public int getnRows() {
        return nRows;
    }
    
    public int getnCols() {
        return nCols;
    }
    public void set(int x,int y,Pawn pawn){
        mat[x][y]=pawn;
    }

    public Pawn getPawn(int i,int j){
        return mat[i][j];
    }
    public void move(Pawn pawn,int fromX,int fromY,int toX,int toY){
        mat[fromX][fromY]=EmptyCell.getEmptyCell();
        mat[toX][toY]=pawn;
    }
    public boolean hasPawn(int x,int y){
        return mat[x][y]!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("------------------------\n").append("  ");
        sb.append(" 0 1  2  3  4 5  6  7");
        sb.append("\n");
        for(int i=0;i<nRows;i++){
            sb.append(i).append(" ");
            for(int j=0;j<nCols;j++){
                Pawn c=mat[i][j];
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
