package object_oriented_design._01_Singleton;

/* 

Double locK: 1st without lock, second with lock
Make instance volatile and lock in 2nd gate 
*/

public class _03_DoubleCheckSingleton {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> DBConnection.getInstance());
        Thread t2 = new Thread(() -> DBConnection.getInstance());

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static class DBConnection {
        private DBConnection() {
        }

        private static volatile DBConnection instance;// mark it volatile so all threads will see it

        static DBConnection getInstance() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (instance == null) {// 1st entry---NO LOCk, so both threads can enter here
                System.out.println(Thread.currentThread().getName() + " entered 1st gate");
                synchronized (DBConnection.class) {
                    // why not DBConnection.this?, coz this is a static method and also instance is
                    // static, so all obj will have this same lock
                    if (instance == null) {// 2nd --- with LOCK
                        System.out.println(Thread.currentThread().getName() + " entered 2nd gate");
                        instance = new DBConnection();
                        System.out.println("Instance initilized by Thread= " + Thread.currentThread().getName());
                    }
                }
            }
            return instance;
        }
    }

}

/*
 * Thread-0 entered 1st gate
 * Thread-1 entered 1st gate
 * Thread-0 entered 2nd gate
 * Initilizing instance...
 * Instance initilized by Thread= Thread-0
 */