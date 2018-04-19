package Meat;

import DAO.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private AddressDAO addressDAO = new AddressDAO();

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
        String[] addArr = addressDAO.getAddress(Integer.parseInt(customArr[2]));
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

        gLabel("Faktura nr.: ", invoiceArr[0], grid, ++rowInd);
        gLabel("Dato: ", invoiceArr[2], grid, ++rowInd);
        gLabel("Kunde: ", customArr[1], grid, ++rowInd);
        gLabel("Kunde nr.: ", invoiceArr[1], grid, ++rowInd);
        gLabel("Addresse: ", addArr[5], grid, ++rowInd);

        double sum = 0;
        for (String[] str : prodList) {
            gLabel("Produkt: ", str[1] + ", " +str[3] + "kr", grid, ++rowInd);
            sum += Double.parseDouble(str[3]);
        }
        gLabel("Totalsum: ", sum + "kr", grid, ++rowInd);

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

        TableView table = new TableView();
        table.setEditable(true);

        TableColumn cusIdCol = new TableColumn("Kunde nr.");
        TableColumn cusNameCol = new TableColumn("Kunde");
        TableColumn cusAddCol = new TableColumn("Addresse");
        TableColumn cusNr = new TableColumn("Tlf. nr.");
        TableColumn cusAccount = new TableColumn("Konto");

        table.getColumns().setAll(cusIdCol, cusNameCol, cusAddCol, cusNr, cusAccount);

        //TODO vise noe i table

        grid.add(table, 0, 0);
        scrollPane.setContent(grid);
        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
    }

    private void gLabel(String a, String b, GridPane c, int row) {
        Label labelA = new Label(a);
        Label labelB = new Label(b);
        labelA.setFont(Font.font(16));
        labelB.setFont(Font.font(16));
        c.add(labelA, 0, row);
        c.add(labelB, 1, row);
    }
}
