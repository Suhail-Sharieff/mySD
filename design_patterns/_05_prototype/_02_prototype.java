package design_patterns._05_prototype;

//we can avoid that repetitve code by adding a clone elemnt, we just cretw 1 obj with attributes and remiang we will call just clone method

// the clone can be a shallow copy(same reference) and deep copy(diff reference) , so choose based on application

class Enemy implements Cloneable{
    String attr1;
    String attr2;
    int attr3;
    int attr4;
    public Enemy(String attr1,String attr2,int attr3,int attr4){
        this.attr1=attr1;
        this.attr2=attr2;
        this.attr3=attr3;
        this.attr4=attr4;
    }
    @Override
    protected Enemy clone() {
        //return this;//shallow copy
        return new Enemy(attr1, attr2, attr3, attr4);//deep copy    
    }
}
public class _02_prototype {
    public static void main(String[] args) {
        Enemy e1=new Enemy("Attr1", "Attr2", 23, 23);//just create one enemu
        Enemy e2=e1.clone();//see how simple it is not
        //suppose i wanna change some attr of e2
        e2.attr1="Changed attr1";//thats all
    }
}
