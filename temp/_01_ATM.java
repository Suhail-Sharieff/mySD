package temp;

public class _01_ATM {

    public static void main(String[] args) {
        ATM atm=new ATM();
        atm.insertCard();
        atm.enterAmount(23);
        System.out.println(atm.getAmtEntered());
    }
}


class ATM implements AtmOperations{
    private ATM_State state;
    public double getAmtEntered() {
        return amtEntered;
    }
    public void setAmtEntered(double amtEntered) {
        this.amtEntered = amtEntered;
    }
    public int getPinEntered() {
        return pinEntered;
    }
    public void setPinEntered(int pinEntered) {
        this.pinEntered = pinEntered;
    }

    private double amtEntered;
    private int pinEntered;
    public ATM() {
        this.state=new IdleState(this);
    }
    public void changeState(ATM_State newState) {
        this.state = newState;
    }
    @Override
    public void enterAmount(double amt) {
        state.enterAmount(amt);
    }

    @Override
    public void enterPin(int pin) {
        state.enterPin(pin);
    }

    @Override
    public void insertCard() {
        state.insertCard();
    }

    @Override
    public void withDraw() {
        state.withDraw();
    }
    
}
interface AtmOperations{
    void insertCard();
    void enterPin(int pin);
    void enterAmount(double amt);
    void withDraw();
}

abstract class ATM_State implements AtmOperations{
    protected ATM atm;//--------------IMP, maintains state of which ATM must be known
    public ATM_State(ATM atm) {
        this.atm = atm;
    }
    public void setState(ATM_State state) {
        atm.changeState(state);
    }
    
}

class IdleState extends ATM_State{

    public IdleState(ATM atm) {
        super(atm);
    }

    @Override
    public void enterAmount(double amt) {
        System.out.println("Please insert the card first!");        
    }

    @Override
    public void enterPin(int pin) {
        System.out.println("Card is not inserted yet!");        
    }

    @Override
    public void insertCard() {
        System.out.println("Card inserted successfullly!");
        setState(new EnterAmountState(atm));
        
    }

    @Override
    public void withDraw() {
        System.out.println("Cannot withdraw before inserting of card!");;
    }

}

class EnterAmountState extends ATM_State{

    public EnterAmountState(ATM atm) {
        super(atm);
    }

    @Override
    public void enterAmount(double amt) {
        System.out.println("Amount recorded: "+amt);  
        atm.setAmtEntered(amt);      
        setState(new EnterPinState(atm));
    }

    @Override
    public void enterPin(int pin) {
       System.out.println("Cannot enter the PIN before entering amount to withdraw!");
    }

    @Override
    public void insertCard() {
       System.out.println("Cannot insert card while intering amount!");
        
    }

    @Override
    public void withDraw() {
        System.out.println("Cannot withdraw without entering amount !");
    }
    
}

class EnterPinState extends ATM_State{

    public EnterPinState(ATM atm) {
        super(atm);
    }

    @Override
    public void enterAmount(double amt) {
        System.out.println("Amount already recorded, cannot do again!");
    }

    @Override
    public void enterPin(int pin) {
       System.out.println("PIN recorded: "+pin);
       atm.setPinEntered(pin);
       setState(new WithDrawAmountState(atm));
    }

    @Override
    public void insertCard() {
        System.out.println("Card cannot be inserted while entering PIN!");        
    }

    @Override
    public void withDraw() {
        System.out.println("Enter the pin first before withdrawing!");        
    }
    
}

class WithDrawAmountState extends ATM_State{

    public WithDrawAmountState(ATM atm) {
        super(atm);
    }

    @Override
    public void enterAmount(double amt) {
        System.out.println("Bro how do u enter amount while withdrawing?");        
    }

    @Override
    public void enterPin(int pin) {
        System.out.println("Bro, cannot reenter pin while withdrawing!");        
    }

    @Override
    public void insertCard() {
        System.out.println("How on earth do u insert card while withdrawing?");        
    }

    @Override
    public void withDraw() {
        System.out.println("Amount withdrawn!");        
        setState(new IdleState(atm));
    }
    
}