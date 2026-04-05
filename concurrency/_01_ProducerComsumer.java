package concurrency;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class _01_ProducerComsumer {
    public static void main(String[] args) throws InterruptedException {


        int processingCap=3;

        ArrayBlockingQueue<Integer>buffer=new ArrayBlockingQueue<>(processingCap);


        Thread producer=new Thread(()->{
            for(int i=0;i<processingCap;i++){
                buffer.offer(i);//can add time limit to it too, if cap is full it blocks 
                System.out.println("PRODUCING: "+i+" AT "+LocalTime.now());
                sleep(1000);
            }
        });
        

        Thread consumer=new Thread(()->{
            for(int i=0;i<processingCap;i++){
                try {
                    System.out.println("CONSUMING: "+buffer.take()+" AT "+LocalTime.now());//if used poll() if q is empty it returns null, if used poll(time,TimeUnit) it blocks consumer till much time if not availbale, if used take() like here it blocks until somone is available
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



        producer.start();
        consumer.start();

        producer.join();
        consumer.join();




    }

    static void sleep(int ms){
        try{Thread.sleep(ms);}catch(InterruptedException ex){}
    }




    static class Way2{
        static Queue<Integer> q=new LinkedList<>();
        static ReentrantLock lock=new ReentrantLock();

        //used 2 condition, one for producer , the other for c0onsumer, actually v can use single conditinn, but then we need to use signalAll(), sinc v can hv multiple producers and cnsumers, all wake up and onyl 1 acquries lock, since v are waking up all, its very inefficient and useless, so by making 2 locks v ensure that producer wakes up consumers and consumers wake up producers
        static Condition producer=lock.newCondition();
        static Condition consumer=lock.newCondition();
        static int maxCap=10;
        void put(int x) throws InterruptedException{
            lock.lock();    
            try{
                while(q.size()==maxCap) producer.await();//if max cap, it releases lock here itself, locks again when woke up, chks condition again(coz its while loop), if satisfies, continues, in finally releases lock
                q.offer(x);
                consumer.signal();//no need of signallAll() coz theere is only 1 othr thread, ie consumer
            }finally{
                lock.unlock();
            }
        }
        int poll() throws InterruptedException{
            lock.lock();
            try{
                while(q.isEmpty()) consumer.await();
                int v=q.poll();
                producer.signal();
                return v;
            }finally{
                lock.unlock();
            }
        }
    }
}
/*
PRODUCING: 0 AT 17:39:11.300156700
CONSUMING: 0 AT 17:39:11.300156700
PRODUCING: 1 AT 17:39:12.326190200
CONSUMING: 1 AT 17:39:12.326190200
PRODUCING: 2 AT 17:39:13.338317800
CONSUMING: 2 AT 17:39:13.338317800
PS C:\Users\suhai\Desktop\mySD> 

*/