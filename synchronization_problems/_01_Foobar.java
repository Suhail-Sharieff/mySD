package synchronization_problems;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

public class _01_Foobar {

    //if number is odd print Foo else print bar
    private static class Way1 {
        private final ReentrantLock lock;
        private final Condition cond;
        private final int n;
        private int turn;

        public Way1(int n) {
            lock = new ReentrantLock();
            cond = lock.newCondition();
            this.n = n;
            turn=1;
        }

        void printFoo() throws InterruptedException {
            for (int i = 1; i <= n; i++) {
                lock.lock();
                try {
                    while (turn==2)
                        cond.await();
                    MyUtils.println("FOO");
                    turn=2;//IMP
                    cond.signal();//MISTAKE:skipped this
                } finally {
                    lock.unlock();
                }
            }
        }

        void printBar() throws InterruptedException {
            for (int i = 1; i <= n; i++) {
                lock.lock();
                try {
                    while (turn==1)
                        cond.await();
                    MyUtils.println("BAR");
                    turn=1;//IMP
                    cond.signal();//IMP
                } finally {
                    lock.unlock();
                }
            }
        }

        /*t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR
        t=50 : Thread=Thread-0 : FOO
        t=50 : Thread=Thread-1 : BAR */
        public static void main(String[] args) throws InterruptedException {
            Way1 obj1 = new Way1(9);

            Thread t1 = new Thread(() -> {
                try {
                    obj1.printFoo();
                } catch (InterruptedException ex) {
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    obj1.printBar();
                } catch (InterruptedException ex) {
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

        }
    }



    //preferred in production when we dont hv complex conditionals, coz much easy to impeemnt
    private static class Way2{
        private final Semaphore foo;
        private final Semaphore bar;    
        private final int n;
        public Way2(int n) {
            foo=new Semaphore(1);
            bar=new Semaphore(0);
            this.n=n;
        }

        void printFoo() throws InterruptedException{
            for(int i=1;i<=n;i++){
                foo.acquire();
                MyUtils.println("FOO"); 
                bar.release();
            }
        }
        void printBar() throws InterruptedException{
            for(int i=1;i<=n;i++){
                bar.acquire();
                MyUtils.println("BAR"); 
                foo.release();
            }
        }
        public static void main(String[] args) throws InterruptedException {
             Way2 obj1 = new Way2(9);

            Thread t1 = new Thread(() -> {
                try {
                    obj1.printFoo();
                } catch (InterruptedException ex) {
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    obj1.printBar();
                } catch (InterruptedException ex) {
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Way1.main(args);
        Way2.main(args);
    }
}