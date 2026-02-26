package games._01_tic_tac_toe;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;


enum Symbol{
    EMPTY('#');
    private final char symbol;
    Symbol(char c) {
        this.symbol=c;
    }
    public char getSymbol() {
        return symbol;
    }
    
}

class Cell{
    private char symbol;
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
    public Cell(char symbol){
        this.symbol=symbol;
    }
    public char getSymbol() {
        return symbol;
    }
    
}

class Board{
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
}
class Player{
    private final int id;
    private final char symbol;
    private int score;
    public Player(int id, char symbol) {
        this.id = id;
        this.symbol = symbol;
        this.score=0;
    }
    public int getId() {
        return id;
    }
    public char getSymbol() {
        return symbol;
    }
    public int getScore(){
        return this.score;
    }
    public void incScore(){
        this.score++;
    }
    public void resetScore(){
        this.score=0;
    }
}

interface PostionStrategy{
    boolean valid(Board board,int i,int j);
}


class ValidPositionStrategy implements PostionStrategy{

    @Override
    public boolean valid(Board board, int i, int j) {
        if(i>=board.getSize() || j>=board.getSize() || i<0 || j<0) return false;
        return board.getCell(i, j).getSymbol()==Symbol.EMPTY.getSymbol();
    }
    
}

interface WinningStrategy {
    boolean checkWin(Board board);
    default boolean allSame(Cell arr[]){
        for(int i=1;i<arr.length;i++) if(arr[i].getSymbol()!=Symbol.EMPTY.getSymbol() && arr[i].getSymbol()!=arr[i-1].getSymbol()) return false;
        return true;
    }
}
interface DrawStrategy{
    boolean checkDraw(Board board);
}

class RowWinningStrategy implements WinningStrategy{
    @Override
    public boolean checkWin(Board board) {
        for(int i=0;i<board.getSize();i++) if(allSame(board.getRow(i))) return true;
        return false;
    }
}
class DrawWinningStrategy implements DrawStrategy{
    @Override
    public boolean checkDraw(Board board) {
        for(int i=0;i<board.getSize();i++) for(int j=0;j<board.getSize();j++) if(board.getCell(i, j).getSymbol()==Symbol.EMPTY.getSymbol()) return false;
        return true;
    }
}
class ColWinningStrategy implements WinningStrategy{
    @Override
    public boolean checkWin(Board board) {
        for(int i=0;i<board.getSize();i++) if(allSame(board.getColumn(i))) return true;
        return false;
    }
}

class Game{
    private final Board board;
    private boolean isOver;
    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }
    private Deque<Player>players;
    private final List<Player>origOrder;
    private final List<PostionStrategy>postionStrategy;
    private final List<WinningStrategy>winningStrategies;
    private final List<DrawStrategy>drawStrategies;
    public Game(Board board,List<Player>players,List<PostionStrategy>positionStrategs,List<WinningStrategy>winningStrategies,List<DrawStrategy>drawStrategies) {
        this.board=board;
        this.isOver=false;
        this.players=new LinkedList<>(players);
        this.postionStrategy=positionStrategs;
        this.winningStrategies=winningStrategies;
        this.drawStrategies=drawStrategies;
        this.origOrder=players;
    }
    public void play(int i,int j){
        if(isOver){System.out.println("Game over already!");return;}
        if(!postitionStrategiesSatisfied(i, j)) {System.out.println("Invalid move!");return;}

        Player currPlayer=players.pollFirst();
        players.addLast(currPlayer);
        board.setCell(i, j, currPlayer.getSymbol());

        if(winningStrategiesSatisfied(i, j)){
            System.out.println(currPlayer+" won the game!");
            currPlayer.incScore();
            setOver(true);
            return;
        }
        if (drawStrategiesSatisfied(i, j)) {
            System.out.println("Game DRAW");
            return;
        }
    }
    private boolean postitionStrategiesSatisfied(int i,int j){
        for(PostionStrategy Strategy:postionStrategy) if(!Strategy.valid(board, i, j)) return false;
        return true;
    }
    private boolean winningStrategiesSatisfied(int i,int j){
        for(WinningStrategy Strategy:winningStrategies) if(!Strategy.checkWin(board))return true;
        return false;
    }
    private boolean drawStrategiesSatisfied(int i,int j){
        for(DrawStrategy Strategy:drawStrategies) if(!Strategy.checkDraw(board))return true;
        return false;
    }
    public void resetGame(){
        this.board.reset();
        setOver(false);
        this.players=new LinkedList<>(origOrder);
    }
    public void restartGame(){
        this.board.reset();
        setOver(false);
        this.players=new LinkedList<>(origOrder);
        resetScores();
    }
    private void resetScores(){
        for(Player e:players) e.resetScore();
    }
}

public class _02_ {
    public static void main(String[] args) {
        
    }
}
