package Meat;

import DAO.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gooey extends Application {
    private Stage primaryStage;
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private InvoiceItemsDAO invoiceItemsDAO = new InvoiceItemsDAO();
    private ProductDAO productDAO = new ProductDAO();

    public static void main(String[] args) {
        DatabaseSingleton.getInstance();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("INFO233 Oblig 3");
        primaryStage.show();
        this.primaryStage = primaryStage;
        invoice(1);
    }

    private void invoice(int id) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(15, 15, 15, 15));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        String[] invoiceArr = invoiceDAO.getInvoice(id);
        String[] customArr = customerDAO.getCustomer(Integer.parseInt(invoiceArr[1]));
        ArrayList<String[]> invItems = invoiceItemsDAO.getInvoiceItems(id);
        ArrayList<String[]> prodList = new ArrayList<>();
        for (String[] str : invItems) {
            String[] prodArr = productDAO.getProduct(Integer.parseInt(str[0]));
            prodList.add(prodArr);
        }

        int rowInd = 0;

        Button cusBtn = new Button("Kunder");
        grid.add(cusBtn, 0, ++rowInd);

        cusBtn.setOnAction((ActionEvent event) -> customers());

        Text scenetitle = new Text("Faktura");
        scenetitle.setFont(Font.font(28));
        grid.add(scenetitle, 0, ++rowInd);

        Label invoiceId = gLabel("Faktura nr.: " + invoiceArr[0]);
        grid.add(invoiceId, 0, ++rowInd);

        Label date = gLabel("Dato: " + invoiceArr[2]);
        grid.add(date, 0, ++rowInd);

        Label customer = gLabel("Kunde: " + customArr[1]);
        grid.add(customer, 0, ++rowInd);

        Label customerNr = gLabel("Kunde nr.: " + invoiceArr[1]);
        grid.add(customerNr, 0, ++rowInd);

        double sum = 0;
        for (String[] str : prodList) {
            Label product = gLabel("Produkt: " + str[1]);
            grid.add(product, 0, ++rowInd);

            Label productPrice = gLabel(", " + str[3] + "kr");
            grid.add(productPrice, 1, rowInd);

            sum += Double.parseDouble(str[3]);
        }

        Label sumLabel = gLabel("Totalsum: " + sum + "kr");
        grid.add(sumLabel, 0, ++rowInd);

        scrollPane.setContent(grid);
        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
    }

    private void customers() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(15, 15, 15, 15));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setContent(grid);
        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
    }

    private Label gLabel(String a) {
        Label label = new Label(a);
        label.setFont(Font.font(16));
        return label;
    }
}
