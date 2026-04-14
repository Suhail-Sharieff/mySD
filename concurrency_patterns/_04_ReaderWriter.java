package concurrency_patterns;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import utils.MyUtils;

public class _04_ReaderWriter {
    // we can use read write lock, but here ill use Semaphors, not Lock(mutex) coz
    // mutex allows only 1 thread to access a resource, sempahores allow multiple

    private int data = 0;
    private int readersCount=0;
    private final Semaphore mutex=new Semaphore(1);//MISTAKE: made sempahore permits as nOFReads allowed, hard to handle
    private final Semaphore writer = new Semaphore(1);
    private final Semaphore readLimmiter;

    public _04_ReaderWriter(int data,int maxConcurrentReadsAllowed) {
        this.data = data;
        this.readLimmiter=new Semaphore(maxConcurrentReadsAllowed);
    }

    void read() {
       try{

            readLimmiter.acquire();//to control no of readers can enter, needed coz we cannot just allow infinite readers to read at a time, coz then writes wont et time to write

            mutex.acquire();
            if(++readersCount==1) writer.acquire();
            mutex.release();

            //mutiple threads may enter here
            MyUtils.println("READ START: " + Thread.currentThread().getName());
            Thread.sleep(5000);  // 🔥 IMPORTANT
            MyUtils.println("READ END: " + Thread.currentThread().getName());


            mutex.acquire();
            if(--readersCount==0) writer.release();
            mutex.release();

            readLimmiter.release();


       }catch(InterruptedException ex){}
    }

    void write(int newVal) {
        try {
            writer.acquire();
            this.data = newVal;
            MyUtils.println("WRITE: " + data);
            writer.release();
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) throws InterruptedException {
        _04_ReaderWriter rw = new _04_ReaderWriter(0,3);

        Thread[] readers = new Thread[20];
        Thread[] writers = new Thread[3];

        for (int i = 0; i < readers.length; i++)
            readers[i] = new Thread(() -> {
                rw.read();
            }, "READER{" + i + "}");
        for (int i = 0; i < writers.length; i++)
            writers[i] = new Thread(() -> {
                rw.write((int) MyUtils.getRand(0, 10));
            }, "WRITER{" + i + "}");

        for (Thread r : readers)
            r.start();
        for (Thread w : writers)
            w.start();

        for (Thread r : readers)
            r.join();
        for (Thread w : writers)
            w.join();
       

        /*
        
       t=44 : Thread=READER{0} : READ START: READER{0}
        t=44 : Thread=READER{1} : READ START: READER{1}
        t=44 : Thread=READER{2} : READ START: READER{2}
        t=50 : Thread=READER{0} : READ END: READER{0}
        t=50 : Thread=READER{4} : READ START: READER{4}
        t=50 : Thread=READER{2} : READ END: READER{2}
        t=50 : Thread=READER{1} : READ END: READER{1}
        t=50 : Thread=READER{3} : READ START: READER{3}
        t=50 : Thread=READER{5} : READ START: READER{5}
        t=55 : Thread=READER{4} : READ END: READER{4}
        t=55 : Thread=READER{6} : READ START: READER{6}
        t=55 : Thread=READER{5} : READ END: READER{5}
        t=55 : Thread=READER{3} : READ END: READER{3}
        t=55 : Thread=READER{7} : READ START: READER{7}
        t=55 : Thread=READER{8} : READ START: READER{8}
        t=0 : Thread=READER{6} : READ END: READER{6}
        t=0 : Thread=READER{9} : READ START: READER{9}
        t=0 : Thread=READER{8} : READ END: READER{8}
        t=0 : Thread=READER{7} : READ END: READER{7}
        t=0 : Thread=READER{10} : READ START: READER{10}
        t=0 : Thread=READER{11} : READ START: READER{11}
        t=5 : Thread=READER{9} : READ END: READER{9}
        t=5 : Thread=READER{12} : READ START: READER{12}
        t=5 : Thread=READER{10} : READ END: READER{10}
        t=5 : Thread=READER{11} : READ END: READER{11}
        t=5 : Thread=READER{13} : READ START: READER{13}
        t=5 : Thread=READER{15} : READ START: READER{15}
        t=10 : Thread=READER{12} : READ END: READER{12}
        t=10 : Thread=READER{16} : READ START: READER{16}
        t=10 : Thread=READER{13} : READ END: READER{13}
        t=10 : Thread=READER{15} : READ END: READER{15}
        t=10 : Thread=READER{17} : READ START: READER{17}
        t=10 : Thread=READER{18} : READ START: READER{18}
        t=15 : Thread=READER{16} : READ END: READER{16}
        t=15 : Thread=READER{19} : READ START: READER{19}
        t=15 : Thread=READER{18} : READ END: READER{18}
        t=15 : Thread=READER{17} : READ END: READER{17}
        t=15 : Thread=READER{14} : READ START: READER{14}
        t=20 : Thread=READER{19} : READ END: READER{19}
        t=20 : Thread=READER{14} : READ END: READER{14}
        t=20 : Thread=WRITER{0} : WRITE: 3
        t=20 : Thread=WRITER{1} : WRITE: 3
        t=20 : Thread=WRITER{2} : WRITE: 3
                
        */

    }

}
