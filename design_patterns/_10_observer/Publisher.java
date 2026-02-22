package design_patterns._10_observer;

public interface Publisher{
    void setData(Data newData);
    void addSubsriber(Subsriber s);
    void removeSubsriber(Subsriber s);
}