import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        
        Callable<Integer>someApi=()->{
            sleep(3000);
            return 1;
        };

        CompletableFuture<Integer>cf=CompletableFuture.supplyAsync(()->{
            try {
                return someApi.call();
            } catch (Exception e) {
                throw new RuntimeException("Api call failed!");
            }
        })
        .orTimeout(5000, TimeUnit.MILLISECONDS)
        .exceptionally(ex->{
            System.out.println(ex.getMessage());
            return-1;
        });

        cf.thenAccept(x->System.out.println(x));

        cf.join();





    }


    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
}