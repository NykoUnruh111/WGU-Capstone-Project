package ViewControllers;

import Handlers.DatabaseHandler;
import Handlers.LoggingHandler;
import MainPackage.Main;
import Models.Component;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * This class handles the logic for the ReportForm screen
 */

public class ReportFormController implements Initializable  {


    public TableView reportTableView;
    public TableColumn<Component, String> reportNameCol;
    public TableColumn<Component, Integer> reportQuantityCol;
    public TableColumn<Component, Float> reportPriceCol;
    public Button CancelButton;
    public Button saveButton;

    public static ObservableList<Component> reportComponents = FXCollections.observableArrayList();

    /**
     * Runs when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        reportComponents = DatabaseHandler.getPCBComponentsReport();

        populateTables();

    }

    /**
     * Populates table
     */
    public void populateTables() {

        //Populate component columns
        reportNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        reportPriceCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPrice() * data.getValue().getQuantity()).asObject());
        reportQuantityCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());

        reportTableView.setItems(reportComponents);

    }

    /**
     * Exits screen
     * @param event
     */
    public void exit(ActionEvent event) {

        Stage stage = (Stage) CancelButton.getScene().getWindow();

        stage.close();

    }

    public void saveReport(ActionEvent event) {

        ObservableList<String> reportList = FXCollections.observableArrayList();

        //Add title of report
        reportList.add("Total Component Report \n");
        reportList.add("Date time of report: " + new Timestamp(System.currentTimeMillis()) + "\n");

        for (int i = 0; i < reportComponents.size(); i++) {

            reportList.add("Component Name: " + reportComponents.get(i).getName()
                    + ", Component Quantity: " + reportComponents.get(i).getQuantity()
                    + ", Total Cost: " + reportComponents.get(i).getPrice() * reportComponents.get(i).getQuantity() + "\n");

        }

        for (int i = 0; i < reportList.size(); i++) {

            LoggingHandler.writeLog(reportList.get(i));

        }

        LoggingHandler.writeLog("\n");

        Main.createAlert("Report Saved!", "Report saved to folder!");

        exit(event);

    }
}
