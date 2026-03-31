import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class Test {

    public static void main(String[] args) {

        Callable<Integer> independentService1 = () -> {
            sleep(1000);
            return 1;
        };
        Callable<Integer> independentService2 = () -> {
            sleep(2000);
            return 2;
        };
        Callable<Integer> independentService3 = () -> {
            sleep(2500);
            return 3;
        };

        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("call1 in "+Thread.currentThread());
                return independentService1.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("call2 in "+Thread.currentThread());
                return independentService2.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("call3 in "+Thread.currentThread());
                return independentService3.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        CompletableFuture<Integer>res=cf1.thenCombine(cf2, (x,y)->x+y).thenCombine(cf3, (x,y)->x+y);

        res.thenAccept(x->System.out.println(x));//all 3 run in parralle resuly in 2500 ms 


        res.join();


    }

    static void sleep(long dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
        }
    }
}