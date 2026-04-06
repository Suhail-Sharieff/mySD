//chain of responsibuliyy


interface MiddlewareFunc{
    boolean pass(String s);
}

abstract class Middleware implements MiddlewareFunc{
    protected Middleware nxt;
    public boolean check(String s){
        if(nxt==null) return pass(s);
        if(pass(s)) return nxt.check(s);
        return false;
    }
}


class Chck1 extends Middleware{

    @Override
    public boolean pass(String s) {
        if(s.contains("key1")){
            System.out.println("chk1 pass");
            return true;
        }
        System.out.println("chk1 fail");
        return false;
    }

}
class Chck2 extends Middleware{

    @Override
    public boolean pass(String s) {
        if(s.contains("key2")){
            System.out.println("chk2 pass");
            return true;
        }
        System.out.println("chk2 fail");
        return false;
    }

}
class Chck3 extends Middleware{
    @Override
    public boolean pass(String s) {
        if(s.contains("key3")){
            System.out.println("chk3 pass");
            return true;
        }
        System.out.println("chk3 fail");
        return false;
    }
}

public class Test {

    public static void main(String[] args) {
        Middleware mw=new Chck1();
        mw.nxt=new Chck2();
        mw.nxt.nxt=new Chck3();

        System.out.println(mw.check("key1 key2"));


    }
}