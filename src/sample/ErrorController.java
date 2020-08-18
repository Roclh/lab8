package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.ErrorManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea errorPane;

    @FXML
    private Button okButton;

    @FXML
    void initialize() {
        errorPane.setEditable(false);
        errorPane.setText(Main.ErrorMessage + "\n\n" + Main.ErrorTrace);

        okButton.setOnAction(event -> {
            Stage stage = (Stage)okButton.getScene().getWindow();
            stage.close();
        });

    }

}