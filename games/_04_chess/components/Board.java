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
        return !mat[x][y].equals(EmptyCell.getEmptyCell());
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("------------------------\n").append("  ");
        sb.append(" 0  1  2  3  4  5  6  7");
        sb.append("\n");
        for(int i=0;i<nRows;i++){
            sb.append(i).append(" ").append(Arrays.toString(mat[i]).replaceAll(",", ""));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String printGoables(int currX,int currY){
        Pawn pawn=getPawn(currX, currY);
        boolean movable[][]=new boolean[nRows][nCols];
        boolean beatable[][]=new boolean[nRows][nCols];
        for(int pos[]:pawn.getListOfPossibleAttacks(this, currX, currY)) beatable[pos[0]][pos[1]]=true;

        for(int pos[]:pawn.getListOfPossibleMoves(this, currX, currY)) movable[pos[0]][pos[1]]=true;

        String ans[][]=new String[nRows][nCols];
        StringBuilder sb=new StringBuilder("------------------------\n").append("  ");
        sb.append(" 0  1  2  3  4  5  6  7");
        sb.append("\n");
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                Pawn p=mat[i][j];
                if(beatable[i][j]) ans[i][j]="$ ";
                else if(movable[i][j]) ans[i][j]="* ";
                else ans[i][j]=p.toString();
            }
            sb.append(i).append(" ").append(Arrays.toString(ans[i])).append("\n");
        }
        return sb.toString().replaceAll(",", "");
    }
}
