package synchronization_problems;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import utils.MyUtils;

/*
1> v hv a bathroom
2> any one gender group(M/F) can occupy the bathroom and never both at the same time
3> once all ppl of one gender leaves bathroom, any gender then can acquire the bathroom

*/

public class _08_UnisexBathroom {


    public static void main(String[] args) throws InterruptedException {
        UnFairBathRoom.main(args);
        UnFairBathRoom2.main(args);
        FairBathRoom.main(args);
        FairBathRoom2.main(args);
        FairBathRoom3.main(args);
    }


    //------------untill one's count doesnt become 0, other shud wait ie unfair to any one ie starvation OCCURS here
    private static class UnFairBathRoom {

        private final Semaphore mutex = new Semaphore(1);
        private final Semaphore maleCountLock = new Semaphore(1);
        private int maleCount = 0;

        private final Semaphore femaleCountLock = new Semaphore(1);
        private int femaleCount = 0;

        void maleEnter() throws InterruptedException {

            maleCountLock.acquire();
            if (++maleCount == 1)
                mutex.acquire();
            maleCountLock.release();

            MyUtils.println("MALE");

            maleCountLock.acquire();
            if (--maleCount == 0)
                mutex.release();
            maleCountLock.release();

        }

        void femaleEnter() throws InterruptedException {
            femaleCountLock.acquire();
            if (++femaleCount == 1)
                mutex.acquire();
            femaleCountLock.release();

            MyUtils.println("FEMALE ");

            femaleCountLock.acquire();
            if (--femaleCount == 0)
                mutex.release();
            femaleCountLock.release();

        }

