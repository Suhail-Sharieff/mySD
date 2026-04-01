package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class _07_FanOut_FanIn {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // thought: wkt cyclic barrier blocks all threads, untill all threads have
        // reached till barrier, meh be v can useit for fain in , fan out i need to
        // think

        List<CompletableFuture<Integer>> li = List.of(
                CompletableFuture.supplyAsync(() -> {
                    sleep(2000);
                    return 1;
                }),
                CompletableFuture.supplyAsync(() -> {
                    sleep(3000);
                    return 2;
                }),
                CompletableFuture.supplyAsync(() -> {
                    sleep(4000);
                    return 3;
                }),
                CompletableFuture.supplyAsync(() -> {
                    sleep(5000);
                    return 4;
                }));

        CompletableFuture<List<Integer>> cf = FAN_IN(li);

        cf.thenAccept(res -> System.out.println(res));

        cf.join();

    }

    static <T> CompletableFuture<List<T>> FAN_IN(List<CompletableFuture<T>> li) {
        return CompletableFuture
                .anyOf(li.toArray(new CompletableFuture[0]))// typecase
                .thenApply(x -> li.stream().map(cf -> cf.join()).toList());
    }

    static <T> List<CompletableFuture<T>> FAN_OUT(List<Supplier<T>> tasks, Executor executor) {
        return tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, executor))
                .toList();
    }

    // just in case, supoose u want anyOf result to return thats enough
    static <T> CompletableFuture<T> anyOfSuccess(List<CompletableFuture<T>> futures) {

        CompletableFuture<T> result = new CompletableFuture<>();

        for (CompletableFuture<T> f : futures) {
            f.whenComplete((val, ex) -> {
                if (ex == null) {
                    result.complete(val); // first success wins
                    futures.forEach(o -> o.cancel(true)); // cancel others---saves CPU cycles and thread starvation @
                                                          // prod
                }
            });
        }

        // if ALL fail → complete exceptionally
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((v, ex) -> {
                    if (!result.isDone()) {
                        result.completeExceptionally(
                                new RuntimeException("All futures failed"));
                    }
                });

        return result;
    }

    /*
     * Navive approach i used: kills the reason for CompletableFuture again coz i hv
     * used join
     * 
     * static <T> void FAN_IN(CyclicBarrier cb, List<CompletableFuture<T>> li,
     * ExecutorService es, List<T> result)
     * throws InterruptedException {
     * for (int k = 0; k < li.size(); k++) {
     * final int i = k;
     * 
     * es.submit(() -> {
     * try {
     * CompletableFuture<T> cf = li.get(i);
     * cf.thenAccept(x -> result.add(x));
     * cf.join();// compute it in its thread
     * System.out.println(
     * i + " th result computed blocked at barrier...by " +
     * Thread.currentThread().getName());
     * cb.await();// wait untill all threads finish computation
     * System.out.println(i + " th result released from barrier! by " +
     * Thread.currentThread().getName());
     * } catch (Exception ex) {
     * }
     * });
     * }
     * }
     * 
     * 
     */

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
        }
    }
}
