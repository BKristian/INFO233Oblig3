package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {

    public Product getProduct(int id) {
        Product product;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM product, category" +
                     " WHERE product.product_id = ? AND product.category = category.category_id")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            product = new Product(id, rs.getString("product_name"), rs.getString("description"),
                    rs.getString("category_name"), rs.getDouble("price"));
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException in ProductDAO.getProduct: " + e.getMessage());
            product = null;
        }
        return product;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM product, category WHERE" +
                     "product.category = category.category_id");
             ResultSet rs = pstmt.executeQuery()) {
            products = new ArrayList<>();
            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"), rs.getString("product_name"),
                        rs.getString("description"), rs.getString("category_name"),
                        rs.getDouble("price")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException in ProductDAO.getAllProducts: " + e.getMessage());
            products = null;
        }
        return products;
    }
}
