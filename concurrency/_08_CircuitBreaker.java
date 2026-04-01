package concurrency;

import java.time.Duration;
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
        private final CompletableFuture<T> producerService; //dependent serv 1
        private final CompletableFuture<T> consumerService; //dependednt serv 2
        private AtomicReference<State>currState=new AtomicReference<State>(State.CLOSED);
        private final AtomicInteger failureCount=new AtomicInteger(0);
        private final AtomicInteger testCountInHalfOpenState=new AtomicInteger(0);
        private final ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1);
        private final Duration waitDurationBtwHalfOpenStateReties;


        public CircuitBreaker(int failureRateThreshold, Duration waitDurationInOpenState,
                int permittedNumberOfCallsInHalfOpenState, CompletableFuture<T> service1,
                CompletableFuture<T> service2,Duration waitDurationBtwOpenStateReties) {
            this.failureRateThreshold = failureRateThreshold;
            this.waitDurationInOpenState = waitDurationInOpenState;
            this.permittedNumberOfCallsInHalfOpenState = permittedNumberOfCallsInHalfOpenState;
            this.producerService = service1;
            this.consumerService = service2;
            this.waitDurationBtwHalfOpenStateReties=waitDurationBtwOpenStateReties;
        }

        public CompletableFuture<T> execute(Supplier<CompletableFuture<T>>supplier){
            return supplier.get().orTimeout(waitDurationInOpenState.toMillis(), TimeUnit.MILLISECONDS)
            .handle((res,ex)->{
                //we hv got response without exception (means v r in Closed or half closed)
                if(ex==null){
                    //case1: if v r in closed state no issue
                    if(currState.get().equals(State.CLOSED)) return CompletableFuture.<T>completedFuture(res);

                    //case2: if v r in half open state
                    if(currState.get().equals(State.HALF_CLOSED)){
                    //----subcase1: v already did permittedNumberOfCallsInHalfOpenState, then jump to closed,also reset testcount to 0 
                        if(testCountInHalfOpenState.get()>=permittedNumberOfCallsInHalfOpenState){
                            testCountInHalfOpenState.set(0);
                            return CompletableFuture.completedFuture(res);
                        }

                    //----subcase2: v havent performed permittedcalls, so return sresult, but dont reset testcount value
                        return CompletableFuture.completedFuture(res);
                    }

                    //case3: no chance of reciving response in Open state, so no handling needed

                }

                //case2: v hv got an exception (so again v must be in Closed or Half closed)
                //----subcase1: v r in closed state, somthng is wrong, so mv to half closed
                if(currState.get().equals(State.CLOSED)){
                    failureCount.incrementAndGet();
                    currState.set(State.HALF_CLOSED);
                }else if(currState.get().equals(State.HALF_CLOSED)){
                //-----subcase2: v r in half closed state
                    failureCount.incrementAndGet();
                    //case1: if failure count exceeds threshold, move to open state, set failure count to 0
                    if(failureCount.get()>=failureRateThreshold){
                        currState.set(State.OPEN);
                        failureCount.set(0);
                    }else{
                        //case2: failure count is less than threshold, so we need to still stay here for retrying till testCount<=maxAllowed value, so schedule retry, each retry shud happn after duration
                        CompletableFuture<T>retryFuture=new CompletableFuture<>();
                        scheduler.schedule(()->{
                            execute(supplier).handle((r,e)->{
                                if(e==null){
                                    //retry succeeded
                                }
                            });
                        }, waitDurationBtwHalfOpenStateReties.toMillis(), TimeUnit.MILLISECONDS);
                    }

                }


            }).thenCompose(x->x);
            
            
        }

        
        
    }
}
