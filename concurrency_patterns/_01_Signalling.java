package concurrency_patterns;

import java.util.concurrent.Semaphore;
import utils.MyUtils;


/*

forproper signal management fof threads, we can definetely use Lock+COndition pattern, but it has some implementation complexities, requires careful implementation and all

Great question! Choosing between **Lock + Condition Variable** and **Semaphore** for signaling depends on your use case, and each has distinct characteristics.

---

## Semaphore vs Lock + Condition Variable for Signaling

| Aspect                        | Semaphore                             | Lock + Condition Variable                   |
|-------------------------------|-------------------------------------|---------------------------------------------|
| **Signal Persistence**         | Signals are persistent (stored as permits). If signal arrives before waiter, it won't be lost. | Signals are lost if no thread is waiting; waiter can block forever if missed. Requires careful use of predicates. |
| **Blocking Behavior**          | `acquire()` blocks until permit available; `release()` never blocks | `wait()` blocks atomically releasing the lock, `signal()` wakes one waiter; no built-in counting. |
| **Complexity**                 | Simpler API for one-way signals or counting events | More flexible for complex wait conditions but requires extra care (e.g., while loops). |
| **Ownership**                  | No ownership; any thread can `release()` or `acquire()` | Must hold associated lock to call `wait()` or `signal()` |
| **Usage Pattern**              | Best for one-way notifications ("event occurred") | Best for waiting on complex predicates involving shared state |
| **Spurious Wakeups**           | Not possible                        | Possible; must re-check condition after wakeup |
| **Typical Use Cases**          | Producer-consumer signaling, one-time event notification, initialization gates | Complex condition waiting, e.g., bounded buffers, resource availability with multiple conditions |

---

## Summary and Recommendation

- Use **Semaphore** when you want a **simple, reliable one-way signal** (e.g., "data ready," "initialization complete") that **remembers signals** even if the waiter isn't yet waiting.

- Use **Lock + Condition Variable** when you need to wait on **complex conditions involving shared state** that cannot be represented by just a count or one-time signal.

---

### Why Semaphore is often better for basic signaling?

- The semaphore’s internal permit count **remembers signals**. If a signal (release) happens before the waiter calls `acquire()`, the permit is stored, so the waiter proceeds immediately without blocking.

- Condition variables **do not remember signals**. If `signal()` is called before `wait()`, the signal is lost, causing the waiter to block indefinitely unless signaled again.

---

### Visual Summary of Signal Persistence:

```mermaid
flowchart LR
    subgraph Semaphore[Semaphore Signaling]
        S1[Signal arrives first] --> S2[Permit stored (count 0 → 1)]
        S2 --> S3[Waiter arrives]
        S3 --> S4[Waiter proceeds immediately (count 1 → 0)]
    end

    subgraph ConditionVariable[Condition Variable]
        C1[Signal arrives first] --> C2[Signal lost! No waiters]
        C2 --> C3[Waiter arrives]
        C3 --> C4[Waiter blocks waiting for next signal]
    end

    style Semaphore fill:#69db7c,stroke:#000,stroke-width:2px,color:#000
    style ConditionVariable fill:#ffa94d,stroke:#000,stroke-width:2px,color:#000
```

---



*/
public class _01_Signalling {

    private  static class Pattern1{
        //say v hv 2 threads, one is initializer of data and other one printer of data, ofcourse we want to data to be initilized first and then acccesses by other for printing

        private Integer data=null;
        private final Semaphore sem=new Semaphore(0);//0 : think like all permits/gates are closed, no body could enter it unless someone calls release



        void initialize(){
            MyUtils.println(Thread.currentThread().getName()+" initing data");
            MyUtils.sleep(2000);
            this.data=34;
            MyUtils.println(Thread.currentThread().getName()+" finsished init, incrementing permit so someone can read it now");
            sem.release();//by default it wud be sem.release(1)
        }
        void printData() throws InterruptedException{
            MyUtils.println("waiting for someone to permit me inside to read data by calling relese");
            sem.acquire();
            MyUtils.println("somebody called relese, i got entry now");
            MyUtils.println("data="+data);
        }
        public static void main(String[] args) throws InterruptedException {
            Pattern1 obj=new Pattern1();
            Thread initiliaizer=new Thread(()->obj.initialize() , "initializer");
            Thread printer=new Thread(()->{
                try {
                    obj.printData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } , "printer");


            initiliaizer.start();
            printer.start();

            initiliaizer.join();
            printer.join();

            
        }
    }



    ///ping pong/alternating pattern, think of it like n layers, having 2 gates each, u can pass only via 1(the open one ie the one eith permit=1) at each layer, 2 doors means 2 semaphores needed
    private static class Pattern2{
        private final int n;
        private final Semaphore odd=new Semaphore(1);//coz since first number is odd, we want odd to be released first for printing
        private final Semaphore even=new Semaphore(0);

        public Pattern2(int n) {
            this.n=n;
        }
        //if number is odd, print ping
        void ping(){
            for(int i=1;i<=n;i++){
                if(i%2!=0){
                    try {
                        odd.acquire();
                        MyUtils.println(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally{
                        even.release();
                    }
                }
            }
        }
        void pong(){
            for(int i=1;i<=n;i++){
                if(i%2==0){
                    try {
                        even.acquire();
                        MyUtils.println(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally{
                        odd.release();
                    }
                }
            }
        }


        public static void main(String[] args) throws InterruptedException {
            Pattern2 obj=new Pattern2(6);
            Thread pinger=new Thread(()->obj.ping(), "pinger");
            Thread ponger=new Thread(()->obj.pong(), "ponger");


            pinger.start();
            ponger.start();

            pinger.join();
            ponger.join();

        }
    }





    public static void main(String[] args) throws InterruptedException {
        // Pattern1.main(args);
        /*
        t=25 : Thread=initializer : initializer initing data
        t=25 : Thread=printer : waiting for someone to permit me inside to read data by calling relese
        t=27 : Thread=initializer : initializer finsished init, incrementing permit so someone can read it now
        t=27 : Thread=printer : somebody called relese, i got entry now
        t=27 : Thread=printer : data=34
        */


        Pattern2.main(args);//no maater waht, 1...n are printeed in order
        /*
        t=51 : Thread=pinger : 1
        t=51 : Thread=ponger : 2
        t=51 : Thread=pinger : 3
        t=51 : Thread=ponger : 4
        t=51 : Thread=pinger : 5
        t=51 : Thread=ponger : 6
        */



    }
}
