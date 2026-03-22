public class Test {

    static int balance = 500;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(4000);
                withDraw();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "withdrawer");
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                deposit();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "depositor");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        ;

        System.out.println(balance);

    }

    static void withDraw() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(300);
        balance -= 100;
    }

    static void deposit() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(200);
        balance += 200;
    }

}