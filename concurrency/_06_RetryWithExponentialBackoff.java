package concurrency;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/*An exponential backoff algorithm is a form of closed-loop control system that reduces the rate of a controlled process in response to adverse events. For example, if a mobile app fails to connect to its server, it might try again 1 second later, then if it fails again, 2 seconds later, then 4, etc. Each time, the pause is multiplied by a fixed amount (in this case, 2). */

//in this case say we hav an Api, that returns result after sleeping for random time if r is odd, esle throws exception, just for simulation
// api should execute within timeout of 5000 ms, 3 retries, scheduler ensures it retries after retryAfter*backoff time

/*
5552
Failed to get due to java.lang.RuntimeException: Some problem at server side. Retrying...
5598
Failed to get due to java.lang.RuntimeException: Some problem at server side. Retrying...
5781
Failed to get due to Time out . Retrying...
5259
Final failure: java.lang.RuntimeException: Failed after max reties!
All retries failed: java.lang.RuntimeException: Failed after max reties!
PS C:\Users\suhai\Desktop\mySD> 
*/
public class _06_RetryWithExponentialBackoff {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Random rand = new Random();
        Callable<Integer> callable = () -> {
            int r = rand.nextInt(4990, 6000);
            System.out.println(r);
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

        // note: its not just executor service, its ScheduledExecutorService
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

        CompletableFuture<Integer> cf = retryWithExponentialBackoffFuture(2, 3, supplier, es, 1000, 5000);

        cf.thenAccept(x -> System.out.println(x)).exceptionally(ex -> {
            System.out.println("Final failure: " + ex.getMessage());
            return null;
        });
        ;

        try {
            cf.join();
        } catch (Exception e) {
            System.out.println("All retries failed: " + e.getMessage());
        } finally {
            es.shutdown();
            es.close();
        }

    }

    static <T> CompletableFuture<T> retryWithExponentialBackoffFuture(
            int backoffFactor,
            int nRetries,
            Supplier<CompletableFuture<T>> supplier,
            ScheduledExecutorService es,
            long retryAfter,
            long timeOutDuration) throws InterruptedException, ExecutionException {
        // ****MISTAKE: add below commented code here, it runs multiple times else */
        // if (nRetries <= 0)
        // return CompletableFuture.failedFuture(new RuntimeException("Failed after max
        // reties!"));
        return supplier.get().orTimeout(timeOutDuration, TimeUnit.MILLISECONDS)
                .handle(
                        (res, ex) -> {
                            if (ex == null)
                                return CompletableFuture.completedFuture(res);
                            if (nRetries <= 0)
                                return CompletableFuture
                                        .<T>failedFuture(new RuntimeException("Failed after max reties!"));

                            System.out.println("Failed to get due to "
                                    + ((ex instanceof TimeoutException) ? "Time out " : ex.getMessage())
                                    + ". Retrying...");
                            CompletableFuture<T> retryFuture = new CompletableFuture<>();

                            es.schedule(() -> {

                                return retryWithExponentialBackoffFuture(backoffFactor, nRetries - 1, supplier, es,
                                        retryAfter * backoffFactor, timeOutDuration)
                                        .whenComplete((r, e) -> {

                                            if (e == null)
                                                retryFuture.complete(r);
                                            else {
                                                retryFuture.completeExceptionally(e);
                                            }

                                        });

                            }, retryAfter, TimeUnit.MILLISECONDS);

                            return retryFuture;
                        })
                .thenCompose(x -> x);

    }

    // naive code i wrote while trying, works but not good:
    // problemsis: get() becoms bloking a single thread, killing concurrency and
    // purpose of completable future, v dont use callble directly in production v
    // use CompletableFuture instead
    /*
     * static <T> CompletableFuture<T> retryWithExponentialBackoffFuture(
     * int backoffFactor,
     * int nRetries,
     * Callable<T>callable,
     * ExecutorService es,
     * T defaultValueOnException,
     * long timeOutDuration
     * ) throws InterruptedException, ExecutionException
     * {
     * if(nRetries<0){
     * System.out.println("All retries failed!");
     * return CompletableFuture.completedFuture(defaultValueOnException);
     * }
     * 
     * CompletableFuture<T>result=CompletableFuture.supplyAsync(()->{
     * try{
     * return callable.call();
     * }catch(Exception ex){
     * throw new RuntimeException(ex);
     * }
     * },es)
     * .orTimeout(timeOutDuration, TimeUnit.MILLISECONDS)
     * .handle((res,ex)->{
     * if(ex==null) return res;
     * if(ex instanceof TimeoutException){
     * System.out.println("TIMEOUT_ERROR");
     * }else System.out.println("ERROR: "+ex.getMessage());
     * System.out.println("***********NRetries Remaing: "+nRetries+"***********");
     * System.out.println("New timeout: "+timeOutDuration*backoffFactor);
     * return defaultValueOnException;
     * })
     * ;
     * if(result.get()!=defaultValueOnException){
     * System.out.println("Result received!");
     * return result;
     * }
     * return retryWithExponentialBackoffFuture(backoffFactor, nRetries-1, callable,
     * es, defaultValueOnException, timeOutDuration*backoffFactor);
     * 
     * }
     */

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
