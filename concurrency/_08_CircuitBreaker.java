package concurrency;

import java.nio.file.StandardCopyOption;
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
            int r = rand.nextInt(4990, 6000);
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




        CircuitBreaker<Integer>cb=new CircuitBreaker<>(3, Duration.ofMillis(4000), 3, Duration.ofMillis(5000));


        


        try{
            CompletableFuture<Integer>cf=cb.execute(supplier);
            cf.thenAccept(System.out::println).exceptionally(ex->{
                System.out.println(ex);
                return null;
            });
            cf.join();
        }catch(Exception ex){}
        

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
        private final Duration threshold_duration_open_to_HalfOpen;
        private final int threshold_count_HalfOpen_to_cosed;
        private final Duration apiTimout;

        private final AtomicReference<State> currState=new AtomicReference<>(State.CLOSED);

        private final AtomicInteger failureCount_in_closed=new AtomicInteger(0);

        private final AtomicInteger successCount_in_halfopen=new AtomicInteger(0);        
        


        private final ScheduledExecutorService es=Executors.newScheduledThreadPool(1);

         

        public CircuitBreaker(int threshold_closed_to_open, Duration threshold_open_to_HalfOpen,int threshold_count_HalfOpen_to_cosed,
                Duration apiTimout) {
            this.threshold_count_closed_to_open = threshold_closed_to_open;
            this.threshold_duration_open_to_HalfOpen = threshold_open_to_HalfOpen;
            this.threshold_count_HalfOpen_to_cosed = threshold_count_HalfOpen_to_cosed;
            this.apiTimout = apiTimout;
        }







        public CompletableFuture<T> execute(Supplier<CompletableFuture<T>>supplier){
            State state=currState.get();
            println("****************************IN "+state+" ****************************");
            switch(state){
                case State.CLOSED->{
                    return supplier.get().orTimeout(apiTimout.toMillis(), TimeUnit.MILLISECONDS).handle((res,ex)->{
                        //if exception occurs, retry for threshold_count_closed_to_open times, if failed thn mv to open state
                        if(ex!=null) {
                            println("Exception occured. Failure status: "+failureCount_in_closed.get()+"/"+threshold_count_closed_to_open);
                            if(failureCount_in_closed.get()>=threshold_count_closed_to_open){
                                println("Failure count has exceeded threshold. Moving to OPEN_STATE");
                                currState.set(State.OPEN);
                                failureCount_in_closed.set(0);
                                return execute(supplier);
                            }else{
                                failureCount_in_closed.incrementAndGet();
                                return execute(supplier);
                            }
                        }else{
                            failureCount_in_closed.set(0);
                            return CompletableFuture.completedFuture(res);
                        }
                    }).thenCompose(x->x);
                }
                case State.HALF_OPEN->{
                    return supplier.get().orTimeout(apiTimout.toMinutes(), TimeUnit.MILLISECONDS).handle((res,ex)->{
                        if(ex!=null){
                            if(successCount_in_halfopen.get()>=threshold_count_HalfOpen_to_cosed){
                                currState.set(State.CLOSED);
                                successCount_in_halfopen.set(0);
                                return CompletableFuture.completedFuture(res);
                            }else{
                                successCount_in_halfopen.incrementAndGet();
                                return execute(supplier);
                            }
                        }else{
                            currState.set(State.OPEN);
                            return execute(supplier);
                        }       
                    }).thenCompose(x->x);
                }
                case State.OPEN->{
                    sleep(threshold_duration_open_to_HalfOpen.toMillis());
                    currState.set(State.HALF_OPEN);
                    return execute(supplier);
                }
            }
            throw new RuntimeException("Some invalid state entered!");
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
