package object_oriented_design._01_Singleton;


/*Prev 2 thread safe approaches are not that intutive to implement at first go, we can use the below shorter code */

public class _04_EagerInitilization {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> DBConnection.getInstance());
        Thread t2 = new Thread(() -> DBConnection.getInstance());

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }


    static class DBConnection{
        static DBConnection instance=new DBConnection();
        private DBConnection(){}
        public static DBConnection getInstance() {
            System.out.println("Instance being accessed by "+Thread.currentThread().getName());
           return instance;
        }

    }
}
/*
The problem here is that this heavy object is initialized in compile time itself, so in very large applications, this is not feasible coz there we need to care about each and every  byte of memory
 it could potentially waste resources if the singleton instance is never used by the client application.

*/

