import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) {

        Random rand = new Random();

        Callable<Integer> callable = () -> {
            int r = rand.nextInt(4990, 6000);
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
        int retries,
        ScheduledExecutorService es,
        long delay,
        int backoff,
        long timeout,
        Supplier<CompletableFuture<T>> supplier) {

    CompletableFuture<T> result = new CompletableFuture<>();

    attempt(result, retries, es, delay, backoff, timeout, supplier);

    return result;
}

static <T> void attempt(
        CompletableFuture<T> result,
        int retries,
        ScheduledExecutorService es,
        long delay,
        int backoff,
        long timeout,
        Supplier<CompletableFuture<T>> supplier) {

    es.schedule(() -> {
        supplier.get()
            .orTimeout(timeout, TimeUnit.MILLISECONDS)
            .whenComplete((res, ex) -> {

                if (ex == null) {
                    result.complete(res);
                    return;
                }

                if (retries <= 0) {
                    result.completeExceptionally(ex);
                    return;
                }

                System.out.println("Retrying after delay: " + delay);

                attempt(result, retries - 1, es,
                        delay * backoff, backoff, timeout, supplier);

            });

    }, delay, TimeUnit.MILLISECONDS);
}

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}