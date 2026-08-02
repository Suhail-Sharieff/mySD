package design_patterns._18_specification;



/*

Java does provide custom Predicate
ex: Predicate<Product> filter = (e -> e<100 ).and(...).or(...).negate()..and so on;

But these are limited to and,or,negate and conditional checks operators, what if we also want to add our custom Predicates?

Then v can use Specification/Predicate pattern

Just create interface CustomePredicate<T>{} and different types of Operations tat impplemnts this interface like AndPredicate,Or...and so on,
pverride the methods that accepts 2 pRAMS with (this,other), so that u can use it with others later one param like .and(predicate).or(..)



*/





public class Main {

    public static void main(String[] args) {
        Product myProduct=new Product( 23, true, false);

        CustomPredicate<Product>cheap_instock_notOnSale=
        new PricePredicate(23)//max price = 23
        .and(new StockAvalabilityPredicate())//stock must be available
        .and(new SaleAvalabilityPredicate().negate())//shud not be on sale
        ;
        
        //myProduct passess all abve predicate

        System.out.println(cheap_instock_notOnSale.pass(myProduct));//true

    }
}
/*
another short way:

public class Main {

    public static void main(String[] args) {
        Product p=new Product(1,340,true);
        CustomPredicate<Product>pred=new IdPredicate().and(new PricePredicate()).and(new AvailablePredicate());
        System.out.println(pred.test(p));
    }
}
class Product{
    int id;
    int price;
    boolean available;
    public Product(int x,int y,boolean z) {
        this.id=x;
        this.price=y;
        this.available=z;
    }
}

class IdPredicate implements CustomPredicate<Product>{
    @Override
    public boolean test(Product obj) {
        return obj.id==1;
    }
} 
class PricePredicate implements CustomPredicate<Product>{
    @Override
    public boolean test(Product obj) {
        return obj.price<=300;
    }
} 
class AvailablePredicate implements CustomPredicate<Product>{
    @Override
    public boolean test(Product obj) {
        return obj.available;
    }
} 



interface CustomPredicate<T> {
    default CustomPredicate<T>and(CustomPredicate<T>o){
        return obj->this.test(obj) && o.test(obj);
    }
    default CustomPredicate<T>or(CustomPredicate<T>o){
        return obj->this.test(obj) && o.test(obj);
    }
    default CustomPredicate<T>not(){
        return obj->!this.test(obj);
    }
    boolean test(T obj);
}


*/
