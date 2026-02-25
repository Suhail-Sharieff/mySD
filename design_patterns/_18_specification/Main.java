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