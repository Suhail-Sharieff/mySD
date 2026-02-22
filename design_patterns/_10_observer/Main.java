package design_patterns._10_observer;

/*

Clinet(publisher) maintains List of those whom he substribed to 

*/

public class Main {
    public static void main(String[] args) {
        Client publisher=new Client();    
        Subsriber attr1Subsriber=new Attr1_Subsriber();
        Subsriber attr2Subsriber=new Attr2_Subsriber();


        publisher.addSubsriber(attr1Subsriber);

        publisher.setData(new Data("AUZ", 0, false));

        publisher.addSubsriber(attr2Subsriber);


        publisher.setData(new Data("NZ", 1, false));

        publisher.removeSubsriber(attr1Subsriber);

        publisher.setData(new Data("IND", 0, true));
    }
}



