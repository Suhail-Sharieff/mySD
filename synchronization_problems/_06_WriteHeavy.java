package synchronization_problems;

import java.util.concurrent.Semaphore;
//concurrency_patterns/_04_ReaderWriter, coduld lead to writers starving to write on resoiurce coz we are realing writeerMutex only when all readers have left, now lets do revwerse, writer priority based locking
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

public class _06_WriteHeavy {
    
    private int shared_data;
    private final Semaphore mutex=new Semaphore(1);//lock for sharedData


    private final Semaphore readTry=new Semaphore(1);//mutex for reader trying to acquire when resource is held by writers
    
    private int readerCount=0;
    private int writerCount=0;
    private final ReentrantLock readerCountLock=new ReentrantLock();
    private final ReentrantLock writerCountLock=new ReentrantLock();

    private void readAcquire() throws InterruptedException{
        readTry.acquire();//will block readers if writers  havnt relesed this 
        try{
            readerCountLock.lock();
            try{
                if(++readerCount==1) mutex.acquire();;
            }finally{
                readerCountLock.unlock();
            }
        }finally{
            readTry.release();
        }
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
        writerCountLock.lock();
        try{
            if(++writerCount==1) readTry.acquire();
        }finally{
            writerCountLock.unlock();
        }
        mutex.acquire();
    }
    private void writeRelease(){
        mutex.release();
        writerCountLock.lock();
        try{
            if(--writerCount==0) readTry.release();
        }finally{
            writerCountLock.unlock();
        }
    }



    //now simple to use read and write

    public void read() throws InterruptedException{
        readAcquire();
        MyUtils.println("READ "+shared_data);
        readRelease();;
    }
    public void write() throws InterruptedException{
        writeAcquire();
        MyUtils.println("WRITE: "+(++shared_data));
        writeRelease();
    }
    

    public static void main(String[] args) throws InterruptedException {

        _06_WriteHeavy rw=new _06_WriteHeavy();

        Thread[] readers = new Thread[20];
        Thread[] writers = new Thread[4];

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
