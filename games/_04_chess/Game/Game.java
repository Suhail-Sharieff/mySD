package games._04_chess.Game;


import games._04_chess.components.Bishop;
import games._04_chess.components.Board;
import games._04_chess.components.King;
import games._04_chess.components.Knight;
import games._04_chess.components.Queen;
import games._04_chess.components.Rook;
import games._04_chess.components.Soldier;
import games._04_chess.enums.Color;

public class Game {
    private final Board board;
    public Game(Board board) {
        this.board = board;
        initGame();
        this.turn=Color.White;
    }


    private Color turn;


    private void initGame(){


        board.set(0,0,new Rook(Color.Black));
        board.set(0, board.getnCols()-1, new Rook(Color.Black));
        board.set(board.getnRows()-1, 0,new Rook(Color.White));
        board.set(board.getnRows()-1, board.getnCols()-1, new Rook(Color.White));
        
        board.set(0,1,new Knight(Color.Black));
        board.set(0, board.getnCols()-2, new Knight(Color.Black));
        board.set(board.getnRows()-1,1,new Knight(Color.White));
        board.set(board.getnRows()-1, board.getnCols()-2, new Knight(Color.White));
        
        board.set(0,2,new Bishop(Color.Black));
        board.set(0, board.getnCols()-3, new Bishop(Color.Black));
        board.set(board.getnRows()-1, 2,new Bishop(Color.White));
        board.set(board.getnRows()-1, board.getnCols()-3, new Bishop(Color.White));
        
        board.set(0, 3, new King(Color.Black));
        board.set(0, 4, new Queen(Color.Black));
        board.set(board.getnRows()-1, 3, new King(Color.White));
        board.set(board.getnRows()-1, 4, new Queen(Color.White));


        for(int j=0;j<board.getnCols();j++) board.set(1, j, new Soldier(Color.Black));
        for(int j=0;j<board.getnCols();j++) board.set(board.getnRows()-2, j, new Soldier(Color.White));
        
    }

    public void play(int fromX,int fromY,int toX,int toY){
        board.move(board.getPawn(fromX, fromY), fromX, fromY, toX, toY);
    }

   
}
