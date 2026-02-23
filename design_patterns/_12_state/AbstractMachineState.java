package design_patterns._12_state;

abstract class AbstractMachineState implements MachineState{
    private VendingMachine machine;
    public VendingMachine getMachine() {
        return machine;
    }
    public AbstractMachineState(VendingMachine machine){
        this.machine=machine;
    }
}
