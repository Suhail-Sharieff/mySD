package design_patterns._05_prototype;

//brute force

//imagine a game where u have to spawn 2 same enemies, ie 2 objects

class Enemy{
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
}
public class _01_prototype {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Enemy e1=new Enemy("Attr1", "Attr2", 23, 23);
        Enemy e2=new Enemy("Attr1", "Attr2", 23, 23);
        //see how repetitve code this
        
    }
}
