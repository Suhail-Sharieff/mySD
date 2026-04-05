import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier bar=new CyclicBarrier(3);

        var t1=new Thread(()->{
            sleep(3000);
            try {
                bar.await();//blocks THIS thread, untill other threads complee too
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        var t2=new Thread(()->{sleep(1000);try {
            bar.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }});
        var t3=new Thread(()->{sleep(2000);try {
            bar.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }});


        t1.start();t2.start();t3.start();
       

        t1.join();
        t2.join();
        t3.join();

    }


    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}
}