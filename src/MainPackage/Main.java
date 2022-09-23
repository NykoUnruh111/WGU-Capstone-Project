package MainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ViewControllers/LoginForm.fxml"));
        primaryStage.setTitle("WGU Capstone");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates an alert pop up
     * @param alertHeader
     * @param alertString
     */
    public static void createAlert(String alertHeader, String alertString) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle(alertHeader);
        alert.setHeaderText(alertString);

        Optional<ButtonType> result = alert.showAndWait();

    }

    /**
     * Checks if string is a number
     * @param str
     * @return
     */
    public static boolean isInt(String str) {

        int i = 0;

        if (str == null) {
            return false;
        }

        int strLength = str.length();

        if (strLength == 0) {

            return false;

        }

        if (str.charAt(0) == '-') {

            if (strLength == 1) {
                return false;
            }

            i++;

        }

        for (; i < strLength; i++) {

            char c = str.charAt(i);

            //Skip over '.'
            if (c == '.') {

                //do nothing

            } else if(c < '0' || c > '9') {

                return false;

            }

        }

        return true;

    }
}
