package concurrency;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class _03_ParallelSummer {
    public static void main(String[] args) {
        long nums[]=new Random().longs(100000000, 0, 10909).toArray();


        printTimeTaken(()->System.out.println(linearSummer(nums, 0, nums.length-1)));//0.11
        printTimeTaken(()->System.out.println(new ParallelSummer(nums, 0, nums.length-1).compute()));//0.04 ie 3 times faster

    }

    static long linearSummer(long nums[],int from,int to){long sum=0l;for(int i=from;i<=to;i++)sum+=nums[i];return sum;}

     static void printTimeTaken(Runnable r){
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        double time = (end - start) / 1e9;
        System.out.println("Elapsed Time: " + time + " seconds");
    }



    static class ParallelSummer extends RecursiveTask<Long>{
        private final long nums[];
        private final int low;
        private final int high;
        private final int threshold;
        public ParallelSummer(long nums[],int low,int high) {
            this.nums=nums;
            this.low=low;
            this.high=high;
            this.threshold=10000;
        }
        @Override
        protected Long compute() {
            if(high-low+1<=threshold) return linearSummer(nums,low,high);
            int mid=(low+high)>>1;
            ParallelSummer left=new ParallelSummer(nums, low, mid);
            ParallelSummer right=new ParallelSummer(nums, mid+1, high);
            left.fork();//run left in `ll
            Long rightRes=right.compute();//run right locally(blockng)
            Long leftRes=left.join();//then join left(bloking)
            return leftRes+rightRes;
        }

    }
}
