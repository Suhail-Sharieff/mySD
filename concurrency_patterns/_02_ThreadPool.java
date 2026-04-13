package concurrency_patterns;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import utils.MyUtils;

public class _02_ThreadPool {
    public static void main(String[] args) {
        ExecutorService es=new ThreadPoolExecutor(3, 6, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(2),new ThreadPoolExecutor.CallerRunsPolicy());//if  q is full and all worers in pool are used, then pool size extends to 6

        for(int i=0;i<10;i++) {
            final int j=i;
            es.submit(()->someApi(j));
        }


        es.shutdown();

        es.close();


    }


    static void someApi(int i){
        MyUtils.sleep(MyUtils.getRand(500, 3000));
        MyUtils.println("done : "+i);
    }

}
