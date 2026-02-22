package design_patterns._10_observer;

import java.util.ArrayList;
import java.util.List;

public 
class Client implements Publisher{
    private List<Subsriber> mySubsribers;
    private Data currData;
    public Data getCurrData() {
        return currData;
    }
    
    public Client() {
        mySubsribers=new ArrayList<>();
    }
    @Override
    public void addSubsriber(Subsriber s){
        mySubsribers.add(s);
    }
    @Override
    public void removeSubsriber(Subsriber s){
        mySubsribers.remove(mySubsribers.indexOf(s));
    }
    private void notify(Data newData) {
        for(Subsriber e:mySubsribers) e.updateData(newData);
    }

    @Override
    public void setData(Data newData) {
        this.currData=newData;
        notify(newData);
    }
}




