package design_patterns._03_Strategy;

public class Sender implements Service {
    private Service service;

    public Sender(Service type) {
        this.service = type;
    }

    @Override
    public void send() {
        service.send();
    }
}
