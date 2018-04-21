package Meat;

import DAO.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Gooey extends Application {
    private static File dbFile = new File("oblig3v1_database.db");
    private static File sqlFile = new File("oblig3v1_database.sql");
    private static String url = "jdbc:sqlite:" + dbFile.getPath();

    private AddressDAO addressDAO = new AddressDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private ProductDAO productDAO = new ProductDAO();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("INFO233 Oblig 3");
        primaryStage.show();
        setUp();

        BorderPane borderPane = new BorderPane();
        ToolBar toolBar = new ToolBar();
        borderPane.setTop(toolBar);

        ScrollPane scrollPane = new ScrollPane(borderPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        //Invoice stuff start
        HBox invoiceBox = new HBox();
        invoiceBox.setSpacing(10);
        loadInvoice(1, invoiceBox);
        //Invoice stuff end

        Button invoiceBtn = new Button("Invoices");
        Button customerBtn = new Button("Customers");
        Button prodcutBtn = new Button("Products");

        //TODO button events go here
        invoiceBtn.setOnAction(e -> borderPane.setCenter(invoiceBox));

        toolBar.getItems().addAll(invoiceBtn, customerBtn, prodcutBtn);

        borderPane.setCenter(invoiceBox);
        primaryStage.setScene(new Scene(scrollPane, 800, 800));
    }

    private void loadInvoice(int id, HBox invoiceBox) {
        invoiceBox.getChildren().setAll();
        Invoice invoice = invoiceDAO.getInvoice(id);
        Customer customer = customerDAO.getCustomer(invoice.getCustomerId());
        Address address = addressDAO.getAddress(customer.getAddressId());

        VBox left = new VBox(new Text("Invoice number:\nInvoice date:\nCustomer:\nCustomer address:\n" +
                "Customer account:\nTotal sum:\nProducts:"));

        StringBuilder allProducts = new StringBuilder();
        double total = 0;
        for (Product p : invoice.getItems()) {
            allProducts.append(p.getName() + ", kr " + p.getPrice() + "\n");
            total += p.getPrice();
        }

        VBox right = new VBox(new Text(invoice.getId() + "\n" + invoice.getDate() + "\n" + customer.getName() +
                "\n" + address.getFullAddress() + "\n" + customer.getAccount() + "\n" + "kr " +  total +
                "\n" + allProducts.toString()));

        invoiceBox.getChildren().setAll(left, right);
    }

    private void setUp() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }

        if(!dbFile.exists()) {
            try (Connection conn = DriverManager.getConnection(url)) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("New database created");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            String sql[];
            try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
                sql = new String(Files.readAllBytes(Paths.get(sqlFile.getPath())), StandardCharsets.UTF_8).split(";");
                for (String s : sql) {
                    stmt.execute(s);
                }
            } catch (SQLException e) {
                System.out.println("SQLException in Gooey.setUp while executing SQL statements: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IOExpetion in Gooey.setUp: " + e.getMessage());
            }
        } else {
            System.out.println("Database-file exists.");
        }
    }

    public static String getUrl() {
        return url;
    }
}
