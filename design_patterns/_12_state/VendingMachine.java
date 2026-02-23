package design_patterns._12_state;

public class VendingMachine implements MachineState{
    private MachineState ctx;
    private Item selectedItem;
    private int insertedMoney;
    
    public VendingMachine() {
        ctx=new IdleState(this);
    }
    public void reset(){
        System.out.println("Resetting mc state to idle state...");
        ctx=new IdleState(this);
        selectedItem=null;
        insertedMoney=0;
        System.out.println("Mc reset success!");
    }
    public MachineState getCtx() {
        return ctx;
    }
    public void setCtx(MachineState ctx) {
        this.ctx = ctx;
    }
    public Item getSelectedItem() {
        return selectedItem;
    }
    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }
    public int getInsertedMoney() {
        return insertedMoney;
    }
    public void setInsertedMoney(int insertedMoney) {
        this.insertedMoney = insertedMoney;
    }

    //-------------imple


    @Override
    public void selectItem(Item item) {
        getCtx().selectItem(item);
    }
    @Override
    public void insertMoney(int money) {
       getCtx().insertMoney(money);
    }
    @Override
    public void dispenseItem() {
       getCtx().dispenseItem();
    }

    
}
