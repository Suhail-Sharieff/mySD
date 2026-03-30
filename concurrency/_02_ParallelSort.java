package concurrency;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class _02_ParallelSort {
    public static void main(String[] args) {

        int nums[]=new Random().ints(100000,0,100).toArray();


        // printTimeTaken(()->new SingleThreadedArraySorter(nums));//0.003
        // printTimeTaken(()->new ParallelSorter(nums, 0, nums.length-1).compute());//0.005

        //observe that still Parralel sort is slower, coz of merge uses memory its too expensive,
        //for production use Arrays.parrallelSort()
        printTimeTaken(()->Arrays.parallelSort(nums));//runs 100X fast
        
    }

    static void printTimeTaken(Runnable r){
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        double time = (end - start) / 1e9;
        System.out.println("Elapsed Time: " + time + " seconds");
    }


    static class ParallelSorter extends RecursiveAction{//use RecursiveTask<T> when want to return T
        private int nums[];
        private final int low;
        private final int high;
        private final int threshold=100000;
        public ParallelSorter(int nums[],int low,int high){
            int clone[]=nums.clone();
            this.nums=clone;
            this.low=low;
            this.high=high;
        }
        @Override
        protected void compute() {
            if(low>=high) return ;
            if(high-low+1<=threshold){Arrays.sort(nums, low, high);return ;}
            int mid=(low+high)>>1;
            ParallelSorter left=new ParallelSorter(nums, low, mid);
            ParallelSorter right=new ParallelSorter(nums, mid+1, high);
            left.fork();//run in separate thread
            right.compute();//compute sequentially locally
            left.join();//join to main when complted
            //so left ran in separate thread and right ran locally
            merge(low, mid, high, nums);
        }
    }


    static class SingleThreadedArraySorter{
        public SingleThreadedArraySorter(int nums[]) {
            int clone[]=nums.clone();
            mergeSort(0, nums.length - 1, clone);
        }
        private void mergeSort(int low,int high,int nums[]){
            if(low>=high) return;
            int mid=(low+high)>>1;
            mergeSort(low, mid, nums);
            mergeSort(mid+1, high, nums);
            merge(low, mid, high, nums);
        }
    }


        
        static void merge(int low,int mid,int high,int nums[]){
            int len=high-low+1;
            int merged[]=new int[len];
            int i=low,j=mid+1,k=0;
            while(i<=mid && j<=high){
                if(nums[i]<=nums[j]) merged[k++]=nums[i++];
                else merged[k++]=nums[j++];
            }
            while(i<=mid) merged[k++]=nums[i++];
            while(j<=high) merged[k++]=nums[j++];

            for(k=low;k<=high;k++) nums[k]=merged[k-low];
        }

    
}
