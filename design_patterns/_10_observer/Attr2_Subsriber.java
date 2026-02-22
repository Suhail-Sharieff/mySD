package design_patterns._10_observer;

public class Attr2_Subsriber implements Subsriber{

    @Override
    public void updateData(Data newData) {
        System.out.println("ATTR2_LISTENED_DATA="+newData);
    }
}