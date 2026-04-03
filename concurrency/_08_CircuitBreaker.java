package concurrency;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class _08_CircuitBreaker {
    public static void main(String[] args) {
         Random rand = new Random();
        Callable<Integer> callable = () -> {
            int r = rand.nextInt(4000, 6000);
            println(Integer.toString(r));
            if (r % 2 == 0)
                throw new RuntimeException("Some server error occured");
            else
                sleep(r);
            return 1;
        };
        Supplier<CompletableFuture<Integer>> supplier = () -> CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException("Some problem at server side");
            }
        });


        ScheduledExecutorService schedulr=Executors.newScheduledThreadPool(1);;


        CircuitBreaker<Integer>cb=new CircuitBreaker<>(
            3, //after this many enter open state
            Duration.ofMillis(5000), //if in open state, try after this much time
            3, //shift from half open to closed only after 3 successes
            Duration.ofMillis(3000),//make consequtive calls by this much in half open
            schedulr,
            Duration.ofMillis(5000)//time under which Api should return response
        );


        try{
            CompletableFuture<Integer>cf=cb.execute(supplier);
            cf.thenAccept(System.out::println).exceptionally(ex->{
                System.out.println(ex);
                return null;
            });
            cf.join();
        }catch(Exception ex){}
        finally{
            schedulr.shutdown();
            schedulr.close();;
        }


    }   
    
    
    static enum State{
        OPEN,
        HALF_OPEN,
        CLOSED
    }


    /*
    CLOSED --(failures >= threshold)--> OPEN
    OPEN --(after wait)--> HALF_OPEN
    HALF_OPEN --(successes >= N)--> CLOSED
    HALF_OPEN --(failure)--> OPEN
    */


    static class CircuitBreaker<T>{

        private final int threshold_count_closed_to_open;
        private final int threshold_count_open_to_HalfOpen;
        private final Duration apiTimout;

        private final AtomicReference<State> currState=new AtomicReference<>(State.CLOSED);
        private final AtomicInteger failureCount=new AtomicInteger(0);
        private final AtomicInteger successCount=new AtomicInteger(0);        
        


        

         

        public CircuitBreaker(int threshold_closed_to_open, int threshold_open_to_HalfOpen,
                Duration apiTimout) {
            this.threshold_count_closed_to_open = threshold_closed_to_open;
            this.threshold_count_open_to_HalfOpen = threshold_open_to_HalfOpen;
            this.apiTimout = apiTimout;
        }







        public CompletableFuture<T> execute(Supplier<CompletableFuture<T>>supplier){
            State state=currState.get();
            switch(state){
                case State.CLOSED->{
                    return supplier.get().orTimeout(apiTimout.toMillis(), TimeUnit.MILLISECONDS).whenComplete((res,ex)->{
                        //if exception occurs, retry for 
                    });
                }
                case State.HALF_OPEN->{
                    return null;
                }
                case State.OPEN->{
                    return null;
                }
                default->{
                    throw new RuntimeException("Invalid state: "+state);
                }
            }
        }

        
        
    }


    static void println(String msg){
        System.out.println(msg+" @ t="+LocalTime.now().getSecond());
    }


    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
