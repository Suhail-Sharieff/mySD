import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import utils.MyUtils;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Test obj = new Test();

        Thread singleBarber = new Thread(() -> {
            try {
                obj.serveCustomer();
            } catch (InterruptedException ex) {
                System.out.println("Barber: Shop is closed, going home!");
            }
        }, "Barber");

        Thread[] customers = new Thread[10];
        for (int i = 0; i < customers.length; i++) {
            final int j = i;
            customers[i] = new Thread(() -> {
                try {
                    obj.customerEnter(j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Customer[" + i + "]");
        }

        singleBarber.start();
        for (Thread c : customers) c.start();
        for (Thread c : customers) c.join();

        singleBarber.interrupt();
        singleBarber.join();
    }

    private final ReentrantLock lock = new ReentrantLock();
    // It helps to name Conditions based on WHO is waiting
    private final Condition barberSleeping = lock.newCondition(); 
    private final Condition waitingRoom = lock.newCondition(); 
    private final Condition barberChair = lock.newCondition(); 

    private final Queue<Integer> q = new LinkedList<>();
    private final int maxCap = 3;
    
    // State variables
    private int customerInChair = -1;
    private boolean haircutDone = false;

    public void customerEnter(int c) throws InterruptedException {
        lock.lock();
        try {
            if (q.size() >= maxCap) {
                MyUtils.println("Customer " + c + " LEFT (Shop Full)");
                return;
            }
            
            MyUtils.println("Customer " + c + " ENTERED");
            q.offer(c);
            
            // Wake up the barber if he is sleeping
            barberSleeping.signal();

            // 1. Wait until the barber calls MY specific ID
            while (customerInChair != c) {
                waitingRoom.await();
            }

            // 2. Wait until my haircut is actually finished
            while (!haircutDone) {
                barberChair.await();
            }
            
            MyUtils.println("Customer " + c + " FINISHED and leaving.");

        } finally {
            lock.unlock();
        }
    }

    public void serveCustomer() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                // Wait for a customer to enter the queue
                while (q.isEmpty()) {
                    barberSleeping.await();
                }

                // Call the next customer over
                customerInChair = q.poll();
                haircutDone = false; 
                
                // Wake up the customers so the chosen one can break their while-loop
                waitingRoom.signalAll(); 
                
                MyUtils.println("Barber is SERVING Customer " + customerInChair);
                
            } finally {
                // IMPORTANT: Unlock here so other customers can still enter the waiting room!
                lock.unlock(); 
            }

            // Simulate the haircut OUTSIDE the lock
            MyUtils.sleep(3000);

            lock.lock();
            try {
                // Haircut is complete
                haircutDone = true;
                
                // Tell the customer in the chair they can leave
                barberChair.signalAll(); 
            } finally {
                lock.unlock();
            }
        }
    }
}