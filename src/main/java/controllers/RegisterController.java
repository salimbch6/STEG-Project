package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.twilio.Twilio;
import com.twilio.rest.lookups.v1.PhoneNumber;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.UserServices;
import utils.MyDataBase;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView securityImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button imageButton;
    @FXML
    private ImageView profileImageView;
    @FXML
    private ImageView animatedImageView;
    @FXML
    private Label imageLabel;

    private UserServices userService;
    private MyDataBase myDataBase; // Database connection
    FileChooser fileChooser = new FileChooser();
    private File selectedFile;

    private Stage currentStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load animated image
        File animatedFile = new File("target/classes/inscrivezanim.gif");
        Image animatedImage = new Image(animatedFile.toURI().toString());
        animatedImageView.setImage(animatedImage);

        // Initialize label messages to be empty
        registrationMessageLabel.setText("");

        // Instantiate UserService
        userService = new UserServices();

        try {
            // Get instance of database connection
            myDataBase = MyDataBase.getInstance();
        } catch (Exception e) {
            // Handle initialization error
            e.printStackTrace();
            registrationMessageLabel.setText("Error: Unable to initialize database connection.");
        }
    }


    public void registerButtonOnAction(ActionEvent event) {
        if (myDataBase == null) {
            registrationMessageLabel.setText("Error: Database connection not initialized.");
            return;
        }
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String setPassword = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String imageString = imageLabel.getText();
        String number = numberField.getText();


        // Check if any fields are empty
        registrationMessageLabel.setText("");

        // Check if any fields are empty
        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || setPassword.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("Please fill in all fields");
            return; // Exit the method
        }

        // Check if passwords match
        if (!setPassword.equals(confirmPassword)) {
            registrationMessageLabel.setText("Passwords do not match");
            return; // Exit the method
        }

        // Check password length
        if (setPassword.length() < 8) {
            registrationMessageLabel.setText("Password must be at least 8 characters long");
            return; // Exit the method
        }

        // Check password complexity
        if (!isPasswordComplex(setPassword)) {
            registrationMessageLabel.setText("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
            return; // Exit the method
        }

        // Validate email format
        if (!isValidEmail(email)) {
            registrationMessageLabel.setText("Invalid email address");
            return; // Exit the method
        }

        // Check if the username already exists
        try {
            if (userService.isUserExistsByUsername(myDataBase.getconn(), username)) {
                registrationMessageLabel.setText("Username already exists");
                return; // Exit the method
            }
            if (userService.isUserExistsByEmail(myDataBase.getconn(), email)) {
                registrationMessageLabel.setText("Email already exists");
                return; // Exit the method
            }
        } catch (SQLException e) {
            e.printStackTrace();
            registrationMessageLabel.setText("Error: Unable to check username/email availability. Please try again.");
            return; // Exit the method
        }

        // Perform the registration process
        try {
            // Register the user
            userService.registerUser(myDataBase.getconn(), firstname, lastname, username, email, setPassword, imageString,0,number);
            //sendSMS(number);

            // Show success message
            registrationMessageLabel.setText("User registered successfully");

            // Close the register window
            closeRegisterWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException appropriately
            registrationMessageLabel.setText("Error: Unable to register user. Please try again.");
        }
    }

    // Method to validate email format using regular expression
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isPasswordComplex(String password) {
        // Password must contain at least one uppercase letter, one lowercase letter, and one digit
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*");
    }

    private void closeRegisterWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }



    public void closeButtonOnAction(ActionEvent event) {
        // Close the register window
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
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
            //maj
            imageLabel.setText(selectedFile.getName().replaceAll("\\s", ""));
            try {
                Image images = new Image("file:" + selectedFile.getPath().toString());
                profileImageView.setImage(images);
                System.out.println(selectedFile.getPath().toString());
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    /*
    private void sendSMS(String recipientNumber) {
        // Check if recipient number starts with '+'
        if (!recipientNumber.startsWith("+")) {
            // Prepend country code assuming it's missing
            recipientNumber = "+216" + recipientNumber;
        }

        // Initialize Twilio with your account SID and authentication token
        String ACCOUNT_SID = "AC2f151fcc129fb5ee57da3a935c5b897e";
        String AUTH_TOKEN = "c5e63dbe6049d51ba786b1b2072ea522";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // SMS message content
        String message = "Bonjour Mr(s) ,\n"
                + "Nous sommes ravis de vous informer que l'inscription a MRBEAST a été affectué avec succées.\n ";


        // Send SMS using Twilio API
        com.twilio.rest.api.v2010.account.Message twilioMessage = com.twilio.rest.api.v2010.account.Message.creator(
                new com.twilio.type.PhoneNumber(recipientNumber),
                new com.twilio.type.PhoneNumber("+12674335043"), message).create();

        System.out.println("SMS envoyé : " + twilioMessage.getSid());
    }

*/
}

