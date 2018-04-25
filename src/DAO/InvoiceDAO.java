package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class InvoiceDAO {

    public Invoice getInvoice(int id) {
        Invoice invoice;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM invoice_items WHERE invoice = ?");
             PreparedStatement pstmtInv = connection.prepareStatement("SELECT * FROM invoice WHERE invoice_id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            pstmtInv.setInt(1, rs.getInt("invoice"));
            ResultSet rsInv = pstmtInv.executeQuery();


            invoice = new Invoice(id, rsInv.getInt("customer"), rsInv.getString("dato"));

            ArrayList<Integer> prodArr = new ArrayList<>();

            while (rs.next()) {
                prodArr.add(rs.getInt("product"));
            }
            for (Integer i : prodArr) {
                invoice.add(new ProductDAO().getProduct(i));
            }
            rs.close();
            rsInv.close();
        } catch (SQLException e) {
            System.out.println("SQLException in InvoiceDAO.getInvoice: " + e.getMessage());
            invoice = null;
        }
        return invoice;
    }

    public ArrayList<Invoice> getAllInvoices() {
        ArrayList<Invoice> invoices;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM invoice");
             ResultSet rs = pstmt.executeQuery()) {
            invoices = new ArrayList<>();
            while (rs.next()) {
                invoices.add(new Invoice(rs.getInt("invoice_id"), rs.getInt("customer"),
                        rs.getString("dato")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException in InvoiceDAO.getAllInvoices: " + e.getMessage());
            invoices = null;
        }
        return invoices;
    }

    public int insertInvoice(Invoice invoice) {
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO invoice (invoice_id, " +
                     "customer, dato) VALUES (?, ?, ?)");
             PreparedStatement max = connection.prepareStatement("SELECT * FROM invoice ORDER BY invoice_id DESC LIMIT 1");
             PreparedStatement pstmtItems = connection.prepareStatement("INSERT INTO invoice_items (invoice, product) " +
                     "VALUES (?, ?)")) {
            ResultSet rs = max.executeQuery();
            int newID = rs.getInt("invoice_id") + 1;
            rs.close();
            pstmt.setInt(1, newID);
            pstmt.setInt(2, invoice.getCustomerId());
            pstmt.setString(3, invoice.getDate());
            pstmt.executeUpdate();

            for (Product p : invoice.getItems()) {
                pstmtItems.setInt(1, newID);
                pstmtItems.setInt(2, p.getId());
                pstmtItems.executeUpdate();
            }
            return newID;
        } catch (SQLException e) {
            System.out.println("SQLException in InvoiceDAO.insertInvoice: " + e.getMessage());
        }
        return -1;
    }
}