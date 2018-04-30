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

    private Customer selectedCustomer;
    private Product selectedProduct;
    private Invoice selectedInvoice;

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
            {selectedInvoice = newValue;
            loadInvoice(newValue.getId(), invoiceBox);}));

        loadInvoice(1, invoiceBox);

        VBox insertInvoiceBox = new VBox();
        insertInvoiceBox.setSpacing(10);
        insertInvoiceBox.setPadding(new Insets(0, 5, 0, 5));
        loadInsertInvoice(insertInvoiceBox, invoiceList, invoiceBox);

        VBox updateInvoiceBox = new VBox();
        updateInvoiceBox.setSpacing(10);
        updateInvoiceBox.setPadding(new Insets(0, 5, 0, 5));

        HBox invoiceButtons = new HBox();
        invoiceButtons.setSpacing(5);
        Button invoiceAdd = new Button("New invoice");
        Button invoiceUpdate = new Button("Update invoice");
        invoiceButtons.getChildren().setAll(invoiceAdd, invoiceUpdate);

        invoiceUpdate.setOnAction(e ->
            {loadUpdateInvoice(updateInvoiceBox, invoiceList, invoiceBox, invoiceList.getSelectionModel().getSelectedIndex());
            borderPane.setCenter(updateInvoiceBox);});
        invoiceAdd.setOnAction(e -> borderPane.setCenter(insertInvoiceBox));

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
            {selectedCustomer = newValue;
            loadCustomer(newValue.getId(), customerBox);});

        loadCustomer(1, customerBox);

        VBox insertCustomerBox = new VBox();
        insertCustomerBox.setSpacing(10);
        insertCustomerBox.setPadding(new Insets(5, 5, 0, 5));
        loadInsertCustomer(insertCustomerBox, customerList, customerBox);

        VBox updateCustomerBox = new VBox();
        updateCustomerBox.setSpacing(10);
        updateCustomerBox.setPadding(new Insets(0, 5, 0, 5));


        HBox customerButtons = new HBox();
        customerButtons.setSpacing(5);
        Button customerAdd = new Button("New customer");
        Button customerUpdate = new Button("Update customer");
        customerButtons.getChildren().setAll(customerAdd, customerUpdate);

        customerUpdate.setOnAction(e ->
            {loadUpdateCustomer(updateCustomerBox, customerList, customerBox, customerList.getSelectionModel().getSelectedIndex());
            borderPane.setCenter(updateCustomerBox);});
        customerAdd.setOnAction(e -> borderPane.setCenter(insertCustomerBox));

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
                loadProduct(newValue.getId(), productBox)));

        loadProduct(1, productBox);

        VBox insertProductBox = new VBox();
        insertProductBox.setSpacing(10);
        insertProductBox.setPadding(new Insets(5, 5, 0, 5));
        loadInsertProduct(insertProductBox, productList, productBox);

        VBox updateProductBox = new VBox();
        updateProductBox.setSpacing(10);
        updateProductBox.setPadding(new Insets(0, 5, 0, 5));

        HBox productButtons = new HBox();
        productButtons.setSpacing(5);
        Button productAdd = new Button("New product");
        Button productUpdate = new Button("Update product");
        productButtons.getChildren().setAll(productAdd, productUpdate);

        productUpdate.setOnAction(e -> {
            loadUpdateProduct(updateProductBox, productList, productBox, productList.getSelectionModel().getSelectedIndex());
            borderPane.setCenter(updateProductBox);});
        productAdd.setOnAction(e -> borderPane.setCenter(insertProductBox));

        Button productBtn = new Button("Products");
        productBtn.setOnAction(e -> {borderPane.setCenter(productBox);
            borderPane.setLeft(productList);
            borderPane.setBottom(productButtons);});
        // Product stuff end

        toolBar.getItems().addAll(invoiceBtn, customerBtn, productBtn);

        // Program defaults to show invoices
        borderPane.setCenter(invoiceBox);
        borderPane.setLeft(invoiceList);
        borderPane.setBottom(invoiceButtons);
        primaryStage.setScene(new Scene(scrollPane, 800, 400));
    }

    private void loadUpdateInvoice(VBox updateInvoiceBox, ListView<Invoice> invoiceList, HBox invoiceBox, int selectedIndex) {
        TextField invoiceCstmr = new TextField(selectedInvoice.getId() + "");
        TextField invoiceDate = new TextField(selectedInvoice.getDate());
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Product p : selectedInvoice.getItems()) {
            sb.append(p.getId());
            ++i;
            if(i != selectedInvoice.getItems().size()) {
                sb.append(",");
            }
        }
        String prodString = sb.toString();
        TextField products = new TextField(prodString);

        HBox invoiceCstmrBox = new HBox(new Label("Customer ID: "), invoiceCstmr);
        HBox invoiceDateBox = new HBox(new Label("Due date: "), invoiceDate);
        HBox productsBox = new HBox(new Label("Products: "), products, new Label("Seperate product IDs with commas."));

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

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
                Invoice alteredInvoice = new Invoice(selectedInvoice.getId(), Integer.parseInt(invoiceCstmr.getText()), invoiceDate.getText());
                for (String s : productArr) {
                    int j = Integer.parseInt(s.trim());
                    alteredInvoice.getItems().add(productDAO.getProduct(j));
                }
                invoiceDAO.updateInvoice(alteredInvoice);

                invoiceList.getItems().remove(selectedIndex);
                invoiceList.getItems().add(selectedIndex, alteredInvoice);
                borderPane.setCenter(invoiceBox);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> borderPane.setCenter(invoiceBox));

        updateInvoiceBox.getChildren().setAll(invoiceCstmrBox, invoiceDateBox, productsBox, insertBtn, back);
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

    private void loadUpdateProduct(VBox updateProductBox, ListView<Product> productList, HBox productBox, int selectedIndex) {
        TextField nameField = new TextField(selectedProduct.getName());
        TextField descripField = new TextField(selectedProduct.getDescription());
        TextField categoField = new TextField(selectedProduct.getCategory());
        TextField priceField = new TextField(selectedProduct.getPrice().toString());

        HBox nameBox = new HBox(new Label("Product name: "), nameField);
        HBox descripBox = new HBox(new Label("Description: "), descripField);
        HBox categoBox = new HBox(new Label("Category: "), categoField);
        HBox priceBox = new HBox(new Label("Price: "), priceField);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        Button insertBtn = new Button("Done");
        insertBtn.setOnAction(e -> {
            if(nameField.getText().isEmpty() || descripField.getText().isEmpty() || categoField.getText().isEmpty() ||
                    priceField.getText().isEmpty()) {
                alert.setContentText("One or more of the text fields are empty. Price must be a number.");
                alert.showAndWait();
            } else {
                Product alteredProduct = new Product(selectedProduct.getId(), nameField.getText(), descripField.getText(),
                        categoField.getText(), Double.parseDouble(priceField.getText()));
                productDAO.updateProduct(alteredProduct);

                productList.getItems().remove(selectedIndex);
                productList.getItems().add(selectedIndex, alteredProduct);
                borderPane.setCenter(productBox);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> borderPane.setCenter(productBox));

        updateProductBox.getChildren().setAll(nameBox, descripBox, categoBox, priceBox, insertBtn, back);
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
                    priceField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("One or more of the text fields are empty. Price must be a number.");
                alert.showAndWait();
            } else {
                Product product = new Product(nameField.getText(), descripField.getText(),
                        categoField.getText(), Double.parseDouble(priceField.getText()));
                int prodID = productDAO.insertProduct(product);
                product.setId(prodID);

                productList.getItems().add(product);
                borderPane.setCenter(productBox);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> borderPane.setCenter(productBox));

        insertProductBox.getChildren().setAll(nameBox, descripBox, categoBox, priceBox, insertBtn, back);
    }

    private void loadUpdateCustomer(VBox updateCustomerBox, ListView<Customer> customerList, HBox customerBox, int selectedIndex) {
        Address address = selectedCustomer.getAddress();
        TextField cstmrNmTxtFld = new TextField(selectedCustomer.getName());
        TextField cstmrStreetNrTxtFld = new TextField(address.getStreetNr());
        TextField cstmrStreetNmTxtFld = new TextField(address.getStreetName());
        TextField cstmrPostCodeTxtFld = new TextField(address.getPostCode());
        TextField cstmrPostTwnTxtFld = new TextField(address.getPostTown());
        TextField cstmrPhoneTxtFld = new TextField(selectedCustomer.getPhone());
        TextField cstmrAccountTxtFld = new TextField(selectedCustomer.getAccount());

        HBox cstmrNmBox = new HBox(new Label("Customer name: "), cstmrNmTxtFld);
        HBox cstmrStreetNrBox = new HBox(new Label("Street number: "), cstmrStreetNrTxtFld);
        HBox cstmrStreetNmBox = new HBox(new Label("Street name: "), cstmrStreetNmTxtFld);
        HBox cstmrPostCodeBox = new HBox(new Label("Postal code: "), cstmrPostCodeTxtFld);
        HBox cstmrPostTwnBox = new HBox(new Label("Postal town: "), cstmrPostTwnTxtFld);
        HBox cstmrPhoneBox = new HBox(new Label("Phone number: "), cstmrPhoneTxtFld);
        HBox cstmrAccountBox = new HBox(new Label("Account number: "), cstmrAccountTxtFld);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        Button insertBtn = new Button("Done");
        insertBtn.setOnAction(e -> {
            if(cstmrNmTxtFld.getText().isEmpty() || cstmrStreetNrTxtFld.getText().isEmpty() ||
                    cstmrStreetNmTxtFld.getText().isEmpty() || cstmrPostCodeTxtFld.getText().isEmpty() ||
                    cstmrPostTwnTxtFld.getText().isEmpty() || cstmrPhoneTxtFld.getText().isEmpty() ||
                    cstmrAccountTxtFld.getText().isEmpty()) {
                alert.setContentText("One or more of the text fields are empty.");
                alert.showAndWait();
            } else {
                Address alteredAddress = new Address(selectedCustomer.getAddressId(), cstmrStreetNrTxtFld.getText(), cstmrStreetNmTxtFld.getText(),
                        cstmrPostCodeTxtFld.getText(), cstmrPostTwnTxtFld.getText());
                addressDAO.updateAddress(alteredAddress);

                Customer alteredCus = new Customer(selectedCustomer.getAddressId(), cstmrNmTxtFld.getText(), cstmrPhoneTxtFld.getText(),
                        cstmrAccountTxtFld.getText());
                customerDAO.updateCustomer(alteredCus);
                alteredCus.setId(selectedCustomer.getId());

                customerList.getItems().remove(selectedIndex);
                customerList.getItems().add(selectedIndex, alteredCus);
                borderPane.setCenter(customerBox);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> borderPane.setCenter(customerBox));

        updateCustomerBox.getChildren().setAll(cstmrNmBox, cstmrStreetNrBox, cstmrStreetNmBox, cstmrPostCodeBox,
                cstmrPostTwnBox, cstmrPhoneBox, cstmrAccountBox, insertBtn, back);
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

    private void loadInvoice(int id, HBox invoiceBox) {
        Invoice invoice = invoiceDAO.getInvoice(id);
        Customer customer = customerDAO.getCustomer(invoice.getCustomerId());
        selectedInvoice = invoice;

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
        selectedCustomer = customer;

        VBox left = new VBox(new Text("Customer ID:\nCustomer name:\nPhone number:\nAddress:\nAccount:"));
        VBox right = new VBox(new Text(customer.getId() + "\n" + customer.getName() + "\n" + customer.getPhone() +
                "\n" + customer.getAddress().getFullAddress() + "\n" + customer.getAccount()));

        customerBox.getChildren().setAll(left, right);
    }

    private void loadProduct(int id, HBox productBox) {
        Product product = productDAO.getProduct(id);
        selectedProduct = product;

        VBox left = new VBox(new Text("Product ID:\nProduct name:\nDescription:\nCategory:\nPrice:"));
        VBox right = new VBox(new Text(product.getId() +
                "\n" + product.getName() +
                "\n" + product.getDescription() +
                "\n" + product.getCategory() +
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

            try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
                String sql[] = new String(Files.readAllBytes(Paths.get(sqlFile.getPath())), StandardCharsets.UTF_8).split(";");
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
