package games._03_mine_sweep._01_brute;

import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private final Board board;
    private boolean isEnded;
    public Game(Board board){
        this.board=board;
    }


    public void click(int i,int j){
        if(isEnded){
            System.out.println("Game ended already!");
            return;
        }
        System.out.println("[CLICK] @ {"+i+","+j+"}");
        if(board.hasBomb(i, j)){
            System.out.println("You clicked on bomb!");
            isEnded=true;
            board.reveal();
            return;    
        }
        if(board.hasFlag(i, j)){
            System.out.println("This position is flagged! Pls unflag first");
            return;
        }
        bfs(i,j);      
        if(board.allSafePositionsVisited()){
            System.out.println("You won the game!");
            isEnded=true;
            board.reveal();
            return;
        }
        System.out.println("------------------------------");
        System.out.println(board);  
    }
    private int dirs[][]={{-1,-1},{-1,0},{-1,1},{1,-1},{1,0},{1,1},{0,-1},{0,1}};
    private void bfs(int i,int j){
        Queue<int[]>q=new LinkedList<>();
        q.offer(new int[]{i,j});
        board.setVisible(i, j);
        while (!q.isEmpty()) {
            int top[]=q.poll();
            int x=top[0];
            int y=top[1];
            if(!board.haveBombAroundMe(x, y)){
                for(int dir[]:dirs){
                    int nx=x+dir[0];
                    int ny=y+dir[1];
                    if(board.isValidPosition(nx, ny)){
                        if(!board.isVisibleCell(nx, ny)){
                            if(!board.hasFlag(nx, ny)){
                                board.setVisible(nx, ny);
                                q.offer(new int[]{nx,ny});
                            }
                        }
                    }
                }
            }
        }
    }

    public void flag(int i,int j){
        System.out.println("[FLAGGING] @ {"+i+","+j+"}");
        board.flag(i, j);
    }
    public void unflag(int i,int j){
        System.out.println("[UN-FLAGGING] @ {"+i+","+j+"}");
        board.unflag(i, j);
    }
}
