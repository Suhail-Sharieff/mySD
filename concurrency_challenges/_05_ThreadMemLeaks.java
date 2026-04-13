package concurrency_challenges;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/*Using normal bounded Executor service using newFixedThreadPool(size), works fine but when a new thread arrives whn pool is fulll it throws an exception, if we want to handle it, we need to build custom pool executor using ThreadPoolExecutor(...) */
public class _05_ThreadMemLeaks {
    public static void main(String[] args) {
        int corePoolSize=10;
        int maxPoolSize=20;
        int maxTimeToKeepAnUnusedThreadInPool=5000;

        LinkedBlockingQueue<Runnable>queue=new LinkedBlockingQueue<>(100);
        RejectedExecutionHandler handlerToHandleRejectedThreadsWhenPoolIsFull=new ThreadPoolExecutor.CallerRunsPolicy();//what to do when new threads arrive when pool reached max cap and queue is also full

        /*
        Policies:

        AbortPolicy: just throws exception like how normal ExecutorService does
        CallerRunsPolicy: queues up new threads, executes them when thread pool becomes free or has space for it, handles natural backpressure
        DiscardPolicy: just rejects the new incoming thread by dropping it
        DiscardOldestPolicy: self explainatory

        
        
        */

        ExecutorService customSafeExceutorPool=new ThreadPoolExecutor(
            corePoolSize, 
            maxPoolSize ,
            maxTimeToKeepAnUnusedThreadInPool, TimeUnit.MILLISECONDS, 
            queue,handlerToHandleRejectedThreadsWhenPoolIsFull
        );

        customSafeExceutorPool.submit(()->{});

        customSafeExceutorPool.shutdown();
        customSafeExceutorPool.close();

        

    }
}
