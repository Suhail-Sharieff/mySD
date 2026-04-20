package concurrency_challenges;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

public class _10_DiningPhilosophers {



    public static void main(String[] args) throws InterruptedException {
        //i hv added while(true).. coz in PROD its continues, to test here, comment while(true), else face infinite loop
        DeadLockSimulation.main(args);
        Soln1.main(args);
        Soln2.main(args);
        Soln4.main(args);
    }

    // ------------------------------------Deadlock code--NOT a solution, just
    // demonstration
    // naively, try to acuqire both forks at a time, if lucky, if any one
    // philosopher successfull uin picking both forks, eventually all will succed
    // but 2 at a time,
    // but no one can stop deadlock if all pick thier left fork (or) any 1 fork
    // exacly, coz then all will be waiting to acquire another fork
    private static class DeadLockSimulation {
        public static void main(String[] args) throws InterruptedException {

            int n = 3;

            DeadLockSimulation obj = new DeadLockSimulation(n);

            Thread philosopher[] = new Thread[n];
            for (int i = 0; i < n; i++) {
                final int j = i;
                philosopher[i] = new Thread(() -> {
                    try {
                        obj.pickUp(j);
                    } catch (InterruptedException ex) {
                    }
                }, "Philosopher[" + i + "]");
            }

            for (Thread t : philosopher)
                t.start();

            for (Thread t : philosopher)
                t.join();

        }

        private final int nPhilosophers;

        public DeadLockSimulation(int n) {
            this.nPhilosophers = n;
            forks = new Semaphore[nPhilosophers];
            for (int i = 0; i < nPhilosophers; i++) {
                forks[i] = new Semaphore(1);
            }
        }

        private final Semaphore forks[];

        void pickUp(int pNo) throws InterruptedException {

            int left=pNo;
            int right=(left+1)%nPhilosophers;

            while (true){
                MyUtils.println("THINKING");

                forks[left].acquire();
                MyUtils.println("PICKED " + left + "th fork");
                forks[right].acquire();
                MyUtils.println("PICKED " + right + "th fork");

                MyUtils.println("EATING");
                MyUtils.sleep(10000);
                MyUtils.println("FINISH");

                forks[left].release();
                MyUtils.println("RELEASED " + left + "th fork");
                forks[right].release();
                MyUtils.println("RELEASED " + right + "th fork");

            }

        }
    }





    //------------intuition for this: if any one person becomes successfull to acquire 2 forks, it will eventually lead to stability in all of them, so we will tweek it by locking minimum index fork first and then max index, for ex consider first fork locking folloing min-max pattern:
    /*

    first locking

    P0 -> min(0,1) ie 0
    P1 -> min(1,2) ie 1
    P2 -> min(2,3) ie 2
    P3 -> min(3,4) ie 3
    P4 -> min(4,0) ie 0 <------observe P4 fougt for 0th and NOT F4, so now F4 is free, 

    so now during second locking:

    P0 -> max(0,1) ie 1 FAIL(coz F1 acquired by P1)
    P1 -> max(1,2) ie 2 FAIL
    P2 -> max(2,3) ie 3 FAIL
    P3 -> max(3,4) ie 4 <---------SUCCESS, coz no body acquired F4 during first locking

    now P3 will proceed eating, release 2 of his forks and the system will evenutlly stabilize, soo the deadlock problem is solved
    
    
    */


    //BUT BUT BUT here also it might be possible for starvation+ unfairness, tho we hv solved deadlock+live starvation


    private static class Soln1 {
        public static void main(String[] args) throws InterruptedException {

            int n = 5;

            Soln1 obj = new Soln1(n);

            Thread philosopher[] = new Thread[n];
            for (int i = 0; i < n; i++) {
                final int j = i;
                philosopher[i] = new Thread(() -> {
                    try {
                        obj.pickUp(j);
                    } catch (InterruptedException ex) {
                    }
                }, "Philosopher[" + i + "]");
            }

            for (Thread t : philosopher)
                t.start();

            for (Thread t : philosopher)
                t.join();

        }

        private final int nPhilosophers;

        public Soln1(int n) {
            this.nPhilosophers = n;
            forks = new Semaphore[nPhilosophers];
            for (int i = 0; i < nPhilosophers; i++) {
                forks[i] = new Semaphore(1);
            }
        }

        private final Semaphore forks[];

