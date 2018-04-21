package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class AddressDAO {

    public Address getAddress(int id) {
        Address address;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM address WHERE address_id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            address = new Address(id, rs.getString("street_number"), rs.getString("street_name"),
                    rs.getString("postal_code"), rs.getString("postal_town"));
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException in AddressDAO.getAddress: " + e.getMessage());
            address = null;
        }
        return address;
    }

    public ArrayList<Address> getAllAddresses() {
        ArrayList<Address> addresses;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM address");
            ResultSet rs = pstmt.executeQuery()) {
            addresses = new ArrayList<>();
            while (rs.next()) {
                addresses.add(new Address(rs.getInt("address_id"), rs.getString("street_number"),
                        rs.getString("street_name"), rs.getString("postal_code"),
                        rs.getString("postal_town")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException in AddressDAO.getAllAddresses: " + e.getMessage());
            addresses = null;
        }
        return addresses;
    }
}