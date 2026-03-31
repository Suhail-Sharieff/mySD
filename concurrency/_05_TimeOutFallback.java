package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class _05_TimeOutFallback {
    public static void main(String[] args) {

        Callable<Integer> someApi = () -> {
            sleep(200);
            return 1;
            // throw new RuntimeException("some error");
        };
        ExecutorService es=Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> cf = getFutureWithTimeOut(someApi, 1000, -1,es);

        cf.thenAccept(System.out::println);
        es.shutdown();;
        cf.join();


    }

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
        }
    }

    static <T> CompletableFuture<T> getFutureWithTimeOut(Callable<T> callable, long timeOutDuration,
            T defaultValueOnException, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService)//using executor service is safer in production than using fork join pool
        .orTimeout(timeOutDuration, TimeUnit.MILLISECONDS)
        .handle((result,exception)->{
            if(exception==null) return result;
            if(exception instanceof TimeoutException){
                System.out.println("TIMEOUT_ERROR: Timeout error occured!");
            }else{
                System.out.println("ERROR: "+exception.getMessage());
            }
            return defaultValueOnException;
        })
        ;
    }
}
