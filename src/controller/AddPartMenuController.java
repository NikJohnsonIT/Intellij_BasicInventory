package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
  Controller class for the Add Part Menu of the application. Allows the user to create a new part object.
  @author Nicholas Johnson
 */
public class AddPartMenuController implements Initializable {

    Stage stage;
    Parent scene;


    /**
      Radio button for InHouse. Selected by default, selecting the outsourced button will update the text field "Machine ID" to read "Company Name".
     */
    @FXML
    private RadioButton addPartInHouseRBtn;

    /**
      Radio button for Outsourced. Not selected by default, selecting this option will update the text field "Machine ID" to read "Company Name".
     */
    @FXML
    private RadioButton addPartOutsourcedRBtn;

    /**
      Machine ID (InHouse) or Company Name (Outsourced) label. The text will update if the user changes the radio button selection.
     */
    @FXML
    private Label addPartMachIdOrCompLbl;

    /**
      FUTURE ENHANCEMENT
      Text field for Part ID. The user cannot enter or interact with this text field as it is both disabled and set to uneditable. ID numbers are automatically generated with IDs starting at 1.
      The program can reach 9999 parts before needing expanded. This would be another opportunity for future expansion should the need arise.
     */
    @FXML
    private TextField addPartIdTxt;

    /**
      Text field for part name. The user must enter a value. The entry is checked and a message displays should a user leave the field blank.
     */
    @FXML
    private TextField addPartNameTxt;

    /**
      Text field for inventory level of parts. The program performs a check to ensure a valid value is entered. Valid values can equal either the min, the max or fall somewhere between the two.
     */
    @FXML
    private TextField addPartInvTxt;

    /**
      Text field for part price. Represented by a double and current values are assumed to be in USD.
     */
    @FXML
    private TextField addPartPriceCostTxt;


    /**
      Text field for the maximum number of parts. This value represents the max inventory level of a given part.
     */
    @FXML
    private TextField addPartMaxTxt;

    /**
      Text field for the minimum number of parts. This value is checked against the max and must be less than the max value. An error message displays if an invalid value is entered.
     */
    @FXML
    private TextField addPartMinTxt;

    /**
      Text field for the machine ID (InHouse) or Company Name(Outsourced). The prompt for this box changes depending on which radio button is selected.
     */
    @FXML
    private TextField addPartMachineIdOrCompTxt;

    /**
      Saves/adds the new part to the inventory, then loads the Main Menu. Also generates a Unique ID for new part object.
      @param event save button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionSavePart (ActionEvent event) throws IOException {

        try {
            int id = 0;
            for (Part part : Inventory.getAllParts()) {
                if (part.getId() > id)
                    id = (part.getId());
                id = ++id;
            }
            String partName = addPartNameTxt.getText();
            int stock = Integer.parseInt(addPartInvTxt.getText());
            Double partPrice = Double.parseDouble(addPartPriceCostTxt.getText());
            int max = Integer.parseInt(addPartMaxTxt.getText());
            int min = Integer.parseInt(addPartMinTxt.getText());
            int machineId;
            String companyName;


            boolean partAdded = false;

            if (partName.isEmpty()) {
                alertMessage(2);
            } else if ((min < 0) || (min > max)) {
                alertMessage(4);
            } else if (!((min <= stock) && (max >= stock))) {
                alertMessage(1);
            } else {
                try {
                    if (addPartInHouseRBtn.isSelected()) {
                        machineId = Integer.parseInt(addPartMachineIdOrCompTxt.getText());
                        InHouse newInHousePartAdd = new InHouse(id, stock, min, max, partName, partPrice, machineId);
                        Inventory.addPart(newInHousePartAdd);
                        Inventory.getPartsIdGenerator().getAndIncrement();
                        addPartIdTxt.setEditable(false);
                        partAdded = true;
                    }
                } catch (Exception e) {
                    alertMessage(5);
                }
                if (addPartOutsourcedRBtn.isSelected()) {
                    companyName = addPartMachineIdOrCompTxt.getText();
                    Outsourced newOutsourcePartAdd = new Outsourced(id, stock, min, max, partName, partPrice, companyName);
                    addPartIdTxt.setEditable(false);
                    Inventory.addPart(newOutsourcePartAdd);
                    Inventory.getPartsIdGenerator().getAndIncrement();

                    partAdded = true;
                }
                if (partAdded) {
                    onActionDisplayMainMenu(event);
                }
            }
        } catch (Exception e) {
            alertMessage(3);
        }
    }

    /**
      Takes the user back to the main screen. This action is called after the part is successfully saved, thus the part can now be accessed via the main menu.
      @param event Cancel button action.
      @throws IOException FXMLLoader.
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
        }


    /**
      Sets label for Machine ID/Company name to read "Machine ID" when the InHouse radio button is selected. Inhouse is selected by default when the add part menu is loaded.
      @param event the InHouse radio button is selected.
     */
    @FXML
    void onActionSelectInhouseRbtn(ActionEvent event) {
        addPartMachIdOrCompLbl.setText("Machine ID");

    }

