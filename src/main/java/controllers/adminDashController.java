package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.Session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class adminDashController implements Initializable {
    @FXML
    private Circle imageView;
    @FXML
    private AnchorPane adminContentArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button acceuilBtn;

    @FXML
    private Button gdrBtn;

    @FXML
    private Button gduBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button signoutBtn;
    private UserServices userServices;
    URL imageUrl;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
// Instantiate UserServices
        userServices = new UserServices();
        User u = userServices.getUserById_Account(Session.getAccount_id()); //star bech tekhedh user mel session
        usernameLabel.setText(u.getUsername());
        try {
            imageUrl = new URL("http://localhost/images/" + u.getProfilePic());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image images = new Image(imageUrl.toString());
        imageView.setFill(new ImagePattern(images));

        acceuilBtn.setOnAction(event -> loadPage("/homeAdmin.fxml"));
        gduBtn.setOnAction(event -> loadPage("/crud.fxml"));
        gdrBtn.setOnAction(event -> loadPage("/recAdmin.fxml"));
        settingsBtn.setOnAction(event -> loadPage("/settingsAdmin.fxml"));
        signoutBtn.setOnAction(event -> signout());



    }

    @FXML
    private void signout() {
        try {
            Stage stage = (Stage) signoutBtn.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");

            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(page));
            adminContentArea.getChildren().clear();
            adminContentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
