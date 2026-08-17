package questions._08_VM;

import java.util.List;

record Item(String name,int price){}
interface State{
    void op1();
    void op2();
    void op3();
}
abstract class VendingMachineState implements State{
    final Vm vm;
    VendingMachineState(Vm vm){this.vm=vm;}
    void setState(VendingMachineState newState){vm.currState=newState;}
}
class S1 extends VendingMachineState{
    public S1(Vm vm) {
        super(vm);
    }
    @Override
    public void op1() {
        System.out.println("done op1");        
        setState(new S2(vm));
    }

    @Override
    public void op2() {
        System.out.println("perform op1 first");
    }

    @Override
    public void op3() {
        System.out.println("perform op1 first");        
    }
}
class S2 extends VendingMachineState{
    public S2(Vm vm) {
        super(vm);
    }
    @Override
    public void op1() {
        System.out.println("already done");
    }

    @Override
    public void op2() {
        System.out.println("done op2");
        setState(new S3(vm));
    }

    @Override
    public void op3() {
        System.out.println("perform op2 first");        
    }
}
class S3 extends VendingMachineState{
    public S3(Vm vm) {
        super(vm);
    }
    @Override
    public void op1() {
        System.out.println("done already");
    }

    @Override
    public void op2() {
        System.out.println("done already");
    }

    @Override
    public void op3() {
        System.out.println("done op3");       
        setState(new S1(vm));
    }
}
public class Vm{
    static Vm instance=new Vm();;
    VendingMachineState currState=new S1(this);
    List<Item>items;//admin apis for stock management
    private Vm(){}
    void op1(){currState.op1();}
    void op2(){currState.op2();}
    void op3(){currState.op3();}
}



