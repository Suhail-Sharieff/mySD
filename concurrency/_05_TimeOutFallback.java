package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class _05_TimeOutFallback {
    public static void main(String[] args) {

        Callable<Integer>someApi=()->{
            sleep(3000);
            return 1;
        };

        CompletableFuture<Integer>cf=getFutureWithTimeOut(someApi, 1000, -1);

        cf.thenAccept(System.out::println);

        cf.join();

        
    }   

    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}


    static <T> CompletableFuture<T> getFutureWithTimeOut(Callable<T> callable,long timeOutDuration,T defaultValueOnException){
        return CompletableFuture.supplyAsync(()->{
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException("Api service down!");
            }
        })
        .orTimeout(timeOutDuration, TimeUnit.MILLISECONDS)
        .exceptionally(ex->{
            System.out.println("EXCEPTION: "+ex.getMessage());
            return defaultValueOnException;
        });
    }
}
