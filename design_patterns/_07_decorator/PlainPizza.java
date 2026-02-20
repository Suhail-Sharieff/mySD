package design_patterns._07_decorator;

class PlainPizza implements Pizza{
    @Override
    public int getCost() {
        return 100;
    }
    @Override
    public String getDesc() {
        return " PLAIN ";
    }
    @Override
    public String toString() {
        return getDesc()+" TOTAL = "+getCost();
    }
}
