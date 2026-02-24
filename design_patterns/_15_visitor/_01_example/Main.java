package design_patterns._15_visitor._01_example;


/*

WKT strategy pattern allows us to change behaviour at run time of code
For example in _03_strategy, we used Sender interface that implements constant number of behavior of Service(there send()), but how it sends it could be varied number (there it was emailService,outlookService)

But now what if i want  to add a few more behaviors instead of just send() in Service interface say send1(),send2() etc too into the same Service interface, then each of emailServie,outlookService must also implement each of these things, overtime it becomes bulky and hard to maintain as well

Visitor pattern solves this problem


*/

/*-------------------------before
interface Service{
    void send1();
    void send2();
    void send3();
}
    */


//--------------------after

interface Service{
    void accept(ServiceVisitor visitor);
}
interface ServiceVisitor{
    //now for each type of service(email and outlook here) make func
    void send(EmailService emailService);
    void send(OutlookService outlookService);
}

class Send1Visitor implements ServiceVisitor {

    @Override
    public void send(EmailService email) {
        System.out.println("Send1 via EMAIL");
    }

    @Override
    public void send(OutlookService outlook) {
        System.out.println("Send1 via OUTLOOK");
    }
}
class Send2Visitor implements ServiceVisitor {

    @Override
    public void send(EmailService email) {
        System.out.println("Send2 via EMAIL");
    }

    @Override
    public void send(OutlookService outlook) {
        System.out.println("Send2 via OUTLOOK");
    }
}
class Send3Visitor implements ServiceVisitor {

    @Override
    public void send(EmailService email) {
        System.out.println("Send3 via EMAIL");
    }

    @Override
    public void send(OutlookService outlook) {
        System.out.println("Send3 via OUTLOOK");
    }
}
class EmailService implements Service{
    @Override
    public void accept(ServiceVisitor visitor) {
        visitor.send(this);
    }
}
class OutlookService implements Service{
    @Override
    public void accept(ServiceVisitor visitor) {
        visitor.send(this);
    }
}

class Sender{
    private Service serviceType;
    public Sender(Service serviceType) {
        this.serviceType=serviceType;
    }
    void send(ServiceVisitor visitorType){
        this.serviceType.accept(visitorType);
    }
}

public class Main {

    public static void main(String[] args) {
        //Earlier when i just had send() that used to send then it looked like this
        /*
        Sender s=new Sender(new EmailService());
        s.send();
        s=new Sender(new OutlookService());
        s.send()
        */

        //but not we have same 2 number of services(ie email and outlook), but now we have 3 types of send (ie Send_1,Send_2,Send_3...)
        Sender s=new Sender(new EmailService());
        s.send(new Send3Visitor());
        s=new Sender(new OutlookService());
        s.send(new Send2Visitor());
    }
}
/*Send3 via EMAIL
Send2 via OUTLOOK */


//now in future u can add send4Vistior class without touching Email or outlookService classes at all