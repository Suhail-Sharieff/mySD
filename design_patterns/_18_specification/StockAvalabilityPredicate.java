package design_patterns._18_specification;

public class StockAvalabilityPredicate implements CustomPredicate<Product>{
    @Override
    public boolean pass(Product item) {
       return item.isInStock();
    }
}