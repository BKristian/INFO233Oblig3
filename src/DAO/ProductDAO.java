package DAO;

import Meat.Gooey;
import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {

    public Product getProduct(int id) {
        ResultSet rs = null;
        Product product;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM product, category" +
                     " WHERE product.product_id = ? AND product.category = category.category_id")) {
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            product = new Product(id, rs.getString("product_name"), rs.getString("description"),
                    rs.getString("category_name"), rs.getDouble("price"));
        } catch (SQLException e) {
            System.out.println("SQLException in ProductDAO.getProduct: " + e.getMessage());
            product = null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQLException in ProductDAO.getProduct finally: " + e.getMessage());
                }
            }
        }
        return product;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM product, category WHERE" +
                     " product.category = category.category_id");
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

    public int insertProduct(Product product) {
        ResultSet rs = null;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO product (product_id, product_name, " +
                     "description, price, category) VALUES (? ,?, ?, ?, ?)");
             PreparedStatement max = connection.prepareStatement("SELECT * FROM product ORDER BY product_id DESC LIMIT 1")) {
            rs = max.executeQuery();
            int newID = rs.getInt("product_id") + 1;

            pstmt.setInt(1, newID);
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, getCategory(product.getCategory()));
            pstmt.executeUpdate();
            return newID;
        } catch (SQLException e) {
            System.out.println("SQLException in ProductDAO.insertProduct: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQLException in ProductDAO.insertProduct finally: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    // Helper for insertProduct
    private int getCategory(String category) {
        ResultSet rs = null;
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
            PreparedStatement pstmt = connection.prepareStatement("SELECT category_id FROM category WHERE " +
                    "category_name = ?");
            PreparedStatement newCat = connection.prepareStatement("INSERT INTO category (category_name, category_id) " +
                    "VALUES (?, ?)");
            PreparedStatement max = connection.prepareStatement("SELECT * FROM category ORDER BY category_id DESC LIMIT 1")) {
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            int toReturn;
            if(rs.next()) {
                toReturn = rs.getInt("category_id");
                return toReturn;
            } else {
                rs = max.executeQuery();
                toReturn = rs.getInt("category_id") + 1;
                newCat.setString(1, category);
                newCat.setInt(2, toReturn);
                newCat.executeUpdate();
                return toReturn;
            }
        } catch (SQLException e) {
            System.out.println("SQLException in ProductDAO.getCategory: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQLException in ProductDAO.getCategory finally: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public void updateProduct(Product product) {
        try (Connection connection = DriverManager.getConnection(Gooey.getUrl());
             PreparedStatement pstmt = connection.prepareStatement("UPDATE product SET " +
                     "product_name = ?, description = ?, price = ?, category = ? WHERE product_id = ?")) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, getCategory(product.getCategory()));
            pstmt.setInt(5, product.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException in ProductDAO.insertProduct: " + e.getMessage());
        }
    }
}