        public static void main(String[] args) throws InterruptedException {
            UnFairBathRoom bathroom = new UnFairBathRoom();
            Thread male[] = new Thread[5];
            Thread female[] = new Thread[3];
            for (int i = 0; i < male.length; i++)
                male[i] = new Thread(() -> {
                    try {
                        bathroom.maleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "MALE[" + i + "]");
            for (int i = 0; i < female.length; i++)
                female[i] = new Thread(() -> {
                    try {
                        bathroom.femaleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "FEMALE[" + i + "]");

            for (int i = 0; i < Math.max(male.length, female.length); i++) {
                if (i < female.length)
                    female[i].start();
                if (i < male.length)
                    male[i].start();
            }

            for (Thread m : male)
                m.join();
            for (Thread f : female)
                f.join();

        }
    }

    //------unfair bathroom, usig ConditionalVariables: learn hwo actions are segreated, initilly i put all logic inside maleEnter and femleEner, it was very complex, so separet into function, each for male and female define 2 func acquire and reease
    /*t=32 : Thread=FEMALE[1] : FEMALE
    t=32 : Thread=FEMALE[2] : FEMALE
    t=32 : Thread=FEMALE[0] : FEMALE
    t=35 : Thread=MALE[0] : MALE
    t=35 : Thread=MALE[2] : MALE
    t=35 : Thread=MALE[1] : MALE
    t=35 : Thread=MALE[3] : MALE
    t=35 : Thread=MALE[4] : MALE */
    private static class  UnFairBathRoom2{

        public static void main(String[] args) throws InterruptedException{
            UnFairBathRoom2 bathroom = new UnFairBathRoom2();
            Thread male[] = new Thread[5];
            Thread female[] = new Thread[3];
            for (int i = 0; i < male.length; i++)
                male[i] = new Thread(() -> {
                    try {
                        bathroom.maleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "MALE[" + i + "]");
            for (int i = 0; i < female.length; i++)
                female[i] = new Thread(() -> {
                    try {
                        bathroom.femaleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "FEMALE[" + i + "]");

            for (int i = 0; i < Math.max(male.length, female.length); i++) {
                if (i < female.length)
                    female[i].start();
                if (i < male.length)
                    male[i].start();
            }

            for (Thread m : male)
                m.join();
            for (Thread f : female)
                f.join();

        }


        private int n_PPl_In_Bathroom=0;
        private final ReentrantLock lock=new ReentrantLock();

        private enum Gender{NONE,MALE,FEMALE}
        private Gender currGender=Gender.NONE;
        private final Condition canEnter;//MISTAKE: took 2 conditions: maleEntry and femaleEntry: thats wrong, just imagine it as layer with 1 door, and u shud decide if u can enter it or not so 1 cond variable, 2 cond variable wud mean that there are 2 doors thats wrong


        public UnFairBathRoom2() {
            canEnter=lock.newCondition();
        }


        private void maleAcquire() throws InterruptedException{
            lock.lock();
            try{
                while(currGender==Gender.FEMALE) canEnter.await();//in future u can also limit nPPl by adding nPPl too, for now i hv skipped it to keep it simple
                n_PPl_In_Bathroom++;
                currGender=Gender.MALE;
                //NO need of canEntrer.sigalAll() here coz other threads will still be waiting on lock.lock() and woulnt have touched while(...)
            }finally{
                lock.unlock();
            }
        }
        private void maleRelease(){
            lock.lock();
            try{
                if(--n_PPl_In_Bathroom==0){
                    currGender=Gender.NONE;
                    canEnter.signalAll();
                }
            }finally{
                lock.unlock();
            }
        }

        public void maleEnter() throws InterruptedException{
            maleAcquire();
            MyUtils.println("MALE");
            MyUtils.sleep(2000);
            maleRelease();
        }




        private void femaleAcquire() throws InterruptedException{
            lock.lock();
            try{
                while(currGender==Gender.MALE) canEnter.await();
                n_PPl_In_Bathroom++;
                currGender=Gender.FEMALE;
            }finally{
                lock.unlock();
            }
        }
        private void femaleRelease() throws InterruptedException{
            lock.lock();
            try{
                if(--n_PPl_In_Bathroom==0){
                    currGender=Gender.NONE;
                    canEnter.signalAll();
                }
            }finally{
                lock.unlock();
            }
        }
        public void femaleEnter() throws InterruptedException{
            femaleAcquire();
            MyUtils.println("FEMALE");
            MyUtils.sleep(3000);
            femaleRelease();
        }

    }



    //-----------------fair bathroom, one may not haveto wait indefiniteky
    /*
     /*t=31 : Thread=FEMALE[0] : FEMALE
    t=31 : Thread=MALE[1] : MALE 
    t=31 : Thread=FEMALE[1] : FEMALE
    t=31 : Thread=FEMALE[2] : FEMALE
    t=31 : Thread=MALE[0] : MALE
    t=31 : Thread=MALE[2] : MALE
    t=31 : Thread=MALE[4] : MALE
    t=31 : Thread=MALE[3] : MALE 
    */
    private static class FairBathRoom{

        private final Semaphore emptyRoom=new Semaphore(1);

        private int maleCount;
        private final Semaphore maleCountLock=new Semaphore(1);

        private int femaleCount;
        private final Semaphore femaleCountLock=new Semaphore(1);

        private final Semaphore queueService=new Semaphore(1,true);//to ensure fairensss

        void maleEnter() throws InterruptedException{

            queueService.acquire();

            maleCountLock.acquire();
            if(++maleCount==1) emptyRoom.acquire();
            maleCountLock.release();

            queueService.release();


            MyUtils.println("MALE ");


            maleCountLock.acquire();
            if(--maleCount==0) emptyRoom.release();
            maleCountLock.release();



        }
        void femaleEnter() throws InterruptedException{

            queueService.acquire();

            femaleCountLock.acquire();
            if(++femaleCount==1) emptyRoom.acquire();
            femaleCountLock.release();

            queueService.release();


            MyUtils.println("FEMALE");


            femaleCountLock.acquire();
            if(--femaleCount==0) emptyRoom.release();
            femaleCountLock.release();



        }


        public static void main(String[] args) throws InterruptedException {
            FairBathRoom bathroom = new FairBathRoom();
            Thread male[] = new Thread[5];
            Thread female[] = new Thread[3];
            for (int i = 0; i < male.length; i++)
                male[i] = new Thread(() -> {
                    try {
                        bathroom.maleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "MALE[" + i + "]");
            for (int i = 0; i < female.length; i++)
                female[i] = new Thread(() -> {
                    try {
                        bathroom.femaleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "FEMALE[" + i + "]");

            for (int i = 0; i < Math.max(male.length, female.length); i++) {
                if (i < female.length)
                    female[i].start();
                if (i < male.length)
                    male[i].start();
            }

            for (Thread m : male)
                m.join();
            for (Thread f : female)
                f.join();

        }

    }




    //-------------------same fair bathroom, but cleaner code version (separated functions), it helps to segrefate reponsitbiities very cleanly, in future supose we want to add anoher gender, then we just need to create private void newGenderAcuire()..release() and then public newGenderENtter()

    /*

    here i hv added timings too to visualize, male taks 2s and female taks 3s in bathroom

    t=3 : Thread=FEMALE[1] : FEMALE
    t=3 : Thread=FEMALE[0] : FEMALE
    t=6 : Thread=MALE[0] : MALE 
    t=8 : Thread=FEMALE[2] : FEMALE
    //see all 4 male entered same time
    t=11 : Thread=MALE[1] : MALE 
    t=11 : Thread=MALE[2] : MALE 
    t=11 : Thread=MALE[3] : MALE 
    t=11 : Thread=MALE[4] : MALE 
    
    */
    private static class FairBathRoom2{

        private final Semaphore emptyRoom=new Semaphore(1);//instead of calling it mutex, its beter to call emptyRoom

        private int maleCount;
        private final Semaphore maleCountLock=new Semaphore(1);

        private int femaleCount;
        private final Semaphore femaleCountLock=new Semaphore(1);

        private final Semaphore queueService=new Semaphore(1,true);//to ensure fairensss



        private void maleAcquire() throws InterruptedException{
            queueService.acquire();
            maleCountLock.acquire();
            if(++maleCount==1) emptyRoom.acquire();
            maleCountLock.release();
            queueService.release();
        }
        private void maleRelease() throws InterruptedException{
            maleCountLock.acquire();
            if(--maleCount==0) emptyRoom.release();
            maleCountLock.release();
        }

        private void femaleAcquire() throws InterruptedException{
            queueService.acquire();
            femaleCountLock.acquire();
            if(++femaleCount==1) emptyRoom.acquire();
            femaleCountLock.release();
            queueService.release();
        }
        private void femaleRelease() throws InterruptedException{
            femaleCountLock.acquire();
            if(--femaleCount==0) emptyRoom.release();
            femaleCountLock.release();
        }



        void maleEnter() throws InterruptedException{
            maleAcquire();
            MyUtils.println("MALE ");
            MyUtils.sleep(2000);
            maleRelease();
        }
        void femaleEnter() throws InterruptedException{
            femaleAcquire();
            MyUtils.println("FEMALE");
            MyUtils.sleep(3000);
            femaleRelease();
        }


        public static void main(String[] args) throws InterruptedException {
            FairBathRoom2 bathroom = new FairBathRoom2();
            Thread male[] = new Thread[5];
            Thread female[] = new Thread[3];
            for (int i = 0; i < male.length; i++)
                male[i] = new Thread(() -> {
                    try {
                        bathroom.maleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "MALE[" + i + "]");
            for (int i = 0; i < female.length; i++)
                female[i] = new Thread(() -> {
                    try {
                        bathroom.femaleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "FEMALE[" + i + "]");

            for (int i = 0; i < Math.max(male.length, female.length); i++) {
                if (i < female.length)
                    female[i].start();
                if (i < male.length)
                    male[i].start();
            }

            for (Thread m : male)
                m.join();
            for (Thread f : female)
                f.join();

        }

    }



    //----------------------fair bathroom with limit on how many in bathroom at a time---uses same approach used in ./_05_ReadHeavy.java
    /*

    see at ma 2 ppl are entering at a time

    t=32 : Thread=FEMALE[0] : FEMALE
    t=35 : Thread=MALE[0] : MALE 
    t=35 : Thread=MALE[2] : MALE 
    t=37 : Thread=FEMALE[1] : FEMALE
    t=37 : Thread=FEMALE[2] : FEMALE
    t=40 : Thread=MALE[3] : MALE 
    t=40 : Thread=MALE[1] : MALE 
    t=42 : Thread=MALE[4] : MALE 
    t=42 : Thread=MALE[5] : MALE 
    t=44 : Thread=MALE[6] : MALE 
    t=44 : Thread=MALE[7] : MALE 
    t=46 : Thread=MALE[9] : MALE 
    t=46 : Thread=MALE[8] : MALE
    */
    private static class FairBathRoom3{
        public FairBathRoom3(int maxCapacityOfBathroom) {
            this.ppl_Limitter=new Semaphore(maxCapacityOfBathroom);
        }

        private final Semaphore ppl_Limitter;


        private final Semaphore emptyRoom=new Semaphore(1);//instead of calling it mutex, its beter to call emptyRoom

        private int maleCount;
        private final Semaphore maleCountLock=new Semaphore(1);

        private int femaleCount;
        private final Semaphore femaleCountLock=new Semaphore(1);

        private final Semaphore queueService=new Semaphore(1,true);//to ensure fairensss



        private void maleAcquire() throws InterruptedException{
            queueService.acquire();
            maleCountLock.acquire();
            if(++maleCount==1) emptyRoom.acquire();
            maleCountLock.release();
            queueService.release();
        }
        private void maleRelease() throws InterruptedException{
            maleCountLock.acquire();
            if(--maleCount==0) emptyRoom.release();
            maleCountLock.release();
        }

        private void femaleAcquire() throws InterruptedException{
            queueService.acquire();
            femaleCountLock.acquire();
            if(++femaleCount==1) emptyRoom.acquire();
            femaleCountLock.release();
            queueService.release();
        }
        private void femaleRelease() throws InterruptedException{
            femaleCountLock.acquire();
            if(--femaleCount==0) emptyRoom.release();
            femaleCountLock.release();
        }



        void maleEnter() throws InterruptedException{
            ppl_Limitter.acquire();
            maleAcquire();
            MyUtils.println("MALE ");
            MyUtils.sleep(2000);
            maleRelease();
            ppl_Limitter.release();
        }
        void femaleEnter() throws InterruptedException{
            ppl_Limitter.acquire();
            femaleAcquire();
            MyUtils.println("FEMALE");
            MyUtils.sleep(3000);
            femaleRelease();
            ppl_Limitter.release();
        }


        public static void main(String[] args) throws InterruptedException {
            FairBathRoom3 bathroom = new FairBathRoom3(2);
            Thread male[] = new Thread[10];
            Thread female[] = new Thread[3];
            for (int i = 0; i < male.length; i++)
                male[i] = new Thread(() -> {
                    try {
                        bathroom.maleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "MALE[" + i + "]");
            for (int i = 0; i < female.length; i++)
                female[i] = new Thread(() -> {
                    try {
                        bathroom.femaleEnter();
                    } catch (InterruptedException ex) {
                    }
                }, "FEMALE[" + i + "]");

            for (int i = 0; i < Math.max(male.length, female.length); i++) {
                if (i < female.length)
                    female[i].start();
                if (i < male.length)
                    male[i].start();
            }

            for (Thread m : male)
                m.join();
            for (Thread f : female)
                f.join();

        }

    }


}
