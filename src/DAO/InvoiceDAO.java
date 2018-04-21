package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class InvoiceDAO {

    public Invoice getInvoice(int id) {
        Invoice invoice;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM invoice WHERE invoice_id = ?");
             PreparedStatement pstmtProd = connection.prepareStatement("SELECT product FROM invoice_items WHERE invoice = ?")) {
            pstmt.setInt(1, id);
            pstmtProd.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            ResultSet rsProd = pstmtProd.executeQuery();
            invoice = new Invoice(id, rs.getInt("customer"), rs.getString("dato"));
            while (rsProd.next()) {
                Product product = new Product(rsProd.getInt("product_id"),
                        rsProd.getString("product_name"), rsProd.getString("description"),
                        rsProd.getString("category"), rsProd.getDouble("price"));
                invoice.add(product);
            }
            rs.close();
            rsProd.close();
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
}