package controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.*;
import static model.Inventory.*;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
  This controller class provides control logic for the Add Product screen. Users can create new products using this screen. Parts can be added to products as associated parts but this is not required for products. For a product to be deleted, associated parts must first be removed.
  @author Nicholas Johnson
 */
public class AddProductMenuController implements Initializable {

    Stage stage;
    Parent scene;
    int index;

    /**
      List of parts associated with the product. This list by default is empty and products must not have associated parts to be deleted.
     */
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();


    /**
      All Parts table view. This table view contains all the current parts and is the same as the parts table view found on the main screen.
     */
    @FXML
    private TableView<Part> addProductAllPartsTable;

    /**
      Column containing the Part ID for the All Parts table. These values are unique and automatically generated. The ID can be used in searches to find specified parts.
     */
    @FXML
    private TableColumn<Part, Integer>allPartsPartIdCol;

    /**
      Column containing the name of each part for the All Parts table. Users can use part names in searches to find specific parts.
     */
    @FXML
    private TableColumn<Part, String>allPartsPartNameCol;

    /**
      Column containing the inventory level for each part in the All Parts table. Referred to as stock in the code.
     */
    @FXML
    private TableColumn<Part, Integer>allPartsInvLvlCol;

    /**
      Column containing the price of each part in the All Parts table. Current values are in USD.
     */
    @FXML
    private TableColumn<Part, Double>allPartsPriceCostPerUnitCol;

    /**
      Associated Parts table view. Defaults as empty, the user may select available parts to add to each product.
     */
    @FXML
    private TableView<Part> addProductAssocPartsTable;

    /**
      Column containing the Part ID for the Associated Parts table. Like all part and product ID, these are unique and generated automatically. ID can also be used to search for parts.
     */
    @FXML
    private TableColumn<Product, Integer> assocPartsPartIdCol;

    /**
      Column containing the name of each part for the Associated Parts table. User's may add any or no parts to a product and can use the names in searches.
     */
    @FXML
    private TableColumn<Product, String> assocPartsPartNameCol;

    /**
      Column containing the inventory level for each part in the Associated Parts table. This value is checked to ensure valid entries.
     */
    @FXML
    private TableColumn<Product, Integer> assocPartsInvLvlCol;

    /**
      Column containing the price of each part in the Associated Parts table. Current values are in USD.
     */
    @FXML
    private TableColumn<Product, Double> assocPartsPriceCostPerUnitCol;

    /**
      Text field for product ID. Automatically generated and can be used in searches.
     */
    @FXML
    private TextField addProductIdTxt;

    /**
      Text field for product name. User must enter a value and is alerted should they leave the field blank.
     */
    @FXML
    private TextField addProductNameTxt;

    /**
      Text field for product inventory level. This value is checked by the program to ensure valid values.
     */
    @FXML
    private TextField addProductInvTxt;

    /**
      Text field for product price. Current values in USD.
     */
    @FXML
    private TextField addProductPriceTxt;

    /**
      Text field for product maximum. This is the maximum number of the product the retailer wishes to have on stock at a given time.
     */
    @FXML
    private TextField addProductMaxTxt;

    /**
      Text field for product minimum. This is the minimum number of products the retailer wishes to have on stock.
     */
    @FXML
    private TextField addProductMinTxt;

    /**
      Search field for parts. ID or name can be searched, a message alerts the user should their search return no results.
     */
    @FXML
    private TextField addProductPartSearch;

    /**
      Adds part object from the All Parts table to the Associated Parts table.
      An error message will display if a user does not select a part.
      @param event add button action.
     */
    @FXML
    void onActionAddPartToProduct (ActionEvent event) {
        Part selectAssocPart = addProductAllPartsTable.getSelectionModel().getSelectedItem();
        if (selectAssocPart == null) {
            alertMessage(4);
        }
        else {
            assocParts.add(selectAssocPart);
            addProductAssocPartsTable.setItems(assocParts);
        }
    }

