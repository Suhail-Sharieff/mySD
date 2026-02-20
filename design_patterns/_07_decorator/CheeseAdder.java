package design_patterns._07_decorator;

public class CheeseAdder extends PizzaDecorator {

    public CheeseAdder(Pizza currentPizza) {
        super(currentPizza);
    }

    @Override
    public int getCost() {
        return currentPizza.getCost()+CostEnums.CHEESE.getCost();
    }

    @Override
    public String getDesc() {
        return currentPizza.getDesc()+" CHEESE ";
    }
    
}
