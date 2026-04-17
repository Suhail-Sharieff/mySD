package synchronization_problems;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class _04_H20 {

    public static void main(String[] args) throws InterruptedException {
        UsingSemaphrores.main(args);System.out.println();
        UsingCyclicBarrier.main(args);System.out.println();
        UsingLocks.main(args);System.out.println();
    }

    // -----------------------usingSempa: prints 2H+10 always: HHO HHO HHO
    private static class UsingSemaphrores {
        private final Semaphore hydrogen = new Semaphore(2);
        private final Semaphore oxygen = new Semaphore(0);

        public void hydrogen() throws InterruptedException {
            hydrogen.acquire();
            System.out.print("H");
            oxygen.release();
        }

        public void oxygen() throws InterruptedException {
            oxygen.acquire(2);// wait for 2 oxygens to be released by hydrogen
            System.out.print("O");
            hydrogen.release(2);// free 2 hydrogens next
        }

        public static void main(String[] args) throws InterruptedException {
            UsingSemaphrores obj = new UsingSemaphrores();
            int nh = 6;
            int no = 3;
            Thread hydrogens[] = new Thread[nh];
            Thread oxygens[] = new Thread[no];

            for (int i = 0; i < nh; i++)
                hydrogens[i] = new Thread(() -> {
                    try {
                        obj.hydrogen();
                    } catch (InterruptedException ex) {
                    }
                });
            for (int i = 0; i < no; i++)
                oxygens[i] = new Thread(() -> {
                    try {
                        obj.oxygen();
                    } catch (InterruptedException ex) {
                    }
                });

            for (Thread h : hydrogens)
                h.start();
            for (Thread o : oxygens)
                o.start();

            for (Thread h : hydrogens)
                h.join();
            for (Thread o : oxygens)
                o.join();

        }

    }

    // --------------using Cycylic Barrier: prints 2H and 1O but order is not
    // gurenteed ie it can HOH, OHH ....etc
    private static class UsingCyclicBarrier {
        public static void main(String[] args) throws InterruptedException {
            UsingCyclicBarrier obj = new UsingCyclicBarrier();
            int nh = 6;
            int no = 3;
            Thread hydrogens[] = new Thread[nh];
            Thread oxygens[] = new Thread[no];

            for (int i = 0; i < nh; i++)
                hydrogens[i] = new Thread(() -> {
                    try {
                        obj.hydrogen();
                    } catch (Exception ex) {
                    }
                });
            for (int i = 0; i < no; i++)
                oxygens[i] = new Thread(() -> {
                    try {
                        obj.oxygen();
                    } catch (Exception ex) {
                    }
                });

            for (Thread h : hydrogens)
                h.start();
            for (Thread o : oxygens)
                o.start();

            for (Thread h : hydrogens)
                h.join();
            for (Thread o : oxygens)
                o.join();

        }

        public UsingCyclicBarrier() {
            hydrogen = new Semaphore(2);
            oxygen = new Semaphore(1);// see NOT 0
            barrier = new CyclicBarrier(3, () -> {
                // when any 3 threads(any combo of H and O ) hit barrier do this
                hydrogen.release(2);
                oxygen.release(1);
            });
        }

        private final CyclicBarrier barrier;
        private final Semaphore hydrogen;
        private final Semaphore oxygen;

        public void hydrogen() throws InterruptedException, BrokenBarrierException {
            hydrogen.acquire();// ensures exactly 2 H enters inside
            System.out.print("H");
            barrier.await();
        }

        public void oxygen() throws InterruptedException, BrokenBarrierException {
            oxygen.acquire();// exacly 1 O enters here
            System.out.print("O");
            barrier.await();
        }

        

    }
        // ------------------------Using Locks: HHO HHO pattern:
    private static class UsingLocks {

            public static void main(String[] args) throws InterruptedException {
                UsingLocks obj = new UsingLocks();
                int nh = 6;
                int no = 3;
                Thread hydrogens[] = new Thread[nh];
                Thread oxygens[] = new Thread[no];

                for (int i = 0; i < nh; i++)
                    hydrogens[i] = new Thread(() -> {
                        try {
                            obj.hydrogen(()->System.out.print("H"));
                        } catch (Exception ex) {
                        }
                    });
                for (int i = 0; i < no; i++)
                    oxygens[i] = new Thread(() -> {
                        try {
                            obj.oxygen(()->System.out.print("O"));
                        } catch (Exception ex) {
                        }
                    });

                for (Thread h : hydrogens)
                    h.start();
                for (Thread o : oxygens)
                    o.start();

                for (Thread h : hydrogens)
                    h.join();
                for (Thread o : oxygens)
                    o.join();

            }

            int i;
            Lock lock;
            Condition c;

            public UsingLocks() {
                lock = new ReentrantLock();
                c = lock.newCondition();
                i = 0;
            }

            public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
                lock.lock();
                try {
                    while (i == 2)
                        c.await();
                    // releaseHydrogen.run() outputs "H". Do not change or remove this line.
                    releaseHydrogen.run();
                    i = (i + 1) % 3;
                    c.signalAll();
                } finally {
                    lock.unlock();
                }
            }

            public void oxygen(Runnable releaseOxygen) throws InterruptedException {
                lock.lock();
                try {
                    while (i == 0 || i == 1)
                        c.await();
                    // releaseHydrogen.run() outputs "H". Do not change or remove this line.
                    releaseOxygen.run();
                    i = (i + 1) % 3;
                    c.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
}
