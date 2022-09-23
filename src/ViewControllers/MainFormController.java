package ViewControllers;

/**
 * FXML Main Form Controller class
 *
 * @author Nathaniel Unruh
 */

import Handlers.DatabaseHandler;
import MainPackage.Main;
import Models.Component;
import Models.PCB;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;


import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    public Button mainReportsButton;
    @FXML
    TextField SearchComponentsTextField;

    @FXML
    TextField SearchPCBTextField;

    @FXML
    TableView<Component> componentTable;

    @FXML
    TableColumn<Component, Integer> componentIDCol;

    @FXML
    TableColumn<Component, String> componentNameCol;

    @FXML
    TableColumn<Component, Float> componentPriceCol;

    @FXML
    TableColumn<Component, String> componentTypeCol;

    @FXML
    TableView<PCB> pcbTable;

    @FXML
    TableColumn<PCB, Integer> pcbIDCol;

    @FXML
    TableColumn<PCB, String> pcbNameCol;


    public static int componentModifyIndex = 0;
    public static int pcbModifyIndex = 0;
    public static boolean componentEdit = false;
    public static boolean pcbEdit = false;


    /**
     * Initializes the form
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("IM ALIIIIVE!!!");

        DatabaseHandler.getAllComponents();
        DatabaseHandler.getAllPCB();

        DatabaseHandler.visibleComponents = DatabaseHandler.allComponentList;
        DatabaseHandler.visiblePCB = DatabaseHandler.allPCBList;

        populateTables();


    }

    /**
     * Populates both tables
     */
    public void populateTables() {

        //Populate component columns
        componentIDCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getID()).asObject());
        componentNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        componentPriceCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPrice()).asObject());
        componentTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));


        //Populate pcb columns
        pcbIDCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getID()).asObject());
        pcbNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        pcbTable.setItems(DatabaseHandler.visiblePCB);
        componentTable.setItems(DatabaseHandler.visibleComponents);

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
     * Opens the add part form
     * @param event
     * @throws IOException
     */
    @FXML
    public void addComponent(ActionEvent event) throws IOException {

        componentEdit = false;

        //Open the Add Part form
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(componentTable.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ViewControllers/AddComponentForm.fxml"));

        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.showAndWait();

            DatabaseHandler.getAllComponents();
            DatabaseHandler.getAllPCB();

            populateTables();

        } catch(IOException e) {

            System.out.println("Error: " + e.getMessage());
        }

    }


    /**
     * Opens the modify form for the selected Part
     * @param event
     * @throws IOException
     */
    @FXML
    public void modifyComponent(ActionEvent event) throws IOException {

        if (componentModifyIndex >= 0) {
            componentEdit = true;

            componentModifyIndex = componentTable.getSelectionModel().getSelectedIndex();

            //Open the Add Part form
            Dialog<ButtonType> dialog = new Dialog();
            dialog.initOwner(componentTable.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ViewControllers/AddComponentForm.fxml"));

            try {

                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.showAndWait();

                DatabaseHandler.getAllComponents();
                DatabaseHandler.getAllPCB();

                populateTables();

            } catch (IOException e) {



            }
        } else {

            Main.createAlert("Error!", "Please select a component to modify!");

        }


    }

    /**
     * Delete part from the parts table
     * @param event
     */
    @FXML
    public void deleteComponent(ActionEvent event) {

        if (componentTable.getSelectionModel().getSelectedIndex() >= 0) {

            //Component tempComponent = DatabaseHandler.visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Deletion confirmation");
            alert.setHeaderText("Are you sure you want to delete?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                System.out.println(DatabaseHandler.visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).getName() + " was removed.");
                Main.createAlert("Deleted!", DatabaseHandler.visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).getName() + " has been deleted!");

                DatabaseHandler.deleteComponent(DatabaseHandler.visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).getID());

                DatabaseHandler.getAllComponents();
                DatabaseHandler.getAllPCB();

                populateTables();

            } else {

                //Display deletion failure alert
                Alert deleteFailed = new Alert(Alert.AlertType.ERROR);
                deleteFailed.initModality(Modality.NONE);
                deleteFailed.setTitle("Deletion failed!");
                deleteFailed.setHeaderText("The deletion was cancelled");

                Optional<ButtonType> failResult = deleteFailed.showAndWait();

                System.out.println(DatabaseHandler.visibleComponents.get(componentTable.getSelectionModel().getSelectedIndex()).getName() + " was not removed.");

            }


            System.out.println(componentTable.getSelectionModel().getSelectedIndex());


        } else {

            System.out.println("Nothing selected!");

        }


    }

    /**
     * Searches products in the product table using the string typed into the search field
     */
    @FXML
    public void searchPCB() {

        if (!SearchPCBTextField.getText().isEmpty()) {

            DatabaseHandler.visiblePCB = FXCollections.observableArrayList();

            System.out.println("Searching for part!");

            if (Main.isInt(SearchPCBTextField.getText())) {

                //Search using pcbID
                for (int i = 0; i < DatabaseHandler.allPCBList.size(); i++) {

                    if (DatabaseHandler.allPCBList.get(i).getID() == Integer.parseInt(SearchPCBTextField.getText())) {

                        DatabaseHandler.visiblePCB.add(DatabaseHandler.allPCBList.get(i));

                    }

                }

            } else {

                //Search using pcb name
                for (int i = 0; i < DatabaseHandler.allPCBList.size(); i++) {

                    if (DatabaseHandler.allPCBList.get(i).getName().contains(SearchPCBTextField.getText())) {

                        DatabaseHandler.visiblePCB.add(DatabaseHandler.allPCBList.get(i));

                    }

                }

            }


            if (DatabaseHandler.visiblePCB.isEmpty()) {

                Main.createAlert("Results", "No results found!");

                DatabaseHandler.getAllPCB();

                DatabaseHandler.visiblePCB = DatabaseHandler.allPCBList;

            }

        } else {

            Main.createAlert("Empty search!", "Nothing in search box!");

            DatabaseHandler.getAllPCB();

            DatabaseHandler.visiblePCB = DatabaseHandler.allPCBList;

        }

        populateTables();

    }

    /**
     * Opens the add product form to add a new product to the product table
     * @param event
     * @throws IOException
     */
    @FXML
    public void addPCB(ActionEvent event) throws IOException {

        pcbEdit = false;

        //Open the Add Part form
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(componentTable.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ViewControllers/AddPCBForm.fxml"));

        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.showAndWait();

            DatabaseHandler.getAllComponents();
            DatabaseHandler.getAllPCB();

            populateTables();

        } catch(IOException e) {

            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Opens the modify product form for the selected product from product table
     * @param event
     * @throws IOException
     */
    @FXML
    public void modifyPCB(ActionEvent event) throws IOException {

        if (pcbTable.getSelectionModel().getSelectedIndex() >= 0) {
            pcbEdit = true;

            pcbModifyIndex = pcbTable.getSelectionModel().getSelectedIndex();

            //Open the Add Part form
            Dialog<ButtonType> dialog = new Dialog();
            dialog.initOwner(componentTable.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ViewControllers/AddPCBForm.fxml"));

            try {

                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.showAndWait();

                DatabaseHandler.getAllComponents();
                DatabaseHandler.getAllPCB();

                populateTables();

            } catch(IOException e) {

                System.out.println("Error: " + e.getMessage());
            }

        } else {

            Main.createAlert("Error!", "Please select a PCB to modify!");

        }
    }

    /**
     * Deletes selected product from the product table
     * @param event
     */
    @FXML
    public void deletePCB(ActionEvent event) {

        if (pcbTable.getSelectionModel().getSelectedIndex() >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Deletion confirmation");
            alert.setHeaderText("Are you sure you want to delete?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                Main.createAlert("Deleted!", DatabaseHandler.visiblePCB.get(pcbTable.getSelectionModel().getSelectedIndex()).getName() + " has been deleted!");


                ////////////////////
                DatabaseHandler.deletePCB(DatabaseHandler.visiblePCB.get(pcbTable.getSelectionModel().getSelectedIndex()).getID());

                DatabaseHandler.getAllPCB();

                populateTables();


            } else {

                //Display deletion failure alert
                Alert deleteFailed = new Alert(Alert.AlertType.ERROR);
                deleteFailed.initModality(Modality.NONE);
                deleteFailed.setTitle("Deletion failed!");
                deleteFailed.setHeaderText("The deletion was cancelled");

                Optional<ButtonType> failResult = deleteFailed.showAndWait();

                System.out.println(DatabaseHandler.visiblePCB.get(pcbTable.getSelectionModel().getSelectedIndex()).getName() + " was not removed.");

            }


            System.out.println(componentTable.getSelectionModel().getSelectedIndex());


        } else {

            System.out.println("Nothing selected!");

        }

    }

    /**
     * Called every time the user clicks the component table
     * @param mouseEvent
     */
    public void handleComponentTableClick(MouseEvent mouseEvent) {

        if (componentTable.getSelectionModel().getSelectedIndex() != -1) {

            componentModifyIndex = componentTable.getSelectionModel().getSelectedIndex();
            //DatabaseHandler.getAppointmentList(DatabaseHandler.customerList.get(currentlySelectedCustomerIndex).getCustomerID());

        }

    }

    /**
     * Called every time the user clicks the PCB table
     * @param mouseEvent
     */
    public void handlePCBTableClick(MouseEvent mouseEvent) {

        if (pcbTable.getSelectionModel().getSelectedIndex() != -1) {

            pcbModifyIndex = pcbTable.getSelectionModel().getSelectedIndex();
            //DatabaseHandler.getAppointmentList(DatabaseHandler.customerList.get(currentlySelectedCustomerIndex).getCustomerID());

        }

    }

    /**
     * Exits the program
     */
    @FXML
    public void exitProgram() {

        System.exit(0);

    }

    /**
     * Open reports screen
     * @param event
     */
    public void openReports(ActionEvent event) {

        //Open the Add Part form
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(componentTable.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ViewControllers/ReportForm.fxml"));

        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.showAndWait();

        } catch(IOException e) {

            System.out.println("Error: " + e.getMessage());
        }

    }
}
