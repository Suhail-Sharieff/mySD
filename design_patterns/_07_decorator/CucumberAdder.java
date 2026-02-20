package design_patterns._07_decorator;

public class CucumberAdder extends PizzaDecorator{

    public CucumberAdder(Pizza currentPizza) {
        super(currentPizza);
    }

    @Override
    public int getCost() {
        return currentPizza.getCost()+CostEnums.CUCUMBER.getCost();//add cucumbr price to currpizza
    }

    @Override
    public String getDesc() {
        return currentPizza.getDesc()+" CUCUMBER ";
    }
    
}
