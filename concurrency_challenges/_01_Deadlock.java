package concurrency_challenges;

import java.util.concurrent.locks.ReentrantLock;

public class _01_Deadlock {
    private static class Bower{
        public final String name;
        public Bower(String name){this.name=name;}
        public void bow(Bower other){synchronized(other){System.out.println(this.name+" bowed to "+other.name);other.bowBack(this);}}
        public void bowBack(Bower other){synchronized(other){System.out.println(other.name+" bowed back to "+this.name);}}
    }

    private static class SolvedBower{
        public final String name;
        public  SolvedBower(String name){this.name=name;}
        public final ReentrantLock lock=new ReentrantLock();
        public void bow(SolvedBower other){
            while(true){
                if(this.lock.tryLock()){
                    System.out.println(this.name+" got his lock");
                    try{
                        if(other.lock.tryLock()){
                            System.out.println(other.name+" got his lock");
                            try{
                                System.out.println(this.name+" bowd to "+other.name);
                                other.bowBack(this);
                                return;
                            }finally{
                                other.lock.unlock();
                            }
                        }
                    }finally{
                        this.lock.unlock();
                    }
                }
                sleep(1000);
            }
        }
        public void bowBack(SolvedBower other){
            System.out.println(other.name+" bowed back to "+this.name);
        }
    }

    static void sleep(long dur){try{Thread.sleep(dur);}catch(InterruptedException ex){}}

