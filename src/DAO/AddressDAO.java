package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class AddressDAO {

    public Address getAddress(int id) {
        ResultSet rs = null;
        Address address;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM address WHERE address_id = ?")) {
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            address = new Address(id, rs.getString("street_number"), rs.getString("street_name"),
                    rs.getString("postal_code"), rs.getString("postal_town"));

        } catch (SQLException e) {
            System.out.println("SQLException in AddressDAO.getAddress: " + e.getMessage());
            address = null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQLException in AddressDAO.getAddress finally: " + e.getMessage());
                }
            }
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

    public int insertAddress(Address address) {
        ResultSet rs = null;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO address (address_id, street_number, " +
                     "street_name, postal_code, postal_town) VALUES (? ,?, ?, ?, ?)");
             PreparedStatement max = connection.prepareStatement("SELECT * FROM address ORDER BY address_id DESC LIMIT 1")) {
            rs = max.executeQuery();
            int newID = rs.getInt("address_id") + 1;
            pstmt.setInt(1, newID);
            pstmt.setString(2, address.getStreetNr());
            pstmt.setString(3, address.getStreetName());
            pstmt.setString(4, address.getPostCode());
            pstmt.setString(5, address.getPostTown());
            pstmt.executeUpdate();
            return newID;
        } catch (SQLException e) {
            System.out.println("SQLException in AddressDAO.insertAddress: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQLException in AddressDAO.insertAddress finally: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public void updateAddress(Address address) {
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("UPDATE address SET street_number = ?, " +
                     "street_name = ?, postal_code = ?, postal_town = ? WHERE address_id = ?")) {
            pstmt.setString(1, address.getStreetNr());
            pstmt.setString(2, address.getStreetName());
            pstmt.setString(3, address.getPostCode());
            pstmt.setString(4, address.getPostTown());
            pstmt.setInt(5, address.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException in AddressDAO.updateAddress: " + e.getMessage());
        }
    }
}