    /**
      Cancels part addition and returns user to the main menu. The part will not be saved and any entered data is discarded.
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
      Sets label for the Machine ID/Company name to read "Company Name" when the Outsourced radio button is selected. By default Inhouse is selected, when the user selects the outsourced button the text updates as does the type of part being saved.
      @param event the Outsourced radio button is selected.
     */
    @FXML
    void onActionSelectOutsourcedRbtn(ActionEvent event) {
        addPartMachIdOrCompLbl.setText("Company Name");

    }

    /**
      Checks the min value is a positive number that is less than the max value. If the value is invalid an error message displays alerting the user to their mistake. Upon correction the value is accepted. The user may also choose to cancel the part entry using the cancel button.
      @param min Minimum stock for the parts.
      @param max Maximum stock for the parts.
      @return boolean value representing whether the min value is valid (true).
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
     RUNTIME ERROR
     Validates the inventory level by checking that the stock level is less than the max and more than the min or equal to either value. Initially this part of the program was crashing when the name field was empty. The error said NULL so I knew there had to be a value assigned to the name field.
     This alert window indicates the necessity for a value in the field and allows the program to continue running when before it crashed.
      @param min Minimum stock level for the part.
      @param max Maximum stock level for the part.
      @param stock The stock level of the part.
      @return Boolean value representing whether the inventory level value is valid (true).
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
      RUNTIME ERROR
      The following error messages handle user entries that would otherwise cause runtime errors. Instead of the program crashing an alert window is displayed and the user is given an opportunity to correct invalid values.
      The various error messages concisely explain to users where issues exist that would otherwise crash the program. Rather than crashing the dialog window allows the user to return to the page to make corrections or discard the part using the cancel button.
      @param alertNumber the number representing the different alert messages.
     */
    private void alertMessage(int alertNumber) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertNumber) {
            case 1:
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid Inventory Level");
                alert.setContentText("Inventory level must be between or equal to either the Min and Max.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error!");
                alert.setHeaderText("Issue adding part to inventory");
                alert.setContentText("Please ensure Name field contains a value.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error!");
                alert.setHeaderText("Empty Name Field");
                alert.setContentText("Please enter a Name for the part.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error!");
                alert.setHeaderText("Min value invalid");
                alert.setContentText("Please enter a value that is both greater than 0, and less than the maximum.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error!");
                alert.setHeaderText("Incorrect entry for Machine ID");
                alert.setContentText("Please enter a a valid Machine ID.");
                alert.showAndWait();
                break;
        }
    }


    /**
      Initializes the controller and pre-selects Inhouse radio button. Once the page loads the user is free to change the selection and enter values as needed.
      @param url the location used to resolve relative paths for root objects, or null if location unknown.
      @param rb resources used to localize root object, or null if root object isn't localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        addPartInHouseRBtn.setSelected(true);
    }
}
