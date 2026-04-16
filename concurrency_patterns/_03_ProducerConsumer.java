package concurrency_patterns;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

public class _03_ProducerConsumer<T> {
    private final int MAX_CAP=3;
    private final BlockingQueue<T> q=new LinkedBlockingDeque<>(MAX_CAP);
    private final ReentrantLock lock=new ReentrantLock();
    private final Condition producer=lock.newCondition();
    private final Condition consumer=lock.newCondition();
    void produce(T x) throws InterruptedException{
        lock.lock();
        try{
            while(q.size()>=MAX_CAP) producer.await();
            q.offer(x);
            MyUtils.println("Produced: "+x);
            consumer.signal();
        }finally{
            lock.unlock();
        }
    }
    void consume() throws InterruptedException{
        lock.lock();
        try{
            while(q.isEmpty()) consumer.await();
            MyUtils.println("Consumed: "+q.poll());
            producer.signal();
        }finally{
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        _03_ProducerConsumer<Integer>obj=new _03_ProducerConsumer<>();
        Random rand=new Random();
        Thread producer=new Thread(
            ()->{
                try{
                    for(int i=0;i<10;i++) obj.produce(rand.nextInt(0,100));
                }catch(InterruptedException ex){}
            }
        );
        Thread consumer=new Thread(
            ()->{
                try{
                    for(int i=0;i<10;i++) {
                        MyUtils.sleep(3000);
                        obj.consume();
                    }
                }catch(InterruptedException ex){}
            }
        );


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

}
