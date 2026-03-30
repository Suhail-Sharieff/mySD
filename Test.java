import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> bq=new ArrayBlockingQueue<>(3);
        
        
        Thread producer=new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    bq.put(i);//blocks if more elemnts than cap
                    System.out.println("produced "+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
         
        Thread consumer=new Thread(()->{
            for(int i=10;i<20;i++){
                try {
                    System.out.println(bq.take());//blocks if q is empty
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try{Thread.sleep(1000);}catch(InterruptedException ex){}
            }
        });

        //


        producer.start();
        Thread.sleep(3000);
        consumer.start();

        producer.join();;
        consumer.join();

        System.out.println(bq);


    }
}