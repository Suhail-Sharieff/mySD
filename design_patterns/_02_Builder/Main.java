package design_patterns._02_Builder;
/*True Builder pattern has:

Separate static Builder class
Immutable final object
build() method

Validation before construction */
public class Main {
    

   
    public static void main(String[] args) {
        Person p=new Person
        .PersonBuilder("Suhail Sharieff")//clean builder with validation possible in build(), immmutable fields, controlled contruction
        .setAge(23)
        .setHeight(5)
        .build();

        System.out.println(p);
    }
}
