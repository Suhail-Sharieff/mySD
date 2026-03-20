public class Test {

    public static void main(String[] args) throws InterruptedException {
        
        int cnt[]={0};
        var t1=new Thread(()->{
            for(int i=0;i<10000;i++) cnt[0]++;
        });

        var t2=new Thread(()->{
            for(int i=0;i<10000;i++) cnt[0]++;
        });

        t1.start();
        t2.start();

        // t1.join();
        // t2.join();
        Thread.sleep(100);

        System.out.println(cnt[0]);

    }


}