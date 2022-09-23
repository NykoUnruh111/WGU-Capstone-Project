package ViewControllers;

/**
 * FXML Add Product Controller class
 *
 * @author Nathaniel Unruh
 */

import Handlers.DatabaseHandler;
import MainPackage.Main;
import Models.Component;
import com.mysql.cj.conf.DatabaseUrlContainer;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPCBController implements Initializable {

    @FXML
    public Button CancelButton;

    @FXML
    public TextField quantityTextField;

    @FXML
    public TableColumn<Component, Integer> addedComponentQTYCol;

    @FXML
    private TextField IDTextField;

    @FXML
    private TextField NameTextField;

    @FXML
    TableView<Component> componentTable;

    @FXML
    TableColumn<Component, Integer> componentIDCol;

    @FXML
    TableColumn<Component, String> componentNameCol;

    @FXML
    TableColumn<Component, Float> componentPriceCol;

    @FXML
    TableView<Component> addedComponentTable;

    @FXML
    TableColumn<Component, Integer> addedComponentIDCol;

    @FXML
    TableColumn<Component, String> addedComponentNameCol;

    @FXML
    TableColumn<Component, Float> addedComponentPriceCol;

    @FXML
    TextField SearchComponentsTextField;

    private ObservableList<Component> visibleComponents = FXCollections.observableArrayList();
    private ObservableList<Component> addedComponentList = FXCollections.observableArrayList();

    /**
     * Initializes the form
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        visibleComponents = DatabaseHandler.allComponentList;

        if (MainFormController.pcbEdit) {

            fillEntries(DatabaseHandler.visiblePCB.get(MainFormController.pcbModifyIndex).getID(), DatabaseHandler.visiblePCB.get(MainFormController.pcbModifyIndex).getName());

        }

        populateTables();

    }

    public void fillEntries(int pcbID, String name) {


        IDTextField.setText(Integer.toString(pcbID));

        NameTextField.setText(name);

        addedComponentList = DatabaseHandler.getPCBComponents(pcbID);

    }

    /**
     * Populate the visibleComponents and addedParts tables
     */
    public void populateTables() {

        //Populate part columns
        componentIDCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getID()).asObject());
        componentNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        //partInvLevelCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        componentPriceCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPrice()).asObject());

        //Populate added part columns
        addedComponentIDCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getID()).asObject());
        addedComponentNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        addedComponentQTYCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        addedComponentPriceCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPrice()).asObject());


        componentTable.setItems(visibleComponents);
        addedComponentTable.setItems(addedComponentList);

    }

    /**
     * Searches the part table using the string from the search text field
     */
    @FXML
    public void searchComponent() {

        if (!SearchComponentsTextField.getText().isEmpty()) {

            DatabaseHandler.visibleComponents = FXCollections.observableArrayList();

            System.out.println("Searching for component!");

            if (Main.isInt(SearchComponentsTextField.getText())) {

                //Search for ID using integer
                System.out.println("Integer");

                for (int i = 0; i < DatabaseHandler.allComponentList.size(); i++) {

                    if (DatabaseHandler.allComponentList.get(i).getID() == Integer.parseInt(SearchComponentsTextField.getText())) {

                        DatabaseHandler.visibleComponents.add(DatabaseHandler.allComponentList.get(i));

                    }

                }

            } else {

                //Search using part name
                for (int i = 0; i < DatabaseHandler.allComponentList.size(); i++) {

                    if (DatabaseHandler.allComponentList.get(i).getName().contains(SearchComponentsTextField.getText())) {

                        DatabaseHandler.visibleComponents.add(DatabaseHandler.allComponentList.get(i));

                    }

                }

            }

            if (DatabaseHandler.visibleComponents.isEmpty()) {

                Main.createAlert("Results", "No results found!");

                DatabaseHandler.visibleComponents = DatabaseHandler.allComponentList;

            }

        } else {

            Main.createAlert("Empty search!", "Nothing in search box!");

            DatabaseHandler.visibleComponents = DatabaseHandler.allComponentList;

        }

        populateTables();

    }

    /**
     * Add component from the component table to the added components table
     */
    @FXML
    public void addComponent() {

        if (componentTable.getSelectionModel().getSelectedIndex() >= 0) {

            if (!quantityTextField.getText().isEmpty()) {
                if (Main.isInt(quantityTextField.getText())) {

                    if (Integer.parseInt(quantityTextField.getText()) >= 1) {

                        //check if component has already been added
                        boolean isDupe = false;
                        for (int i = 0; i < addedComponentList.size(); i++) {

                            if (addedComponentList.get(i).getID() == visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).getID()) {

                                isDupe = true;
                                visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).setQuantity(addedComponentList.get(i).getQuantity() + Integer.parseInt(quantityTextField.getText()));

                                addedComponentList.set(i, visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()));

                                System.out.println("THIS IS A DUPLICATE");

                            }

                        }

                        if (!isDupe) {

                            visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).setQuantity(Integer.parseInt(quantityTextField.getText()));
                            addedComponentList.add(visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()));

                        }

                        //populateTables();

                    } else {

                        Main.createAlert("Error!", "Please enter a number above 0!");

                    }

                } else {

                    Main.createAlert("Error!", "Please only enter numbers for quantity!");

                }


            } else {

                //Display nothing selected alert
                Main.createAlert("Nothing selected!", "There was no item selected to add!");

            }
        } else {

            Main.createAlert("Error!", "Please enter a quantity!");

        }

    }

    /**
     * Remove component from the added components table
     */
    @FXML
    public void removeAssociatedComponents() {

        if (addedComponentTable.getSelectionModel().getSelectedIndex() >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Removal confirmation");
            alert.setHeaderText("Are you sure you want to remove?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                ////////////////
                if (MainFormController.pcbEdit) {

                    DatabaseHandler.deletePCBComponents(Integer.parseInt(IDTextField.getText()), addedComponentList.get(addedComponentTable.getSelectionModel().getSelectedIndex()).getID());

                }

                addedComponentList.remove(addedComponentTable.getSelectionModel().getSelectedIndex());

            } else {

                //Display deletion failure alert
                Main.createAlert("Removal failed!", "The removal was cancelled!");

            }

        } else {

            //Display nothing selected alert
            Main.createAlert("Nothing selected!", "There was no item selected to remove!");

        }

    }

    /**
     * Saves the product and returns to main menu
     * @param event
     * @throws IOException
     */
    @FXML
    public void savePCB(ActionEvent event) throws IOException {

        //Check that the name field is not empty
        if (!NameTextField.getText().isEmpty()) {

            if (MainFormController.pcbEdit) {

                //Do the update modify stuff
                DatabaseHandler.updatePCB(Integer.parseInt(IDTextField.getText()), NameTextField.getText());
                for (int i = 0; i < addedComponentList.size(); i++) {

                    DatabaseHandler.addPCBComponents(Integer.parseInt(IDTextField.getText()), addedComponentList.get(i).getID(), addedComponentList.get(i).getQuantity());

                }

            } else {

                //Do the initial add to db
                DatabaseHandler.addPCB(NameTextField.getText());
                DatabaseHandler.getAllPCB();

                for (int i = 0; i < addedComponentList.size(); i++) {

                    DatabaseHandler.addPCBComponents(DatabaseHandler.allPCBList.get(DatabaseHandler.allPCBList.size() - 1).getID(), addedComponentList.get(i).getID(), addedComponentList.get(i).getQuantity());

                }


            }

            System.out.println("PCB Saved!");

            Stage stage = (Stage) CancelButton.getScene().getWindow();

            stage.close();

            }  else {

            //Create alert if the name field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error!");
            alert.setHeaderText("Name is empty!");

            Optional<ButtonType> result = alert.showAndWait();

        }

    }

    /**
     * Returns to main menu without saving
     * @param event
     * @throws IOException
     */
    @FXML
    public void cancel(ActionEvent event) throws IOException {

        //Return to Main form
        Stage stage = (Stage) CancelButton.getScene().getWindow();

        stage.close();


    }


}
