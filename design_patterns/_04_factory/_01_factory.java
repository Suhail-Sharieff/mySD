package design_patterns._04_factory;
/*

Prev pattern was strategy pattern, there we used to create object in main method ie client creates object of type of service he wants at run time, now waht if we want to abstract this too
Object creation is happening directly in main().
So creation logic is NOT centralized.
That violates Factory principle.

*/
public class _01_factory {
     public static void main(String[] args) {
        Sender client = SenderFactory.getSender(SenderType.EMAIL);//see now client doesnt have to care abt Sender obj and its type creation at all, ie we have centralized that logic into a separate SenderFactory Class
        client.send();
        client = SenderFactory.getSender(SenderType.OUTLOOK);
        client.send();
    }

    static enum SenderType{
        EMAIL,
        OUTLOOK
    }
    static class SenderFactory{
        static Sender getSender(SenderType type){
            if(type.equals(SenderType.EMAIL)) return new Sender(new EmailService());
            return new Sender(new OutlookService());
        }
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
