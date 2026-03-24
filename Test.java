import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Test {

    public static void main(String[] args) {
        ThreadFactory tf=Thread.ofVirtual().name("worker-", 0).factory();
        ExecutorService ser=Executors.newVirtualThreadPerTaskExecutor();
        // ExecutorService ser=Executors.newThreadPerTaskExecutor(tf);
        for(int i=0;i<10;i++){
            final int j=i;
            ser.submit(()->{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" "+j);
            });
        }

        System.out.println("hi");

        ser.close();
    }
}