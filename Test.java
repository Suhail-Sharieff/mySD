import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Counter cnter=new Counter();
        Thread t1=new Thread(()->cnter.increment());
        Thread t2=new Thread(()->cnter.increment());


        t1.start();;
        t2.start();

        t1.join();
        t2.join();

        System.out.println(cnter.getCount());
    }
}


class Counter {
    ReentrantLock lock;
    private int cnt;
    public Counter() {
        lock=new ReentrantLock();
    }
    public void increment(){
        lock.lock();
        for(int i=0;i<10000;i++) cnt++;
        lock.unlock();
    }

    public synchronized int getCount(){
        return cnt;
    }
    
}
