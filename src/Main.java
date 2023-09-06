import java.sql.*;


public class Main {
    public static void main(String[] args) {
        UserDatabaseManager userDatabaseManager = new UserDatabaseManager();
        userDatabaseManager.printAllUsers();
    }
}