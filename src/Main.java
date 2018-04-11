import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static String url = "jdbc:sqlite:oblig3v1_database.db";

    public static void main(String args[]) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Feil i lasting av JDBC-driver " + e);
        }
        createNewDatabase();
    }

    private static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("New database created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void test() {
        String query = "SELECT *" +
                       "FROM address";
    }
}
