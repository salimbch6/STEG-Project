package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;
import utils.Session;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class clientInterfaceController implements Initializable {

    @FXML
    private Hyperlink signoutLink;
    @FXML
    private Circle clientProfilePic;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField numberField;
    @FXML
    private Button editProfileButton;
    @FXML
    private Button imageButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label imageLabel;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label valideditLabel;

    private UserServices userServices;
    private User u;
    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UserServices
        userServices = new UserServices();

        // Get current user data from session
        u = userServices.getUserById_Account(Session.getAccount_id());
        initData(u);
    }

    public void initData(User user) {
        u = user;

        // Populate the form fields with user data
        firstnameTextField.setText(u.getFirstname());
        lastnameTextField.setText(u.getLastname());
        usernameTextField.setText(u.getUsername());
        emailTextField.setText(u.getEmail());
        numberField.setText(u.getNumber());
       // imageLabel.setText(u.getProfilePic());

        // Load user profile image

    }

    @FXML
    public void saveButtonOnAction(ActionEvent actionEvent) {
        // Validate input fields
        if (!validateFields()) {
            return;
        }

        // Update user data
        u.setFirstname(firstnameTextField.getText());
        u.setLastname(lastnameTextField.getText());
        u.setUsername(usernameTextField.getText());
        u.setEmail(emailTextField.getText());
        u.setPassword(setPasswordField.getText());
        u.setPassword(confirmPasswordField.getText());
       // u.setProfilePic(imageLabel.getText());
        u.setNumber(numberField.getText());

        // Update user data in the database
        try {
            UserServices userServices = new UserServices();
            userServices.updateUser(MyDataBase.getInstance().getconn(), u);

            // Show confirmation message or handle successful update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        // Implement field validation
        // ...
        return true;
    }

    @FXML
    private void imageOnMouseClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.JPG", "*.gif"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageLabel.setText(selectedFile.getName().replaceAll("\\s", ""));
            try {
                Image images = new Image("file:" + selectedFile.getPath());
                profileImageView.setImage(images);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid image file: " + e.getMessage());
            }
        }
    }

    @FXML
    public void signoutOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) signoutLink.getScene().getWindow();
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
}
