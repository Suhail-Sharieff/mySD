package design_patterns._07_decorator;

public abstract class PizzaDecorator extends PlainPizza{
    protected Pizza currentPizza;
    public PizzaDecorator(Pizza currentPizza){
        this.currentPizza=currentPizza;
    }
}
