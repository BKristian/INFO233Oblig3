package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO extends DAO {
    public String[] getCategory(int id) {
        String sql = "SELECT * FROM category WHERE category_id = ?";
        String[] result = new String[2];
        try (Connection conn = connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            result[0] = rs.getString("category_id");
            result[1] = rs.getString("category_name");
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
        return result;
    }
}
