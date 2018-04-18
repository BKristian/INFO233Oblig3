package DAO;

import java.sql.*;

public class InvoiceDAO extends DAO {

    private void insertInvoice() {

    }

    private String[] getInvoice(int id) {
        String sql = "SELECT * FROM invoice WHERE invoice_id = ?";
        String[] result = null;
        try (Connection conn = this.connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery(sql);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
        return result;
    }
}
