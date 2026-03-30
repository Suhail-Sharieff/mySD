import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        Supplier<Integer>sp=()->23;


        ThreadLocal<Integer>tl=ThreadLocal.withInitial(sp);



        Thread t1=new Thread(()->{
            tl.set(23000);System.out.println(tl.get());try {
                tl.remove();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }System.out.println(tl.get());
        });

        Thread t2=new Thread(()->{tl.set(34000);System.out.println(tl.get());});


        t1.start();
        t2.start();


        t1.join();;
        t2.join();;

    }
}