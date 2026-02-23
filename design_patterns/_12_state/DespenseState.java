package design_patterns._12_state;

public class DespenseState extends AbstractMachineState{

    public DespenseState(VendingMachine machine) {
        super(machine);
    }
    @Override
    public void selectItem(Item item) {
        System.out.println("U have already selected item "+this.getMachine().getSelectedItem()+"and inserted money for that, u cannot select another item bfr depensing previous!");
    }

    @Override
    public void insertMoney(int money) {
        System.out.println("U have already selected item "+this.getMachine().getSelectedItem()+ " and inserted money for that, u cannot reinsert money");
    }

    @Override
    public void dispenseItem() {
       System.out.println("Dispensing item..");
       getMachine().reset();
    }

    @Override
    public void reset() {
        System.out.println("Cannot reset while dispensing item!");
    }
    
}
