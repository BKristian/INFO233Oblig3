package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceItemsDAO extends DAO{
    public ArrayList<String[]> getInvoiceItems(int id) {
        String sql = "SELECT * FROM invoice_items WHERE invoice = ?";
        ArrayList<String[]> list = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] result = new String[2];
                result[0] = rs.getString("invoice");
                result[1] = rs.getString("product");
                list.add(result);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
        return list;
    }
}
