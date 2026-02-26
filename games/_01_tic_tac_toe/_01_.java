package games._01_tic_tac_toe;

import java.util.Arrays;


/*

The problem with below design:
- Full violation of Single Responsibilty, coz the Game  is 
    - managing board
    - managing win logic and nMoves
    - handling edge cases
    - traking score
    - switching players
- play(i,j) is too complex and handling too much
- moves, turn, isEnded handled manully
- Design breaks 
    - say i wanna change winning strategy in future
    - change symbols  
- this design is restricted to 2 players so are 2 symbols(X and O), need full refactoring for multiplayer  

*/

public class _01_ {
    public static void main(String[] args) {

        Player p1=new Player("A");
        Player p2=new Player("B");

        Game game=new Game(p1, p2);

        game.play(0, 0);
        game.play(2, 0);
        game.play(0, 0);//invalid coz 1st its already marked
        game.play(1, 1);
        game.play(2, 2);
        game.play(0, 2);
        game.play(2, 1);

        game.startNewGame();
        game.resetGame();
    }
}

class Player{
    private final String name;
    private int points;
    public Player(String name) {
        this.name=name;
    }
    public String getName() {
        return name;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    @Override
    public String toString() {
        return "Player [name=" + name + ", points=" + points + "]";
    }
    
}
class Game{
    private char[][]matrix;
    private Player player1;
    private Player player2;
    private int turn ;
    private boolean ended;
    private int nMoves;
    public Game(Player p1,Player p2){
        matrix=new char[3][3];
        this.player1=p1;
        this.player2=p2;
        turn=0;
        ended=false;
        nMoves=0;
        resetMatrix();
    }

    private void resetMatrix(){
        for(var row:matrix) Arrays.fill(row, '#');
    }

    public void play(int i,int j){

        if(ended){
            System.out.println("Game already ended please start new game");
            return;
        }

        if(!validMove(i, j)){
            System.out.println("Invalid move");
            return;
        }

        matrix[i][j]=(turn==0)?'X':'O';
        nMoves++;
        System.out.println("Board after move: ");
        printBoard();

        boolean hasWinning=hasWinning();
        if(nMoves==9 && !hasWinning){
            System.out.println("All cells filled and no winning. DRAW!");
            ended=true;
            return;
        }

        if(hasWinning){
            Player winner=(turn==0)?player1:player2;
            winner.setPoints(winner.getPoints()+1);
            System.out.println("The winner is "+winner);
            ended=true;
            return;
        }

        turn=1-turn;
        System.out.println("Now its "+(turn==0?player1.getName():player2.getName())+"'s turn");

    }
    private void printBoard(){
        for(var row:matrix) System.out.println(Arrays.toString(row));
    }



    public void startNewGame(){
        System.out.println("Start playing new game!");
        resetMatrix();
        player1.setPoints(0);
        player2.setPoints(0);
        nMoves=0;
        ended=false;
    }
    public void resetGame(){
        System.out.println("Reset success start playing..");
        resetMatrix();
        nMoves=0;
        ended=false;
    }

    private boolean validMove(int i,int j){
        return i>=0 && j>=0 && i<3 && j<3 && matrix[i][j]=='#';//v cnt place cell if its not empty so is the last condition
    }
   
    private boolean hasWinning(){
        for(var row:matrix) if(allSame(row)) return true;
        for(int j=0;j<3;j++) if(allSame(matrix[0][j],matrix[1][j],matrix[2][j])) return true;
        if(allSame(matrix[0][0],matrix[1][1],matrix[2][2])) return true;
        return allSame(matrix[0][2],matrix[1][1],matrix[2][0]);
    }

    private boolean allSame(char ...arr){//chks if all OOO or xxx 
        if(arr[0]=='#') return false;
        for(int i=1;i<arr.length;i++) if(arr[i]!=arr[i-1]) return false;
        return true;
    }


}