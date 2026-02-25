package design_patterns._18_specification;

public 
interface CustomPredicate<T>{
    boolean pass(T item);
    default CustomPredicate<T> or(CustomPredicate<T> p){
        return new CustomOrPredicate<T>(this,p);
    }
    default CustomPredicate<T> and(CustomPredicate<T> p){
        return new CustomAndPredicate<T>(this,p);
    }
    default CustomPredicate<T> negate(){
        return new CustomNegatePredicate<T>(this);
    }
}
