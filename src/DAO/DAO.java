package DAO;

import Meat.DatabaseSingleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    private DatabaseSingleton db = DatabaseSingleton.getInstance();
    private Connection conn = null;

    public Connection connect() {
        try {
            conn = DriverManager.getConnection(db.getUrl());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
