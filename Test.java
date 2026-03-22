import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Test {




    static CompletableFuture<Integer> create(int x){
        return CompletableFuture.supplyAsync(()->{
            sleep(2333);
            return x;
        });
    }

    static void sleep(int dur){
        try{System.out.println("sleep: "+Thread.currentThread().getName());Thread.sleep(dur);}catch(Exception e){}
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<Integer>cf=create(34).    
        thenApply((x)->{return x*2;});


        cf.thenAccept((x)->{sleep(4333);System.out.println(x);});

        System.out.println("hi");


        cf.join();
    }
}