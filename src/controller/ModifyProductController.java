package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static model.Inventory.*;

/**
  This is a class that provides control logic for the modify Product screen. Here products can be modified and updated to suit the business needs.
  @author Nicholas Johnson
 */
public class ModifyProductController implements Initializable{

    Stage stage;
    Parent scene;
    int index;
    /**
      Product object selected from the Main Menu. This will be the product that is modified.
     */
    Product selectedProduct;

    /**
      List of parts associated with the product. The list will be empty if there are no associated parts, and must be empty for product deletion to occur.
     */
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    /**
      All parts table view. This contains all parts and will appear identical to the parts table from the main menu.
     */
    @FXML
    private TableView<Part> modifyProductAllPartsTbl;

    /**
      Column for Part IDs for the all parts table. These are automatically generated and can be used in searches to find parts.
     */
    @FXML
    private TableColumn<Part, Integer>allPartsPartIdCol;

    /**
      Column for Part Names for the all parts table. These can also be used in part searches, multiple matches will return where applicable.
     */
    @FXML
    private TableColumn<Part, String>allPartsPartNameCol;

    /**
      Column for the inventory level for the all parts table. This value is checked to ensure valid entries.
     */
    @FXML
    private TableColumn<Part, Integer>allPartsInvLvlCol;

    /**
      Column for the price for the all parts table. Current prices are in USD.
     */
    @FXML
    private TableColumn<Part, Double>allPartsPriceCostPerUnitCol;

    /**
      Table view for the associated parts. These are the parts associated with the product and must be empty if a user needs to delete a product.
     */
    @FXML
    private TableView<Part> modifyProductAssocPartsTbl;

    /**
      Column for Part IDs of associated parts. These ID are unique and can be used to search for the parts.
     */
    @FXML
    private TableColumn<Part, Integer> assocPartsPartIdCol;

    /**
      Column for Part Names of associated parts. The names of parts can be searched in the search fields, where applicable multiple results will appear.
     */
    @FXML
    private TableColumn<Part, String> assocPartsPartNameCol;

    /**
      Column for the inventory levels of associated parts.
     */
    @FXML
    private TableColumn<Part, Integer> assocPartsInvLvlCol;

    /**
      Column for the price of associated parts. Current prices are in USD.
     */
    @FXML
    private TableColumn<Part, Double> assocPartsPriceCostPerUnitCol;

    /**
      Product ID text field. Similar to part ID these are unique and automatically generated.
     */
    @FXML
    private TextField modifyProductIdTxt;

    /**
      Product name text field. The names of a product as well as the ID can be used to search for them.
     */
    @FXML
    private TextField modifyProductNameTxt;

    /**
      Product inventory level text field. The current stock level of the product, the value is checked to ensure valid entries.
     */
    @FXML
    private TextField modifyProductInvTxt;

    /**
      Product price text field. Current prices are in USD.
     */
    @FXML
    private TextField modifyProductPriceTxt;

    /**
      Product max inventory level text field. The max stock for the product that the retailer wants on hand at a given time.
     */
    @FXML
    private TextField modifyProductMaxTxt;

    /**
      Product min inventory level text field. The minimum number of the product the retailer needs on hand.
     */
    @FXML
    private TextField modifyProductMinTxt;

    /**
      Part search text field. ID or name can be used to search for parts to be added to a given product.
     */
    @FXML
    private TextField modifyProductPartSearch;

