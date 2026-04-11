package concurrency_challenges;
//condition ex: a single path , 2 threads try at same time, both fail, retry after exactly same time again(ie no ranfom backoff), again fail...loop continues, the system seems to be running, but high CPU cycles wastage since threads arent sleeping here
import java.util.concurrent.locks.ReentrantLock;
/*
T2 FAILED...RETRYING IN 1000ms....
T1 SUCCESS to lock!
T3 FAILED...RETRYING IN 1000ms....
T3 FAILED...RETRYING IN 1000ms....
T2 FAILED...RETRYING IN 1000ms....
T2 SUCCESS to lock!
T3 FAILED...RETRYING IN 1000ms....
T3 FAILED...RETRYING IN 1000ms....
T3 SUCCESS to lock!
3
*/
public class _02_LiveLock {
    public static void main(String[] args) throws InterruptedException {
        
        Counter cnter=new Counter();


        var t1=new Thread(()->cnter.incr(),"T1");
        var t2=new Thread(()->cnter.incr(),"T2");
        var t3=new Thread(()->cnter.incr(),"T3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println(cnter.getCount());


    }   



    private static class Counter{
        private int count=0;
        private final ReentrantLock lock=new ReentrantLock();
        // private final Random rand=new Random();
        private long fixedRetryTime=2000;
        public void incr(){
            while(true){
                if(lock.tryLock()){
                    try{
                        System.out.println(Thread.currentThread().getName()+" SUCCESS to lock!");
                        this.count++;
                        sleep(fixedRetryTime);//block so other threads content to acquire by sleeping to simulate livelok
                        return;
                    }finally{
                        lock.unlock();
                    }
                }else{
                    System.out.println(Thread.currentThread().getName()+" FAILED...RETRYING IN "+1000+"ms....");
                    sleep(1000);//many retries all at a time after 1s, SOLUTION: add randome jitter instead of fixed 1s jitter
                }
            }
        }
        public int getCount() {
            return count;
        }
        private void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
    }


}
