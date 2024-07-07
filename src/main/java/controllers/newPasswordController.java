package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import services.UserServices;

import java.sql.SQLException;

public class newPasswordController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Label errorMsgLabel;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    @FXML
    public void changepasswordOnAction(ActionEvent actionEvent) throws SQLException {
        String newPassword = newPasswordField.getText();

        if (newPassword.isEmpty()) {
            errorMsgLabel.setText("Password cannot be empty.");
            return;
        }

        if (newPassword.length() < 8) {
            errorMsgLabel.setText("Password must be at least 8 characters long.");
            return;
        }

        if (!newPassword.matches(".*[A-Z].*")) {
            errorMsgLabel.setText("Password must contain at least one uppercase letter.");
            return;
        }

        // Initialize UserServices
        UserServices userServices = new UserServices();

        // Update the password in the database
        userServices.updatePasswordByEmail(email, newPassword);

        // Close the current window
        Stage stage = (Stage) newPasswordField.getScene().getWindow();
        stage.close();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loader.load()));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Update");
        alert.setHeaderText(null);
        alert.setContentText("Password updated successfully!");
        alert.showAndWait();
    }}

