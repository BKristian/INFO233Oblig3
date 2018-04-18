package Meat;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gooey extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        DatabaseSingleton.getInstance();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("INFO233 Oblig 3");
        primaryStage.show();
        this.primaryStage = primaryStage;
        invoice();
    }

    private void invoice() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(15, 15, 15, 15));

        Scene scene = new Scene(grid, 300, 275);


        Text scenetitle = new Text("Faktura");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 28));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label invoiceID = new Label("Faktura nr.:");
        invoiceID.setFont(Font.font("Tahoma", 16));
        grid.add(invoiceID, 0, 1);

        Label customer = new Label("Kunde:");
        customer.setFont(Font.font("Tahoma", 16));
        grid.add(customer, 0, 2);

        Label date = new Label("Dato:");
        date.setFont(Font.font("Tahoma", 16));
        grid.add(date, 0, 3);

        primaryStage.setScene(scene);
    }
}
