package DAO;

import java.sql.*;

public class InvoiceDAO extends DAO {

    public void insertInvoice() {

    }

    public String[] getInvoice(int id) {
        String sql = "SELECT * FROM invoice WHERE invoice_id = ?";
        String[] result = new String[3];
        try (Connection conn = connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            result[0] = rs.getString("invoice_id");
            result[1] = rs.getString("customer");
            result[2] = rs.getString("dato");
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
        return result;
    }
}
