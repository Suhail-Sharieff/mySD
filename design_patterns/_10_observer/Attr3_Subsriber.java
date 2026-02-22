package design_patterns._10_observer;

public class Attr3_Subsriber implements Subsriber{

    @Override
    public void updateData(Data newData) {
        System.out.println("ATTR3_LISTENED_DATA="+newData);
    }
}