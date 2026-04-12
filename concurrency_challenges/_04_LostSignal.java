package concurrency_challenges;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/*

1> lost signal could occur when we cond.await() bfr acquiirng lock(), thats wrong, coz producer may call signal bfr consumer acuallt statrs waiting---LostSignal problem, 
2> crt way is lock first, then call await(), WKT await releaes lock itself, so no issue at all
3> another way lot singla can hapen is when say v hv mulitple consumers, and after producing we call consumer.signal() instaed of consumer.signalAll()



*/
public class _04_LostSignal {
    private boolean initilized = false;
    private ReentrantLock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();

    public void produce() throws InterruptedException {
        lock.lock();
        ;
        try {
            initilized = true;
            System.out.println("Prouduced by " + Thread.currentThread().getName());
            cond.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (!initilized)
                cond.await();//releases lock
            System.out.println("Consumed by " + Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        _04_LostSignal obj = new _04_LostSignal();
        Thread producer = new Thread(() -> {
            try {
                obj.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "producer");
        Thread consume = new Thread(() -> {
            try {
                obj.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "consumer");

        producer.start();
        consume.start();

        producer.join();
        consume.join();

    }
}
