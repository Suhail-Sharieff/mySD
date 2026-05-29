package state_mngt._02_VendingMc;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Item a = new Item(200, "Soda");
        Item b = new Item(100, "Chips");
        
        HashMap<Item, Integer> stock = new HashMap<>();
        stock.put(a, 3);
        stock.put(b, 5);

        VM vm = new VM(stock);
        
        // Simulation
        vm.selectItems(List.of(a, b));      // Transitions to AmountEntry
        vm.insertAmount(350.0);             // Adds money, transitions to Dispensing
        vm.dispense();                      // Dispenses and returns to Idle
    }
}

record Item(int price, String name) {}

class VM {
    private final Map<Item, Integer> stock;
    private List<Item> selectedItems = new ArrayList<>();
    private double balance = 0;
    private VM_State currState = new IdleState();

    public VM(Map<Item, Integer> stock) { this.stock = stock; }

    // Delegate methods to state
    public void selectItems(List<Item> items) { currState.selectItems(this, items); }
    public void insertAmount(double amt) { currState.insertAmount(this, amt); }
    public void dispense() { currState.dispense(this); }

    // Getters/Setters for State
    public void setState(VM_State state) { this.currState = state; }
    public Map<Item, Integer> getStock() { return stock; }
    public void setSelectedItems(List<Item> items) { this.selectedItems = items; }
    public List<Item> getSelectedItems() { return selectedItems; }
    public double getBalance() { return balance; }
    public void addBalance(double amt) { this.balance += amt; }
    public double getTotalPrice() { return selectedItems.stream().mapToDouble(Item::price).sum(); }
    public void reset() { selectedItems.clear(); balance = 0; }
}

interface VM_State {
    void selectItems(VM vm, List<Item> items);
    void insertAmount(VM vm, double amt);
    void dispense(VM vm);
}

class IdleState implements VM_State {
    public void selectItems(VM vm, List<Item> items) {
        for (Item i : items) {
            if (vm.getStock().getOrDefault(i, 0) <= 0) {
                System.out.println(i.name() + " out of stock!"); return;
            }
        }
        vm.setSelectedItems(items);
        System.out.println("Selected items. Total: " + vm.getTotalPrice());
        vm.setState(new AmountEntry());
    }
    public void insertAmount(VM vm, double amt) { System.out.println("Select items first."); }
    public void dispense(VM vm) { System.out.println("Select items first."); }
}

class AmountEntry implements VM_State {
    public void selectItems(VM vm, List<Item> items) { System.out.println("Items already selected."); }
    public void insertAmount(VM vm, double amt) {
        vm.addBalance(amt);
        if (vm.getBalance() >= vm.getTotalPrice()) {
            System.out.println("Amount sufficient. Ready to dispense.");
            vm.setState(new Dispensing());
        } else {
            System.out.println("Insufficient funds. Need: " + (vm.getTotalPrice() - vm.getBalance()));
        }
    }
    public void dispense(VM vm) { System.out.println("Insert money first."); }
}

class Dispensing implements VM_State {
    public void selectItems(VM vm, List<Item> items) { System.out.println("Dispensing in progress."); }
    public void insertAmount(VM vm, double amt) { System.out.println("Already paid."); }
    public void dispense(VM vm) {
        double change = vm.getBalance() - vm.getTotalPrice();
        System.out.println("Dispensing items... Returning change: " + change);
        
        // Logic to update stock
        for (Item i : vm.getSelectedItems()) {
            vm.getStock().put(i, vm.getStock().get(i) - 1);
        }
        
        vm.reset();
        vm.setState(new IdleState());
    }
}