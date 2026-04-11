package concurrency_challenges;

import java.util.concurrent.locks.ReentrantLock;

//ex: a high priority and a low priority thread tries to acquire lock, lock assigned for high prioirty first, it compltes its work and immeditely retries to lock again or some another high priority thread comes at this point, now again they fight to acquire lock, again assgined to high prority, low priority never got lock 
public class _03_Starvation {
    public static void main(String[] args) throws InterruptedException {
        Counter counter=new Counter();
        int highPriority=10;
        int lowPriority=3;
        var hp_t1=new Thread(()->counter.incr(), "HP_T1");hp_t1.setPriority(highPriority);
        var hp_t2=new Thread(()->counter.incr(), "HP_T2");hp_t2.setPriority(highPriority);
        var lp_t1=new Thread(()->counter.incr(), "LP_T1");lp_t1.setPriority(lowPriority);

        hp_t1.start();
        hp_t2.start();
        lp_t1.start();

        hp_t1.join();
        hp_t2.join();
        lp_t1.join();


        /*see that high priority threads acquire locks first, then last chance for low priority, in large systemst this accumulates, low prioirt thread never gets a lock at all
        HP_T1 got LOCK!
        HP_T2 WAITING...
        LP_T1 WAITING...
        HP_T2 got LOCK!
        LP_T1 WAITING...
        LP_T1 got LOCK! */


    }

    private static class Counter{
        private int count=0;
        ReentrantLock lck=new ReentrantLock();
        
        public boolean incr(){
            while (true) {
                if(lck.tryLock()){
                    System.out.println(Thread.currentThread().getName()+" got LOCK!");
                    try{
                        count++;
                        sleep(1000);
                        return true;
                    }finally{
                        lck.unlock();
                    }
                }else System.out.println(Thread.currentThread().getName()+" WAITING...");
                sleep(2000);
            }
        }

        public int getCount() {
            return count;
        }
    }

    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
}