        void pickUp(int pNo) throws InterruptedException {

            int left=pNo;
            int right=(pNo+1)%nPhilosophers;

            int i=Math.min(left, right);
            int j=Math.max(left, right);

            while (true){
                MyUtils.println("THINKING");

                forks[i].acquire();
                MyUtils.println("PICKED " + i + "th fork");
                forks[j].acquire();
                MyUtils.println("PICKED " + j + "th fork");

                MyUtils.println("EATING");
                MyUtils.sleep(10000);
                MyUtils.println("FINISH");

                forks[i].release();
                MyUtils.println("RELEASED " + i + "th fork");
                forks[j].release();
                MyUtils.println("RELEASED " + j + "th fork");

            }

        }
    }


    //we dont control how each philospher picks like above
    // make a centralized controller(here limitter) which allows first (n-1) philo to simultaneusly pick fork, resulting in 1 NON locked fork always, so it will be picked by someone in need, and eventually all will complete eating, but still this SOln is NOT fair+starvation free
    private static class Soln2{
         public static void main(String[] args) throws InterruptedException {

            int n = 5;

            Soln2 obj = new Soln2(n);

            Thread philosopher[] = new Thread[n];
            for (int i = 0; i < n; i++) {
                final int j = i;
                philosopher[i] = new Thread(() -> {
                    try {
                        obj.pickUp(j);
                    } catch (InterruptedException ex) {
                    }
                }, "Philosopher[" + i + "]");
            }

            for (Thread t : philosopher)
                t.start();

            for (Thread t : philosopher)
                t.join();

        }

        private final int nPhilosophers;

        public Soln2(int n) {
            this.nPhilosophers = n;
            forks = new Semaphore[nPhilosophers];
            for (int i = 0; i < nPhilosophers; i++) {
                forks[i] = new Semaphore(1);
            }
            this.limitter=new Semaphore(n-1);//for ex: if 5 philosophers, allows 4 to pick fork simultaneously
        }

        private final Semaphore forks[];
        private final Semaphore limitter;

