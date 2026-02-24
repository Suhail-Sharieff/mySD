package design_patterns._17_repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JDBCRepository implements UserRepository{//why didnt directly make UserRepository as class and implement methods there?, coz what if in future like her how we have repository interacting with actual DB, i want to interact with in memeory?

    private Connection connection;

    public Connection getConnection () throws SQLException{
        if(connection==null) connection=DriverManager.getConnection("localhost", "codemint", "Codemint@123");
        return connection;
    }

    @Override
    public Optional<User> getUserById(int id) {
        try (
            Connection c=getConnection();
            PreparedStatement stmt=c.prepareStatement("select * from user where user_id=?");
        ) {
            stmt.setInt(0, id);
            ResultSet res=stmt.executeQuery();
            if(res.next()){
                return Optional.of(new User(id, res.getString("userName"), res.getString("email")));
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean registerUser() {
        //logic for registering user
        return true;
    }

    @Override
    public boolean saveUser(User user) {
        // logic for saving user
        return false;
    }
    
}
