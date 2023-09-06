import javax.xml.transform.Result;
import java.sql.*;

public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/personal_trainer";
    String username = "root";
    String password = "FIT3170.balance";

    Connection connection;

    private void makeConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet processQuery(String query) {
        ResultSet result = null;
        try {
            if (connection == null) {
                makeConnection();
            }
            assert connection != null;
            Statement statement = connection.createStatement();
            result = statement.executeQuery(query);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

}
