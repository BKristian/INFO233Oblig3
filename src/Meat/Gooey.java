package Meat;

import DAO.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private BorderPane borderPane = new BorderPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("INFO233 Oblig 3");
        primaryStage.show();
        setUp();

        borderPane.setPadding(new Insets(0, 5, 5, 5));
        ToolBar toolBar = new ToolBar();
        borderPane.setTop(toolBar);

        ScrollPane scrollPane = new ScrollPane(borderPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        // Invoice stuff start
        HBox invoiceBox = new HBox();
        invoiceBox.setSpacing(10);
        invoiceBox.setPadding(new Insets(0, 5, 0, 5));

        ListView<Invoice> invoiceList = new ListView<>();
        invoiceList.setItems(FXCollections.observableArrayList(invoiceDAO.getAllInvoices()));
        invoiceList.getSelectionModel().selectFirst();
        invoiceList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
                loadInvoice(newValue.getId(), invoiceBox)));

        loadInvoice(invoiceList.getSelectionModel().getSelectedItem().getId(), invoiceBox);

        VBox insertInvoiceBox = new VBox();
        insertInvoiceBox.setSpacing(10);
        insertInvoiceBox.setPadding(new Insets(0, 5, 0, 5));
        loadInsertInvoice(insertInvoiceBox, invoiceList, invoiceBox);

        HBox invoiceButtons = new HBox();
        invoiceButtons.setSpacing(5);
        Button invoiceAdd = new Button("New invoice");
        Button invoiceRemove = new Button("Remove invoice");
        invoiceButtons.getChildren().setAll(invoiceAdd, invoiceRemove);

        invoiceAdd.setOnAction(e -> borderPane.setCenter((insertInvoiceBox)));

        Button invoiceBtn = new Button("Invoices");
        invoiceBtn.setOnAction(e -> {borderPane.setCenter(invoiceBox);
            borderPane.setLeft(invoiceList);
            borderPane.setBottom(invoiceButtons);});
        // Invoice stuff end

        // Customer stuff start
        HBox customerBox = new HBox();
        customerBox.setSpacing(10);
        customerBox.setPadding(new Insets(0, 5, 0, 5));

        ListView<Customer> customerList = new ListView<>();
        customerList.setItems(FXCollections.observableArrayList(customerDAO.getAllCustomers()));
        customerList.getSelectionModel().selectFirst();
        customerList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                loadCustomer(newValue.getId(), customerBox));

        loadCustomer(customerList.getSelectionModel().getSelectedItem().getId(), customerBox);

        VBox insertCustomerBox = new VBox();
        insertCustomerBox.setSpacing(10);
        insertCustomerBox.setPadding(new Insets(5, 5, 0, 5));
        loadInsertCustomer(insertCustomerBox, customerList, customerBox);

        HBox customerButtons = new HBox();
        customerButtons.setSpacing(5);
        Button customerAdd = new Button("New customer");
        Button customerRemove = new Button("Remove customer");
        customerButtons.getChildren().setAll(customerAdd, customerRemove);

        customerAdd.setOnAction(e -> borderPane.setCenter((insertCustomerBox)));

        Button customerBtn = new Button("Customers");
        customerBtn.setOnAction(e -> {borderPane.setCenter(customerBox);
            borderPane.setLeft(customerList);
            borderPane.setBottom(customerButtons);});
        // Customer stuff end

        // Product stuff start
        HBox productBox = new HBox();
        productBox.setSpacing(10);
        productBox.setPadding(new Insets(0, 5, 0, 5));

        ListView<Product> productList = new ListView<>();
        productList.setItems(FXCollections.observableArrayList(productDAO.getAllProducts()));
        productList.getSelectionModel().selectFirst();
        productList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
                loadCustomer(newValue.getId(), productBox)));

        loadProduct(productList.getSelectionModel().getSelectedItem().getId(), productBox);

        VBox insertProductBox = new VBox();
        insertProductBox.setSpacing(10);
        insertProductBox.setPadding(new Insets(5, 5, 0, 5));
        loadInsertProduct(insertProductBox, productList, customerBox);

        HBox productButtons = new HBox();
        productButtons.setSpacing(5);
        Button productAdd = new Button("New product");
        Button productRemove = new Button("Remove product");
        productButtons.getChildren().setAll(productAdd, productRemove);

        productAdd.setOnAction(e -> borderPane.setCenter((insertProductBox)));

        Button productBtn = new Button("Products");
        productBtn.setOnAction(e -> {borderPane.setCenter(productBox);
            borderPane.setLeft(productList);
            borderPane.setBottom(productButtons);});
        // Product stuff end

        toolBar.getItems().addAll(invoiceBtn, customerBtn, productBtn);

        // Default to show invoices
        borderPane.setCenter(invoiceBox);
        borderPane.setLeft(invoiceList);
        borderPane.setBottom(invoiceButtons);
        primaryStage.setScene(new Scene(scrollPane, 800, 400));
    }

    private void loadInsertProduct(VBox insertProductBox, ListView<Product> productList, HBox productBox) {
        TextField nameField = new TextField("");
        TextField descripField = new TextField("");
        TextField categoField = new TextField("");
        TextField priceField = new TextField("");

        HBox nameBox = new HBox(new Label("Product name: "), nameField);
        HBox descripBox = new HBox(new Label("Description: "), descripField);
        HBox categoBox = new HBox(new Label("Category: "), categoField);
        HBox priceBox = new HBox(new Label("Price: "), priceField);

        Button insertBtn = new Button("Done");
        insertBtn.setOnAction(e -> {
            if(nameField.getText().isEmpty() || descripField.getText().isEmpty() || categoField.getText().isEmpty() ||
                    priceField.getText().isEmpty())
        });
    }

    private void loadInsertCustomer(VBox insertCustomerBox, ListView<Customer> customerList, HBox customerBox) {
        TextField cstmrNmTxtFld = new TextField("");
        TextField cstmrStreetNrTxtFld = new TextField("");
        TextField cstmrStreetNmTxtFld = new TextField("");
        TextField cstmrPostCodeTxtFld = new TextField("");
        TextField cstmrPostTwnTxtFld = new TextField("");
        TextField cstmrPhoneTxtFld = new TextField("");
        TextField cstmrAccountTxtFld = new TextField("");

        HBox cstmrNmBox = new HBox(new Label("Customer name: "), cstmrNmTxtFld);
        HBox cstmrStreetNrBox = new HBox(new Label("Street number: "), cstmrStreetNrTxtFld);
        HBox cstmrStreetNmBox = new HBox(new Label("Street name: "), cstmrStreetNmTxtFld);
        HBox cstmrPostCodeBox = new HBox(new Label("Postal code: "), cstmrPostCodeTxtFld);
        HBox cstmrPostTwnBox = new HBox(new Label("Postal town: "), cstmrPostTwnTxtFld);
        HBox cstmrPhoneBox = new HBox(new Label("Phone number: "), cstmrPhoneTxtFld);
        HBox cstmrAccountBox = new HBox(new Label("Account number: "), cstmrAccountTxtFld);



        Button insertBtn = new Button("Done");
        insertBtn.setOnAction(e -> {
            if(cstmrNmTxtFld.getText().isEmpty() || cstmrStreetNrTxtFld.getText().isEmpty() ||
                    cstmrStreetNmTxtFld.getText().isEmpty() || cstmrPostCodeTxtFld.getText().isEmpty() ||
                    cstmrPostTwnTxtFld.getText().isEmpty() || cstmrPhoneTxtFld.getText().isEmpty() ||
                    cstmrAccountTxtFld.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("One or more of the text fields are empty.");
                alert.showAndWait();
            } else {
                Address address = new Address(cstmrStreetNrTxtFld.getText(), cstmrStreetNmTxtFld.getText(),
                        cstmrPostCodeTxtFld.getText(), cstmrPostTwnTxtFld.getText());
                int addID = addressDAO.insertAddress(address);
                address.setId(addID);

                Customer customer = new Customer(addID, cstmrNmTxtFld.getText(), cstmrPhoneTxtFld.getText(),
                        cstmrAccountTxtFld.getText());
                int cusID = customerDAO.insertCustomer(customer);
                customer.setId(cusID);

                customerList.getItems().add(customer);
                borderPane.setCenter(customerBox);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> borderPane.setCenter(customerBox));

        insertCustomerBox.getChildren().setAll(cstmrNmBox, cstmrStreetNrBox, cstmrStreetNmBox, cstmrPostCodeBox,
                cstmrPostTwnBox, cstmrPhoneBox, cstmrAccountBox, insertBtn, back);
    }

    private void loadInsertInvoice(VBox insertInvoiceBox, ListView<Invoice> invoiceList, HBox invoiceBox) {
        TextField invoiceCstmr = new TextField("");
        TextField invoiceDate = new TextField("");
        TextField products = new TextField("");

        HBox invoiceCstmrBox = new HBox(new Label("Customer ID: "), invoiceCstmr);
        HBox invoiceDateBox = new HBox(new Label("Due date: "), invoiceDate);
        HBox productsBox = new HBox(new Label("Products: "), products, new Label("Seperate product IDs with commas."));

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        //SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

        Button insertBtn = new Button("Done");
        insertBtn.setOnAction(e -> {
            if(invoiceCstmr.getText().isEmpty() || invoiceDate.getText().isEmpty()
                    || Integer.parseInt(invoiceCstmr.getText()) < 1) {
                alert.setContentText("One or more of the text fields are empty.");
                alert.showAndWait();
            } else if(customerDAO.getCustomer(Integer.parseInt(invoiceCstmr.getText())) == null) {
                alert.setContentText("Customer with ID " + invoiceCstmr.getText() + " does not exist.");
            } else { //TODO sjekk at dato til invoice er en dato
                String[] productArr = products.getText().split(",");
                Invoice invoice = new Invoice(Integer.parseInt(invoiceCstmr.getText()), invoiceDate.getText());
                for (String s : productArr) {
                    int i = Integer.parseInt(s.trim());
                    invoice.getItems().add(productDAO.getProduct(i));
                }

                int invID = invoiceDAO.insertInvoice(invoice);
                invoice.setId(invID);
                invoiceList.getItems().add(invoice);
                borderPane.setCenter(invoiceBox);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> borderPane.setCenter(invoiceBox));

        insertInvoiceBox.getChildren().setAll(invoiceCstmrBox, invoiceDateBox, productsBox, insertBtn, back);
    }

    private void loadInvoice(int id, HBox invoiceBox) {
        Invoice invoice = invoiceDAO.getInvoice(id);
        Customer customer = customerDAO.getCustomer(invoice.getCustomerId());

        VBox left = new VBox(new Text("Invoice number:\nInvoice date:\nCustomer:\nCustomer ID:\nCustomer address:\n" +
                "Customer account:\nTotal sum:\nProducts:"));

        StringBuilder allProducts = new StringBuilder();
        double total = 0;
        for (Product p : invoice.getItems()) {
            String s = p.getName() + ", kr " + p.getPrice() + "\n";
            allProducts.append(s);
            total += p.getPrice();
        }

        VBox right = new VBox(new Text(invoice.getId() + "\n" + invoice.getDate() + "\n" + customer.getName() +
                "\n" + customer.getId() + "\n" + customer.getAddress().getFullAddress() + "\n" + customer.getAccount() + "\n" + "kr " +  total +
                "\n" + allProducts.toString()));

        invoiceBox.getChildren().setAll(left, right);
    }

    private void loadCustomer(int id, HBox customerBox) {
        Customer customer = customerDAO.getCustomer(id);

        VBox left = new VBox(new Text("Customer ID:\nCustomer name:\nPhone number:\nAddress:\nAccount:"));
        VBox right = new VBox(new Text(customer.getId() + "\n" + customer.getName() + "\n" + customer.getPhone() +
                "\n" + customer.getAddress().getFullAddress() + "\n" + customer.getAccount()));

        customerBox.getChildren().setAll(left, right);
    }

    private void loadProduct(int id, HBox productBox) {
        Product product = productDAO.getProduct(id);

        VBox left = new VBox(new Text("Product ID:\nProduct name:\nDescription:\nPrice:"));
        VBox right = new VBox(new Text(product.getId() + "\n" + product.getName() + "\n" + product.getDescription() +
                "\nkr " + product.getPrice()));

        productBox.getChildren().setAll(left, right);
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
