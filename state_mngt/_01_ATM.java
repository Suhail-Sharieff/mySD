package state_mngt;

/*

- atm shud support withdrawal, deposit, balance enquiry
- exception handling'
- user enters card(has AccountNumber+PIN) -> entersPin -> Atm fetches Account from BankServer -> user Authenticated -> he can now choose what he wants to do {withDraw,Deposit,enquire}
- so the ATM was in Idle state -> EnterCard state -> Enter Pin State -> Authenticated state -> {WithDraw,Deposit,Enquire} -> Idle State ie State Design Pattern
- if usr wanna withdraw amt, we also need Fixed denomination of coins for tht v can use enums
*/

import java.util.HashMap;
import java.util.List;

enum Denomination{
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500);
    private final int value;
    Denomination(int value) {
        this.value=value;
    }
    public int getValue() {
        return value;
    }
}


class Account{
    private final int accNumber;
    private int balance;
    public Account(int accNumber){this.accNumber=accNumber;this.balance=100;}

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public int getAccNumber() {
        return accNumber;
    }
}

class BankServer{
    public Account getAccount(int accNumber){
        if(accNumber==1) return new Account(accNumber);
        return  new Account(123);
    }
}

class Atm{
    private AtmState currState;
    private  Account currAccount;
    private Card card;
    private  final BankServer server;
    private final TransactionManager manager;
    private final CashDispenser dispenser;

    public Atm(BankServer server, TransactionManager manager,CashDispenser dispenser) {
        card=null;
        currAccount=null;
        this.server=server;
        this.manager=manager;
        this.dispenser=dispenser;
        setCurrState(new IdleState());
    }

    public void setCurrState(AtmState currState) {
        this.currState = currState;
    }

    public void setCard(Card card) {
            this.card = card;
    }

    public void setCurrAccount(Account currAccount) {
            this.currAccount = currAccount;
    }

    public Account getCurrAccount() {
        return currAccount;
    }

    public Card getCard() {
        return card;
    }

    public  void ejectCard(){
        System.out.println("Ejecting card!");
        setCard(null);
        setCurrAccount(null);
        setCurrState(new IdleState());
    }

    public void insertCard(Card card){currState.insertCard(this,card);}
    public  void insertPIN(int PIN){currState.insertPIN(this,server,PIN);}
    public  void depositAmount(int amt){currState.depositAmount(manager,this,amt);}
    public  void dispenseAmount(int amt){currState.dispenseAmount(manager,this,amt,dispenser);}

}

interface AtmState{
    void insertCard(Atm atm,Card card);
    void insertPIN(Atm atm,BankServer server,int PIN);
    void depositAmount(TransactionManager manager,Atm atm,int amt);
    void dispenseAmount(TransactionManager manager,Atm atm,int amt,CashDispenser dispenser);
    default void ejectCard(Atm atm){atm.ejectCard();};
}

class CardNotInsertedException extends  RuntimeException{@Override public String toString() {return "Card not inserted!";}}
class CardAlreadyInsertedException extends  RuntimeException{@Override public String toString() {return "Card already inserted!";}}
class IncorrectPinException extends  RuntimeException{@Override public String toString() {return "Incorrect PIN";}}
class EnterPinException extends  RuntimeException{@Override public String toString() {return "Enter PIN first";}}
class PinAlreadyEnteredException extends  RuntimeException{@Override public String toString() {return "PIN already recorded";}}
class InsufficientBalanceException extends  RuntimeException{@Override public String toString() {return "Insufficent Balance";}}
class InvalidDispensibleAmountException extends  RuntimeException{@Override public String toString() {return "Cannot dispense such amount with current denominations";}}


record Card(int accNumber, int PIN) {
}

class IdleState implements AtmState{

    public IdleState() {
        System.out.println("ATM IN IDLE STATE");
    }

    @Override
    public void insertCard(Atm atm,Card card) {
        System.out.println("Card inserted!");
        atm.setCard(card);
        atm.setCurrState(new PinEntryState());
    }

