package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;


import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;
import utils.Session;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

import static services.UserServices.hashPassword;

public class userController implements Initializable {
    @FXML
    private Label captchaLabel;
    @FXML
    private TextField captchaTextField;

    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;

    @FXML
    private PasswordField enterPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;

    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView captchaImageView;
    @FXML
    private ImageView stegImageView;
    @FXML
    private Hyperlink registerLink;
    @FXML
    private Hyperlink forgetPasswordLink;
    @FXML
    private CheckBox showpasswordCheckBox;
    @FXML
    private TextField showpasswordField;

    private UserServices userServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("target/classes/steganim.gif");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
        File captchaFile = new File("target/classes/captcha.png");
        Image captchaImage = new Image(captchaFile.toURI().toString());
        captchaImageView.setImage(captchaImage);
        File logoFile = new File("target/classes/steglogo2.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        stegImageView.setImage(logoImage);



        registerLink.setText("Don't have an account? Register here");
        forgetPasswordLink.setText("Forget your Password ?");
        showpasswordField.setVisible(false);
        enterPasswordField.setVisible(true);


        // Instantiate UserServices
        userServices = new UserServices();
        afficher();
    }

    @FXML
    private void showPassword() {
       if(showpasswordCheckBox.isSelected()){
           showpasswordField.setText(enterPasswordField.getText());
           showpasswordField.setVisible(true);
           enterPasswordField.setVisible(false);
       }else{
           enterPasswordField.setText(showpasswordField.getText());
           showpasswordField.setVisible(false);
           enterPasswordField.setVisible(true);
       }
    }

    public void CancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(ActionEvent event) {
        String usernameOrEmail = usernameTextField.getText();
        String password = enterPasswordField.getText();
        String enteredCaptcha = captchaTextField.getText();

        // Check if CAPTCHA is correct
        if (!verifyCaptcha(enteredCaptcha)) {
            loginMessageLabel.setText("Please confirm that you're not a robot.");
            return; // Exit the method if CAPTCHA is incorrect
        }

        if (!usernameOrEmail.isBlank() && !password.isBlank()) {
            try {
                MyDataBase connectNow = new MyDataBase();
                Connection connectDB = connectNow.getconn();

                UserServices userServices = new UserServices(); // Instantiate UserServices
                User user = userServices.getUserByUsernameOrEmail(connectDB, usernameOrEmail); // Retrieve user object

                if (user != null) {
                    int etat = user.getEtat(); // Assuming getEtat() is a method in the User model

                    if (etat == 0) {
                        int isValidLogin = userServices.validateLogin(connectDB, usernameOrEmail, password); // Validate password directly
                        if (isValidLogin != -1) {
                            Session.setAccount_id(isValidLogin);
                            System.out.println(Session.getAccount_id());
                            User u = userServices.getUserById_Account(isValidLogin);
                            System.out.println(u);
                            if (u.getId_role() == 1) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root);
                                Stage clientStage = new Stage();
                                clientStage.setTitle("Client Management");
                                clientStage.setScene(scene);
                                clientStage.show();
                                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                                currentStage.close();
                            } else {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminDash.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root);
                                Stage crudStage = new Stage();
                                crudStage.setTitle("Admin Management");
                                crudStage.setScene(scene);
                                crudStage.show();
                                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                                currentStage.close();
                            }
                        } else {
                            loginMessageLabel.setText("Invalid login. Please try again");
                        }
                    } else if (etat == 1) {
                        loginMessageLabel.setText("Login not allowed. Please contact support.");
                    }
                } else {
                    loginMessageLabel.setText("User not found. Please check your credentials.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                loginMessageLabel.setText("An error occurred. Please try again later.");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            loginMessageLabel.setText("Username/Email and password cannot be blank.");
        }
    }



    public void createAccountForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage registerstage = new Stage();
            registerstage.setTitle("Login");
            registerstage.setScene(scene);
            registerstage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openRegisterPage(ActionEvent event) {
        createAccountForm();
    }

    public void forgetPasswordOnAction(ActionEvent actionEvent) {
        try {

            Stage stage = (Stage) forgetPasswordLink.getScene().getWindow();
            stage.close();
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/email.fxml"));
            Parent root = loader.load();

            // Create a new stage for the login.fxml window
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Email Verification");

            // Show the login.fxml window
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String generateRandomAlphabets(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = chars.charAt(random.nextInt(chars.length()));
            sb.append(c);
        }
        return sb.toString();
        
    }

    public void afficher() {
        // Initialize CAPTCHA
        String captchaText = generateRandomAlphabets(6); // Adjust the length as needed
        captchaLabel.setText(captchaText);
        
    }

    private boolean verifyCaptcha(String enteredCaptcha) {
        String generatedCaptcha = captchaLabel.getText();
        return enteredCaptcha.equalsIgnoreCase(generatedCaptcha);
    }
}

