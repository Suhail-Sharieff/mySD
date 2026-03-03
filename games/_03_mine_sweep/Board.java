package games._03_mine_sweep;

import java.util.List;

public class Board {

    private int mat[][];
    private boolean isVisible[][];
    private final List<int[]>bombPositions;
    private final int nRows;
    private final int nCols;
    private int nVisible;

    public Board(int nRows,int nCols,List<int[]>bombPositions) {
        this.nRows=nRows;
        this.nCols=nCols;
        mat=new int[nRows][nCols];
        isVisible=new boolean[nRows][nCols];
        this.bombPositions=bombPositions;
        placeBombs();
    }

    private void placeBombs(){
        for(int [] pos:bombPositions){
            int x=pos[0];
            int y=pos[1];
            if(hasBomb(x, y)) continue;//already has bobm
            mat[x][y]=-1;
            for(int i=-1;i<=1;i++){
                for(int j=-1;j<=1;j++){
                    int nx=x+i;
                    int ny=y+j;
                    if(isValidPosition(nx, ny)){
                        if(!hasBomb(nx, ny)){
                            mat[nx][ny]++;
                        }
                    }
                }
            }
        }
    }




    public boolean isValidPosition(int i,int j){
        return i>=0 && j>=0 && i<nRows && j<nCols;
    }


    public boolean isVisibleCell(int i,int j){
        return isVisible[i][j];
    }

    public boolean hasBomb(int i,int j){
        return mat[i][j]==-1;
    }

    public void setVisible(int i,int j){
        if(isVisibleCell(i, j)) return;//otherwise v will overcount nVisible
        nVisible++;
        isVisible[i][j]=true;
    }

    public boolean allSafePositionsVisited(){
        System.out.println(nVisible);
        return nRows*nCols==nVisible+bombPositions.size();
    }

    public boolean haveBombAroundMe(int i,int j){
        return mat[i][j]!=0;
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                if(isVisibleCell(i, j)){
                    sb.append(mat[i][j]);
                }
                else{
                    sb.append('.');
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void reveal(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                if(hasBomb(i, j)) sb.append('B');
                else if(isVisibleCell(i, j)){
                    sb.append(mat[i][j]);
                }
                else{
                    sb.append('.');
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

}
