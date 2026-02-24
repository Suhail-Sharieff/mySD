package design_patterns._17_repository;

public class UserService {
    UserRepository myRepo;
    public UserService(UserRepository myRepo){
        this.myRepo=myRepo;
    }
    //any methods u want to impl
}
