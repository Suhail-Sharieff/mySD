public class Test {


    static final Object obj=new Object();
    public static void main(String[] args) throws InterruptedException {
        
        Thread holder=new Thread(()->{
            synchronized(obj){
                System.out.println("holder holding resource");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Holder releasing lock");
            }
        });
        Thread capturer=new Thread(()->{
             synchronized(obj){
                System.out.println("capturer holding resource");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("capturer releasing resource");
            }
        });


        holder.start();
        Thread.sleep(300);
        capturer.start();
        Thread.sleep(300);
        System.out.println(capturer.getState());

        holder.join();
        capturer.join();;

    }


}