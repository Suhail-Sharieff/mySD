package design_patterns._12_state;

public interface MachineState {
    void selectItem(Item item);
    void insertMoney(int money);
    void dispenseItem();
    void reset();
}
