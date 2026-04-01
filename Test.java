import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Test {

    public static void main(String[] args) {

        List<CompletableFuture<Integer>> li = List.of(
                get(300),
                get(1000),
                get(4000));

        CompletableFuture<List<Integer>>cf=CompletableFuture.allOf(li.toArray(value -> new CompletableFuture[0])).thenApply(x -> {
           return li.stream().map(e->e.join()).toList();
        })
        ;

        cf.thenAccept(x->System.out.println(x)).join();

    }

    static CompletableFuture<Integer> get(long dur) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(dur);
            return (int) dur;
        });
    }

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
        }
    }
}