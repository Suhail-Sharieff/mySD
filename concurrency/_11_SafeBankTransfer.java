package concurrency;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class _11_SafeBankTransfer {
    private static class BankAccount{
        private final ReentrantLock lock;
        int balance;
        public BankAccount(ReentrantLock lock, int balance) {
            this.lock = lock;
            this.balance = balance;
        }
        public boolean transferWithTimeout(BankAccount from,BankAccount to,Duration timeout,int amt) throws InterruptedException{
            Instant now=Instant.now();
            Instant end=now.plusMillis(timeout.toMillis());
            while(now.isBefore(end)){
                if(from.lock.tryLock()){
                    System.out.println("acquiredlock on FROM acccount...");
                    try{
                        if(to.lock.tryLock()){
                            try{
                                System.out.println("acuiredlock on TO account...");
                                from.balance-=amt;
                                to.balance+=amt;
                                return true;
                            }finally{
                                System.out.println("Releasing TO lock");
                                to.lock.unlock();
                            }
                        }
                    }finally{
                        System.out.println("Releasing FROM lock ");
                        from.lock.unlock();
                    }
                }
                Thread.sleep(1000);
            }
            return false;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        BankAccount sender=new BankAccount(new ReentrantLock(),100);
        BankAccount receiver=new BankAccount(new ReentrantLock(), 200);

        Thread transaction1=new Thread(()->{
            try {
                sender.transferWithTimeout(sender, receiver, Duration.ofMillis(3000), 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread transaction2=new Thread(()->{
            try {
                sender.transferWithTimeout(sender, receiver, Duration.ofMillis(3000), 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //just using pessissimistic locking, ie locking every stuff bfr modifying or reading it making it atomic oprs
        //sender sends 2 transactions simultaneously, one of 50 , other of 30, his balnce must be 20

        transaction1.start();
        transaction2.start();

        transaction1.join();
        transaction2.join();

        System.out.println("balance of sender : "+sender.balance);//20


        /*
        acquiredlock on FROM acccount...
        acuiredlock on TO account...
        Releasing TO lock
        Releasing FROM lock
        acquiredlock on FROM acccount...
        acuiredlock on TO account...
        Releasing TO lock
        Releasing FROM lock
        balance of sender : 20
        git push */



    }
}