    public static void main(String[] args) throws InterruptedException{

        Bower a=new Bower("A");
        Bower b=new Bower("B");
        //uncomment above and do like below to see deadlock: find pid using jps, then jstack <pid>:
        /*
        PS C:\Users\suhai\Desktop\mySD> jps
4724 Jps
14588 Main
27596 org.eclipse.equinox.launcher_1.7.100.v20251111-0406.jar
PS C:\Users\suhai\Desktop\mySD> jstack 14588
2026-04-11 10:01:10
Full thread dump Java HotSpot(TM) 64-Bit Server VM (21.0.9+7-LTS-338 mixed mode, sharing):

Threads class SMR info:
_java_thread_list=0x00000222738d9bd0, length=13, elements={
0x000002224ef79850, 0x000002226ebaffc0, 0x000002226ebb0c20, 0x000002226ebb3890,
0x000002226ebb4620, 0x000002226ebb95e0, 0x000002226ebba040, 0x000002226ebc4ed0,
0x000002226ebcd110, 0x00000222731cc120, 0x00000222732e80a0, 0x0000022273a27850,
0x0000022273a27ec0
}

"main" #1 [19284] prio=5 os_prio=0 cpu=703.12ms elapsed=19.31s tid=0x000002224ef79850 nid=19284 in Object.wait()  [0x000000c7d73fe000]
   java.lang.Thread.State: WAITING (on object monitor)   
        at java.lang.Object.wait0(java.base@21.0.9/Native Method)
        - waiting on <0x0000000714015ce0> (a java.lang.Thread)
        at java.lang.Object.wait(java.base@21.0.9/Object.java:366)
        at java.lang.Thread.join(java.base@21.0.9/Thread.java:2078)
        - locked <0x0000000714015ce0> (a java.lang.Thread)
        at java.lang.Thread.join(java.base@21.0.9/Thread.java:2154)
        at concurrency_challenges._01_Deadlock.main(_01_Deadlock.java:57)
        at java.lang.invoke.LambdaForm$DMH/0x0000022201030c00.invokeStatic(java.base@21.0.9/LambdaForm$DMH)       
        at java.lang.invoke.LambdaForm$MH/0x0000022201150000.invoke(java.base@21.0.9/LambdaForm$MH)
        at java.lang.invoke.Invokers$Holder.invokeExact_MT(java.base@21.0.9/Invokers$Holder)
        at jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(java.base@21.0.9/DirectMethodHandleAccessor.java:154)
        at jdk.internal.reflect.DirectMethodHandleAccessor.invoke(java.base@21.0.9/DirectMethodHandleAccessor.java:103)
        at java.lang.reflect.Method.invoke(java.base@21.0.9/Method.java:580)
        at com.sun.tools.javac.launcher.Main.execute(jdk.compiler@21.0.9/Main.java:484)
        at com.sun.tools.javac.launcher.Main.run(jdk.compiler@21.0.9/Main.java:208)
        at com.sun.tools.javac.launcher.Main.main(jdk.compiler@21.0.9/Main.java:135)

"Reference Handler" #9 [27096] daemon prio=10 os_prio=2 cpu=0.00ms elapsed=19.27s tid=0x000002226ebaffc0 nid=27096 waiting on condition  [0x000000c7d7bff000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.ref.Reference.waitForReferencePendingList(java.base@21.0.9/Native Method)
        at java.lang.ref.Reference.processPendingReferences(java.base@21.0.9/Reference.java:246)
        at java.lang.ref.Reference$ReferenceHandler.run(java.base@21.0.9/Reference.java:208)

"Finalizer" #10 [13724] daemon prio=8 os_prio=1 cpu=0.00ms elapsed=19.27s tid=0x000002226ebb0c20 nid=13724 in Object.wait()  [0x000000c7d7cff000]
   java.lang.Thread.State: WAITING (on object monitor)   
        at java.lang.Object.wait0(java.base@21.0.9/Native Method)
        - waiting on <0x0000000713222b18> (a java.lang.ref.NativeReferenceQueue$Lock)
        at java.lang.Object.wait(java.base@21.0.9/Object.java:366)
        at java.lang.Object.wait(java.base@21.0.9/Object.java:339)
        at java.lang.ref.NativeReferenceQueue.await(java.base@21.0.9/NativeReferenceQueue.java:48)
        at java.lang.ref.ReferenceQueue.remove0(java.base@21.0.9/ReferenceQueue.java:158)
        at java.lang.ref.NativeReferenceQueue.remove(java.base@21.0.9/NativeReferenceQueue.java:89)
        - locked <0x0000000713222b18> (a java.lang.ref.NativeReferenceQueue$Lock)
        at java.lang.ref.Finalizer$FinalizerThread.run(java.base@21.0.9/Finalizer.java:173)

"Signal Dispatcher" #11 [24264] daemon prio=9 os_prio=2 cpu=0.00ms elapsed=19.27s tid=0x000002226ebb3890 nid=24264 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Attach Listener" #12 [5448] daemon prio=5 os_prio=2 cpu=0.00ms elapsed=19.27s tid=0x000002226ebb4620 nid=5448 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Service Thread" #13 [22176] daemon prio=9 os_prio=0 cpu=0.00ms elapsed=19.27s tid=0x000002226ebb95e0 nid=22176 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Monitor Deflation Thread" #14 [16628] daemon prio=9 os_prio=0 cpu=0.00ms elapsed=19.27s tid=0x000002226ebba040 nid=16628 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #15 [25508] daemon prio=9 os_prio=2 cpu=312.50ms elapsed=19.27s tid=0x000002226ebc4ed0 nid=25508 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"C1 CompilerThread0" #23 [23140] daemon prio=9 os_prio=2 cpu=265.62ms elapsed=19.27s tid=0x000002226ebcd110 nid=23140 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"Notification Thread" #27 [6828] daemon prio=9 os_prio=0 cpu=0.00ms elapsed=19.23s tid=0x00000222731cc120 nid=6828 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Common-Cleaner" #28 [13384] daemon prio=8 os_prio=1 cpu=0.00ms elapsed=19.21s tid=0x00000222732e80a0 nid=13384 waiting on condition  [0x000000c7d89fe000]
   java.lang.Thread.State: TIMED_WAITING (parking)       
        at jdk.internal.misc.Unsafe.park(java.base@21.0.9/Native Method)
        - parking to wait for  <0x0000000713234338> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.parkNanos(java.base@21.0.9/LockSupport.java:269)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(java.base@21.0.9/AbstractQueuedSynchronizer.java:1852)
        at java.lang.ref.ReferenceQueue.await(java.base@21.0.9/ReferenceQueue.java:71)
        at java.lang.ref.ReferenceQueue.remove0(java.base@21.0.9/ReferenceQueue.java:143)
        at java.lang.ref.ReferenceQueue.remove(java.base@21.0.9/ReferenceQueue.java:218)
        at jdk.internal.ref.CleanerImpl.run(java.base@21.0.9/CleanerImpl.java:140)
        at java.lang.Thread.runWith(java.base@21.0.9/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.9/Thread.java:1583)
        at jdk.internal.misc.InnocuousThread.run(java.base@21.0.9/InnocuousThread.java:186)

"Thread-0" #35 [7768] prio=5 os_prio=0 cpu=15.62ms elapsed=18.08s tid=0x0000022273a27850 nid=7768 waiting for monitor entry  [0x000000c7d95fe000]
   java.lang.Thread.State: BLOCKED (on object monitor)   
        at concurrency_challenges._01_Deadlock$Bower.bowBack(_01_Deadlock.java:10)
        - waiting to lock <0x0000000714015c48> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock$Bower.bow(_01_Deadlock.java:9)
        - locked <0x0000000714015c58> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock.lambda$main$0(_01_Deadlock.java:51)
        at concurrency_challenges._01_Deadlock$$Lambda/0x000002220114f408.run(Unknown Source)
        at java.lang.Thread.runWith(java.base@21.0.9/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.9/Thread.java:1583)

"Thread-1" #36 [9320] prio=5 os_prio=0 cpu=15.62ms elapsed=18.08s tid=0x0000022273a27ec0 nid=9320 waiting for monitor entry  [0x000000c7d96ff000]
   java.lang.Thread.State: BLOCKED (on object monitor)   
        at concurrency_challenges._01_Deadlock$Bower.bowBack(_01_Deadlock.java:10)
        - waiting to lock <0x0000000714015c58> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock$Bower.bow(_01_Deadlock.java:9)
        - locked <0x0000000714015c48> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock.lambda$main$1(_01_Deadlock.java:52)
        at concurrency_challenges._01_Deadlock$$Lambda/0x0000022201150800.run(Unknown Source)
        at java.lang.Thread.runWith(java.base@21.0.9/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.9/Thread.java:1583)

"GC Thread#6" os_prio=2 cpu=0.00ms elapsed=18.36s tid=0x0000022273b1a380 nid=14428 runnable

"GC Thread#5" os_prio=2 cpu=0.00ms elapsed=18.37s tid=0x0000022273d33640 nid=19412 runnable

"GC Thread#4" os_prio=2 cpu=0.00ms elapsed=18.37s tid=0x0000022273df1420 nid=21688 runnable

"GC Thread#3" os_prio=2 cpu=15.62ms elapsed=18.37s tid=0x0000022273df1070 nid=13264 runnable

"GC Thread#2" os_prio=2 cpu=0.00ms elapsed=18.37s tid=0x0000022273df0cc0 nid=13756 runnable

"GC Thread#1" os_prio=2 cpu=0.00ms elapsed=18.37s tid=0x0000022273d32e90 nid=25804 runnable

"VM Thread" os_prio=2 cpu=15.62ms elapsed=19.29s tid=0x000002226eb8bb00 nid=6752 runnable

"VM Periodic Task Thread" os_prio=2 cpu=15.62ms elapsed=19.29s tid=0x000002226eb76cf0 nid=21036 waiting on condition

"G1 Service" os_prio=2 cpu=0.00ms elapsed=19.30s tid=0x000002226ea471a0 nid=27328 runnable

"G1 Refine#0" os_prio=2 cpu=0.00ms elapsed=19.30s tid=0x000002226ea46620 nid=11992 runnable

"G1 Main Marker" os_prio=2 cpu=0.00ms elapsed=19.31s tid=0x000002224efecf80 nid=7712 runnable

"G1 Conc#0" os_prio=2 cpu=0.00ms elapsed=19.31s tid=0x000002224efeda90 nid=14928 runnable

"GC Thread#0" os_prio=2 cpu=15.62ms elapsed=19.31s tid=0x000002224efdc480 nid=9340 runnable

JNI global refs: 16, weak refs: 0


Found one Java-level deadlock:
=============================
"Thread-0":
  waiting to lock monitor 0x0000022273a4af30 (object 0x0000000714015c48, a concurrency_challenges._01_Deadlock$Bower),
  which is held by "Thread-1"

"Thread-1":
  waiting to lock monitor 0x0000022273a4a910 (object 0x0000000714015c58, a concurrency_challenges._01_Deadlock$Bower),
  which is held by "Thread-0"

Java stack information for the threads listed above:     
===================================================      
"Thread-0":
        at concurrency_challenges._01_Deadlock$Bower.bowBack(_01_Deadlock.java:10)
        - waiting to lock <0x0000000714015c48> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock$Bower.bow(_01_Deadlock.java:9)
        - locked <0x0000000714015c58> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock.lambda$main$0(_01_Deadlock.java:51)
        at concurrency_challenges._01_Deadlock$$Lambda/0x000002220114f408.run(Unknown Source)
        at java.lang.Thread.runWith(java.base@21.0.9/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.9/Thread.java:1583)
"Thread-1":
        at concurrency_challenges._01_Deadlock$Bower.bowBack(_01_Deadlock.java:10)
        - waiting to lock <0x0000000714015c58> (a concurrency_challenges._01_Deadlock$Bower)
_01_Deadlock.java:9)
        - locked <0x0000000714015c48> (a concurrency_challenges._01_Deadlock$Bower)
        at concurrency_challenges._01_Deadlock.lambda$main$1(_01_Deadlock.java:52)
        at concurrency_challenges._01_Deadlock$$Lambda/0x0000022201150800.run(Unknown Source)
        at java.lang.Thread.runWith(java.base@21.0.9/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.9/Thread.java:1583)

Found 1 deadlock.

PS C:\Users\suhai\Desktop\mySD>
        
        */


        // SolvedBower a=new SolvedBower("A");
        // SolvedBower b=new SolvedBower("B");
        Thread t1=new Thread(()->a.bow(b));
        Thread t2=new Thread(()->b.bow(a));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
