package questions._02_tic_tac_toe;

import java.util.Deque;
import java.util.LinkedList;

enum Symbol{
    X,
    O
}

class Cell{
    Symbol symbol;
    public Cell(Symbol symbol){this.symbol=symbol;}
}

class Board{
    private Cell[][] board;
    Board(){initBoard();}
    private void initBoard(){this.board=new Cell[3][3];}
    void set(int x,int y,Symbol s){this.board[x][y]=new Cell(s);}
    boolean isSet(int x,int y){return this.board[x][y]!=null;}
    void reset(){initBoard();}
    Cell cellAt(int i,int j){return board[i][j];}
}
class Player{
    Symbol symbolAssigned;
    public Player(Symbol symbol){this.symbolAssigned=symbol;}
}
interface WinningStrategy{//we can also add Draw Startegy, for now i hv skipped
    boolean isWiningMove(Board board);
}
//further we can also have RowWinningStrategy,ColWinning Strategy or DiagonalWinnignStrategy etc, for simplicity i just kept 1 winning strategy that hecks row, col and diaognals, ---here its doing too much
class DefaultWinningStrategy implements WinningStrategy{
    @Override
    public boolean isWiningMove(Board board) {
        //check any row has all same
        for(int i=0;i<3;i++){
            int x=0,o=0;
            for(int j=0;j<3;j++) if(board.cellAt(i, j)!=null && board.cellAt(i, j).symbol.equals(Symbol.X)) x++;else o++;
            if(x==3 || o==3) return true;
        }
        //check any col has all same
        for(int j=0;j<3;j++){
            int x=0,o=0;
            for(int i=0;i<3;i++) if(board.cellAt(i, j)!=null && board.cellAt(i, j).symbol.equals(Symbol.X)) x++;else o++;
            if(x==3 || o==3) return true;
        }
        //check if diagoanl has all same
        if(board.cellAt(0, 0).symbol.equals(board.cellAt(1, 1).symbol) && board.cellAt(1, 1).symbol.equals(board.cellAt(2, 2).symbol)) return true;
        if(board.cellAt(0, 2).symbol.equals(board.cellAt(1, 1).symbol) && board.cellAt(1, 1).symbol.equals(board.cellAt(2, 0).symbol)) return true;
        return false;
    }
}
class Game{
    private final Board board;
    private Deque<Player>players;
    public final WinningStrategy winningStrategy;
    public Game(Board board,WinningStrategy strategy){this.board=board;this.winningStrategy=strategy;init();}
    void init(){
        this.players=new LinkedList<>();
        players.offer(new Player(Symbol.X));
        players.offer(new Player(Symbol.O));
    }
    void reset(){board.reset();init();}
    int getTurn(){return players.getFirst().symbolAssigned.equals(Symbol.X)?0:1;}
    void move(int x,int y){
        //make a check before placing something here by throwing custom exceptions, for now i hv skipped for simplicity
        board.set(x, y, players.getFirst().symbolAssigned);
        if(winningStrategy.isWiningMove(board)){
            announceWinner();
            return;
        }
        toggleTurn();
    }
    private void toggleTurn(){players.offerLast(players.removeFirst());}
    private void announceWinner(){
        System.out.println(getTurn()+" th player WON");
    }
}