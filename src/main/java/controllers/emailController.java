package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserServices;
import models.User;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;

public class emailController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField otpField;
    @FXML
    private Button verifyButton;
    @FXML
    private Button backButton;

    private UserServices.EmailSender emailSender = new UserServices.EmailSender();
    private String generatedCode;

    public void sendButtonOnAction(ActionEvent actionEvent) {
        // Generate a 6-digit random code
        generatedCode = generateRandomCode(6);

        // Send email with the generated code
        emailSender.sendEmail(emailField.getText(), generatedCode, "Change Password", "Your verification code is: " + generatedCode);
    }

    public void verifyButtonOnAction(ActionEvent actionEvent) throws IOException {
        String enteredCode = otpField.getText();

        if (enteredCode.equals(generatedCode)) {
            try {
                Stage stage = (Stage) verifyButton.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPassword.fxml"));
                Parent root = loader.load();

                // Access the controller of newPassword.fxml
                newPasswordController passwordUpdateController = loader.getController();
                passwordUpdateController.setEmail(emailField.getText());

                Stage crudStage = new Stage();
                crudStage.setScene(new Scene(root));
                crudStage.setTitle("CRUD Interface");

                // Show the newPassword.fxml window
                crudStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Display error message
            JOptionPane.showMessageDialog(null, "Incorrect verification code. Please try again.");
        }
    }
    private String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append random digit (0-9)
        }
        return sb.toString();
    }

    @FXML
    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            // Close the current window
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();

            // Load the crud.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
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
}
