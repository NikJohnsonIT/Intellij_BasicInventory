package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
  Controller class providing the logic controls for the modify part menu. Allows the user to modify existing parts.

  @author Nicholas Johnson
 */
public class ModifyPartMenuController implements Initializable{

    /**
      Part object selected from the Main Menu Controller. The information for this part will pre-populate the fields on the modify part menu.
     */
    private Part selectedPart;

    /**
      The Machine ID or Company Name label for the part. Changing the radio button will determine which text is displayed.
     */
    @FXML
    private Label modifyPartMachIdOrCompLbl;

    Stage stage;
    Parent scene;

    /**
      InHouse radio button. Selecting this button causes a text field on the menu to read "Machine ID".
     */
    @FXML
    private RadioButton modifyPartInHouseRBtn;

    /**
      Outsourced radio button. Selecting this button causes a text field on the menu to read "Company Name".
     */
    @FXML
    private RadioButton modifyPartOutsourcedRBtn;

    /**
      Toggle group for radio buttons. Only one radio button can be selected.
     */
    @FXML
    private ToggleGroup partOriginTG;

    /**
      Part ID text field. This is automatically generated and cannot be interacted with by the user.
     */
    @FXML
    private TextField modifyPartIdTxt;

    /**
      Part name text field. This is a name entered by the user and can be used in searches to locate the part.
     */
    @FXML
    private TextField modifyPartNameTxt;

    /**
      Inventory level text field. The stock on hand of a given part.
     */
    @FXML
    private TextField modifyPartInvTxt;

    /**
      Part price text field. The price in USD of the part.
     */
    @FXML
    private TextField modifyPartPriceCostTxt;

    /**
      Part Max stock text field. The maximum number of the part that the retailer wants on hand at any given time.
     */
    @FXML
    private TextField modifyPartMaxTxt;

    /**
      Part min stock text field. The minimum number of parts the retailer wants on hand at any given time.
     */
    @FXML
    private TextField modifyPartMinTxt;

    /**
      Machine ID or Company Name text field. The text changes depending on which radio button is selected at the top of the menu.
     */
    @FXML
    private TextField modifyPartMachineIdOrCompTxt;

    /**
      Updates/replaces part in inventory then loads the Main Menu.
      Text fields will be validated through error messages which prevent invalid data entries.
      @param event Save button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionSaveModPart (ActionEvent event) throws IOException {

        try {
            int id = selectedPart.getId();
            String partName = modifyPartNameTxt.getText();
            int stock = Integer.parseInt(modifyPartInvTxt.getText());
            double price = Double.parseDouble(modifyPartPriceCostTxt.getText());
            int max = Integer.parseInt(modifyPartMaxTxt.getText());
            int min = Integer.parseInt(modifyPartMinTxt.getText());
            int machineId;
            String companyName;
            boolean partAddSuccess = false;

            if (partName.isEmpty()) {
                alertMessage(5);
            } else if ((min < 0) || (min > max)) {
                alertMessage(4);
            } else if (!((min <= stock) && (max >= stock))) {
                alertMessage(2);
            } else {
                try {
                    if (modifyPartInHouseRBtn.isSelected()) {
                        machineId = Integer.parseInt(modifyPartMachineIdOrCompTxt.getText());
                        InHouse newInHousePartAdd = new InHouse(id, stock, min, max, partName, price, machineId);
                        Inventory.addPart(newInHousePartAdd);
                        partAddSuccess = true;
                    }
                } catch (Exception e) {
                    alertMessage(1);
                }
                if (modifyPartOutsourcedRBtn.isSelected()) {
                    companyName = modifyPartMachineIdOrCompTxt.getText();
                    Outsourced newOutsourcedPartAdd = new Outsourced(id, stock, min, max, partName, price, companyName);
                    Inventory.addPart(newOutsourcedPartAdd);
                    partAddSuccess = true;
                }
                if (partAddSuccess) {
                    Inventory.swapPartModifyMenu(selectedPart);
                    onActionReturnToMain(event);
                }
            }
        } catch (Exception e) {
            alertMessage(1);
        }
    }

    /**
      This method saves changes and returns the user to the main menu. Upon returning to the main menu, the changes will be reflected in the table showing parts.
      @param event the save button is clicked.
      @throws IOException FXMLLoader
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
      Cancels part modifications and returns user to the main menu. This will not save any data.
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
      Sets modifyPartMachIdOrComplbl label to "Machine ID" when the inhouse radio button is selected. This allows the user to create an inhouse part and the object will be saved as such.
      @param event InHouse radio button action.
     */
    @FXML
    void onActionSelectInhouseRbtn(ActionEvent event) {
        modifyPartMachIdOrCompLbl.setText("Machine ID");
    }

