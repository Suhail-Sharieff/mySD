package concurrency;

import java.util.concurrent.atomic.AtomicReference;

public class _12_OptimisticCounter {
    public static void main(String[] args) throws InterruptedException {

        OptimisticLockCounter counter=new OptimisticLockCounter();

        Thread threads[]=new Thread[100];
        for(int i=0;i<threads.length;i++) threads[i]=new Thread(()->counter.incrVal());

        for(Thread t:threads) t.start();

        for(Thread t:threads) t.join();

        System.out.println(counter.getVal());//ans is 100 always


    }



    static class OptimisticLockCounter{//uses no locks, jus CAS(compare n set), whenver a thread wants to modify data (here incrVal()), it compares the state it knows with the actual version, if match, set new State, else try again


        record CounterState(int val,int version){}//so that v dont have to manualy impl equals(Counter state o){return o.val==this.val && o.version==this.version;}, record will handle that
        private final AtomicReference<CounterState>currState;

        
        public OptimisticLockCounter() {
            currState=new AtomicReference<>(new CounterState(0, 0));
        }
        public int getVal() {
            return currState.get().val;
        }
        public boolean incrVal() {
            CounterState newState;
            CounterState presentState;
            do{
                presentState=currState.get();
                int currVal=presentState.val,currVersion=presentState.version;
                newState=new CounterState(currVal+1, currVersion+1);
            }while(!currState.compareAndSet(presentState, newState));//see that v hvnt used locks anywhere, v just compare with prev state and set it to new state, it compares internally using equals(), retries, YES, it does waste CPU cycles, so use when thrd contention is low and retry actions are cheap

            return true;
        }
       
    }
}
