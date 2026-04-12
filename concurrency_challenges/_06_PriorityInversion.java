package concurrency_challenges;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

/*
say there are 2 resources R1 and R2

t=0 LP acquires R1, say it needs 3 seconds to complete

t=1 MP acquires R2, but CPU gives time to MP now, say it needs 5 seconds to complete, LP pauses still 2 seconds time remaining

t=6 MP completes releases R2, CPU now shift to LP which has 2s work

t=8 LP completes its work, HP acquires R1 now

Observe that HP though needed to wait for 2 seconds to get R1, had to wait 5+2 seconds rather coz CPU shifted to MP holding R2 for 5s, then 2s remaining LP's work

simply wasting time


*/

/*

t=58 : LP acquired lock on resource1
t=59 : MP acquired lock on resource2
t=1 : LP released lock on resource1
t=1 : HP acquired lock on resource1
t=4 : HP released lock on resource1
t=4 : MP released lock on resource2

*/
public class _06_PriorityInversion {

    static final Object resource1=new Object();
    static final Object resource2=new Object();


    public static void main(String[] args) throws InterruptedException {
        //fight for resource 1
        Thread LP=new Thread(()->{
            oprOnResource1();
        },"LP");

        Thread HP=new Thread(()->{
            sleep(1000);
            oprOnResource1();
        },"HP");

        //for resource 2
         Thread MP=new Thread(()->{
            sleep(1500);
            oprOnResource2();
        },"MP");

        LP.setPriority(1);
        MP.setPriority(5);
        HP.setPriority(10);

        LP.start();
        MP.start();
        HP.start();

        LP.join();
        MP.join();
        HP.join();
    }


    static void oprOnResource1(){
        synchronized(resource1){
            printTime(Thread.currentThread().getName()+" acquired lock on resource1");
            sleep(3000);
            printTime(Thread.currentThread().getName()+" released lock on resource1");
        }
    }
    static void oprOnResource2(){
        synchronized(resource2){
            printTime(Thread.currentThread().getName()+" acquired lock on resource2");
            sleep(5000);
            printTime(Thread.currentThread().getName()+" released lock on resource2");
        }
    }


    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}

    static void printTime(String s){
        System.out.println("t="+LocalTime.now().getSecond()+" : "+s);
    }
    
}
