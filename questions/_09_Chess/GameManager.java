package questions._09_Chess;

import java.util.List;



/*
GameManager provides apis for users to start match
Match is set btw users using some strategy
match will hv rules, it runs those rules before each move by each collegue
each match will have pair of collegues, board, rules

*/



public class GameManager {
    List<Match>matches;
    UserRepo userRepo;
    MatchMakingStrategy matchMakingStrategy;
    Match getMatch(int rating){return matchMakingStrategy.findMatch(rating,userRepo);};
}
class User{
    int id;
    String name;
    int rating;
}
class UserRepo{
    List<User>users;
    //apis for user related operations
}
record Player(User u,Color choosenColor){};
class Match{
    int id;
    Board chessBoard;
    Player black;
    Player white;
    List<Move>moves;
    MatchRuleEngine ruleEngine;
    MatchStatus matchStatus;
    Color currentTurn;
    void move(Position x,Position y){
        //make checks...
        chessBoard.move(x,y);
    }
    void reset(){};
    void end(){}
}
enum MatchStatus{
    STARTED,
    ENDED,
    ABORTED
}

record Position(int x,int y){}
record Move(Position from,Position to,Piece p){}

abstract class Piece{
    Color color;
    String symbol;
    PieceType type;
    abstract List<Position>getPossibleMoves(Board board,Position currentPos);
}
enum Color{BLACK,WHITE}
enum PieceType{ROOK,BISHOP,KNIGHT,KING,QUEEN,PAWN}
class King extends Piece{@Override List<Position> getPossibleMoves(Board board, Position currentPos) {return null;}}
class Queen extends Piece{@Override List<Position> getPossibleMoves(Board board, Position currentPos) {return null;}}
class Rook extends Piece{@Override List<Position> getPossibleMoves(Board board, Position currentPos) {return null;}}
class Bishop extends Piece{@Override List<Position> getPossibleMoves(Board board, Position currentPos) {return null;}}
class Knight extends Piece{@Override List<Position> getPossibleMoves(Board board, Position currentPos) {return null;}}
class Pawn extends Piece{@Override List<Position> getPossibleMoves(Board board, Position currentPos) {return null;}}


class Board{
    Piece[][]board=new Piece[8][8];
    void move(Position from,Position to){}
    //...all other apis to move pieces
}

interface MatchMakingStrategy{
    Match findMatch(int rating,UserRepo repo);//queries repo and finds users, fixes match btw them
}
//....some class will implement it
interface MatchRuleEngine{
    boolean checkValidMove(Move m);
    boolean isCheckMate(Board b);
    boolean isStaleMate(Board b);
    boolean isDraw(Board b);
}