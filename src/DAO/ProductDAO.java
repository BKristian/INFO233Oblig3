package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO extends DAO {
    public String[] getProduct(int id) {
    String sql = "SELECT * FROM product WHERE product_id = ?";
    String[] result = new String[5];
    try (Connection conn = connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        result[0] = rs.getString("product_id");
        result[1] = rs.getString("product_name");
        result[2] = rs.getString("description");
        result[3] = rs.getString("price");
        result[4] = rs.getString("category");
        rs.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

    closeConnection();
    return result;
}
}
