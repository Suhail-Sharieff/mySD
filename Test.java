import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        int serviceCount = 3;
        CountDownLatch servicesReady = new CountDownLatch(serviceCount);
        ExecutorService executor = Executors.newFixedThreadPool(serviceCount);

        // Start database connection pool
        executor.submit(() -> {
            try {
                System.out.println("1 Initializing database pool...");
                Thread.sleep(2000);  // Simulate slow init
                System.out.println("1 Database pool ready");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                servicesReady.countDown();
            }
        });

        // Warm up cache
        executor.submit(() -> {
            try {
                System.out.println("2 Warming up cache...");
                Thread.sleep(1500);
                System.out.println("2 Cache ready");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                servicesReady.countDown();
            }
        });

        // Load configuration
        executor.submit(() -> {
            try {
                System.out.println("3 Loading configuration...");
                Thread.sleep(500);
                System.out.println("3 Configuration loaded");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                servicesReady.countDown();
            }
        });

        // Wait for all services
        System.out.println("Waiting for services to initialize...");
        servicesReady.await();
        System.out.println("All services ready! Starting to accept requests.");
        
        executor.shutdown();
    }
}