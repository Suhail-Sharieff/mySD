package design_patterns._03_Strategy;

public class OutlookService implements Service {

    @Override
    public void send() {
        System.out.println("Sending outlook");
    }

}