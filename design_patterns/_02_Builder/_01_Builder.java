package design_patterns._02_Builder;
/*True Builder pattern has:

Separate static Builder class
Immutable final object
build() method

Validation before construction */
public class _01_Builder {
    

    static class Person{

       

        //brute force is that we build diff number/types of constructors to set specific attributes or all

       
        
        //MISTAKE: i dint write builder , i directly wrote getters and then setters that returns `this`, but then if v do like that, usr may not provide values for some at all making it null, to avoid that we need to make feild final, but then we have to init all attr in contructor itself, so better use Builder that upon `build()` returns built person, in this way we also achive immutability



         //all filds final here------IMMUTABLILITY achievdd
        private final String name;
        private final int age;
        private final int height;
        public Person(PersonBuilder builder){
            this.name=builder.name;
            this.age=builder.age;
            this.height=builder.height;
        }

        static class PersonBuilder{
            private final String name;
            private int age=0;
            private int height=0;
            public PersonBuilder(String name) {
                this.name=name;
            }
            PersonBuilder setAge(int age){
                this.age=age;
                return this;
            }
            
            PersonBuilder setHeight(int height){
                this.height=height;
                return this;
            }
            Person build(){
                return new Person(this);
            }
        }
        
        @Override
        public String toString() {return name+" "+height+" "+age;}

    }


    public static void main(String[] args) {
        Person p=new Person
        .PersonBuilder("Suhail Sharieff")//clean builder with validation possible in build(), immmutable fields, controlled contruction
        .setAge(23)
        .setHeight(5)
        .build();

        System.out.println(p);
    }
}
