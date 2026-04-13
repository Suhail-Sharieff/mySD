package utils;

import java.time.LocalTime;
import java.util.Random;

public class MyUtils {
    public static final Random rand=new Random();
    public static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
    public static void println(Object o){System.out.println("t="+LocalTime.now().getSecond()+" : Thread="+Thread.currentThread().getName()+" : "+o.toString());}
    public static long getRand(long from, long to){return rand.nextLong(from, to+1);}
}