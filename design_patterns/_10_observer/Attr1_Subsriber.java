package design_patterns._10_observer;

public class Attr1_Subsriber implements Subsriber{

    @Override
    public void updateData(Data newData) {
        System.out.println("ATTR1_LISTENED_DATA="+newData);
    }
}