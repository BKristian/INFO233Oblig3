package Meat;

import DAO.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

        Scene scene = new Scene(grid, 700, 400);

        String[] invoiceArr = invoiceDAO.getInvoice(id);
        String[] customArr = customerDAO.getCustomer(Integer.parseInt(invoiceArr[1]));
        InvoiceItems invIts = invoiceItemsDAO.getInvoiceItems(id);
        ArrayList<String[]> list = invIts.getList();
        ArrayList<String[]> prodList = new ArrayList<>();
        for (String[] str : list) {
            String[] prodArr = productDAO.getProduct(Integer.parseInt(str[0]));
            prodList.add(prodArr);
        }

        Text scenetitle = new Text("Faktura");
        scenetitle.setFont(Font.font(28));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label invoiceId = new Label("Faktura nr.: " + invoiceArr[0]);
        invoiceId.setFont(Font.font(16));
        grid.add(invoiceId, 0, 1);

        Label date = new Label("Dato: " + invoiceArr[2]);
        date.setFont(Font.font(16));
        grid.add(date, 0, 2);

        Label customer = new Label("Kunde: " + customArr[1]);
        customer.setFont(Font.font(16));
        grid.add(customer, 0, 4);

        Label customerNr = new Label("Kunde nr.: " + invoiceArr[1]);
        customerNr.setFont(Font.font(16));
        grid.add(customerNr, 0, 5);

        double sum = 0;
        int rowInd = 6;
        for (String[] str : prodList) {
            Label product = new Label("Produkt: " + str[1]);
            product.setFont(Font.font(16));
            grid.add(product, 0, rowInd);

            Label productPrice = new Label(" Pris: " + str[3]);
            productPrice.setFont(Font.font(16));
            grid.add(productPrice, 1, rowInd++);

            sum += Double.parseDouble(str[3]);
        }

        Label sumLabel = new Label("Totalsum: " + sum);
        sumLabel.setFont(Font.font(16));
        grid.add(sumLabel, 0, rowInd++);
        primaryStage.setScene(scene);
    }
}
