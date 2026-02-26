package games._01_tic_tac_toe._02_.components;

import java.util.Arrays;

public class Board{
    private final int size;
    public int getSize() {
        return size;
    }
    private final Cell[][] board;
    public Board(int size) {
        this.size=size;
        this.board=new Cell[size][size];
        initBoard();
    }
    private void initBoard(){
        for(int i=0;i<size;i++) for(int j=0;j<size;j++) board[i][j]=new Cell(Symbol.EMPTY.getSymbol());
    }
    public Cell getCell(int i,int j){
        return board[i][j];
    }
    public void setCell(int i,int j,char symbol){
        board[i][j].setSymbol(symbol);;
    }
    public Cell[] getRow(int i){
        return board[i];
    }
    public Cell[] getColumn(int j){
        Cell[]col=new Cell[size];
        for(int i=0;i<size;i++) col[i]=board[i][j];
        return col;
    }
    public void reset(){
        initBoard();
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(Cell[]row:board) sb.append(Arrays.toString(row)).append("\n");
        return sb.toString();

    }
    
}