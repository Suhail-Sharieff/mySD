package concurrency;
import java.util.*;
import java.util.concurrent.locks.*;

public class _10_StripedHashMap<K,V> {
    private final int nStripes=16;
    private final Lock stripeLock[];
    private final List<HashMap<K,V>>stripes;
    public _10_StripedHashMap() {
        this.stripeLock=new ReentrantLock[nStripes];
        for(int i=0;i<nStripes;i++) stripeLock[i]=new ReentrantLock();
        this.stripes=new ArrayList<>();
        for(int i=0;i<nStripes;i++) stripes.add(new HashMap<K,V>());
    }
    private int stripeIdx(K key){
        return key.hashCode()%nStripes;
    }
    public void put(K key,V value){
        int stripeIdx=stripeIdx(key);
        stripeLock[stripeIdx].lock();
        try{
            stripes.get(stripeIdx).put(key, value);
        }finally{
            stripeLock[stripeIdx].unlock();
        }
    }
    public V get(K key){
        int stripeIdx=stripeIdx(key);
        stripeLock[stripeIdx].lock();
        try{
            return stripes.get(stripeIdx).get(key);
        }finally{
            stripeLock[stripeIdx].unlock();
        }
    }
    public void remove(K key){
        int stripeIdx=stripeIdx(key);
        stripeLock[stripeIdx].lock();
        try{
            stripes.get(stripeIdx).remove(key);
        }finally{
            stripeLock[stripeIdx].unlock();
        }
    }
    public int getSizeOfHashMap(){
        //lock all first
        for(Lock lock:stripeLock) lock.lock();
        int sz=0;
        try{
            for(var bucket:stripes) sz+=bucket.size();
            return sz;
        }finally{
            for(Lock lock:stripeLock) lock.unlock();
        }
    }


    public static void main(String[] args) {
        _10_StripedHashMap<Integer,Integer>hs=new _10_StripedHashMap<>();
        hs.put(23, 5);
        System.out.println(hs.get(23));
    }
}
