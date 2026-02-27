package games._02_snake_ladder;

import java.util.Random;

public class Dice{
    private final int low;
    private final int high;
    private final Random rand;
    public Dice(int low,int high){
        this.low=low;
        this.high=high;
        this.rand=new Random();
    }
    public int roll(){
        return rand.nextInt(low,high+1);
    }

}