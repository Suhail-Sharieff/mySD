package design_patterns._03_Strategy;

/*
ALlows client to change type of email sending service at runtime

it uses polymorphism

*/
public class _01_strategy {
    public static void main(String[] args) {
        Sender client = new Sender(new EmailService());
        client.send();
        client = new Sender(new OutlookService());// chng to outlook at runtime
        client.send();
    }

    static interface Service {
        void send();
    }

    static class Sender implements Service {
        private Service service;

        public Sender(Service type) {
            this.service = type;
        }

        @Override
        public void send() {
            service.send();
        }
    }

    static class EmailService implements Service {

        @Override
        public void send() {
            System.out.println("Sending email");
        }

    }

    static class OutlookService implements Service {

        @Override
        public void send() {
            System.out.println("Sending outlook");
        }

    }

}
