package concurrency;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
            Duration.ofMillis(5000)
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
        HALF_CLOSED,
        CLOSED
    }


    static class CircuitBreaker<T>{

        private final int failureRateThreshold;//aftr how many failures it shud enter OPEN
        private final Duration waitDurationInOpenState;//how long it shud be in OPEN once it enters it
        private final int permittedNumberOfCallsInHalfOpenState;//how many calls shud it try in half open state bfr entering CLOSED
        private AtomicReference<State>currState=new AtomicReference<State>(State.CLOSED);
        private final AtomicInteger failureCount=new AtomicInteger(0);
        private final AtomicInteger testCountInHalfOpenState=new AtomicInteger(0);
        private final ScheduledExecutorService scheduler;
        private final Duration waitDurationBtwHalfOpenStateReties;
        private final Duration apiTimeout;


        public CircuitBreaker(int failureRateThreshold, Duration waitDurationInOpenState,
                int permittedNumberOfCallsInHalfOpenState,
                Duration waitDurationBtwOpenStateReties,ScheduledExecutorService scheduler,Duration apiTimout) {
            this.failureRateThreshold = failureRateThreshold;
            this.waitDurationInOpenState = waitDurationInOpenState;
            this.permittedNumberOfCallsInHalfOpenState = permittedNumberOfCallsInHalfOpenState;
            this.waitDurationBtwHalfOpenStateReties=waitDurationBtwOpenStateReties;
            this.scheduler=scheduler;
            this.apiTimeout=apiTimout;
        }

        public CompletableFuture<T> execute(Supplier<CompletableFuture<T>>supplier){
            return supplier.get().orTimeout(apiTimeout.toMillis(), TimeUnit.MILLISECONDS)
            .handle((res,ex)->{
                //we hv got response without exception (means v r in Closed or half closed)
                if(ex==null){
                    println("No exception was thrown!");
                    //case1: if v r in closed state no issue
                    if(currState.get().equals(State.CLOSED)) {
                        println("In Closed state, returning result!");
                        return CompletableFuture.<T>completedFuture(res);
                    }

                    //case2: if v r in half open state
                    if(currState.get().equals(State.HALF_CLOSED)){
                    //----subcase1: v already did permittedNumberOfCallsInHalfOpenState, then jump to closed,also reset testcount to 0 
                        println("In half closed state");
                        if(testCountInHalfOpenState.get()>=permittedNumberOfCallsInHalfOpenState){
                            println("Tested in half closed state for "+testCountInHalfOpenState+" times, works, returning result!");

                            testCountInHalfOpenState.set(0);
                            currState.set(State.CLOSED);

                            return CompletableFuture.completedFuture(res);
                        }

                    //----subcase2: v havent performed permittedcalls, so retry, but dont reset testcount value
                        println("Received result in half closed state, but cannot shift to closed state coz still need to test "+(permittedNumberOfCallsInHalfOpenState-testCountInHalfOpenState.get())+" times!");
                    }

                    //case3: no chance of reciving response in Open state, so no handling needed

                }   
                println("EXCEPTION "+ex);
                //case2: v hv got an exception (so again v must be in Closed or Half closed)
                //----subcase1: v r in closed state, somthng is wrong, so mv to half closed
                if(currState.get().equals(State.CLOSED) || currState.get().equals(State.HALF_CLOSED)){
                        if(currState.get().equals(State.CLOSED)){
                        failureCount.incrementAndGet();
                        currState.set(State.HALF_CLOSED);
                        println("Exception was recvd in CLOSED state, shifting to HALF_CLOSED");
                    }else if(currState.get().equals(State.HALF_CLOSED)){
                        println("Exception was recvd in HALF_CLOSED");
                    //-----subcase2: v r in half closed state
                        failureCount.incrementAndGet();
                        //case1: if failure count exceeds threshold, move to open state, set failure count to 0
                        if(failureCount.get()>=failureRateThreshold){
                            println("Failure count is above threshold, shifting to OPEN");
                            currState.set(State.OPEN);
                            failureCount.set(0);
                        }
                    }
                    println("Retrying....");
                    //retry:
                    CompletableFuture<T>retryFuture=new CompletableFuture<>();
                    scheduler.schedule(()->{
                        execute(supplier).whenComplete((r,e)->{
                            if(e==null){
                                //retry succeeded
                                retryFuture.complete(res);
                            }else{
                                retryFuture.completeExceptionally(e);
                            }
                        });
                    }, waitDurationBtwHalfOpenStateReties.toMillis(), TimeUnit.MILLISECONDS);
                    return retryFuture;
                }

                //the state is open state
                println("Exception recvd in OPEN state. Retrying..");
                //then v need to retry after waitDurationInOpenState
                
                CompletableFuture<T>retryFuture=new CompletableFuture<>();
                    scheduler.schedule(()->{
                        execute(supplier).whenComplete((r,e)->{
                            if(e==null){
                                //retry succeeded
                                failureCount.decrementAndGet();
                                retryFuture.complete(res);
                            }else{
                                retryFuture.completeExceptionally(e);
                            }
                        });
                    }, waitDurationInOpenState.toMillis(), TimeUnit.MILLISECONDS);
                return retryFuture;


            }).thenCompose(x->x);
            
            
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
