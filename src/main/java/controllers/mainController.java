package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.Session;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    @FXML
    private Circle imageView;
    @FXML
    private ImageView likeImageView;

    @FXML
    private Label usernameLabel;
    @FXML
    private Button editpbtn;
    @FXML
    private Button homebtn;
    @FXML
    private Button recbtn;
    @FXML
    private Button signoutbtn;

    @FXML
    private AnchorPane contentArea;

    private User currentUser;
    private UserServices userServices;

    URL imageUrl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File likeFile = new File("target/classes/clientDash.gif");
        Image likeImage = new Image(likeFile.toURI().toString());
        likeImageView.setImage(likeImage);

        // Initialize UserServices
        userServices = new UserServices();
        // Get current user data
        currentUser = userServices.getUserById_Account(Session.getAccount_id());

        usernameLabel.setText(currentUser.getUsername());
        try {
            imageUrl = new URL("http://localhost/images/" + currentUser.getProfilePic());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image images = new Image(imageUrl.toString());
        imageView.setFill(new ImagePattern(images));

        // Set up button actions
        homebtn.setOnAction(event -> loadPage("/home.fxml"));
        recbtn.setOnAction(event -> loadPage("/reclamations.fxml"));
        editpbtn.setOnAction(event -> loadEditProfile());
        signoutbtn.setOnAction(event -> signoutOnAction());

    }

    @FXML
    public void signoutOnAction() {
        try {
            Stage stage = (Stage) signoutbtn.getScene().getWindow();
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

    private void loadEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientInterface.fxml"));
            Parent root = loader.load();
            clientInterfaceController clientController = loader.getController();
            clientController.initData(currentUser);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(page));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
