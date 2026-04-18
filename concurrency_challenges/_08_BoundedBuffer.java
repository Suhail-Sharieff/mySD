package concurrency_challenges;
//producer consumer pattern

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

public class _08_BoundedBuffer<T> {
    public static void main(String[] args) throws InterruptedException {

        MonitorBasedSoln<Integer> obj = new MonitorBasedSoln<>();
        // SemaphoreBasedSoln<Integer> obj2 = new SemaphoreBasedSoln<>();




        Thread producer[] = new Thread[10];
        Thread consumer[] = new Thread[1];
        Random rand = new Random();
        for (int i = 0; i < producer.length; i++)
            producer[i] = new Thread(() -> {
                try {
                    obj.produce(rand.nextInt(0, 10));
                } catch (InterruptedException ex) {
                }
            });
        for (int i = 0; i < consumer.length; i++)
            consumer[i] = new Thread(() -> {
                try {
                    obj.consume();
                } catch (InterruptedException ex) {
                }
            });

        for (int i = 0; i < Math.max(producer.length, consumer.length); i++) {
            if (i < producer.length)
                producer[i].start();
            if (i < consumer.length)
                consumer[i].start();
        }

        for (Thread p : producer)
            p.join();
        for (Thread c : consumer)
            c.join();
    }

    //-------------------------using conditional variables(easy+preferred)/Monnitors
    private static class MonitorBasedSoln<T> {
        private final Queue<T> buffer = new LinkedList<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final int maxSize = 3;
        private final Condition producer;
        private final Condition consumer;

        public MonitorBasedSoln() {
            producer = lock.newCondition();
            consumer = lock.newCondition();
        }

        void consume() throws InterruptedException {
            lock.lock();
            try {
                while (buffer.isEmpty())
                    consumer.await();// no busy waiting coz its released automatically if this condition isnt true
                MyUtils.println("CONSUME: " + buffer.poll());
                producer.signalAll();
            } finally {
                lock.unlock();
            }
        }

        void produce(T item) throws InterruptedException {
            lock.lock();
            try {
                while (buffer.size() >= maxSize)
                    producer.await();
                buffer.offer(item);
                MyUtils.println("PRODUCE: " + item);
                consumer.signalAll();
            } finally {
                lock.unlock();
            }
        }

    }


    @SuppressWarnings("unused")
    /*t=50 : Thread=Thread-0 : PRODUCE: 9
t=50 : Thread=Thread-4 : PRODUCE: 3
t=50 : Thread=Thread-6 : PRODUCE: 2
t=50 : Thread=Thread-10 : CONSUME: 9
t=50 : Thread=Thread-8 : PRODUCE: 2
..... */
    private static class SemaphoreBasedSoln<T>{

        private final Semaphore mutex=new Semaphore(1);
        private final Queue<T> buffer=new LinkedList<>();


        private final Semaphore emptySlots=new Semaphore(3);//kkep nPermits=MAX size of queue allowed
        private final Semaphore fullSlots=new Semaphore(0);


        @SuppressWarnings("unused")
        void produce(T item) throws InterruptedException{
            emptySlots.acquire();

            mutex.acquire();
            MyUtils.println("PRODUCE: " + item);
            buffer.offer(item);
            mutex.release();
            
            fullSlots.release();
        }

        @SuppressWarnings("unused")
        void consume() throws InterruptedException{
            fullSlots.acquire();

            mutex.acquire();
            MyUtils.println("CONSUME: " + buffer.poll());
            mutex.release();

            emptySlots.release();
        }

    }

}
