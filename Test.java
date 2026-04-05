import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    static ReentrantLock lock = new ReentrantLock();
    static Condition cond = lock.newCondition();
    static int turn = 1;

    public static void main(String[] args) throws InterruptedException {

        Thread a = new Thread(() -> {
            try {
                printFirst();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        Thread b = new Thread(() -> {
            try {
                printSecond();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        Thread c = new Thread(() -> {
            try {
                printThird();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        a.start();
        ;
        b.start();
        c.start();
        ;

        a.join();
        b.join();
        c.join();

    }

    static void printFirst() throws InterruptedException {
        lock.lock();
        try {
            while (turn != 1)
                cond.await();
            ;
            System.out.println("first");
            turn = 2;
            cond.signal();

        } finally {
            lock.unlock();;
        }
    }

    static void printSecond() throws InterruptedException {
        lock.lock();
        try {
            while (turn != 2)
                cond.await();
            System.out.println("second");
            turn=3;
            cond.signal();

        } finally {
            lock.unlock();
        }
    }

    static void printThird() throws InterruptedException {
        lock.lock();
        try {
            while (turn != 3)
                cond.await();
            System.out.println("third");
            turn=0;
            cond.signal();

        } finally {
            lock.unlock();
        }
    }

}