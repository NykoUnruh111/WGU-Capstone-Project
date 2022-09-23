package ViewControllers;

/**
 * FXML Add Part Controller class
 *
 * @author Nathaniel Unruh
 */

import Handlers.DatabaseHandler;
import MainPackage.Main;
import Models.ThroughHole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddComponentController implements Initializable {

    @FXML
    private TextField IDTextField;

    @FXML
    private TextField NameTextField;


    @FXML
    private TextField PriceTextField;

    @FXML
    private RadioButton throughHoleRadioButton;
    @FXML
    private RadioButton SMDRadioButton;

    @FXML
    private Button CancelButton;

    private boolean throughHole = true;

    /**
     * Radio button code for if the inhouse radio button is selected
     */
    @FXML
    public void throughHoleSelected() {

        //De-select opposite radio button
        SMDRadioButton.setSelected(false);
        throughHoleRadioButton.setSelected(true);

        //Set boolean
        throughHole = true;

    }

    /**
     * Radio button code for if the outsourced radio button is selected
     */
    @FXML
    public void SMDSelected() {

        //De-select opposite radio button
        throughHoleRadioButton.setSelected(false);
        SMDRadioButton.setSelected(true);
        //Set boolean
        throughHole = false;

    }

    /**
     * Saves the part and returns to main menu
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void savePart(ActionEvent event) throws IOException {

        //Check that the name field is not empty
        if (!NameTextField.getText().isEmpty()) {

            //Check that the price field is not empty and is greater than 0
            if (!PriceTextField.getText().isEmpty() && Main.isInt(PriceTextField.getText()) && Double.parseDouble(PriceTextField.getText()) > 0) {

                if (MainFormController.componentEdit) {


                    //Add database entry for throughole
                    //Inventory.addPart(new InHouse(MainFormController.partID, NameTextField.getText(), Double.parseDouble(PriceTextField.getText()), Integer.parseInt(InvTextField.getText()), Integer.parseInt(MinTextField.getText()), Integer.parseInt(MaxTextField.getText()), Integer.parseInt(machineCompanyTextField.getText())));

                    DatabaseHandler.updateComponent(Integer.parseInt(IDTextField.getText()), NameTextField.getText(), Float.parseFloat(PriceTextField.getText()), throughHole);

                    Stage stage = (Stage) CancelButton.getScene().getWindow();

                    stage.close();

                } else {

                    DatabaseHandler.addComponent(NameTextField.getText(), Float.parseFloat(PriceTextField.getText()), throughHole);

                    Stage stage = (Stage) CancelButton.getScene().getWindow();

                    stage.close();

                }


            }  else {

                //Create alert if price is below 0
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error!");
                alert.setHeaderText("Price must be a number and above 0!");

                Optional<ButtonType> result = alert.showAndWait();


            }

        } else {

            //Create alert if the name field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error!");
            alert.setHeaderText("Name is empty!");

            Optional<ButtonType> result = alert.showAndWait();

        }


    }

    public void fillEntries(int componentID, String name, float price, boolean throughHole) {

        if (throughHole) {

            throughHoleSelected();

        } else {

            SMDSelected();

        }

        IDTextField.setText(Integer.toString(componentID));

        NameTextField.setText(name);
        PriceTextField.setText(Float.toString(price));



    }


    /**
     * Returns to main menu without saving
     * @param event
     * @throws IOException
     */
    @FXML
    public void cancel(ActionEvent event) throws IOException {

        Stage stage = (Stage) CancelButton.getScene().getWindow();

        stage.close();

    }

    /**
     * Initializes and checks if the page will be blank for a new component or if the entries will be filled for editing an existing component
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        if (MainFormController.componentEdit) {

            boolean throughHole;

            if (DatabaseHandler.visibleComponents.get(MainFormController.componentModifyIndex) instanceof ThroughHole) {

                throughHole = true;

            } else {

                throughHole = false;

            }

            fillEntries(DatabaseHandler.visibleComponents.get(MainFormController.componentModifyIndex).getID(), DatabaseHandler.visibleComponents.get(MainFormController.componentModifyIndex).getName(), DatabaseHandler.visibleComponents.get(MainFormController.componentModifyIndex).getPrice(), throughHole);

        }



    }
}
