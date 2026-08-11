package questions._06_snake_ladder;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

abstract class Jumpable{
    final int fromPos;
    final int toPos;

    Jumpable(int fromPos,int toPos){
        this.fromPos=fromPos;
        this.toPos=toPos;
    }
}

class Snake extends Jumpable{
    Snake(int fromPos,int toPos){
        super(fromPos,toPos);
        if(fromPos<=toPos)
            throw new IllegalArgumentException("Snake must go down");
    }
}

class Ladder extends Jumpable{
    Ladder(int fromPos,int toPos){
        super(fromPos,toPos);
        if(fromPos>=toPos)
            throw new IllegalArgumentException("Ladder must go up");
    }
}

class Board{
    final int size;
    final Map<Integer,Jumpable> jumps;

    Board(int size){
        this.size=size;
        this.jumps=new HashMap<>();
    }

    void addSnake(Snake snake){
        jumps.put(snake.fromPos,snake);
    }

    void addLadder(Ladder ladder){
        jumps.put(ladder.fromPos,ladder);
    }

    int getPosition(int currPos,int diceValue){
        int nextPos=currPos+diceValue;

        if(nextPos>size)
            return currPos;

        Jumpable jump=jumps.get(nextPos);

        if(jump!=null)
            return jump.toPos;//if snake or ladder is there, use it

        return nextPos;
    }
}

class Player{
    final int id;
    final String name;
    int currPosition;

    Player(int id,String name){
        this.id=id;
        this.name=name;
        this.currPosition=0;
    }
}

class Dice{
    final int minVal;
    final int maxVal;
    final Random rand;

    Dice(int minVal,int maxVal){
        if(minVal>maxVal)
            throw new IllegalArgumentException();

        this.minVal=minVal;
        this.maxVal=maxVal;
        this.rand=new Random();
    }

    int roll(){
        return rand.nextInt(minVal,maxVal+1);
    }
}

enum GameStatus{
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

interface WinningStrategy{
    boolean hasWon(int position,int boardSize);
}

class ExactWinningStrategy implements WinningStrategy{
    @Override
    public boolean hasWon(int position,int boardSize){
        return position==boardSize;
    }
}

public class Game{

    private final Dice dice;
    private final Deque<Player> players;
    private final Board board;
    private final WinningStrategy winningStrategy;

    private GameStatus status;

    private Game(GameBuilder builder){
        this.dice=builder.dice;
        this.players=builder.players;
        this.board=builder.board;
        this.winningStrategy=builder.winningStrategy;
        this.status=GameStatus.NOT_STARTED;
    }

    static class GameBuilder{
        Dice dice;
        Deque<Player> players=new LinkedList<>();
        Board board;
        WinningStrategy winningStrategy;

        GameBuilder setDice(Dice dice){
            this.dice=dice;
            return this;
        }

        GameBuilder setPlayers(Deque<Player> players){
            this.players=players;
            return this;
        }

        GameBuilder setBoard(Board board){
            this.board=board;
            return this;
        }

        GameBuilder setWinningStrategy(WinningStrategy strategy){
            this.winningStrategy=strategy;
            return this;
        }

        Game build(){
            return new Game(this);
        }
    }

    Player getCurrentPlayer(){
        return players.peekFirst();
    }

    void playTurn(){

        Player player=players.pollFirst();

        int diceValue=dice.roll();

        int newPosition=
                board.getPosition(player.currPosition,diceValue);

        player.currPosition=newPosition;

        if(winningStrategy.hasWon(
                player.currPosition,
                board.size)){

            status=GameStatus.COMPLETED;
            System.out.println(player.name+" won");
            return;
        }

        players.offerLast(player);
    }

    void play(){

        status=GameStatus.IN_PROGRESS;

        while(status==GameStatus.IN_PROGRESS){
            playTurn();
        }
    }

    void reset(){
        status=GameStatus.NOT_STARTED;

        for(Player player:players){
            player.currPosition=0;
        }
    }
}