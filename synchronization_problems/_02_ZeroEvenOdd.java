package synchronization_problems;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class _02_ZeroEvenOdd {

    public static void main(String[] args) throws InterruptedException {
        // Way1.main(args);
        Way2.main(args);
    }

    // ------------------using Semaphores
    private static class Way1 {
        public Way1(int n) {
            this.n = n;
        }

        public static void main(String[] args) throws InterruptedException {
            Way1 obj = new Way1(22);
            Thread t1 = new Thread(() -> {
                try {
                    obj.zero();
                } catch (InterruptedException ex) {
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    obj.even();
                } catch (InterruptedException ex) {
                }
            });
            Thread t3 = new Thread(() -> {
                try {
                    obj.odd();
                } catch (InterruptedException ex) {
                }
            });

            t1.start();
            t2.start();
            t3.start();
            ;

            t1.join();
            t2.join();
            t3.join();
        }

        private final Semaphore zero = new Semaphore(1);
        private final Semaphore even = new Semaphore(0);
        private final Semaphore odd = new Semaphore(0);
        private final int n;
        private int curr = 0;

        void zero() throws InterruptedException {
            for (int i = 1; i <= n; i++) {
                zero.acquire();
                System.out.print(0);
                curr++;
                if (curr % 2 == 1)
                    odd.release();
                else
                    even.release();
            }
        }

        void even() throws InterruptedException {
            for (int i = 1; i <= n / 2; i++) {// n/2 even nos from 1..n, MISTAKE: wrote n
                even.acquire();
                System.out.print(curr);
                zero.release();
            }
        }

        void odd() throws InterruptedException {
            for (int i = 1; i <= (n + 1) / 2; i++) {// IMP: MISTAKE wrote n/2 here also, but there are (n+1)/2 odd nums
                                                    // from 1..n
                odd.acquire();
                System.out.print(curr);
                zero.release();
            }
        }
    }

    // -----------------using conditional vars:(see howboth codes are almost same, just we v usd turn variable extra 0-zeroPrinter,1->oddPrinter,2->evenPreinter) coz ther ewe directly allowed 1 sema for zero, but here its based on conditions, so extra turn varibale is needed
    private static class Way2 {

        private final int n;

        public Way2(int n) {
            this.n = n;
            // lock on curr
            zeroPrinter = lock.newCondition();
            oddPrinter = lock.newCondition();
            evenPrinter = lock.newCondition();

        }

        public static void main(String[] args) throws InterruptedException {
            Way2 obj = new Way2(7);//01020304050607
            Thread t1 = new Thread(() -> {
                try {
                    obj.zero();
                } catch (InterruptedException ex) {
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    obj.even();
                } catch (InterruptedException ex) {
                }
            });
            Thread t3 = new Thread(() -> {
                try {
                    obj.odd();
                } catch (InterruptedException ex) {
                }
            });

            t1.start();
            t2.start();
            t3.start();
            ;

            t1.join();
            t2.join();
            t3.join();
        }

        private int turn = 0;
        private int curr = 0;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition zeroPrinter;
        private final Condition oddPrinter;
        private final Condition evenPrinter;

        void zero() throws InterruptedException {
            for (int i = 1; i <= n; i++) {
                lock.lock();
                try {
                    while (turn != 0)
                        zeroPrinter.await();
                    System.out.print(0);
                    curr++;
                    if (curr % 2 != 0) {
                        turn = 1;
                        oddPrinter.signal();
                    } else {
                        turn = 2;
                        evenPrinter.signal();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        void odd() throws InterruptedException {
            for (int i = 1; i <= (n+1)/2; i++) {
                lock.lock();
                try {
                    while (turn != 1)
                        oddPrinter.await();
                    System.out.print(curr);
                    turn = 0;
                    zeroPrinter.signal();
                } finally {
                    lock.unlock();
                }
            }
        }

        void even() throws InterruptedException {
            for (int i = 1; i <= n/2; i++) {
                lock.lock();
                try {
                    while (turn != 2)
                        evenPrinter.await();
                    System.out.print(curr);
                    turn = 0;
                    zeroPrinter.signal();
                } finally {
                    lock.unlock();
                }
            }
        }

    }
}
