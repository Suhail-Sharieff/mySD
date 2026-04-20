import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) {
        
    }


    final int n=5;
    enum State{EATING,THINKING,HUNGRY}
    int owner[];
    ReentrantLock lock=new ReentrantLock();
    Condition cond[];
    State state[];
    boolean isDirty[];

    public Test() {
        owner=new int[n];
        for(int i=0;i<n;i++) {
            owner[i]=Math.min(i, (i+1)%n);
        }
        cond=new Condition[n];
        for(int i=0;i<n;i++) cond[i]=lock.newCondition();
        state=new State[n];
        for(int i=0;i<n;i++) state[i]=State.THINKING;
        isDirty=new boolean[n];
        Arrays.fill(isDirty, true);
    }


    void pickFork(int pid) throws InterruptedException{
        lock.lock();
        try{
            state[pid]=State.HUNGRY;
            int left=pid;
            int right=(pid+1)%n;
            System.out.println(pid+ "is hungry");
            while(owner[left]!=pid || owner[right]!=pid){
                requestFork(pid, left);
                requestFork(pid, right);
                if(owner[left]!=pid || owner[right]!=pid) cond[pid].await();
            }
            System.out.println(pid+" got forks "+left+" and "+right+". Eating.....");
            state[pid]=State.EATING;

        }finally{
            lock.unlock();
        }
    }
    void putFork(int pid){
        lock.lock();
        try{
            state[pid]=State.THINKING;  
            int left=pid;
            int right=(pid+1)%n;
            isDirty[left]=true;
            isDirty[right]=true;
            System.out.println(pid+" completed eating "+left+" n "+right+" are dirty now, chking if hungry neighbors wants them");

            int leftNeigh=(pid-1+n)%n;
            int rightNeigh=(pid+1)%n;

            if(state[leftNeigh]==State.HUNGRY){
                requestFork(leftNeigh, left);
            }
            if(state[rightNeigh]==State.HUNGRY){
                requestFork(rightNeigh, right);
            }
        }finally{
            lock.unlock();
        }
    }
    void eat(int pid) throws InterruptedException{
        pickFork(pid);
        state[pid]=State.EATING;
        putFork(pid);
    }

    void requestFork(int requesterId,int forkNumber){
        int currOwner=owner[requesterId];
        if(currOwner==requesterId) return;
        if(state[currOwner]!=State.EATING && isDirty[forkNumber]){
            isDirty[forkNumber]=false;
            owner[forkNumber]=requesterId;
            System.out.println(currOwner+" passed "+forkNumber+" to "+requesterId);
            cond[requesterId].signal();
        }
    }







}
