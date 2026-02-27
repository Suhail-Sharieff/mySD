package games._02_snake_ladder;

import java.util.Map;

public 
class Board{
    private final Map<Integer,Integer>snakeJumps;
    private final Map<Integer,Integer>ladderJumps;
    private final int size;
    
    public Board(Map<Integer, Integer> snakeJumps, Map<Integer, Integer> ladderJumps, int size) {
        this.snakeJumps = snakeJumps;
        this.ladderJumps = ladderJumps;
        this.size = size;
    }
    public int getNextPosition(int position){
        if(snakeJumps.containsKey(position)){
            System.out.println("Bit by snake going to "+snakeJumps.get(position));
            return snakeJumps.get(position);
        }
        if(ladderJumps.containsKey(position)){
            System.out.println("Got ladder jumpiing to "+ladderJumps.get(position));
            return ladderJumps.get(position);
        }
        return position;
    }

    public int getSize() {
        return size;
    }
}
