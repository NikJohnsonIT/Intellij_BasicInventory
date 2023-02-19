package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Product;

import java.util.Objects;

/**
 FUTURE ENHANCEMENT a future enhancement for the program would be to represent product price with a function that
 returns price values reflective of a user's local currency. This would lay the groundwork for the program to expand
 into internation applications.
 This is the main class including the entirety of the program.
 @author Nicholas Johnson
 */
public class Main extends Application {

    /**
      This is the start of the method that begins the program. The program exists as a basic inventory manager.
      @param primaryStage This is the main stage and is called whenever the program loads.
      @throws Exception Throws exception if the .fxml file does not exist.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/MainMenu.fxml")));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



    /**
      The main method of the program. A sample part and product are provided as models.
      @param args Important for javadoc creation.
     */
    public static void main(String[] args) {


        int partId = Inventory.getPartsIdGenerator().get();
        InHouse inHouse1 = new InHouse(partId, 5, 1, 10, "Saddle Bags", 85.0, 75);


        Inventory.addPart(inHouse1);


        int productId = Inventory.getProductsIdGenerator().get();
        Product product1 = new Product(productId, "Road Bike (Commuter)", 999.99, 3, 1, 5);

        Inventory.addProduct(product1);

      launch(args);

    }
}
