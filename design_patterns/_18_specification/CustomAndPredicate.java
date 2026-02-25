package design_patterns._18_specification;

public class CustomAndPredicate<T> implements CustomPredicate<T>{
    private CustomPredicate<T>left;
    private CustomPredicate<T>right;
    public CustomAndPredicate(CustomPredicate<T>left,CustomPredicate<T>right) {
        this.left=left;
        this.right=right;
    }
    @Override
    public boolean pass(T item) {
        return left.pass(item) && right.pass(item);
    }
}