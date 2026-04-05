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
        static Condition notFull=lock.newCondition();
        static Condition notEmpty=lock.newCondition();
        static int maxCap=10;
        void put(int x) throws InterruptedException{
            lock.lock();
            try{
                while(q.size()==maxCap) notFull.await();
                q.offer(x);
                notEmpty.signal();
            }finally{
                lock.unlock();
            }
        }
        int poll() throws InterruptedException{
            lock.lock();
            try{
                while(q.isEmpty()) notEmpty.await();
                int v=q.poll();
                notFull.signal();
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