package design_patterns._09_flyweight;


/*
    FLYWEIGHT PATTERN
    ------------------
    Intrinsic state  -> stored inside flyweight (shared)
    Extrinsic state  -> passed during method call (not stored)
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class MutableData{
    int x;
    int y;
    public MutableData(int x,int y){
        setX(x);
        setY(y);
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    @Override
    public String toString() {
        return "x="+getX()+" y="+getY();
    }
}
class SharedData{
    String col;
    String url;
    public SharedData(String col,String url){
        this.col=col;
        this.url=url;
    }
    public String getCol() {
        return col;
    }
    public String getUrl() {
        return url;
    }
    @Override
    public String toString() {
        return "col="+getCol()+" url="+getUrl()+" hash="+hashCode();
    }
}
class Ball{
    MutableData mutable;
    SharedData shared;
    public Ball(MutableData mutable,SharedData shared){
        this.mutable=mutable;
        this.shared=shared;
    }
    @Override
    public String toString() {
        return mutable+" "+shared;
    }
}
class SharedDataFactory{
    private static HashMap<String,SharedData>cache=new HashMap<>();
    public static SharedData getBall(String col,String url){
        String key=col+":"+url;
        cache.putIfAbsent(key, new SharedData(col, url));
        return cache.get(key);
    }
}
/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        List<Ball> balls=new ArrayList<>();
        String colors[]={"C1","C2","C3"};
        String urls[]={"U1","U2","U3"};

        Random rand=new Random();
        for(int i=0;i<10;i++){
            //variable attributes
            int x=rand.nextInt(0,10);
            int y=rand.nextInt(0,10);
            MutableData mutable=new MutableData(x, y);

            //shared data
            String color=colors[rand.nextInt(0,3)];
            String url=urls[rand.nextInt(0,3)];
            SharedData shared=SharedDataFactory.getBall(color, url);//see how we use same memory for shared data

            balls.add(new Ball(mutable,shared));
            
        }
        // System.out.println(balls);
        balls.forEach(System.out::println);
    }
}
/*
x=0 y=3 col=C3 url=U2 hash=1096979270
x=9 y=8 col=C1 url=U2 hash=764977973
x=4 y=8 col=C2 url=U2 hash=381259350
x=5 y=5 col=C2 url=U3 hash=2129789493
x=7 y=3 col=C1 url=U2 hash=764977973
x=6 y=0 col=C1 url=U3 hash=668386784
x=1 y=1 col=C2 url=U2 hash=381259350
x=5 y=1 col=C2 url=U3 hash=2129789493
x=1 y=6 col=C2 url=U3 hash=2129789493
x=9 y=7 col=C2 url=U3 hash=2129789493
*/
