package design_patterns._17_repository;


/*

Instead of putting all methods in Userclass, we create UserReposotry which has knowledge of interacting with DB ie how to fetch data, 
User service have no idea how to fetch data,it just maintains a refof UserRepository and asks data for it, so v hv decoupled all opr


*/
public class Main {

    public static void main(String[] args) {
        UserRepository myRepo=new JDBCRepository();

        UserService service=new UserService(myRepo);
        //now can do operations using servie.meth()
        System.out.println(service);

    }
    
}


