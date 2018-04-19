package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAO extends DAO{
    public String[] getAddress(int id) {
        String sql = "SELECT * FROM address WHERE address_id = ?";
        String[] result = new String[6];
        try (Connection conn = connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            result[0] = rs.getString("address_id");
            result[1] = rs.getString("street_name");
            result[2] = rs.getString("street_number");
            result[3] = rs.getString("postal_code");
            result[4] = rs.getString("postal_town");
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
        result[5] = result[1] + " " + result[2] + " " + result[3] + " " + result[4];
        return result;
    }
}
