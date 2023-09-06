import java.sql.ResultSet;

public class UserDatabaseManager {
    public void printAllUsers() {
        DBConnection database = new DBConnection();
        ResultSet result = database.processQuery("select * from user;");
        try {
            while (result.next()) {
                System.out.println(
                        result.getInt(1)
                                + " | " + result.getString(2)
                                + " | " + result.getString(3)
                                + " | " + result.getString(4)
                                + " | " + result.getDate(5) + " | "
                );
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
}
