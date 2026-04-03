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
/*
4280 @ t=47
Exception occurred. Failure status: 1/3 @ t=47
****************************IN CLOSED **************************** @ t=47
5934 @ t=47
Exception occurred. Failure status: 2/3 @ t=47
****************************IN CLOSED **************************** @ t=47
5587 @ t=47
Exception occurred. Failure status: 3/3 @ t=52
Failure count has exceeded threshold. Moving to OPEN_STATE @ t=52
****************************IN OPEN **************************** @ t=52
Will try to move to HALF_OPEN in 4000ms @ t=52
****************************IN HALF_OPEN **************************** @ t=56
4160 @ t=56
Failed in HALF_OPEN. Moving back to OPEN_STATE. @ t=56
****************************IN OPEN **************************** @ t=56
Will try to move to HALF_OPEN in 4000ms @ t=56
****************************IN HALF_OPEN **************************** @ t=0
5653 @ t=0
Failed in HALF_OPEN. Moving back to OPEN_STATE. @ t=5
****************************IN OPEN **************************** @ t=5
Will try to move to HALF_OPEN in 4000ms @ t=5
****************************IN HALF_OPEN **************************** @ t=9
5279 @ t=9
Failed in HALF_OPEN. Moving back to OPEN_STATE. @ t=14
****************************IN OPEN **************************** @ t=14
Will try to move to HALF_OPEN in 4000ms @ t=14
****************************IN HALF_OPEN **************************** @ t=18
5476 @ t=18
Failed in HALF_OPEN. Moving back to OPEN_STATE. @ t=18
****************************IN OPEN **************************** @ t=18
Will try to move to HALF_OPEN in 4000ms @ t=18
****************************IN HALF_OPEN **************************** @ t=22
5476 @ t=22
Failed in HALF_OPEN. Moving back to OPEN_STATE. @ t=22
****************************IN OPEN **************************** @ t=22
Will try to move to HALF_OPEN in 4000ms @ t=22
****************************IN HALF_OPEN **************************** @ t=26
5292 @ t=26
Failed in HALF_OPEN. Moving back to OPEN_STATE. @ t=26
****************************IN OPEN **************************** @ t=26
Will try to move to HALF_OPEN in 4000ms @ t=26
****************************IN HALF_OPEN **************************** @ t=30
4921 @ t=30
Success in HALF_OPEN! Success Status: 1/3 @ t=35
1
PS C:\Users\suhai\Desktop\mySD> 
*/
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
        


        // v can use a daemon thread factory so the jvm can exit if we forget to shut it down
        private final ScheduledExecutorService es = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

         

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
                            int currentFailures = failureCount_in_closed.incrementAndGet();
                            println("Exception occurred. Failure status: " + currentFailures + "/" + threshold_count_closed_to_open);

                            if(currentFailures>=threshold_count_closed_to_open){
                                println("Failure count has exceeded threshold. Moving to OPEN_STATE");
                                currState.compareAndSet(State.CLOSED, State.OPEN);
                            }
                            //retry
                            return execute(supplier);
                        }else{
                            failureCount_in_closed.set(0);
                            return CompletableFuture.completedFuture(res);
                        }
                    }).thenCompose(x->x);
                }
                case State.HALF_OPEN->{
                    return supplier.get().orTimeout(apiTimout.toMillis(), TimeUnit.MILLISECONDS).handle((res,ex)->{
                        if(ex==null){
                            int currentSuccesses = successCount_in_halfopen.incrementAndGet();
                            println("Success in HALF_OPEN! Success Status: " + currentSuccesses + "/" + threshold_count_HalfOpen_to_cosed);
                            if (currentSuccesses >= threshold_count_HalfOpen_to_cosed) {
                                println("Success threshold met. Moving back to CLOSED_STATE.");
                                currState.compareAndSet(State.HALF_OPEN, State.CLOSED);
                                successCount_in_halfopen.set(0);
                                failureCount_in_closed.set(0);
                            }
                            return CompletableFuture.completedFuture(res);
                        }else{
                            println("Failed in HALF_OPEN. Moving back to OPEN_STATE.");
                            currState.compareAndSet(State.HALF_OPEN,State.OPEN);
                            successCount_in_halfopen.set(0);
                            return execute(supplier);
                        }       
                    }).thenCompose(x->x);
                }
                case State.OPEN->{
                    //MISTKAE code: bloking(due to sleep)+recursive
                    /*
                    sleep(threshold_duration_open_to_HalfOpen.toMillis());
                    currState.set(State.HALF_OPEN);
                    return execute(supplier);
                    */
                   println("Will try to move to HALF_OPEN in "+threshold_duration_open_to_HalfOpen.toMillis()+"ms");
                    CompletableFuture<T> delayedRetry = new CompletableFuture<>();
                    es.schedule(() -> {
                        // Safely transition to HALF_OPEN and trigger the retry
                        currState.compareAndSet(State.OPEN, State.HALF_OPEN);
                        successCount_in_halfopen.set(0);
                        
                        execute(supplier).whenComplete((res, ex) -> {
                            if (ex != null) delayedRetry.completeExceptionally(ex);
                            else delayedRetry.complete(res);
                        });
                        
                    }, threshold_duration_open_to_HalfOpen.toMillis(), TimeUnit.MILLISECONDS);
                    
                    return delayedRetry;
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
