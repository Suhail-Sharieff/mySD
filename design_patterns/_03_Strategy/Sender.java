package design_patterns._03_Strategy;

public class Sender {
    private Service service;

    public Sender(Service type) {
        this.service = type;
    }

    public void send() {
        service.send();
    }
}
