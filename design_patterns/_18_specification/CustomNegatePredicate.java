package design_patterns._18_specification;

public 
class CustomNegatePredicate<T> implements CustomPredicate<T>{
    private CustomPredicate<T>curr;
    public CustomNegatePredicate(CustomPredicate<T>curr) {
        this.curr=curr;
    }
    @Override
    public boolean pass(T item) {
        return !(curr.pass(item));
    }
}
