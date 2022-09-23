package ViewControllers;

import Handlers.DatabaseHandler;
import MainPackage.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class handles all the logic for the initial login screen
 */
public class LoginFormController implements Initializable {


    public Text loginUsernameLabel;
    public Text loginPasswordLabel;

    public TextField loginUsernameTextField;
    public TextField loginPasswordTextField;

    public Button loginButton;
    public Text loginLocationLabel;
    public Text loginUsersLocationText;

    ZoneId zone;

    /**
     * Handle login button
     * @param event
     */
    public void handleLoginButton(ActionEvent event) throws IOException {

        DatabaseHandler.dbConnect();
        boolean loggedIn = DatabaseHandler.login(loginUsernameTextField.getText().toString(), loginPasswordTextField.getText().toString());


        if (loggedIn) {

            DatabaseHandler.currentUser = loginUsernameTextField.getText().toString();
            //Do stuff when logged in
            System.out.println("Logged in successfully!");
            Parent p = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            Scene scene = new Scene(p);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        } else {

            Main.createAlert("Error!", "Incorrect username or password.");

        }
    }

    /**
     * I
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {





    }
}
