package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class updateController implements Initializable {

    @FXML
    private ImageView updateImageView;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField numberField;
    @FXML
    private Button imageButton;
    @FXML
    private Label imageLabel;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Label registrationMessageLabel;



    private User selectedUser;
    private File selectedFile;
    private UserServices userServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File updateFile = new File("target/classes/updateanim.gif");
        Image updateImage = new Image(updateFile.toURI().toString());
        updateImageView.setImage(updateImage);
        userServices = new UserServices(); // Instantiate UserServices
        registrationMessageLabel.setText("");
        confirmPasswordLabel.setText("");
    }

    // Method to receive selected user's data from crudController
    public void initData(User user) throws MalformedURLException {
        selectedUser = user;
        URL imageUrl;
        // Populate the form fields with selected user's data
        firstnameTextField.setText(selectedUser.getFirstname());
        lastnameTextField.setText(selectedUser.getLastname());
        usernameTextField.setText(selectedUser.getUsername());
        emailTextField.setText(selectedUser.getEmail());
        //setPasswordField.setText(selectedUser.getPassword());
        //confirmPasswordField.setText(selectedUser.getPassword());
        imageLabel.setText(user.getProfilePic());
        numberField.setText(selectedUser.getNumber());
        try {
            imageUrl = new URL("http://localhost/images/"+user.getProfilePic());
            System.out.println(imageUrl);
            Image images = new Image(imageUrl.toString());
            profileImageView.setImage(images);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Password fields can be left empty for security reasons
    }
    private boolean validateFields() {
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String setPassword = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String number = numberField.getText();


        // Check if any fields are empty
        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() ||
                email.isEmpty() || number.isEmpty() ||setPassword.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("Please fill in all fields");
            return false;
        }

        // Check if passwords match
        if (!setPassword.equals(confirmPassword)) {
            confirmPasswordLabel.setText("Passwords do not match");
            return false;
        }

        // Check password length
        if (setPassword.length() < 8) {
            confirmPasswordLabel.setText("Password must be at least 8 characters long");
            return false;
        }

        // Check password complexity
        if (!isPasswordComplex(setPassword)) {
            confirmPasswordLabel.setText("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
            return false;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            confirmPasswordLabel.setText("Invalid email address");
            return false;
        }

        // Clear error messages if validation passes
        registrationMessageLabel.setText("");
        confirmPasswordLabel.setText("");

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isPasswordComplex(String password) {
        // Password must contain at least one uppercase letter, one lowercase letter, and one digit
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*");
    }

    // Method to display an error message for a specific field
    private void displayErrorMessage(Label label, String message) {
        label.setText(message);
    }

    @FXML
    public void saveButtonOnAction(ActionEvent actionEvent) {
        // Validate input fields
        if (!validateFields()) {
            return; // Exit the method if validation fails
        }

        // Update selected user's data with the modified data from the form
        selectedUser.setFirstname(firstnameTextField.getText());
        selectedUser.setLastname(lastnameTextField.getText());
        selectedUser.setUsername(usernameTextField.getText());
        selectedUser.setEmail(emailTextField.getText());
        selectedUser.setPassword(setPasswordField.getText());
        selectedUser.setPassword(confirmPasswordField.getText());
        selectedUser.setProfilePic(imageLabel.getText());
        selectedUser.setNumber(numberField.getText());

        // Get database connection
        Connection connectDB = MyDataBase.getInstance().getconn();
        String htdocsPath = "C:/xampp/htdocs/images/";
        File destinationFile = new File(htdocsPath + imageLabel.getText().replaceAll("\\s", ""));
        if(selectedFile!=null){
            try (InputStream in = new FileInputStream(selectedFile);
                 OutputStream out = new FileOutputStream(destinationFile)) {
                byte[] buf = new byte[8192];
                int length;
                while ((length = in.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }} catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


        // Update user data in the database
        try {
            UserServices userServices = new UserServices(); // Instantiate UserServices
            userServices.updateUser(connectDB, selectedUser); // Update user data

            // Close the update window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

            // Load the crud.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminDash.fxml"));
            Parent root = loader.load();

            // Create a new stage for the crud.fxml window
            Stage crudStage = new Stage();
            crudStage.setScene(new Scene(root));
            crudStage.setTitle("CRUD Interface");

            // Show the crud.fxml window
            crudStage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle any exceptions
        }
    }


    @FXML
    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            // Close the current window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

            // Load the crud.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crud.fxml"));
            Parent root = loader.load();

            // Create a new stage for the crud.fxml window
            Stage crudStage = new Stage();
            crudStage.setScene(new Scene(root));
            crudStage.setTitle("CRUD Interface");

            // Show the crud.fxml window
            crudStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                Image images = new Image("file:"+selectedFile.getPath().toString());
                profileImageView.setImage(images);
                System.out.println(selectedFile.getPath().toString());
            } catch (Exception ex) {
                System.out.println(ex);
            }

        }
    }
}