package design_patterns._12_state;

public enum Item {

    ICE_CREAM(100),
    CHOCOLATE(120),
    SANDWITCH(300);

    private int cost;
    public int getCost() {
        return cost;
    }
    private Item(int cost){
        this.cost=cost;
    }
    @Override
    public String toString() {
        return "["+this.name()+" "+this.cost+"]";
    }
}
