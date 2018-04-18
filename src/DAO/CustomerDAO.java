package DAO;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO extends DAO{
    public String[] getCustomer(int id) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        String[] result = new String[5];
        try (Connection conn = connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            result[0] = rs.getString("customer_id");
            result[1] = rs.getString("customer_name");
            result[2] = rs.getString("address");
            result[3] = rs.getString("phone_number");
            result[4] = rs.getString("billing_account");
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
        return result;
    }

    public ArrayList<String[]> getAllCustomers() {
        String sql = "SELECT * FROM customer";
        ArrayList<String[]> list = new ArrayList<>();
        try (Connection conn = connect(); Statement stmt  = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String[] result = new String[5];
                result[0] = rs.getString("customer_id");
                result[1] = rs.getString("customer_name");
                result[2] = rs.getString("address");
                result[3] = rs.getString("phone_number");
                result[4] = rs.getString("billing_account");
                list.add(result);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
