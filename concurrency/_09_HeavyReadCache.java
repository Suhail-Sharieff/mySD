package concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class _09_HeavyReadCache {
    private final ReadWriteLock rwLock;
    private int data;

    private final Lock readLock;
    private final Lock writeLock;

    public _09_HeavyReadCache() {
        rwLock=new ReentrantReadWriteLock();
        this.data=1;
        this.readLock=rwLock.readLock();
        this.writeLock=rwLock.writeLock();
    }

    public void write(int newVal){
        writeLock.lock();
        try{
            System.out.println("WRITE....");
            this.data=newVal;
            sleep(3000);
        }finally{
            writeLock.unlock();
        }
    }

    public int read(){
        readLock.lock();
        try{
            System.out.println("READ...");
            return this.data;
        }finally{
            readLock.unlock();
        }
    }


    private void sleep(long dur){
        try{Thread.sleep(dur);}catch(InterruptedException ex){}
    }



    public static void main(String[] args) throws InterruptedException {
        _09_HeavyReadCache cache=new _09_HeavyReadCache();
        Thread[]readers=new Thread[10];
        for(int i=0;i<readers.length;i++) readers[i]=new Thread(()->{
            cache.read();
        });
        Thread writer=new Thread(()->cache.write(23));;

        for(Thread t:readers) t.start();
        writer.start();

        for(Thread t:readers) t.join();
        writer.join();;
    }
    /*
    READ...
READ...
READ...
READ...
WRITE....
READ...
READ...
READ...
READ...
READ...
    
    */
}
