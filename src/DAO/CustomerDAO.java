package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    public Customer getCustomer(int id) {
        Customer customer;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customer WHERE customer_id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            customer = new Customer(id, rs.getInt("address"), rs.getString("customer_name"),
                    rs.getString("phone_number"), rs.getString("billing_account"));
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException in CustomerDAO.getCustomer: " + e.getMessage());
            customer = null;
        }
        return customer;
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customer");
             ResultSet rs = pstmt.executeQuery()) {
            customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"), rs.getInt("address"),
                        rs.getString("customer_name"), rs.getString("phone_number"),
                        rs.getString("billing_account")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException in CustomerDAO.getAllCustomers: " + e.getMessage());
            customers = null;
        }
        return customers;
    }

    public int insertCustomer(Customer customer) {
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customer (customer_id, " +
                    "customer_name, address, phone_number, billing_account) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement max = connection.prepareStatement("SELECT * FROM customer ORDER BY customer_id DESC LIMIT 1")) {
            ResultSet rs = max.executeQuery();
            int newID = rs.getInt("customer_id") + 1;
            rs.close();
            pstmt.setInt(1, newID);
            pstmt.setString(2, customer.getName());
            pstmt.setInt(3, customer.getAddressId());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getAccount());
            pstmt.executeUpdate();
            return newID;
        } catch (SQLException e) {
            System.out.println("SQLException in CustomerDAO.insertCustomer: " + e.getMessage());
        }
        return -1;
    }
}