    /**
      Sets modifyPartMachIdOrComplbl label to "Company Name" when the Outsourced radio button is selected. This allows the user to create an outsourced part and the object will be saved as such.
      @param event Outsourced radio button action.
     */
    @FXML
    void onActionSelectOutsourcedRbtn(ActionEvent event) {
        modifyPartMachIdOrCompLbl.setText("Company Name");

    }

    /**
      Validates minimum value is less than max and more than zero. This ensures the program will continue to run, displaying an alert window when it would otherwise crash. This prompts the user to correct or cancel the changes.
      @param min The minimum value for stock of the part.
      @param max The maximum value for stock of the part.
      @return Boolean value that indicates if min is valid (true).
     */
    private boolean minValid(int min, int max) {
        boolean isValid = true;
        if (min <= 0 || min >= max) {
            isValid = false;
            alertMessage(1);
        }
        return isValid;
    }

    /**
      This validates the stock value falls between the min and max values. If it does not an alert message is displayed where the program would otherwise crash. This allows the user to cancel changes or correct errors.
      @param min The minimum stock for the part.
      @param max The maximum stock for the part.
      @param stock the number of items on hand.
      @return
     */
    private boolean inventoryValid(int min, int max, int stock) {
        boolean isValid = true;
        if (stock < min || stock > max) {
            isValid = false;
            alertMessage(3);
        }
        return isValid;
    }

    /**
      Various Alert types to display. These alerts handle any issues that would otherwise cause errors that would lead the program to failing. These alerts instead allow the user to fix or cancel modifications.
      @param alertNumber Alert message to display.
     */
    private void alertMessage(int alertNumber) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertNumber) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Machine ID");
                alert.setContentText("Machine ID may only contain numbers, please enter a valid value.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Inventory Value");
                alert.setContentText("Inventory level must be between or equal to either the Min or Max.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText("Please enter valid value for all fields.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Min");
                alert.setContentText("Min value must be greater than zero and less and the Max. Please enter a valid value.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText("Name field must contain a valid value.");
                alert.showAndWait();
                break;
        }
    }

    /**
      Initializes controller and populates fields with data from the selected part from the Main Menu. The fields in this menu will pre-populate with information from the part to be modified.
      @param url Location used to resolve relative paths for root object or null if the location is unknown.
      @param rb Resources used to localize root object or null if the object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //the hand-off
        selectedPart = MainMenuController.getPartToModify();

        if (selectedPart instanceof InHouse) {
            modifyPartInHouseRBtn.setSelected(true);
            modifyPartMachIdOrCompLbl.setText("Machine ID");
            modifyPartMachineIdOrCompTxt.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }
        if (selectedPart instanceof Outsourced) {
            modifyPartOutsourcedRBtn.setSelected(true);
            modifyPartMachIdOrCompLbl.setText("Company Name");
            modifyPartMachineIdOrCompTxt.setText(((Outsourced) selectedPart).getCompanyName());
        }
        modifyPartIdTxt.setText(String.valueOf(selectedPart.getId()));
        modifyPartNameTxt.setText(selectedPart.getPartName());
        modifyPartInvTxt.setText(String.valueOf(selectedPart.getStock()));
        modifyPartPriceCostTxt.setText(String.valueOf(selectedPart.getPrice()));
        modifyPartMaxTxt.setText(String.valueOf(selectedPart.getMax()));
        modifyPartMinTxt.setText(String.valueOf(selectedPart.getMin()));

    }
}
