package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class settingsAdminController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private CheckBox notificationsCheckbox;

    @FXML
    private CheckBox dailyReportsCheckbox;

    @FXML
    private void initialize() {
        // Load current settings (This is just an example, replace with actual logic)
        usernameField.setText("Admin");
        emailField.setText("admin@gmail.com");
        notificationsCheckbox.setSelected(true);
        dailyReportsCheckbox.setSelected(false);
    }

    @FXML
    private void saveSettings() {
        // Save settings logic (replace with actual logic)
        String username = usernameField.getText();
        String email = emailField.getText();
        boolean notificationsEnabled = notificationsCheckbox.isSelected();
        boolean dailyReportsEnabled = dailyReportsCheckbox.isSelected();

        // For demonstration purposes
        System.out.println("Settings saved:");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Notifications: " + notificationsEnabled);
        System.out.println("Daily Reports: " + dailyReportsEnabled);

        // Close the settings window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelSettings() {
        // Close the settings window without saving
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
