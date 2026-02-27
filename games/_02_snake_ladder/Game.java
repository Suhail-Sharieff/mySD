package games._02_snake_ladder;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public 
class Game{
    private final Deque<Player>players;
    private final Board board;
    private final HashMap<Player,Integer>positions;
    private final List<Win>winningStrategy;
    private boolean isEnded;
    private final Dice dice;

    public boolean isEnded() {
        return isEnded;
    }
    public void setEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }
    public Game(List<Player> players, Board board,List<Win>winningStrategy,Dice dice) {
        this.players = new LinkedList<>(players);
        this.board = board;
        this.positions = new HashMap<>();
        this.winningStrategy=winningStrategy;
        this.dice=dice;
        initPositions();
    }
    private void initPositions(){
        for(Player p:players) positions.put(p, 0);
    }
    public void play(){
        if(isEnded){
            System.out.println("Game ended already!");
            return;
        }
        Player currPlayer=players.pollFirst();
        int currPoss=positions.get(currPlayer);
        int move=dice.roll();
        int newPoss=currPoss+move;
        if(!isValidPosition(newPoss)){
            System.out.println("Exceeds the board size!");
            return;
        }
        players.addLast(currPlayer);
        System.out.print(currPlayer.getSymbol()+"'s turn:  ");
        newPoss=board.getNextPosition(newPoss);
        positions.put(currPlayer, newPoss);
        printBoard();

        if(isWinningMove(newPoss)){
            System.out.println(currPlayer+" won by reaching "+newPoss);
            setEnded(true);
            return;
        }
    }

    private boolean isWinningMove(int position){
        for(Win w:winningStrategy) if(w.isWin(board, position)) return true;
        return false;
    }

    private boolean isValidPosition(int position){
        return position<board.getSize();
    }

    

    private void printBoard(){
        System.out.println("Current board: ");
        int n=board.getSize();
        int sqrt=(int)(Math.sqrt(n));
        char mat[][]=new char[sqrt][sqrt];
        for(char []row:mat) Arrays.fill(row, '.');
        for(var e:positions.entrySet()){
            Player p=e.getKey();
            int pos=e.getValue();
            int r=pos/sqrt;
            int c=pos%sqrt;
            mat[r][c]=p.getSymbol();
        }
        for(char row[]:mat) System.out.println(Arrays.toString(row));
    }
    
}
