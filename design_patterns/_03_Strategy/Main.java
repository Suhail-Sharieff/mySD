package design_patterns._03_Strategy;

/*
ALlows client to change type of email sending service at runtime

it uses polymorphism

*/
public class Main {
    public static void main(String[] args) {
        Sender client = new Sender(new EmailService());
        client.send();//Sending email
        client = new Sender(new OutlookService());// chng to outlook at runtime
        client.send();//Sending outlook
    }

}
