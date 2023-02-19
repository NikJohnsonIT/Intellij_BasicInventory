package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import model.Inventory;
import model.Part;
import model.Product;
import static model.Inventory.*;

/**
  This controller class provides logic controls for the main menu of the application.

  If the user does not select a part and clicks the modify button, a runtime error will occur. This is due to a null value being passed
  to the initialize method of the AddPartMenuController.
  method of this class.

  @author Nicholas Johnson
 */

public class MainMenuController implements Initializable {
    Stage stage;
    Parent scene;

    /**
     Part object selected by user from the corresponding table view. Once selected the user can edit the part.
     */
    private static Part partToModify;

    /**
      Product object selected by user from the corresponding table view. Once selected the user can add associated parts and make other edits to the product.
     */
    private static Product productToModify;

    /**
      Text field used to search parts list. Parts can be searched for by name or ID.
     */
    @FXML
    private TextField mainMenuPartSearch;

    /**
      Table view containing the parts. Contains both Inhouse and Outsourced parts.
     */
    @FXML
    private TableView<Part> partsTableView;

    /**
      Column containing Part ID. This is an automatically generated and unique value that is not editable by the user.
     */
    @FXML
    private TableColumn<Part, Integer> partsPartIdCol;

    /**
      Column containing part names. The user can then use these part names in searches to make navigating easier.
     */
    @FXML
    private TableColumn<Part, String>partPartsNameCol;

    /**
      Column containing the inventory level of each part. This value is checked to ensure it falls between the min and max values.
     */
    @FXML
    private TableColumn<Part, Integer>partInventoryLevelCol;

    /**
      Column containing the price of each part. Prices are currently in US dollars. A further enhancement to the program
      would be to have the price represented by a function that could be updated based off the user's location. This would
      aid expansion efforts as such functionality lays the groundwork for potential international dealings.

     */
    @FXML
    private TableColumn<Part, Double>partPriceCostPerUnitCol;

    /**
      Table view containing products. Products can have associated parts but it is not required. For a product to be deleted, it must not have associated parts.
     */
    @FXML
    private TableView<Product> productsTableView;

    /**
      Text field used to search list of products. Products can be searched using either name or ID values.
     */
    @FXML
    private TextField mainMenuProductSearch;

    /**
      Column containing Product ID. This value is automatically generated and the user cannot interact with it,
     */
    @FXML
    private TableColumn<Product, Integer> productsProductIdCol;

    /**
      Column containing the name of each product. These names can be used in searches to find products.
     */
    @FXML
    private TableColumn<Product, String> productsProductNameCol;

    /**
      Column containing the inventory level of each product. The value is checked to ensure a valid entry.
     */
    @FXML
    private TableColumn<Product, Integer> productsInventoryLevelCol;

    /**
      Column containing the price of each product. Current prices are in USD.
     */
    @FXML
    private TableColumn<Product, Double> productsPriceCostPerUnit;

    /**
      Gets the user-selected part object from the part table. This part can then be modified.
      @return A part object or null if none is selected.
     */
    public static Part getPartToModify() {
        return partToModify;
    }

    /**
      Gets user-selected product object from the product table. This product can then be modified.
      @return A product object or null if none is selected.
     */
    public static Product getProductToModify() {
        return productToModify;
    }