    /**
      Adds part object selected from all parts table to the associated parts table.In order to delete this product the user will need to remove these associated parts.
      If no part is selected then an error message displays.
      @param event add part button pressed.
     */
    @FXML
    void onActionAddPartToProduct (ActionEvent event) {
        Part selectedPart = modifyProductAllPartsTbl.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            alertMessage(4);
        }
        else {
            assocParts.add(selectedPart);
            modifyProductAssocPartsTbl.setItems(assocParts);
        }
    }

    /**
      Displays dialog asking the user to confirm the removal of a selected part from the associated parts table.
      If no part is selected and error message displays.
      @param event Remove button action.
     */
    @FXML
    void onActionRemovePartFromProduct (ActionEvent event) {
        Part selectedPart =  modifyProductAssocPartsTbl.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            alertMessage(4);
        }
        else if (selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setContentText("You are about to remove this associated part. Continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                modifyProductAssocPartsTbl.setItems(assocParts);
            }
        }
    }

    /**
      Searches based off user-input value to search bar, refreshing the parts table view with the results.
      Searches can be done using either ID or name.
     *@param event Part search button action.
     */
    @FXML
    void modifyProductSearch(ActionEvent event) {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = modifyProductPartSearch.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) || part.getPartName().contains(searchString)) {
                partsFound.add(part);
            }
        }
        modifyProductAllPartsTbl.setItems(partsFound);
        if (partsFound.size() == 0) {
            alertMessage(3);
        }
    }

    /**
      When search field is cleared the parts list will refresh. The user must press enter in the empty field to refresh the list.
      @param event Parts search text field key pressed.
     */
    @FXML
    void searchKeyPressed(KeyEvent event) {
        if (modifyProductPartSearch.getText().isEmpty()) {
            modifyProductAllPartsTbl.setItems(Inventory.getAllParts());
        }
    }

    /**
      Various alert messages to be displayed. These are displayed where the program would otherwise encounter errors. This allows the user to correct or cancel any modifications that are invalid.
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
                alert.setHeaderText("Issue Modifying Product");
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
      This method saves changes and returns the user to the main menu. The modified product will now appear in the products table of the main menu.
      @param event teh save button is clicked.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
      Cancels product modifications and returns user to the main menu. A confirmation window is displayed to ensure the user did not accidentally press the button.
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
      Replace the product in inventory and return to Main Menu. The newly modified part will now appear in the products table on the main menu.
      @param event Save button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionSaveProduct (ActionEvent event) throws IOException {


        try {
            int id = selectedProduct.getId();
            String productName = modifyProductNameTxt.getText();
            Double price = Double.parseDouble(modifyProductPriceTxt.getText());
            int stock = Integer.parseInt(modifyProductInvTxt.getText());
            int min = Integer.parseInt(modifyProductMinTxt.getText());
            int max = Integer.parseInt(modifyProductMaxTxt.getText());

            if (productName.isEmpty()) {
                alertMessage(6);
            } else if ((min < 0) || (min > max)) {
                alertMessage(1);
            } else if (!((min <= stock) && (max > stock))) {
                alertMessage(5);


            } else {
                Product newProduct = new Product(id, productName, price, stock, min, max);
                for (Part part : assocParts) {
                    newProduct.addAssociatedPart(part);
                }
                Inventory.addProduct(newProduct);
                Inventory.swapProductModifyMenu(selectedProduct);
                onActionReturnToMain(event); //Is this the issue?
            }
        } catch (Exception e) {
            alertMessage(4);
        }
    }

    /**
      Validation that min is greater than 0 and also less than the max. If an invalid value is entered an alert message allowing the user to go back and make corrections.
      @param min the min value.
      @param max the max value.
      @return boolean indicator of the validity.
     */
    private boolean minValid(int min, int max) {
        boolean isValid = true;
        if(min <= 0 || min >= max) {
            isValid = false;
            alertMessage(5);
        }
        return isValid;
    }

    /**
      Validation that the inventory is less than the max, more than the min, or equal to one of the two values. If invalid an alert message wars the user allowing them to go back and make corrections.
      @param min Minimum stock for the part.
      @param max Maximum, stock for the part.
      @param stock Inventory level of the part.
      @return Boolean value indicating if the value is valid (true).
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
      Initializes the controller and populates the text fields with product data selected from Main Menu. The text fields contain information already assigned to the product. The two tables contain all the parts and a list of parts associated with the selected product.
      @param url Location to be used to resolve relative paths for root object or null of the location is unknown.
      @param resourceBundle Resources used to localize root object or null if the object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedProduct = MainMenuController.getProductToModify();
        assocParts = selectedProduct.getAllAssociatedParts();

        modifyProductAllPartsTbl.setItems(Inventory.getAllParts());
        allPartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        allPartsInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //associated parts table, is this where I use the filteredParts method? red flagged
        modifyProductAssocPartsTbl.setItems(assocParts);
        assocPartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        assocPartsInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsPriceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        FilteredList<Part> partFilteredList = new FilteredList<>(getAllParts(), b -> true);
        modifyProductPartSearch.textProperty().addListener((observable, oldValue, newValue) -> {
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
        SortedList<Part> sortedParts = new SortedList<>(partFilteredList);
        sortedParts.comparatorProperty().bind(modifyProductAllPartsTbl.comparatorProperty());
        modifyProductAllPartsTbl.setItems(sortedParts);

        selectedProduct = MainMenuController.getProductToModify();
        assocParts = selectedProduct.getAllAssociatedParts();
        modifyProductAssocPartsTbl.setItems(assocParts);

        modifyProductIdTxt.setText(String.valueOf(selectedProduct.getId()));
        modifyProductNameTxt.setText(selectedProduct.getProductName());
        modifyProductInvTxt.setText(String.valueOf(selectedProduct.getStock()));
        modifyProductPriceTxt.setText(String.valueOf(selectedProduct.getPrice()));
        modifyProductMaxTxt.setText(String.valueOf(selectedProduct.getMax()));
        modifyProductMinTxt.setText(String.valueOf(selectedProduct.getMin()));
    }
}
