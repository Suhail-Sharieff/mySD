package design_patterns._12_state;

class VendorMachine{
    private StateNode currState;

    public void setCurrState(StateNode currState) {
        this.currState = currState;
    }

    public VendorMachine() {
        currState=new IdleState();
        currState.next=new ItemSelectedState();
        currState.next.next=new HasMoneyState();
        currState.next.next.next=new DispensingState();
        currState.next.next.next.next=new IdleState();
    }

    public void startTransaction(){
        while (!(currState instanceof DispensingState)) {
            currState.execute();
            currState=currState.next;
        }
        currState.execute();
        currState=currState.next;
    }

    public StateNode getCurrState() {
        return currState;
    }

    
    
}
public class Main {
    public static void main(String[] args) {
        VendorMachine mc=new VendorMachine();
        mc.startTransaction();
    }
}
interface Executable{
    void execute();
}

enum CurrProcess{
    IDLE, ITEM_SELECTED, HAS_MONEY, DISPENSING
}
abstract class StateNode implements Executable{
    protected CurrProcess currProcess;
    protected StateNode next;
    public StateNode(CurrProcess process){
        this.currProcess=process;
    }
    @Override
    public String toString() {
        return currProcess.name();
    }
}

class IdleState extends StateNode{
    public IdleState() {
        super(CurrProcess.IDLE);
    }
    @Override
    public void execute() {
        System.out.println("In idle state...");
    }
}
class ItemSelectedState extends StateNode{
    public ItemSelectedState() {
        super(CurrProcess.ITEM_SELECTED);
    }
    @Override
    public void execute() {
        System.out.println("In item selected state...");
    }
}
class HasMoneyState extends StateNode{
    public HasMoneyState() {
        super(CurrProcess.HAS_MONEY);
    }
    @Override
    public void execute() {
        System.out.println("In hasmoney state...");
    }
}
class DispensingState extends StateNode{
    public DispensingState() {
        super(CurrProcess.DISPENSING);
    }
    @Override
    public void execute() {
        System.out.println("In dispensing state...");
    }
}