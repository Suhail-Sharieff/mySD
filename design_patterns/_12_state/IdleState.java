package design_patterns._12_state;

public class IdleState extends AbstractMachineState{

   public IdleState(VendingMachine machine) {
    super(machine);
   }
    @Override
    public void selectItem(Item item) {
        getMachine().setSelectedItem(item);
        getMachine().setCtx(new ItemSelectedState(getMachine()));
        System.out.println("Item Selected = "+item);
    }

    @Override
    public void insertMoney(int money) {
       System.out.println("Cannot insert money in idle state. Pls select item first!");
    }

    @Override
    public void dispenseItem() {
       System.out.println("No dispensible item in idle state. Pls insert money first!");
    }

    @Override
    public void reset() {
        System.out.println("You are still in idle state. So nothing to reset!");
    }
    
}
