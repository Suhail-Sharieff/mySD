package object_oriented_design._01_Singleton;

public class _05_Bill_Pugh_Singleton {

    public static void main(String[] args) {
        DBConnection obj;//no memeoty allocated yet

        obj=DBConnection.getInstance();//only now the memory is allocated to object

        System.out.println(obj);
    }


    static class DBConnection {
    

        private DBConnection(){}

        static class DBConnectionInnerClass{
            static DBConnection instance=new DBConnection();
        }

        static DBConnection getInstance(){
            return DBConnectionInnerClass.instance;
        }

    }
}