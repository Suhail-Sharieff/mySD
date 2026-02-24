package design_patterns._17_repository;

import java.util.Optional;

public interface UserRepository {
    boolean registerUser();
    Optional<User> getUserById(int id);
    boolean saveUser(User user);
}
