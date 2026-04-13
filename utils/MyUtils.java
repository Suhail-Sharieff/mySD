package utils;

import java.time.LocalTime;

public class MyUtils {
    public static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
    public static void println(Object o){System.out.println("t="+LocalTime.now().getSecond()+" : Thread="+Thread.currentThread().getName()+" : "+o.toString());}
}