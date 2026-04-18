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
        private final int numChairs;
        private final Lock lock = new ReentrantLock();
        private final Condition customerAvailable = lock.newCondition();
        private final Condition barberAvailable = lock.newCondition();

        private int waiting = 0;
        private boolean barberReady = false;
        private final Queue<Integer> customerQueue = new LinkedList<>();
        private int nextTicket = 0;
        private int nowServing = 0;

        @SuppressWarnings("unused")
        public FairBarber(int chairs) {
            this.numChairs = chairs;
        }

        @SuppressWarnings("unused")
        public void barber() throws InterruptedException {
            while (true) {
                lock.lock();
                try {
                    // Wait for a customer
                    while (waiting == 0) {
                        System.out.println("Barber is sleeping");
                        customerAvailable.await();
                    }

                    // Get next customer
                    waiting--;
                    nowServing = customerQueue.poll();
                    barberReady = true;
                    barberAvailable.signalAll(); // Wake the right customer

                } finally {
                    lock.unlock();
                }

                // Cut hair outside lock
                cutHair();

                lock.lock();
                try {
                    barberReady = false;
                } finally {
                    lock.unlock();
                }
            }
        }

        @SuppressWarnings("unused")
        public boolean customer(int id) throws InterruptedException {
            lock.lock();
            try {
                if (waiting >= numChairs) {
                    System.out.println("Customer " + id + " leaves (no chairs)");
                    return false;
                }

                // Take a ticket and sit
                int myTicket = nextTicket++;
                customerQueue.add(myTicket);
                waiting++;
                System.out.println("Customer " + id + " sits (ticket " + myTicket + ")");

                // Wake the barber
                customerAvailable.signal();

                // Wait for our turn
                while (!barberReady || nowServing != myTicket) {
                    barberAvailable.await();
                }

            } finally {
                lock.unlock();
            }

            getHaircut(id);
            return true;
        }

        private void cutHair() {
            System.out.println("Barber is cutting hair");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        private void getHaircut(int id) {
            System.out.println("Customer " + id + " is getting haircut");
        }
    }
}