package dataStructures;

import java.util.Arrays;



//caller will just tell how many string are there,and what is the positiveFalse rate(tho not present it tells yes) he can tolerate

public class _02_bloom_filters {
    public static void main(String[] args) {
        HashStrategy strategy = new HashFunc1();
        BloomFilterConfig config=new BloomFilterConfig(100, 0.01d);

        BloomFilter filter = new BloomFilter(config,strategy);

        System.out.println(filter.mightContain("apple"));
        filter.insert("apple");
        System.out.println(filter.mightContain("apple"));
    }
}

interface HashStrategy {
    int hash(int seed,int bitArrayLength, String s);
}

class HashFunc1 implements HashStrategy {
    @Override
    public int hash(int seed,int mod, String s) {
        int hash = s.hashCode() * seed;
        hash = hash % mod;
        if (hash < 0) hash += mod;
        return hash;
    }
}

class HashFunc2 implements HashStrategy {
    @Override
    public int hash(int seed,int mod, String s) {
        int hash = s.hashCode() * seed % (mod >> 1);
        if (hash < 0) hash += (mod >> 1);
        return hash;
    }
}

class HashFunc3 implements HashStrategy {
    @Override
    public int hash(int seed,int mod, String s) {
        int hash = s.hashCode() * seed % (mod >> 2);
        if (hash < 0) hash += (mod >> 2);
        return hash;
    }
}

class BloomFilterConfig {
    // caller provided fields
    private final int expectedNumberOfStrings;// n

    public int getExpectedNumberOfStrings() {
        return expectedNumberOfStrings;
    }

    public double getTolerableFalsePositiveRate() {
        return tolerableFalsePositiveRate;
    }

    public int getOptimalBitarrayLengthNeeded() {
        return optimalBitarrayLengthNeeded;
    }

    public int getOptimalNumberOfHashFunctionsNeeded() {
        return optimalNumberOfHashFunctionsNeeded;
    }

    private final double tolerableFalsePositiveRate;// p

    // dynmically genrated optimal values as per caller demands
    private final int optimalBitarrayLengthNeeded;// m
    private final int optimalNumberOfHashFunctionsNeeded;

    public BloomFilterConfig(int expectedNumberOfStrings, double tolerableFalsePositiveRate) {
        this.expectedNumberOfStrings = expectedNumberOfStrings;
        this.tolerableFalsePositiveRate = tolerableFalsePositiveRate;

        // m=-(n ln(p))/(ln(p)^2)
        this.optimalBitarrayLengthNeeded = (int) Math.ceil(
                -(expectedNumberOfStrings * Math.log(tolerableFalsePositiveRate)) / (Math.log(2) * Math.log(2)));

        // k = (m / n) * ln(2)
        this.optimalNumberOfHashFunctionsNeeded = Math.max(1, (int) Math.round(
                ((double) this.optimalBitarrayLengthNeeded / expectedNumberOfStrings) * Math.log(2)));
    }

}

class BitArray {
    private final int bitArrayLength;

    public int getBitArrayLength() {
        return bitArrayLength;
    }

    private boolean bit[];

    public BitArray(int bitArrayLength) {
        this.bitArrayLength = bitArrayLength;
        bit = new boolean[bitArrayLength];
    }

    public void set(int hash) {
        bit[hash] = true;
    }

    public boolean isSet(int hash) {
        return bit[hash];
    }

    public void reset() {
        Arrays.fill(bit, false);
    }
}

class BloomFilter {
    private final BitArray bitArray;
    private final BloomFilterConfig config;
    private final HashStrategy hashingStrategy;
    public BloomFilter(BloomFilterConfig config,HashStrategy hashingStrategy) {
        this.config = config;
        this.bitArray = new BitArray(config.getOptimalBitarrayLengthNeeded());
        this.hashingStrategy=hashingStrategy;   
    }

    public synchronized void insert(String s){
        for(int i=1;i<=config.getOptimalNumberOfHashFunctionsNeeded();i++){
            int hash=hashingStrategy.hash(i,config.getOptimalBitarrayLengthNeeded(), s);
            bitArray.set(hash);
        }
    }
    public synchronized boolean mightContain(String s){
        for(int i=1;i<=config.getOptimalNumberOfHashFunctionsNeeded();i++){
            int hash=hashingStrategy.hash(i,config.getOptimalBitarrayLengthNeeded(), s);
            if(!bitArray.isSet(hash)) return false;
        }
        return true;
    }
}