    /**
      Loads Add Part Menu controller. From this screen the user can create a new part object.
      @param event Add button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddPartMenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
      This method loads the ModifyPartMenuController.

      An error message will display if no part is selected.
      @param event Modify button action for part.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {

        partToModify = partsTableView.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            alertMessage(5);
        }
        else {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyPartMenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
      Searches the parts table based off user-entered values. The parts list updates
      as the user continues to enter data.

      Searches may be conducted using ID or Name.
      @param actionEvent Search button action for Part.
     */
    @FXML
    void onActionSearchPart(ActionEvent actionEvent) {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = mainMenuPartSearch.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) || part.getPartName().contains(searchString)) {
                partsFound.add(part);
            }
        }
        partsTableView.setItems(partsFound);
        if (partsFound.size() == 0) {
            alertMessage(4);
        }
    }


    /**
     Searches products based off user-entered text. The products table view updates with the corresponding search results.
     Product ID as well as Name can be used to conduct searches.
     @param event Part search button action.
     */
    @FXML
    void onActionSearchProduct(ActionEvent event) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = mainMenuProductSearch.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) || product.getProductName().contains(searchString)) {
                productsFound.add(product);
            }
        }
        productsTableView.setItems(productsFound);
        if (productsFound.size() == 0) {
            alertMessage(1);
        }
    }

    /**
      This method refreshes the parts table when the search field is empty. This will show all parts.
      @param event Parts search text field key pressed.
     */
    @FXML
    void partSearchTextKeyPressed(KeyEvent event) {
        if (mainMenuPartSearch.getText().isEmpty()) {
            partsTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
      Deletes User-selected part from the part table.

      An error message is displayed should the user fail to select a part and presses the delete part button.
      A confirmation window will prompt the user if they wish to proceed before part deletion is confirmed.
      @param event Delete button action for Part.
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {

        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            alertMessage(5);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete the selected record?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }
    }

    /**
      Load the Add Product Menu. From here the user can create a new product object.
      @param event Add button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/AddProductMenu.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
      Loads ModifyProductController.
      An error message will display if no product is selected.
      @param event Product modify button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {

        productToModify = productsTableView.getSelectionModel().getSelectedItem();
        if (productToModify == null) {
            alertMessage(3);
        }
        else {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyProductMenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /**
      Refreshes the product table view when no text is entered to the search. The table will then show all products.
      @param event enter is pressed with no search field.
     */
    @FXML
    void productSearchTextKeyPressed(KeyEvent event) {
        if (mainMenuProductSearch.getText().isEmpty()) {
            productsTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
      Deletes the user-selected product from the product table.
      An error message displays if no product is selected,additionally a confirmation dialog appears before deletion
      of the product is confirmed.
      This will also prevent a user from deleting a product with any associated parts.
      @param event Product delete button action.
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            alertMessage(3);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING!");
            alert.setContentText("Do you wish to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Part> assocParts = selectedProduct.getAllAssociatedParts();
                if (assocParts.size() >= 1) {
                    alertMessage (2);
                }
                else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }
    }

    /**
      Various alert Messages assigned to variables for more concise coding. These messages display where issues would otherwise cause the program to crash.
      @param alertNumber Alert message selector.
     */
    private void alertMessage(int alertNumber) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);

        switch (alertNumber) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Product not found!");
                alert.showAndWait();
                break;
            case 2:
                alertError.setTitle("Error");
                alertError.setHeaderText("Parts Associated");
                alertError.setContentText("All parts must be removed from product before it can be deleted.");
                alertError.showAndWait();
                break;
            case 3:
                alertError.setTitle("Error");
                alertError.setHeaderText("No product selected");
                alertError.showAndWait();
                break;
            case 4:
                alert.setTitle("Information");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
            case 5:
                alertError.setTitle("Error");
                alertError.setHeaderText("No Part Selected");
                alertError.showAndWait();
                break;
        }
    }

    /**
      This button exits the program. The user can also use the red X in the top right corner.
      @param event Exit button action.
     */
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);

    }

    /**
      Initializes controller and populates the table views. If there is no default values when the program first loads, the tables will remain empty until the user creates objects.
      @param url The location used to resolve relative paths for root object or null if the location is unknown.
      @param rb Resources used ot localize the root object or null if it was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        partsTableView.setItems(Inventory.getAllParts());
        partsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        productsTableView.setItems(Inventory.getAllProducts());
        productsProductIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsProductNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsPriceCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        FilteredList<Part> partFilteredList = new FilteredList<>(getAllParts(), b -> true);
        mainMenuPartSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            partFilteredList.setPredicate(part -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (part.getPartName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else return String.valueOf(part.getId()).contains(lowerCaseFilter);
            });
        });

        SortedList<Part> partSortedList = new SortedList<>(partFilteredList);
        partSortedList.comparatorProperty().bind(partsTableView.comparatorProperty());
        partsTableView.setItems(partSortedList);

        FilteredList<Product> productFilteredList = new FilteredList<>(getAllProducts(), b -> true);
        mainMenuProductSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            productFilteredList.setPredicate(product -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else return String.valueOf(product.getId()).contains(lowerCaseFilter);
            });
        });
        SortedList<Product> productSortedList = new SortedList<>(productFilteredList);
        productSortedList.comparatorProperty().bind(productsTableView.comparatorProperty());
        productsTableView.setItems(productSortedList);

    }


}