    /**
      Displays a confirmation dialog and removes selected part from the associated parts table.

      An error message is displayed if a user selects no item.
      @param event Remove button action.
     */
    @FXML
    void onActionRemovePartFromProduct (ActionEvent event) {
        Part selectedPart = addProductAssocPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            alertMessage(3);
        }
        else if (selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Caution!");
            alert.setContentText("Remove selected part from product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                addProductAssocPartsTable.setItems(assocParts);
            }
        }
    }

    /**
      Searches for a part based off the entered value. As the user enters more into the search, the list of parts updates.
      Part can be searched using either ID or Name.

      When no value is entered into the search field, the list returns all parts.

      @param event Search Button action.
     */
    @FXML
    void addProductSearch(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsMatch = FXCollections.observableArrayList();
        String searchString = addProductPartSearch.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) || part.getPartName().contains(searchString)) {
                partsMatch.add(part);
            }
        }
        addProductAllPartsTable.setItems(partsMatch);
        if (partsMatch.size() == 0) {
            alertMessage(3);
        }
        if (addProductPartSearch.getText().isEmpty()) {
            addProductAllPartsTable.setItems(Inventory.getAllParts());
        }
    }

    /**
      Adds the new product object to the inventory and returns to the Main Menu.

      Text fields are validated through error messages, preventing empty and invalid values.

      @param event Save button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {
        try {
            int id = 0;
            for (Product product : Inventory.getAllProducts()) {
                if (product.getId() > id)
                    id = (product.getId());
                id = ++id;
            }
            String productName = addProductNameTxt.getText();
            int stock = Integer.parseInt(addProductInvTxt.getText());
            Double price = Double.parseDouble(addProductPriceTxt.getText());
            int max = Integer.parseInt(addProductMaxTxt.getText());
            int min = Integer.parseInt(addProductMinTxt.getText());
            String companyName = null;
            if (productName.isEmpty()) {
                alertMessage(2);
            }
            else if ((min < 0) || (min > max)) {
                alertMessage(1);
            }
            else if (!((min <= stock) && (max >= stock))) {
                alertMessage(5);
            }
            else {
                Product newProduct = new Product(id, productName, price, stock, min, max);
                for (Part part : assocParts) {
                    newProduct.addAssociatedPart(part);
                }
                Inventory.addProduct(newProduct);
                Inventory.getProductsIdGenerator().getAndIncrement();
                onActionDisplayMainMenu(event);
            }
            } catch (Exception e){
            alertMessage(4);
        }
    }

    /**
      This takes the user back to the main menu after successfully saving a new product object. If further edits need to occur the object is now accessible from main menu.
      @param event the save button is pressed.
      @throws IOException FXMLLoader
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
      Validates minimum value is both greater than zero and less than the maximum. If the value is invalid an alert is displayed.
      @param min Minimum stock for the part.
      @param max Maximum stock for the part.
      @return Boolean indicator representing validity. Valid = true.
     */
    private boolean minValid(int min, int max) {
        boolean isValid = true;
        if (min <= 0 || min >= max) {
            isValid = false;
            alertMessage(5);
        }
        return isValid;
    }

    /**
      Validates inventory level value either equals the min, the max or falls between the two values. If the value is invalid then an error message is displayed.
      @param min The minimum stock level for the part.
      @param max The maximum stock level for the part.
      @param stock Inventory level for the part.
      @return Boolean indicator representing a valid value if true.
     */
    private boolean inventoryValid(int min, int max, int stock) {
        boolean isValid = true;
        if (stock < min || stock > max) {
            isValid = false;
            alertMessage(1);
        }
        return isValid;
    }

    /**
      Various alert messages to be displayed. These messages are displayed where the program would otherwise fail or crash.
      @param alertNumber Alert number/message selector.
     */
    private void alertMessage(int alertNumber) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

        switch (alertNumber) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Min");
                alert.setContentText("Please enter a value greater than 0 but less than the max.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Name Field Empty!");
                alert.setContentText("Please enter a value for the Product Name.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("No Part Selected!");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Issue Adding Product");
                alert.setContentText("Fields left blank or invalid values have been entered.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Inventory");
                alert.setContentText("Please enter a value equal to or between the Min and Max.");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Information");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
        }
    }

    /**
      Cancels product addition and returns user to the main menu. This will not save the product information.
      @param event Cancel is pressed.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discard changes and return to Main Menu?");
        Optional<ButtonType> confirmCancellation = alert.showAndWait();
        if(confirmCancellation.isPresent() && confirmCancellation.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
      Initializes the controller and populates tables. The parts table will match the parts table from the main menu.
      @param url The location used to resolve relative paths for root object or null if location unknown.
      @param resourceBundle The resources used to localize root object or null if it was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addProductAllPartsTable.setItems(Inventory.getAllParts());
        allPartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        allPartsInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        assocPartsInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsPriceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        FilteredList<Part> partFilteredList = new FilteredList<>(getAllParts(), b -> true);

        addProductPartSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            partFilteredList.setPredicate((part -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(part.getPartName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                }
                else if (String.valueOf(part.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            }));
        });

        SortedList<Part> partSortedList = new SortedList<>(partFilteredList);
        partSortedList.comparatorProperty().bind(addProductAllPartsTable.comparatorProperty());
        addProductAllPartsTable.setItems(partSortedList);
    }
}
