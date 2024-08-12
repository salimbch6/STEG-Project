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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;
import utils.Session;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private ImageView profileImageView;
    @FXML
    private Label valideditLabel;

    private UserServices userServices;
    private User u;
    private File selectedFile; // Store the selected file here

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

        // Load user profile image
        if (u.getProfilePic() != null && !u.getProfilePic().isEmpty()) {
            Image image = new Image("file:" + System.getProperty("user.home") + File.separator + u.getProfilePic());
            profileImageView.setImage(image);
        }
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
        if (selectedFile != null) {
            u.setProfilePic(selectedFile.getName()); // Store only the file name
        }
        u.setNumber(numberField.getText());

        // Update user data in the database
        try {
            UserServices userServices = new UserServices();
            userServices.updateUser(MyDataBase.getInstance().getconn(), u);

            // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise à jour du profil");
            alert.setHeaderText(null);
            alert.setContentText("Votre profil a été modifié avec succès.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de mise à jour");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour de votre profil. Veuillez réessayer.");
            alert.showAndWait();
        }
    }

    private boolean validateFields() {
        // Clear previous error messages
        valideditLabel.setText("");

        // Validate first name
        if (firstnameTextField.getText().isEmpty()) {
            valideditLabel.setText("Veuillez entrer votre prénom.");
            return false;
        }

        // Validate last name
        if (lastnameTextField.getText().isEmpty()) {
            valideditLabel.setText("Veuillez entrer votre nom de famille.");
            return false;
        }

        // Validate username
        if (usernameTextField.getText().isEmpty()) {
            valideditLabel.setText("Veuillez entrer votre nom d'utilisateur.");
            return false;
        }

        // Validate email
        if (emailTextField.getText().isEmpty() || !isValidEmail(emailTextField.getText())) {
            valideditLabel.setText("Veuillez entrer une adresse e-mail valide.");
            return false;
        }

        // Validate password fields
        if (setPasswordField.getText().isEmpty()) {
            valideditLabel.setText("Veuillez entrer un mot de passe.");
            return false;
        }
        if (!setPasswordField.getText().equals(confirmPasswordField.getText())) {
            valideditLabel.setText("Les mots de passe ne correspondent pas.");
            return false;
        }

        // Validate phone number
        if (numberField.getText().isEmpty() || !isValidPhoneNumber(numberField.getText())) {
            valideditLabel.setText("Veuillez entrer un numéro de téléphone valide.");
            return false;
        }

        // All validations passed
        valideditLabel.setText(""); // Clear any previous validation messages
        return true;
    }

    private boolean isValidEmail(String email) {
        // Simple regex for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regex for phone number validation (exactly 8 digits)
        String phoneNumberRegex = "^\\d{8}$";
        return phoneNumber.matches(phoneNumberRegex);
    }

    @FXML
    private void imageOnMouseClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.JPG", "*.gif"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage); // Store the selected file
        if (selectedFile != null) {
            try {
                Image image = new Image("file:" + selectedFile.getPath());
                profileImageView.setImage(image);
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
