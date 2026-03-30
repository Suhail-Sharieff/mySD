import java.util.concurrent.SynchronousQueue;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> sq = new SynchronousQueue<>();

        var t1 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println(sq.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();

        // Start the producer put AFTER consumer thread started, else face deadlock
        sq.put(23);

        t1.join();
    }
}