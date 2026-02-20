package design_patterns._03_Strategy;

public class EmailService implements Service {

    @Override
    public void send() {
        System.out.println("Sending email");
    }

}
