package design_patterns._18_specification;

public 
class SaleAvalabilityPredicate implements CustomPredicate<Product>{
    @Override
    public boolean pass(Product item) {
       return item.isOnSale();
    }
}
