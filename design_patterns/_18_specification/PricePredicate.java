package design_patterns._18_specification;

public class PricePredicate implements CustomPredicate<Product>{
    private final int maxAffordablePrice;
    public PricePredicate(int maxAffordablePrice){
        this.maxAffordablePrice=maxAffordablePrice;
    }
    @Override
    public boolean pass(Product item) {
       return this.maxAffordablePrice >= item.getPrice();
    }
}