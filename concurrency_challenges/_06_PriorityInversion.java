package concurrency_challenges;

import java.time.LocalTime;

/*
say there are 2 resources R1 and R2

t=0 LP acquires R1, say it needs 3 seconds to complete, HP also wants to acquire R1, but LP acquires so waits

t=1 MP acquires R2, but CPU gives time to MP now, say it needs 5 seconds to complete, LP pauses still 2 seconds time remaining

t=6 MP completes releases R2, CPU now shift to LP which has 2s work

t=8 LP completes its work, HP acquires R1 now

Observe that HP though needed to wait for 2 seconds to get R1, had to wait 5+2 seconds rather coz CPU shifted to MP holding R2 for 5s, then 2s remaining LP's work

simply wasting time


*/

/*

t=58 : LP acquired lock on resource1
t=59 : MP acquired lock on resource2
t=1 : LP released lock on resource1
t=1 : HP acquired lock on resource1
t=4 : HP released lock on resource1
t=4 : MP released lock on resource2

*/



/*

Solutuon:
Great question! Priority Inheritance and Priority Ceiling Protocol are two classic solutions to the **priority inversion problem** in real-time systems. They both help prevent a low-priority task from blocking a high-priority task indefinitely, but they work differently with distinct trade-offs.

Let me explain both in detail and highlight their differences clearly:

---

## Priority Inheritance Protocol (PIP)

### How it works:
- When a **high-priority thread (HP)** is blocked waiting for a mutex held by a **low-priority thread (LP)**, the LP **temporarily inherits** the HP’s priority.
- This **boosts the LP’s priority** to the level of the highest-priority thread waiting on the mutex.
- The LP runs with the boosted priority, preventing it from being preempted by medium-priority tasks.
- Once the LP releases the mutex, it **reverts back** to its original priority.
- Then the HP acquires the mutex and proceeds.

### Key points:
- **Dynamic priority boost** only when needed (when HP is blocked).
- LP executes at the elevated priority only as long as it holds the mutex needed by HP.
- Prevents unbounded blocking caused by medium-priority tasks.
- Allows normal priorities when no blocking occurs.

---

## Priority Ceiling Protocol (PCP)

### How it works:
- Each mutex is assigned a **priority ceiling**, which is the **highest priority** of any thread that may lock it.
- When a thread acquires a mutex, its priority is **immediately raised** to the mutex’s priority ceiling.
- This means the thread runs at **at least the ceiling priority** while holding the mutex.
- This prevents any medium-priority thread from preempting it.
- Once the mutex is released, the thread’s priority is restored to its original value.

### Key points:
- Priority boost happens **proactively when the mutex is acquired** (not just when blocking occurs).
- Prevents **deadlocks** by disallowing threads from acquiring mutexes if their priority is lower than the ceiling.
- Helps bound blocking times and improves schedulability analysis.
- Requires knowledge of all thread priorities that use the mutex in advance.

---

## Detailed Comparison

| Feature                       | Priority Inheritance Protocol (PIP)                    | Priority Ceiling Protocol (PCP)                      |
|------------------------------|--------------------------------------------------------|-----------------------------------------------------|
| **When priority boost happens** | Only when a higher-priority thread is blocked by LP holding mutex | Immediately on acquiring the mutex                   |
| **Priority boost duration**   | Temporary, only while LP holds the mutex blocking HP    | Temporary, for entire duration mutex is held         |
| **Deadlock prevention**       | Does not prevent deadlocks                              | Prevents deadlocks by priority ceiling enforcement   |
| **Complexity**                | Medium — dynamic boosts, but simpler to implement      | Higher — requires static priority ceilings per mutex |
| **Blocking analysis**         | More complex, depends on runtime behavior               | Easier, with bounded blocking times                   |
| **Overhead**                  | Lower — only boosts when blocking occurs                | Slightly higher — priority always boosted while holding mutex |
| **Use case example**          | Mars Pathfinder bug fix with priority inheritance enabled| Systems requiring strict real-time guarantees and deadlock freedom |
| **Requires knowledge of all priorities** | No, dynamic and opportunistic                      | Yes, must assign ceilings based on all possible users |

---

## Intuitive Analogy

- **Priority Inheritance** is like **temporarily loaning the VIP pass** to the low-priority person blocking the high-priority person, so they get fast tracked just until they finish.
- **Priority Ceiling** is like **giving the VIP pass immediately as soon as the person enters the VIP room**, ensuring no interruptions while inside.

---

## Summary

| Protocol             | Boost Timing          | Prevents Deadlocks | Complexity | Use Case                          |
|----------------------|----------------------|--------------------|------------|---------------------------------|
| Priority Inheritance  | When blocking occurs  | No                 | Medium     | Fixes priority inversion bugs dynamically |
| Priority Ceiling      | On mutex acquire      | Yes                | Higher     | Real-time systems needing strict guarantees |

---

If you want, I can provide code examples or diagrams to illustrate these protocols further. Would you like that?

*/
public class _06_PriorityInversion {

    static final Object resource1=new Object();
    static final Object resource2=new Object();


    public static void main(String[] args) throws InterruptedException {
        //fight for resource 1
        Thread LP=new Thread(()->{
            oprOnResource1();
        },"LP");

        Thread HP=new Thread(()->{
            sleep(1000);
            oprOnResource1();
        },"HP");

        //for resource 2
         Thread MP=new Thread(()->{
            sleep(1500);
            oprOnResource2();
        },"MP");

        LP.setPriority(1);
        MP.setPriority(5);
        HP.setPriority(10);

        LP.start();
        MP.start();
        HP.start();

        LP.join();
        MP.join();
        HP.join();
    }


    static void oprOnResource1(){
        synchronized(resource1){
            printTime(Thread.currentThread().getName()+" acquired lock on resource1");
            if(Thread.currentThread().getName().equals("LP")) Thread.currentThread().setPriority(7);//priotiy celing method  (inheritance methodwhen set to 10, coz it inherited from HP), when done this, CPU doenst shift to MP since 7>5
            sleep(3000);
            printTime(Thread.currentThread().getName()+" released lock on resource1");
        }
    }
    static void oprOnResource2(){
        synchronized(resource2){
            printTime(Thread.currentThread().getName()+" acquired lock on resource2");
            sleep(5000);
            printTime(Thread.currentThread().getName()+" released lock on resource2");
        }
    }


    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}

    static void printTime(String s){
        System.out.println("t="+LocalTime.now().getSecond()+" : "+s);
    }
    
}