        void pickUp(int pNo) throws InterruptedException {

            int left=pNo;
            int right=(pNo+1)%nPhilosophers;


            while (true){
                MyUtils.println("THINKING");

                limitter.acquire();//---------------------(n-1) philo wil enter at max, the last 1 phiolo who entered late will wait, so 1 fork will always be free at any cost, so it can be picked by philosher who needs it, eventully all will eat

                MyUtils.println("DECIDED TO EAT");
                forks[left].acquire();
                MyUtils.println("PICKED " + left + "th fork");
                forks[right].acquire();
                MyUtils.println("PICKED " + right + "th fork");

                MyUtils.println("EATING");
                MyUtils.sleep(10000);
                MyUtils.println("FINISH");

                forks[left].release();
                MyUtils.println("RELEASED " + left + "th fork");
                forks[right].release();
                MyUtils.println("RELEASED " + right + "th fork");

                limitter.release();//------------------------

            }

        }
    }
    /*
    t=0 : Thread=Philosopher[0] : THINKING
t=0 : Thread=Philosopher[0] : DECIDED TO EAT
t=0 : Thread=Philosopher[2] : THINKING
t=0 : Thread=Philosopher[2] : DECIDED TO EAT
t=0 : Thread=Philosopher[4] : THINKING
t=0 : Thread=Philosopher[3] : THINKING
t=0 : Thread=Philosopher[0] : PICKED 0th fork
t=0 : Thread=Philosopher[2] : PICKED 2th fork
t=0 : Thread=Philosopher[0] : PICKED 1th fork
t=0 : Thread=Philosopher[1] : THINKING
t=0 : Thread=Philosopher[4] : DECIDED TO EAT
t=0 : Thread=Philosopher[3] : DECIDED TO EAT
t=0 : Thread=Philosopher[2] : PICKED 3th fork
t=0 : Thread=Philosopher[0] : EATING
t=0 : Thread=Philosopher[4] : PICKED 4th fork
t=0 : Thread=Philosopher[2] : EATING
t=10 : Thread=Philosopher[0] : FINISH
t=10 : Thread=Philosopher[2] : FINISH
t=10 : Thread=Philosopher[4] : PICKED 0th fork
t=10 : Thread=Philosopher[4] : EATING
t=10 : Thread=Philosopher[0] : RELEASED 0th fork
t=10 : Thread=Philosopher[2] : RELEASED 2th fork
t=10 : Thread=Philosopher[0] : RELEASED 1th fork
t=10 : Thread=Philosopher[3] : PICKED 3th fork
t=10 : Thread=Philosopher[2] : RELEASED 3th fork
t=10 : Thread=Philosopher[1] : DECIDED TO EAT
t=10 : Thread=Philosopher[1] : PICKED 1th fork
t=10 : Thread=Philosopher[1] : PICKED 2th fork
t=10 : Thread=Philosopher[1] : EATING
t=20 : Thread=Philosopher[4] : FINISH
t=20 : Thread=Philosopher[1] : FINISH
t=20 : Thread=Philosopher[1] : RELEASED 1th fork
t=20 : Thread=Philosopher[1] : RELEASED 2th fork
t=20 : Thread=Philosopher[3] : PICKED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 0th fork
t=20 : Thread=Philosopher[3] : EATING
t=30 : Thread=Philosopher[3] : FINISH
t=10 : Thread=Philosopher[4] : PICKED 0th fork
t=10 : Thread=Philosopher[4] : EATING
t=10 : Thread=Philosopher[0] : RELEASED 0th fork
t=10 : Thread=Philosopher[2] : RELEASED 2th fork
t=10 : Thread=Philosopher[0] : RELEASED 1th fork
t=10 : Thread=Philosopher[3] : PICKED 3th fork
t=10 : Thread=Philosopher[2] : RELEASED 3th fork
t=10 : Thread=Philosopher[1] : DECIDED TO EAT
t=10 : Thread=Philosopher[1] : PICKED 1th fork
t=10 : Thread=Philosopher[1] : PICKED 2th fork
t=10 : Thread=Philosopher[1] : EATING
t=20 : Thread=Philosopher[4] : FINISH
t=20 : Thread=Philosopher[1] : FINISH
t=20 : Thread=Philosopher[1] : RELEASED 1th fork
t=20 : Thread=Philosopher[1] : RELEASED 2th fork
t=20 : Thread=Philosopher[3] : PICKED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 0th fork
t=20 : Thread=Philosopher[3] : EATING
t=30 : Thread=Philosopher[3] : FINISH
t=10 : Thread=Philosopher[1] : DECIDED TO EAT
t=10 : Thread=Philosopher[1] : PICKED 1th fork
t=10 : Thread=Philosopher[1] : PICKED 2th fork
t=10 : Thread=Philosopher[1] : EATING
t=20 : Thread=Philosopher[4] : FINISH
t=20 : Thread=Philosopher[1] : FINISH
t=20 : Thread=Philosopher[1] : RELEASED 1th fork
t=20 : Thread=Philosopher[1] : RELEASED 2th fork
t=20 : Thread=Philosopher[3] : PICKED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 0th fork
t=20 : Thread=Philosopher[3] : EATING
t=30 : Thread=Philosopher[3] : FINISH
t=20 : Thread=Philosopher[1] : RELEASED 2th fork
t=20 : Thread=Philosopher[3] : PICKED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 4th fork
t=20 : Thread=Philosopher[4] : RELEASED 0th fork
t=20 : Thread=Philosopher[3] : EATING
t=30 : Thread=Philosopher[3] : FINISH
t=20 : Thread=Philosopher[3] : EATING
t=30 : Thread=Philosopher[3] : FINISH
t=30 : Thread=Philosopher[3] : FINISH
t=30 : Thread=Philosopher[3] : RELEASED 3th fork
t=30 : Thread=Philosopher[3] : RELEASED 4th fork
PS C:\Users\suhai\Desktop\mySD> git add.     
git: 'add.' is not a git command. See 'git --help'.

     */



    //in interviews, Soln2/Soln3 is enough, but follow up may be asked to make it fair+starvation free as well, then the blwo soln works its called Chandy/Misra soln

    private static class Soln4{//Chandy Misra soln

       

        /*Algorithm:
        
        1> a fork is "isDirty" if it was used by some philosopher to eat
        2> assume initially all forks are dirty(means each philosopher had eaten with them) and ith fork is held by ith philosopher
        3> whenevr a philosopher wants to eat, he requests both forks(left and right) from his neighbors
        4> if a neighbor holds a dirty fork, he will clean and immediatly give it to him
        5> else he will first eat using that fork making it dirty, then clean and give him

        void pickup(int pid){

            int left=pid;
            int right=(pid+1)%n;

            //wait to get 2 forks
            requestFork(left);
            requestFork(right);
            //now he has 2 clean forks

            //eat

            //now both are dirty, just release them if someone requests them
            releaseIfRequested(left);
            releaseIfRequested(right);
        
        }
        

        */


       enum State { THINKING, HUNGRY, EATING }

    private final int n;
    private final State[] state;
    
    // Instead of checking if a fork is "free", we strictly track WHO owns it.
    private final int[] forkOwner;
    private final boolean[] isDirty;

    // A SINGLE lock protects the entire table's state variables
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition[] conditions;

