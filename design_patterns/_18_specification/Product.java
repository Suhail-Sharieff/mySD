package design_patterns._18_specification;

public class Product {
    private double price;
    private boolean inStock;
    private boolean onSale;

    public Product(double price,
                   boolean inStock, boolean onSale) {
        this.price = price;
        this.inStock = inStock;
        this.onSale = onSale;
    }

    public double getPrice() { return price; }
    public boolean isInStock() { return inStock; }
    public boolean isOnSale() { return onSale; }

    @Override
    public String toString() {
        return "Product [price=" + price + ", inStock=" + inStock + ", onSale=" + onSale + "]";
    }

    
}