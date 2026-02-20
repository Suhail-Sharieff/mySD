package design_patterns._07_decorator;

public enum CostEnums {
    CUCUMBER(50),
    CHEESE(100);
    private int cost;
    public int getCost() {
        return cost;
    }
    CostEnums(int cost) {
        this.cost=cost;
    }
}
