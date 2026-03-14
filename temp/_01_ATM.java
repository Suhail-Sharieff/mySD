package temp;

import java.util.Map;


/*

- atm should support enuiry of balance, deposit(assuming no limit for nTimes per day and how much per transaction), withdraw(handle edge cases too like of of balance and supoort custom denominations)
- asssume the authentication using PIN and just hardcode user details (name,pin,balance)
- for now assume the card entered will hv user info

*/

class AuthServer{
    private static final Map<String,User>userInfo=Map.of("user1@gmail.com", new User("User1", "user1@gmail.com"),"user2@gmail.com",new User("user2", "user2@gmail.com"));
    static User getUser(String email){
        return userInfo.getOrDefault(email, null);
    }
}
class User{
    private final String name;
    private final String email;
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
class Card{
    private final String email;

    public Card(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }

}
class DBServer{
    private static final Map<String,Double>balance=Map.of("user1@gmail.com",1200d,"user2@gmail.com",900d);
    static double getBalance(User user){
        return balance.getOrDefault(user.getEmail(), 0d);
    }
    static void withdraw(User user,double amt){
        if(getBalance(user)<amt) throw new RuntimeException("Insufficient balance!");
        balance.put(user.getEmail(), balance.get(user.getEmail())-amt);
    }
    static void deposit(User user,double amt){
        balance.put(user.getEmail(), balance.get(user.getEmail())+amt);
    }
}
interface AtmOps{
    void enterCard(Card card);
    void enterPin(int pin);
    void deposit(double amt);
    void withdraw(double amt);
    double getBalance();
}

class Atm{
    private AtmState currState;
    private User user;
    private Card card;
    
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public User getUser() {
        return user;
    }

    public void setCurrState(AtmState currState) {
        this.currState = currState;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}

abstract class AtmState implements AtmOps{
    protected final Atm myAtm;

    public AtmState(Atm myAtm) {
        this.myAtm = myAtm;
    }
    
}

class IdleState extends AtmState{

    

    public IdleState(Atm myAtm) {
        super(myAtm);
    }

    @Override
    public void deposit(double amt) {
       throw new RuntimeException("Enter PIN first!");
    }

   
    @Override
    public void enterPin(int pin) {
        throw new RuntimeException("Insert card first!");
    }

    @Override
    public void withdraw(double amt) {
        throw new RuntimeException("INsert card first");
    }

    @Override
    public double getBalance() {
        throw new RuntimeException("Insert card first!");
    }

    @Override
    public void enterCard(Card card) {
        System.out.println("card entered");
       myAtm.setUser(AuthServer.getUser(card.getEmail()));
       myAtm.setCurrState(new PinEntryState(myAtm));
    }
    
}

class PinEntryState extends AtmState{

    

    @Override
    public double getBalance() {
        throw new RuntimeException("Enter Pin first!");
    }

    public PinEntryState(Atm myAtm) {
        super(myAtm);
    }

    @Override
    public void deposit(double amt) {
        throw new RuntimeException("Enter pin first");
    }

    @Override
    public void enterCard(Card card) {
        throw new RuntimeException("Card is already inserted");
    }

    @Override
    public void enterPin(int pin) {
        System.out.println("Pin entered!");
        myAtm.setUser(AuthServer.getUser(myAtm.getCard().getEmail()));
    }

    @Override
    public void withdraw(double amt) {
        throw new RuntimeException("Enter PIN first!");
    }

}


class WithdrawState extends AtmState{

    public WithdrawState(Atm myAtm) {
        super(myAtm);
    }

    @Override
    public void deposit(double amt) {
        throw new RuntimeException("Cannot deposit while withdrawing!");
    }

    @Override
    public void enterCard(Card card) {
        throw new RuntimeException("Card is already entered!");
    }

    @Override
    public void enterPin(int pin) {
        throw new RuntimeException("Pin is already entered!");
    }

    @Override
    public double getBalance() {
       throw new RuntimeException("Cannot get balance while withdrawing!");
    }

    @Override
    public void withdraw(double amt) {
        System.out.println("With drawfing "+amt);
       DBServer.withdraw(myAtm.getUser(), amt);
       myAtm.setCurrState(new IdleState(myAtm));
    }
}





public class _01_ATM {

    public static void main(String[] args) {
        
    }


}
