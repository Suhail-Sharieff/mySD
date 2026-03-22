import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {
        List<Integer>li=new Random().ints(10000000l).boxed().collect(Collectors.toList());
        long st=System.nanoTime();
        double pSum=li.parallelStream().map(e->Math.sqrt(e)).reduce(0d, (x,y)->x+y);
        long en=System.nanoTime();
        System.out.println((en-st)/(double)1e6);//takes 122.39 ms

        st=System.nanoTime();
        double seqSum=li.stream().map(e->Math.sqrt(e)).reduce(0d, (x,y)->x+y);
        en=System.nanoTime();
        System.out.println((en-st)/(double)1e6);//takes 238.31 ms

        //see parrallel ops are faster

    }
}