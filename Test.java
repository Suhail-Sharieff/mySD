interface CP<T>{
    boolean pass(T it);
    default CP<T> and(CP<T>o){return new CAP<T>(this, o);}
    default CP<T> or(CP<T>o){return new COP<T>(this, o);}
    default CP<T> negate(CP<T>o){return new CNP<T>(this);}
}
class CAP<T> implements CP<T>{
    final CP<T>l,r;
    public CAP(CP<T>l,CP<T>r) {
        this.l=l;
        this.r=r;
    }
    @Override
    public boolean pass(T it) {
        return l.pass(it) && r.pass(it);
    }
}
class COP<T> implements CP<T>{
    final CP<T>l,r;
    public COP(CP<T>l,CP<T>r) {
        this.l=l;
        this.r=r;
    }
    @Override
    public boolean pass(T it) {
        return l.pass(it) || r.pass(it);
    }
}
class CNP<T> implements CP<T>{
    final CP<T>c;
    public CNP(CP<T>c) {
        this.c=c;
    }
    @Override
    public boolean pass(T it) {
        return !c.pass(it);
    }
}
class Prod{
    int pr;
    boolean av;
}
class PP implements CP<Prod>{

    @Override
    public boolean pass(Prod it) {
        return it.pr<=30;
    }
    
}
class AP implements CP<Prod>{

    @Override
    public boolean pass(Prod it) {
        return it.av;
    }
    
}
public class Test {

    public static void main(String[] args) {
        Prod prod=new Prod();
        prod.av=true;
        prod.pr=45;
        CP<Prod>cp=new PP().and(new AP());
        System.out.println(cp.pass(prod));
    }
}