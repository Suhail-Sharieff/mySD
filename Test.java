//custom predicate

public class Test {




    public static void main(String[] args) {

        PricePredicate price=new PricePredicate();
        Item itm=new Item(99, false);
        System.out.println(price.pass(itm));
        
    }
}


interface CustomPredicate<T>{
    boolean pass(T item);
    default CustomPredicate<T> or(CustomOrPredicate<T> other){return new CustomOrPredicate<>(this,other);}
    default CustomPredicate<T> and(CustomOrPredicate<T> other){return new CustomAndPredicate<>(this,other);}
    default CustomPredicate<T> not(){return new CustomNotPredicate<>(this);}
}
class CustomOrPredicate<T> implements CustomPredicate<T>{
    private final CustomPredicate<T> first;
    private final CustomPredicate<T> second;
    public CustomOrPredicate(CustomPredicate<T> first, CustomPredicate<T> second) {
        this.first = first;
        this.second = second;
    }
    @Override
    public boolean pass(T item) {
        return first.pass(item) && second.pass(item);
    }
}

class CustomAndPredicate<T> implements CustomPredicate<T>{
    private final CustomPredicate<T> first;
    private final CustomPredicate<T> second;
    public CustomAndPredicate(CustomPredicate<T> first, CustomPredicate<T> second) {
        this.first = first;
        this.second = second;
    }
    @Override
    public boolean pass(T item) {
        return first.pass(item) && second.pass(item);
    }
}
class CustomNotPredicate<T> implements CustomPredicate<T>{
    private final CustomPredicate<T> curr;
    public CustomNotPredicate(CustomPredicate<T> curr) {
        this.curr = curr;
    }
    @Override
    public boolean pass(T item) {
        return !curr.pass(item);
    }
}



class PricePredicate implements CustomPredicate<Item>{

    @Override
    public boolean pass(Item item) {
        return item.price<=100;
    }
    
}






class Item{
    int price;
    boolean isInStock;
    public Item(int price,boolean isInStock){
        this.isInStock=isInStock;
        this.price=price;
    }
}