package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/*An exponential backoff algorithm is a form of closed-loop control system that reduces the rate of a controlled process in response to adverse events. For example, if a mobile app fails to connect to its server, it might try again 1 second later, then if it fails again, 2 seconds later, then 4, etc. Each time, the pause is multiplied by a fixed amount (in this case, 2). */
public class _06_RetryWithExponentialBackoff {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        
        Callable<Integer>someApi=()->{
            sleep(5000);
            return 1;
        };

        ExecutorService es=Executors.newFixedThreadPool(4);
        

        CompletableFuture<Integer>cf=retryWithExponentialBackoffFuture(2, 3, someApi, es, -1, 1000);

        cf.thenAccept(x->System.out.println(x));

        es.shutdown();;

        cf.join();




    }


    static <T> CompletableFuture<T> retryWithExponentialBackoffFuture(
        int backoffFactor,
        int nRetries,
        Callable<T>callable,
        ExecutorService es,
        T defaultValueOnException,
        long timeOutDuration
    ) throws InterruptedException, ExecutionException
    {
        if(nRetries<0){
            System.out.println("All retries failed!");
            return CompletableFuture.completedFuture(defaultValueOnException);
        }   

        CompletableFuture<T>result=CompletableFuture.supplyAsync(()->{
            try{
                return callable.call();
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        },es)
        .orTimeout(timeOutDuration, TimeUnit.MILLISECONDS)
        .handle((res,ex)->{
            if(ex==null) return res;
            if(ex instanceof TimeoutException){
                System.out.println("TIMEOUT_ERROR");
            }else System.out.println("ERROR: "+ex.getMessage());
            System.out.println("***********NRetries Remaing: "+nRetries+"***********");
            System.out.println("New timeout: "+timeOutDuration*backoffFactor);
            return defaultValueOnException;
        })
        ;
        if(result.get()!=defaultValueOnException){
            System.out.println("Result received!");
            return result;
        }
        return retryWithExponentialBackoffFuture(backoffFactor, nRetries-1, callable, es, defaultValueOnException, timeOutDuration*backoffFactor);

    } 



    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){Thread.currentThread().interrupt();}}



    


}
