import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Test {

     public static void main(String[] args) {
        int nums[]={1,2,3,4,5,6,7,8,9,10};
        Summer summer=new Summer(nums, 0, nums.length-1);
        System.out.println(ForkJoinPool.commonPool().invoke(summer));
     }

}


class Summer extends RecursiveTask<Integer>{

    private final int nums[];
    private final int from;
    private final int to;
    private final int thrshHold=3;
    

    public Summer(int[] nums, int from, int to) {
        this.nums = nums;
        this.from = from;
        this.to = to;
    }



    @Override
    protected Integer compute() {
        int len=to-from;
        if(len<=thrshHold){
            int sum=0;
            for(int i=from;i<=to;i++) sum+=nums[i];
            return sum;
        }
        int mid=(from+to)>>1;
        Summer left=new Summer(nums, from, mid);
        Summer right=new Summer(nums, mid+1, to);
        left.fork();
        right.fork();
        int leftResult=left.join();System.out.println("["+from+","+mid+" via "+Thread.currentThread()+"]");
        int rightResult=right.join();System.out.println("["+mid+","+to+" via "+Thread.currentThread()+"]");
        return leftResult+rightResult;
    }
    
}