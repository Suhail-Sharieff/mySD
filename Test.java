import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) {

        ExecutorService ioBound=Executors.newFixedThreadPool(3);
        ExecutorService cpuBound=Executors.newFixedThreadPool(10);



        // CompletableFuture<String>res= 
        //     apiRequest("url1")
        //     .thenCompose(res1->apiRequest("url2").thenApply(res2->res1+"\t"+res2))
        //     .thenApply(String::toUpperCase);

        CompletableFuture<Object>res= 
            apiRequest("url1")
            .thenApplyAsync(x->apiRequest("url2"),ioBound)
            .thenApplyAsync(x->apiRequest("url3"),cpuBound);
            ;

        
        
        res.thenAcceptAsync(r->System.out.println(r));
        System.out.println("Hi im executed first");
        res.join();

        // ioBound.close();
        // cpuBound.close();
    }

    static CompletableFuture<String> apiRequest(String url){
        return CompletableFuture.supplyAsync(()->{
            try{
                Thread.sleep(3000);
                System.out.println("fetched for "+url);
            }catch(Exception e){}
            return "Response = "+url;
        });
    }
}