package design_patterns._10_observer;

public class Data {
    String attr1;
    int attr2;
    boolean attr3;
    public Data(String attr1,int attr2,boolean attr3){
        this.attr1=attr1;
        this.attr2=attr2;
        this.attr3=attr3;
    }
    @Override
    public String toString() {
        return "Data [attr1=" + attr1 + ", attr2=" + attr2 + ", attr3=" + attr3 + "]";
    }
}




