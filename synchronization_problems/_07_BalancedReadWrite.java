package synchronization_problems;
//now we try to make both reads and writes balanced

import java.util.concurrent.Semaphore;
//all reads and write threds must pass through a single channel called as serviceQueue, when readers want to access resource they shud wait unitl writers have relesed queue and same applies for latter
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

public class _07_BalancedReadWrite {

    private int data;
    private final Semaphore mutex=new Semaphore(1);


    private int readerCount=0;
    private final ReentrantLock readerCountLock=new ReentrantLock();


    private final Semaphore serviceQueue=new Semaphore(1,true);//make it fair for reads and writes



    private void readAcquire() throws InterruptedException{
        serviceQueue.acquire();//wait till writers release them
        readerCountLock.lock();
        try{
            if(++readerCount==1) mutex.acquire();
        }finally{
            readerCountLock.unlock();
        }
        serviceQueue.release();//MISTAKE: dont add this in readRealse() , coz we dont want readers to block our queue
    }
    private void readRelease(){
        readerCountLock.lock();
        try{
            if(--readerCount==0) mutex.release();
        }finally{
            readerCountLock.unlock();
        }
    }

    private void writeAcquire() throws InterruptedException{
        serviceQueue.acquire();
        mutex.acquire();
    }
    private void writeRelease(){
        mutex.release();
        serviceQueue.release();
    }


    public void read() throws InterruptedException{
        readAcquire();
        MyUtils.println("READ: "+data);
        readRelease();
    }

    public void write() throws InterruptedException{
        writeAcquire();
        MyUtils.println("WRITE: "+(++data));
        writeRelease();
    }


    public static void main(String[] args) throws InterruptedException {
        _07_BalancedReadWrite rw = new _07_BalancedReadWrite();

        Thread[] readers = new Thread[20];
        Thread[] writers = new Thread[20];

        for (int i = 0; i < readers.length; i++)
            readers[i] = new Thread(() -> {
                try {
                    rw.read();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "READER{" + i + "}");
        for (int i = 0; i < writers.length; i++)
            writers[i] = new Thread(() -> {
                try {
                    rw.write();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "WRITER{" + i + "}");

        for (Thread r : readers)
            r.start();
        for (Thread w : writers)
            w.start();

        for (Thread r : readers)
            r.join();
        for (Thread w : writers)
            w.join();
       
    }




}
