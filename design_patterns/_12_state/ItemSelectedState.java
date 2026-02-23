package design_patterns._12_state;

public class ItemSelectedState extends AbstractMachineState{

    public ItemSelectedState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void selectItem(Item item) {
        System.out.println("Cannot select item again since u already selected item "+item);
    }

    @Override
    public void insertMoney(int money) {
        if(money < getMachine().getSelectedItem().getCost()){
            System.out.println("You need to insert "+(getMachine().getSelectedItem().getCost()-money)+" more to proceed. Pls insert more money or reset");
            return;
        }
        System.out.println("Money inserted. Returning extra money = "+(getMachine().getSelectedItem().getCost()-money));
        getMachine().setCtx(new DespenseState(getMachine()));
        getMachine().setInsertedMoney(Math.min(money, getMachine().getSelectedItem().getCost()));
    }

    @Override
    public void dispenseItem() {
        System.out.println("Cannot dispense item bfr u enter money into machine, so pls enter money first!");
    }

    @Override
    public void reset() {
        getMachine().reset();
    }
    
}
