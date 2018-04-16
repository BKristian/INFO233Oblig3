import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private static File dbFile = new File("oblig3v1_database.db");
    private static File sqlFile = new File("oblig3v1_database.sql");
    private static String url = "jdbc:sqlite:oblig3v1_database.db";

    public static DatabaseSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    private DatabaseSingleton() {
        if(!dbFile.exists()) {
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("New database created");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Database-file exists.");
        }

        executeSQLFile();
    }

    private void executeSQLFile() {
        String sqlString = null;
        try {
            sqlString = new String(Files.readAllBytes(Paths.get(sqlFile.getPath())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String[] statements = sqlString.split(";");

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            for (String s : statements) {
                stmt.execute(s);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
