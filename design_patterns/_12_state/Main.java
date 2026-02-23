package design_patterns._12_state;

public class Main {

    public static void main(String[] args) {
        VendingMachine vm =new VendingMachine();
        vm.insertMoney(1); // Invalid in IdleState
        vm.dispenseItem();//invalid
        vm.selectItem(Item.CHOCOLATE);
        vm.insertMoney(200);
        vm.dispenseItem();

        System.out.println("\n--- Second Transaction ---");
        vm.selectItem(Item.ICE_CREAM);
        vm.insertMoney(290);
        vm.dispenseItem();

        
    }
}