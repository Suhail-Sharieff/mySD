import java.lang.reflect.AnnotatedWildcardType;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class Test {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        


        // CompletableFuture<CompletableFuture<Address>> x=getUser(23)
        // .thenApply(user->getUserLocation(user));
        CompletableFuture<Address> x=getUser(23)
        .thenCompose(user->getUserLocation(user));


        System.out.println(x.getClass().descriptorString());


        x.join();   


    }


    static CompletableFuture<User> getUser(int id){ return CompletableFuture.supplyAsync(()->new User(id));}
    static CompletableFuture<Address> getUserLocation(User u){return CompletableFuture.supplyAsync(()->new Address(u));}
    static CompletableFuture<Order> getOrders(Address addr){return CompletableFuture.supplyAsync(()->new Order(addr));}

    


    
    static void sleep(int dur){try{System.out.println(Thread.currentThread());Thread.sleep(dur);}catch(InterruptedException ex){}}
}


class User{
    public User(int id){}
}

class Address{
    public Address(User u){}
}
class Order{
    public Order(Address addr){}
}