    @Override
    public void insertPIN(Atm atm,BankServer server,int PIN) {
        throw new CardNotInsertedException();
    }

    @Override
    public void depositAmount(TransactionManager manager,Atm atm,int amt) {
        throw  new CardNotInsertedException();
    }

    @Override
    public void dispenseAmount(TransactionManager manager,Atm atm,int amt,CashDispenser dispenser) {
        throw  new CardNotInsertedException();
    }

    @Override
    public void ejectCard(Atm atm) {
        throw  new CardNotInsertedException();
    }
}
class PinEntryState implements  AtmState{

    public PinEntryState() {
        System.out.println("ATM IN PIN ENTRY STATE");
    }

    @Override
    public void insertCard(Atm atm, Card card) {
        throw new CardAlreadyInsertedException();
    }

    @Override
    public void insertPIN(Atm atm,BankServer server,int PIN) {
        if(atm.getCard().PIN()!=PIN) throw new IncorrectPinException();
        System.out.println("Recorded PIN.User authenticated!");
        atm.setCurrAccount(server.getAccount(atm.getCard().accNumber()));
        atm.setCurrState(new AuthenticatedState());
    }

    @Override
    public void depositAmount(TransactionManager manager,Atm atm,int amt) {
        throw new EnterPinException();
    }

    @Override
    public void dispenseAmount(TransactionManager manager,Atm atm,int amt,CashDispenser dispenser) {
        throw new EnterPinException();
    }


}

class AuthenticatedState implements  AtmState{

    public AuthenticatedState() {
        System.out.println("ATM IN AUTHENTICATED STATE");
    }

    @Override
    public void insertCard(Atm atm, Card card) {
        throw new CardAlreadyInsertedException();
    }

    @Override
    public void insertPIN(Atm atm, BankServer server,int PIN) {
        throw  new PinAlreadyEnteredException();
    }

    @Override
    public void depositAmount(TransactionManager manager,Atm atm,int amt) {
        System.out.println("Depositing amount..");
        manager.depositAmount(atm.getCurrAccount(),amt);
        atm.ejectCard();
    }

    @Override
    public void dispenseAmount(TransactionManager manager,Atm atm,int amt,CashDispenser dispenser) {
        manager.withDrawAmount(atm.getCurrAccount(),amt);
        System.out.println("Dispensing amount");
        dispenser.dispenseCash(amt);
        atm.ejectCard();
    }
}

class TransactionManager{
    void depositAmount(Account account,int amt){
        account.setBalance(account.getBalance()+amt);
    }
    void withDrawAmount(Account account,int amt){
        if(account.getBalance()<amt) throw  new InsufficientBalanceException();
        account.setBalance(account.getBalance()-amt);
    }
}

class CashDispenser{
    void dispenseCash(int amt){
        if(!possibleToDispense(amt)) throw  new InvalidDispensibleAmountException();
        HashMap<Denomination,Integer>hs=new HashMap<>();
        Denomination[] denominations =Denomination.values();
        int len=denominations.length;
        for(int i=len-1;i>=0;i--){
            Denomination curr=denominations[i];
            int val=curr.getValue();
            int cnt=amt/val;
            if(cnt>0){
                hs.put(curr,cnt);
            }
            amt-=val*cnt;
        }
        System.out.println("Cash Dispensed "+hs);
    }

    private boolean possibleToDispense(int amt){
        Denomination[] denominations=Denomination.values();
        int len=denominations.length;
        for(int i=len-1;i>=0;i--){
            Denomination curr=denominations[i];
            int val=curr.getValue();
            int cnt=amt/val;
            amt-=val*cnt;
        }
        return amt==0;

    }
}

public class _01_ATM{
    public static void main(String[] args) {
        BankServer server=new BankServer();
        TransactionManager manager=new TransactionManager();
        CashDispenser dispenser =new CashDispenser();
        Atm atm=new Atm(server,manager,dispenser);

        Card card=new Card(1,123);

        atm.insertCard(card);
        atm.insertPIN(123);
        atm.dispenseAmount(90);

    }
}