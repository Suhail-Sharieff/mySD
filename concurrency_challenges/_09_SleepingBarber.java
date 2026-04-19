package concurrency_challenges;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

/*

1> there are N waiting chairs and 1 barber chair
2> barber sleeps when no customer on HIS chair
3> when some customer comes in he wakes barber up
4> say N=3, 3 customers come and sat on waiting chairs al together
5> barber wakes up, calls one customer to his own chair to do haircut, at the same time another customer fills up this vacancy
6> other barbers see that all 3 chairs are occupied and they leave
7> barber finishes cutting to 4 customers(3 waiting+1 alread on his chair) and closes

t=15 : Thread=Customer[0] : Customer 0: SAT ON WAITING CHAIR
t=15 : Thread=Customer[1] : Customer 1: SAT ON WAITING CHAIR
t=15 : Thread=Customer[0] : Customer 0: BEING SERVED (Sitting in chair)
t=15 : Thread=Customer[2] : Customer 2: SAT ON WAITING CHAIR
t=15 : Thread=Customer[3] : Customer 3: SAT ON WAITING CHAIR
t=15 : Thread=Customer[4] : Customer 4: SHOP FULL. LEFT
t=15 : Thread=Customer[5] : Customer 5: SHOP FULL. LEFT
t=15 : Thread=Customer[6] : Customer 6: SHOP FULL. LEFT
t=15 : Thread=Customer[7] : Customer 7: SHOP FULL. LEFT
t=15 : Thread=Customer[8] : Customer 8: SHOP FULL. LEFT
t=15 : Thread=Customer[9] : Customer 9: SHOP FULL. LEFT
t=18 : Thread=Customer[0] : Customer 0: HAIRCUT DONE. Leaving shop.
t=18 : Thread=Customer[1] : Customer 1: BEING SERVED (Sitting in chair)
t=21 : Thread=Customer[2] : Customer 2: BEING SERVED (Sitting in chair)
t=21 : Thread=Customer[1] : Customer 1: HAIRCUT DONE. Leaving shop.
t=24 : Thread=Customer[2] : Customer 2: HAIRCUT DONE. Leaving shop.
t=24 : Thread=Customer[3] : Customer 3: BEING SERVED (Sitting in chair)
t=27 : Thread=Customer[3] : Customer 3: HAIRCUT DONE. Leaving shop.
Barber: Shop is closed, going home!




*/

//---------------------unfair barber shop, the FIFO order of customers may not be followed
public class _09_SleepingBarber<T> {

    public static void main(String[] args) throws InterruptedException {

        _09_SleepingBarber<Integer> obj = new _09_SleepingBarber<>(3);

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
                    obj.customerArrive(j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Customer[" + i + "]");
        }

        singleBarber.start();
        for (Thread c : customers) {
            c.start();
        }

        // Wait for all customers to finish
        for (Thread c : customers) {
            c.join();
        }

        // All customers are done. Interrupt the sleeping barber to close the program.
        singleBarber.interrupt();
        singleBarber.join();
    }

    private final int nWaitingChairs;
    private final Semaphore customer;
    private final Semaphore barber;
    private final Semaphore mutex = new Semaphore(1);

    // NEW: Semaphore to signal the haircut is complete
    private final Semaphore haircutFinished;

    private int nCustomersInside = 0;

    public _09_SleepingBarber(int nWaitingChairs) {
        this.nWaitingChairs = nWaitingChairs;
        this.customer = new Semaphore(0);
        this.barber = new Semaphore(0);
        this.haircutFinished = new Semaphore(0); // Initialize to 0
    }

    void customerArrive(T c) throws InterruptedException {
        mutex.acquire();
        if (nCustomersInside < nWaitingChairs) {
            MyUtils.println("Customer " + c + ": SAT ON WAITING CHAIR");
            nCustomersInside++;
            customer.release();// immediatly inform barber about ur arrival, so then he'll call u in his chair
                               // immeditly and other customer can come in at same time, if placed below
                               // mutex.release(), 1 waiting chair becomes empty and blocked untill that person
                               // finishes haircut ie loss of 1 customer, so do this here only
            mutex.release();

            // Wait for barber to call you up
            barber.acquire();
            MyUtils.println("Customer " + c + ": BEING SERVED (Sitting in barber's chair)");

            // Wait for the barber to finish cutting the hair
            haircutFinished.acquire();
            MyUtils.println("Customer " + c + ": HAIRCUT DONE. Leaving shop.");

        } else {
            MyUtils.println("Customer " + c + ": SHOP FULL. LEFT");
            mutex.release();
        }
    }

    void serveCustomer() throws InterruptedException {
        while (true) {
            // Wait for customer to come
            customer.acquire();

            // first empty that chair bfr releasing barber, coz this customer will now go to
            // barber's chair for haircut, annouce other custmer if any to come in first
            mutex.acquire();
            nCustomersInside--;
            mutex.release();

            // Tell the customer it is their turn
            barber.release();

            // Simulate cutting hair
            MyUtils.sleep(3000);

            // Tell the specific customer in the chair that they are done
            haircutFinished.release();
        }
    }

    // ---------------------Fair solution(just mention if asked, genreally they dont
    // ask )

    @SuppressWarnings("unused")
    private static class FairBarber {
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
}