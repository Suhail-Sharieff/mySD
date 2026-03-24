import java.text.Format;
import java.util.concurrent.CompletableFuture;
import java.util.logging.*;

public class Test {

    public static void main(String[] args) throws Exception {

        Logger log = Logger.getLogger("test");

        // Create file handler
        FileHandler fh = new FileHandler("test.log", true);
        fh.setFormatter(new SimpleFormatter());
        log.addHandler(fh);

        log.setUseParentHandlers(true); // avoid console spam

        long startTime = System.currentTimeMillis();

        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> sum(3000))
                        .thenApply(result -> {
                            long endTime = System.currentTimeMillis();
                            log.log(Level.WARNING,"Execution time: " + (endTime - startTime) + " ms");
                            return result;
                        });

        // Main thread is NOT blocked here
        System.out.println("Main thread continues...");

        future.join(); // only if you want to wait before program exits
    }

    static int sum(int dur){
        sleep(dur);
        return 23;
    }

    static void sleep(int dur){
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(dur);
        } catch (InterruptedException ignored) {}
    }
}