    public Soln4(int n) {
        this.n = n;
        this.state = new State[n];
        this.forkOwner = new int[n];
        this.isDirty = new boolean[n];
        this.conditions = new Condition[n];

        for (int i = 0; i < n; i++) {
            state[i] = State.THINKING;
            conditions[i] = lock.newCondition();

            // CHANDY-MISRA INITIALIZATION:
            // To prevent an initial deadlock, the graph of fork ownership MUST be acyclic (no circles).
            // We give the fork to the philosopher with the lower ID.
            // Philosopher 0 will start holding both Fork 0 and Fork n-1. 
            int leftPhilosopher = i;
            int rightPhilosopher = (i + 1) % n;
            forkOwner[i] = Math.min(leftPhilosopher, rightPhilosopher);
            
            // All forks start dirty so they can be immediately requested and handed over
            isDirty[i] = true; 
        }
    }

    public void pickup(int pid) throws InterruptedException {
        // ALWAYS wrap state changes in the lock
        lock.lock();
        try {
            state[pid] = State.HUNGRY;
            int leftFork = pid;
            int rightFork = (pid + 1) % n;

            System.out.println("Philosopher [" + pid + "] is HUNGRY.");

            // Wait until we own both forks
            while (forkOwner[leftFork] != pid || forkOwner[rightFork] != pid) {
                
                // Ask neighbors for the forks if we don't have them
                requestFork(pid, leftFork);
                requestFork(pid, rightFork);

                // If a neighbor is eating or the fork is clean, they won't give it to us yet.
                // We must sleep and wait for them to finish and signal us.
                if (forkOwner[leftFork] != pid || forkOwner[rightFork] != pid) {
                    conditions[pid].await(); 
                }
            }

            // We broke out of the loop, meaning we own both forks!
            state[pid] = State.EATING;
            System.out.println("Philosopher [" + pid + "] GOT BOTH FORKS and is EATING...");

        } finally {
            lock.unlock();
        }
    }

    public void putdown(int pid) {
        lock.lock();
        try {
            state[pid] = State.THINKING;
            int leftFork = pid;
            int rightFork = (pid + 1) % n;

            // 1. We finished eating, so our forks become dirty
            isDirty[leftFork] = true;
            isDirty[rightFork] = true;
            System.out.println("Philosopher [" + pid + "] finished eating. Forks " + leftFork + " and " + rightFork + " are now DIRTY.");

            // 2. Check if our neighbors were waiting for these forks. 
            // If they are hungry, we hand the newly dirtied forks over immediately.
            int leftNeighbor = (pid - 1 + n) % n;
            int rightNeighbor = (pid + 1) % n;

            if (state[leftNeighbor] == State.HUNGRY) {
                requestFork(leftNeighbor, leftFork);
            }
            if (state[rightNeighbor] == State.HUNGRY) {
                requestFork(rightNeighbor, rightFork);
            }

        } finally {
            lock.unlock();
        }
    }

    // Helper method to process a fork request
    private void requestFork(int requesterPid, int forkIdx) {
        int owner = forkOwner[forkIdx];
        
        // If the requester already owns it, do nothing
        if (owner == requesterPid) return; 

        // THE GOLDEN RULE OF CHANDY-MISRA:
        // If the owner is NOT currently eating, and the fork is DIRTY, they MUST give it up.
        // (Even if the owner is also hungry! This prevents deadlocks).
        if (state[owner] != State.EATING && isDirty[forkIdx]) {
            
            forkOwner[forkIdx] = requesterPid; // Transfer ownership
            isDirty[forkIdx] = false;          // Fork is cleaned in transit!
            
            System.out.println("   -> Philosopher [" + owner + "] cleaned and passed Fork " + forkIdx + " to Philosopher [" + requesterPid + "]");
            
            // Wake up the requester so they can check their while-loop again
            conditions[requesterPid].signal(); 
        }
    }

    // ==========================================
    // TEST SIMULATION
    // ==========================================
    public static void main(String[] args) {
        int n = 3;
        Soln4 table = new Soln4(n);

        Thread[] philosophers = new Thread[n];
        for (int i = 0; i < n; i++) {
            final int pid = i;
            philosophers[i] = new Thread(() -> {
                try {
                    // Each philosopher tries to eat 3 times
                    // for (int j = 0; j < 3; j++) {
                        table.pickup(pid);
                        Thread.sleep((long) (Math.random() * 1000)); // Simulate eating time
                        table.putdown(pid);
                        Thread.sleep((long) (Math.random() * 1000)); // Simulate thinking time
                    // }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (Thread t : philosophers) t.start();
    }



    }

}
