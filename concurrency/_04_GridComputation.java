package concurrency;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//cyclic barrier works similar to countdown latch , but it provides resetting option too that latch doesnt provide, we can use it to wait say till all threads finish some action bfr moving to nxt action

//v hv 3 threads
//v have 4X4 matrix
//v want to process blocks of 2X2 by all 3 threads together
// meaning, for each block we want to move to next block only when all 3 threads hav performed computation on it

public class _04_GridComputation {

    private static final int nThreads=3;
    private static double matrix[][]=new double[4][4];
    private static Random rand=new Random();

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        CyclicBarrier cb=new CyclicBarrier(nThreads, ()->{
            System.out.println("Matrix formed successfully:");
            for(var row:matrix) System.out.println(Arrays.toString(row));
        });

        ExecutorService es=Executors.newFixedThreadPool(nThreads);


        //v will proces 2X2 matrix at a time

        for(int t=0;t<nThreads;t++){
            final int tid=t;
            es.submit(()->{
                try{
                    for(int i=0;i<matrix.length;i+=2){
                        for(int j=0;j<matrix[0].length;j+=2){
                            process(i, i+1, j, j+1);
                            sleep(rand.nextLong(100,200));
                            System.out.println("thread "+tid+" completed filling matrix from {"+i+","+j+"} to {"+(i+1)+","+(j+1)+"}");
                            cb.await();//THIS THREAD (not main thread)waits till all 3 threads have completed processng this block
                        }
                    }
                }catch(Exception ex){}
            });

        }


        
        es.shutdown();
        
        


        
        

    }



    private static void process(int sx,int ex,int sy,int ey){
        for(int i=sx;i<=ex;i++) for(int j=sy;j<=ey;j++){
            matrix[i][j]=Math.random();
        }
    }
    private static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException e){}}
}
