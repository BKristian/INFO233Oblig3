package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class InvoiceDAO {

    public Invoice getInvoice(int id) {
        Invoice invoice;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM invoice_items WHERE invoice = ?");
             PreparedStatement pstmtInv = connection.prepareStatement("SELECT * FROM invoice WHERE invoice_id = ?");
             PreparedStatement pstmtProd = connection.prepareStatement("SELECT * FROM product WHERE product_id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            pstmtInv.setInt(1, rs.getInt("invoice"));
            ResultSet rsInv = pstmtInv.executeQuery();

            pstmtProd.setInt(1, rs.getInt("product"));
            ResultSet rsProd = pstmtProd.executeQuery();

            invoice = new Invoice(id, rsInv.getInt("customer"), rsInv.getString("dato"));

            while (rsProd.next()) {
                invoice.add(new ProductDAO().getProduct(rsProd.getInt("product_id")));
            }
            rs.close();
            rsInv.close();
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