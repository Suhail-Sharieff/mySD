import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    /*
    pool-1-thread-2 woke at 09:46:34.230866200
    pool-1-thread-1 woke at 09:46:36.263810300
    pool-1-thread-3 woke at 09:46:36.761054500
    pool-1-thread-3 got released at 09:46:36.761688900
    pool-1-thread-1 got released at 09:46:36.762791600
    pool-1-thread-2 got released at 09:46:36.762791600 */
    public static void main(String[] args) {
        int nThreads=3;
        CyclicBarrier barrier=new CyclicBarrier(nThreads);
        ExecutorService es=Executors.newFixedThreadPool(nThreads);
        es.submit(()->new Service(barrier).run());
        es.submit(()->new Service(barrier).run());
        es.submit(()->new Service(barrier).run());

        es.shutdown();

    }


    static Random rand=new Random();
    static void sleep(){try{Thread.sleep(rand.nextLong(1000, 5000));}catch(InterruptedException ex){}}

    static class Service implements Runnable{
        private final CyclicBarrier barrier;
        public Service(CyclicBarrier barrier) {
            this.barrier=barrier;
        }
        @Override
        public void run() {
            sleep();
            System.out.println(Thread.currentThread().getName()+" woke at "+LocalTime.now());
            try{barrier.await();}catch(Exception ex){}
            System.out.println(Thread.currentThread().getName()+" got released at "+LocalTime.now());
        }
    }
}