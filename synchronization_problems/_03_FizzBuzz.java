package synchronization_problems;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class _03_FizzBuzz {

    // design decision:

    // using semaphores can be complex coz we arent sure whom to ive next control to
    // (fizz or buzz or fizzbuzz or number) coz we cant predixct if next number
    // would be div by 3 or 5 o neither, since conditons are complex its btter to
    // use conditiaonl variables

    public static void main(String[] args) throws InterruptedException {
        _03_FizzBuzz obj = new _03_FizzBuzz(10);
        Thread fizz = new Thread(() -> {
            try {
                obj.fizz();
            } catch (InterruptedException ex) {
            }
        });
        Thread buzz = new Thread(() -> {
            try {
                obj.buzz();
            } catch (InterruptedException ex) {
            }
        });
        Thread fizzBuzz = new Thread(() -> {
            try {
                obj.fizzbuzz();
            } catch (InterruptedException ex) {
            }
        });
        Thread number = new Thread(() -> {
            try {
                obj.number();
            } catch (InterruptedException ex) {
            }
        });

        fizz.start();
        buzz.start();
        fizzBuzz.start();
        number.start();

        fizz.join();
        buzz.join();
        fizzBuzz.join();
        number.join();

    }

    private int n;
    private ReentrantLock lock;
    private Condition cond;
    private int curr;

    public _03_FizzBuzz(int n) {
        this.n = n;
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
        this.curr = 1;
    }

    // printFizz.run() outputs "fizz". 3 ~5
    public void fizz() throws InterruptedException {
        while (curr <= n) {// can be replaced with `while(true)` as well
            lock.lock();
            try {
                while (curr <= n && !(curr % 3 == 0 && curr % 5 != 0))// mISTAKE: wrote curr>n instaed of currM=n
                    cond.await();
                if (curr > n)
                    return;// MISTAKE: i left this
                System.out.print("fizz ");
                curr++;
                cond.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    // printBuzz.run() outputs "buzz". 5 ~3
    public void buzz() throws InterruptedException {
        while (curr <= n) {
            lock.lock();
            try {
                while (curr <= n && !(curr % 5 == 0 && curr % 3 != 0))
                    cond.await();
                if (curr > n)
                    return;
                System.out.print("buzz ");
                curr++;
                cond.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz". 3 5
    public void fizzbuzz() throws InterruptedException {
        while (curr <= n) {
            lock.lock();
            try {
                while (curr <= n && !(curr % 3 == 0 && curr % 5 == 0))
                    cond.await();
                if (curr > n)
                    return;
                System.out.print("fizzbuzz ");
                curr++;
                cond.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer. either
    public void number() throws InterruptedException {
        while (curr <= n) {
            lock.lock();
            try {
                while (curr <= n && (curr % 3 == 0 || curr % 5 == 0))
                    cond.await();// mISTAKE: wrote && instaed of ||
                if (curr > n)
                    return;
                System.out.print(curr + " ");
                curr++;
                cond.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    // --------------------------------Semaphore based solution: so wt wre we re
    // thinking is how to release particular sepahore,what if we create a function
    // that decides which sem to reease based on value of curr??

    @SuppressWarnings("unused")
    private static class SemaphoreBAsedSoln {

        @SuppressWarnings("unused")
        public static void main(String[] args) throws InterruptedException {
            SemaphoreBAsedSoln obj=new SemaphoreBAsedSoln(10);
            Thread fizz = new Thread(() -> {
                try {
                    obj.fizz();
                } catch (InterruptedException ex) {
                }
            });
            Thread buzz = new Thread(() -> {
                try {
                    obj.buzz();
                } catch (InterruptedException ex) {
                }
            });
            Thread fizzBuzz = new Thread(() -> {
                try {
                    obj.fizzbuzz();
                } catch (InterruptedException ex) {
                }
            });
            Thread number = new Thread(() -> {
                try {
                    obj.number();
                } catch (InterruptedException ex) {
                }
            });

            fizz.start();
            buzz.start();
            fizzBuzz.start();
            number.start();

            fizz.join();
            buzz.join();
            fizzBuzz.join();
            number.join();
        }

        private final int n;
        private int curr;

        private final Semaphore fizz = new Semaphore(0);
        private final Semaphore buzz = new Semaphore(0);
        private final Semaphore fizzbuzz = new Semaphore(0);
        private final Semaphore number = new Semaphore(0);

        public SemaphoreBAsedSoln(int n) {
            this.n = n;
            curr = 1;
            signalCorrectSemaphore();
        }

        public void signalCorrectSemaphore() {
            if (curr > n) {
                // signal All to terminate (coz they call break on crr>n)
                fizz.release();
                buzz.release();
                fizzbuzz.release();
                number.release();
            } else {
                if (curr % 3 == 0 && curr % 5 == 0)
                    fizzbuzz.release();
                else if (curr % 3 == 0 && curr % 5 != 0)
                    fizz.release();
                else if (curr % 3 != 0 && curr % 5 == 0)
                    buzz.release();
                else
                    number.release();
            }
        }

        public void fizz() throws InterruptedException {
            while (curr <= n) {//can use true instead of curr<=n
                fizz.acquire();
                if (curr > n)//IMP: coz curr may hv been exceeded n by other threads
                    break;
                System.out.print("fizz ");
                curr++;
                signalCorrectSemaphore();
            }
        }

        public void buzz() throws InterruptedException {
            while (curr <= n) {
                buzz.acquire();
                if (curr > n)
                    break;
                System.out.print("buzz ");
                curr++;
                signalCorrectSemaphore();
            }
        }

        public void fizzbuzz() throws InterruptedException {
            while (curr <= n) {
                fizzbuzz.acquire();
                if (curr > n)
                    break;
                System.out.print("fizzbuzz ");
                curr++;
                signalCorrectSemaphore();
            }
        }

        public void number() throws InterruptedException {
            while (curr <= n) {
                number.acquire();
                if (curr > n)
                    break;
                System.out.print(curr + " ");
                curr++;
                signalCorrectSemaphore();
            }
        }

    }

}
