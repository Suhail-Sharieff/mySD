package games._01_tic_tac_toe._02_.components;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import games._01_tic_tac_toe._02_.strategies.DrawStrategy;
import games._01_tic_tac_toe._02_.strategies.PostionStrategy;
import games._01_tic_tac_toe._02_.strategies.WinningStrategy;

public class Game{
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
        printBoard();
        
        if(winningStrategiesSatisfied(i, j)){
            currPlayer.incScore();
            System.out.println(currPlayer+" won the game!");
            setOver(true);
            return;
        }
        if (drawStrategiesSatisfied(i, j)) {
            System.out.println("Game DRAW");
            setOver(true);
            return;
        }
        System.out.println("Turn is of "+players.peekFirst());
    }
    private boolean postitionStrategiesSatisfied(int i,int j){
        for(PostionStrategy Strategy:postionStrategy) if(!Strategy.valid(board, i, j)) return false;
        return true;
    }
    private boolean winningStrategiesSatisfied(int i,int j){
        for(WinningStrategy Strategy:winningStrategies) if(Strategy.checkWin(board))return true;
        return false;
    }
    private boolean drawStrategiesSatisfied(int i,int j){
        for(DrawStrategy Strategy:drawStrategies) if(Strategy.checkDraw(board))return true;
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
    private void printBoard(){
        System.out.println(board);
    }
}


