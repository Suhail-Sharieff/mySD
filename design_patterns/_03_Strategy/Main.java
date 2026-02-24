package design_patterns._03_Strategy;

/*
ALlows client to change type of email sending service at runtime

it uses polymorphism

Use this pattern when number of behaviors it performs (here its send()) is contant, but how it performs that behaviors(here its via email, outlook) is likely to change or to be added more in future.

NOTE: this method is not good if we want to change number of behaviots itself, for that there's Visitor pattern, refer _15_visitor

*/
public class Main {
    public static void main(String[] args) {
        Sender client = new Sender(new EmailService());
        client.send();//Sending email
        client = new Sender(new OutlookService());// chng to outlook at runtime
        client.send();//Sending outlook
    }

}
