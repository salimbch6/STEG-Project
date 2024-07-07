package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class editProfileController implements Initializable {
    @FXML
    private Label editProfileLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editProfileLabel.setText("Welcome to the Edit Profile Page!");
    }
}
