import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) {
        Random rand = new Random();
        Callable<Integer> callable = () -> {
            int r = rand.nextInt(4000, 6000);
            println(Integer.toString(r));
            // if (r % 2 == 0)
            //     throw new RuntimeException("Some server error occured");
            // else
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

        AsyncCircuitBreaker<Integer> cb = new AsyncCircuitBreaker<>(3, Duration.ofMillis(4000));
        try {
            CompletableFuture<Integer> cf = cb.execute(supplier);
            cf.thenAccept(System.out::println).exceptionally(ex -> {
                System.out.println(ex);
                return null;
            });
            cf.join();
        } catch (Exception ex) {
        }
    }

     static void println(String msg) {
        System.out.println(msg + " @ t=" + LocalTime.now().getSecond());
    }

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}


class AsyncCircuitBreaker<T> {

    private enum State { CLOSED, OPEN, HALF_OPEN }

    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final int failureThreshold;
    private final Duration openDuration;
    private volatile Instant openedAt;


    public AsyncCircuitBreaker(int failureThreshold, Duration openDuration) {
        this.failureThreshold = failureThreshold;
        this.openDuration = openDuration;
    }

    public CompletableFuture<T> execute(Supplier<CompletableFuture<T>> operation) {
        State currentState = state.get();

        // Circuit is open - fail fast
        if (currentState == State.OPEN) {
            if (shouldAttemptReset()) {
                state.compareAndSet(State.OPEN, State.HALF_OPEN);
            } else {
                return CompletableFuture.failedFuture(
                    new RuntimeException("Circuit breaker is open"));
            }
        }

        return operation.get().orTimeout(5000, TimeUnit.MILLISECONDS)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    handleFailure();
                } else {
                    handleSuccess();
                }
            });
    }

    private void handleSuccess() {
        failureCount.set(0);
        state.set(State.CLOSED);
    }

    private void handleFailure() {
        if (failureCount.incrementAndGet() >= failureThreshold) {
            state.set(State.OPEN);
            openedAt = Instant.now();
        }
    }

    private boolean shouldAttemptReset() {
        return openedAt != null &&
            Instant.now().isAfter(openedAt.plus(openDuration));
    }
}