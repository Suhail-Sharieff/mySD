import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) {

        Random rand = new Random();

        Callable<Integer> callable = () -> {
            int r = rand.nextInt(4900, 5000);
            System.out.println(r);
            if (r % 2 == 0)
                throw new RuntimeException("Some server error occured!");
            else {
                sleep(r);
                return 1;
            }
        };

        Supplier<CompletableFuture<Integer>> supplier = () -> {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return callable.call();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        };

        ScheduledExecutorService es = Executors.newScheduledThreadPool(2);

        CompletableFuture<Integer>cf=retry_with_exponential_backoff(3, es, 1000, 2, 5000, supplier);

        try{
            cf.thenAccept(v->System.out.println(v)).exceptionally(ex->{System.out.println(ex);return null;});
            cf.join();
        }catch(Exception e){
            // System.out.println(e);
        }finally{
            es.shutdown();
            es.close();;
        }


    }

    static <T> CompletableFuture<T> retry_with_exponential_backoff(
            int nRetries,
            ScheduledExecutorService es,
            long retyAfter,
            int backoff,
            long apiTimeout,
            Supplier<CompletableFuture<T>> supplier) {
        return supplier.get().orTimeout(apiTimeout, TimeUnit.MILLISECONDS)
                .handle((res, ex) -> {
                    if (ex == null)
                        return CompletableFuture.completedFuture(res);
                    if(nRetries<=0) return CompletableFuture.<T>failedFuture(ex);

                    System.out.println("Failed due to "+ex.getClass()+" NRetries Remaining: "+(nRetries-1));

                    CompletableFuture<T> retryFuture = new CompletableFuture<>();

                    es.schedule(() -> {
                        return retry_with_exponential_backoff(nRetries-1, es, retyAfter*backoff, backoff, apiTimeout, supplier)
                        .whenComplete((result,exception)->{
                            if(exception==null) retryFuture.complete(result);
                            else retryFuture.completeExceptionally(exception);
                        });
                    }, retyAfter , TimeUnit.MILLISECONDS);

                    return retryFuture;
                })
                .thenCompose(x -> x);
    }

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}