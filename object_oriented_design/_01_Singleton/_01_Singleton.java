package object_oriented_design._01_Singleton;

//DB conn is very expensive, v cant afford to create it again and again, so we create a singleton class , that provides same instance to all those who need it

public class _01_Singleton {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        DBConnection heavyObj1 = DBConnection.getInstance();
        DBConnection heavyObj2 = DBConnection.getInstance();
    }

    static class DBConnection {//NOTE: here DBConnection.instance can be accessed coz the below class is class within a class, in prod, its written separately, so no worries

        private DBConnection() {
        }// we dont want anyone to create obj of our DB conn, usr can acees by calling `DBConnection.getInstance()`

        //why private static : coz we wnt usr to call DBConnection.getInstance() whichs a static meth, so to acess this instance, even instance needs to be static. But if we just made static usr can still acess this instance usingDBConnection.instance, so v made it priv
        private static DBConnection instance;


        static DBConnection getInstance() {
            if (instance == null)
                instance = new DBConnection();
            return instance;
        }
    }
}
