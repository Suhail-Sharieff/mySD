package object_oriented_design._01_Singleton;
//in prev approach since the instance was NOT thread safe, coz 2 threads when try to access getInstance at same time, then it may b instantited twice

/*
Initializing instance by Thread-0
Instance Hash ID=76571692
Instance Hash ID=76571692
*/
public class _02_ThreadSafeSingleton {
    public static void main(String[] args) throws InterruptedException {
        //2 threads trying to initialize myInstance simultaneously
        Thread t1=new Thread(()->{
            DBConnection.getInstance();
        });
        Thread t2=new Thread(()->{
            DBConnection.getInstance();
        });
        t1.start();//dont use run, coz run is like sync call
        t2.start();

        t1.join();
        t2.join();



        
    }
    static class DBConnection{
        private DBConnection(){}
        private static DBConnection instance;
        static synchronized DBConnection getInstance(){
            if(instance==null){
                System.out.println("Initializing instance by "+Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                instance=new DBConnection();
            }
            System.out.println("Instance Hash ID="+instance.hashCode());
            return instance;
        }
    }    

    /*

    op without `synchronized`


    Initializing instance by Thread-0
    Initializing instance by Thread-1
    Instance Hash ID=object_oriented_design._01_Singleton._02_ThreadSafeSingleton$DBConnection@39297879
    Instance Hash ID=object_oriented_design._01_Singleton._02_ThreadSafeSingleton$DBConnection@293fd138

    see the same instance was initialized twice though v wanted once only
    */
}
