import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class Test2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        // CompletableFuture<CompletableFuture<Address>> x=getUser(23)
        // // .thenApply(user->getUserLocation(user));
        // CompletableFuture<Order> cf=getUser(23)
        // .thenCompose(user->getUserLocation(user))



        cf.thenAccept(res->System.out.println(res));
        System.out.println("hi");

        cf.join();


    }


    static void printThread(String op){System.out.println("for "+op+": "+Thread.currentThread().getName());}

    static CompletableFuture<User> getUser(int id){ return CompletableFuture.supplyAsync(()->new User(id));}
    static CompletableFuture<Address> getUserLocation(User u){return CompletableFuture.supplyAsync(()->new Address(u));}
    static CompletableFuture<Order> getOrders(Address addr){return CompletableFuture.supplyAsync(()->new Order(addr));}

    


    
    static void sleep(int dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
}


class User{
    public User(int id){Test.sleep(2000);Test.printThread("getUser");}
}

class Address{
    public Address(User u){Test.sleep(3000);Test.printThread("getAddreess");}
}
class Order{
    public Order(Address addr){Test.sleep(2000);Test.printThread("getOrder");}
}