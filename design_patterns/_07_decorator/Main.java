package design_patterns._07_decorator;

/*
Every pizza has a cost, but pizza can be decorated using say Cheese, Cucumber each must add some cost to original cost

*/

public class Main {
    public static void main(String[] args) {
        Pizza myPizza=new PlainPizza();//100 rs normal pizza
        System.out.println(myPizza);// PLAIN  TOTAL = 100
        myPizza=new CucumberAdder(myPizza);//cucumber adds 50 rs cost
        System.out.println(myPizza);// PLAIN  CUCUMBER  TOTAL = 150
        myPizza=new CheeseAdder(myPizza);//cheese addes 100 rs cost
        System.out.println(myPizza);// PLAIN  CUCUMBER  CHEESE  TOTAL = 250
    }
}
//this version can agan be extended to make it Factory based, i have stopped here to make it simple