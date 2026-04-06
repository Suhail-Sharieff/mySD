interface AtmBehaviour{
    void step1();
    void step2();
    void step3();
}
class Atm{
    AtmBehaviour currState;
    public Atm(){this.currState=new FirstState(this);}
    void step1(){currState.step1();}
    void step2(){currState.step2();}
    void step3(){currState.step3();}
    void setState(AtmState newState){currState=newState;}
}
abstract class AtmState implements AtmBehaviour{
    protected final Atm atm;
    public AtmState(Atm mc) {
        this.atm=mc;
    }
}


class FirstState extends AtmState{

    public FirstState(Atm mc) {
        super(mc);
    }

    @Override
    public void step1() {
        System.out.println("Step 1 complete");
        atm.setState(new SecondState(atm));
    }

    @Override
    public void step2() {
        System.out.println("Complete step1 firt");
    }

    @Override
    public void step3() {
        System.out.println("Complete step1 first");        
    }
}
class SecondState extends AtmState{

    public SecondState(Atm mc) {
        super(mc);
    }

    @Override
    public void step1() {
        System.out.println("Step 1 already done");
    }

    @Override
    public void step2() {
        System.out.println("step2 complete");
        atm.setState(new ThirdState(atm));
    }

    @Override
    public void step3() {
        System.out.println("Complete step2 first");        
    }
}
class ThirdState extends AtmState{

    public ThirdState(Atm mc) {
        super(mc);
    }

    @Override
    public void step1() {
        System.out.println("Step 1 already done");
    }

    @Override
    public void step2() {
        System.out.println("Step 2 already done");
    }

    @Override
    public void step3() {
        System.out.println("step3 complete");        
        atm.setState(new FirstState(atm));
    }
}
public class Test {

    public static void main(String[] args) {
        Atm atm=new Atm();

        atm.step1();
        atm.step2();
        atm.step3();

        atm.step2();;

    }
}