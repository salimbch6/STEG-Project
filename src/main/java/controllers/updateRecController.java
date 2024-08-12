package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Reclamation;
import services.ReclamationServices;
import utils.MyDataBase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class updateRecController implements Initializable {

    @FXML
    private TextField adresseTextField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField codePostalTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private Button envoyerBtn;

    @FXML
    private TextField lieuTextFiled;

    @FXML
    private TextField marqueTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField numCompteurTextField;

    @FXML
    private TextField numTelTextField;

    @FXML
    private TextField objetTextField;

    @FXML
    private TextField prenomTextField;
    @FXML
    private TextField referenceTextField;
    @FXML
    private ComboBox<?> typeIncidentComboBox;
    @FXML
    private ComboBox <?>categorieIncidentComboBox;

    private Reclamation selectedReclamation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any required data here
    }

    @FXML
    void cancelBtnOnAction(ActionEvent event) {
        try {
            // Load the adminDash.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminDash.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) cancelBtn.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Optionally, you can set the title for the new scene
            stage.setTitle("Admin Dashboard");

            // Show the new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, you can show an alert if the loading fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the Admin Dashboard.");
            alert.showAndWait();
        }
    }


    @FXML
    void envoyerBtnOnAction(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            updateReclamationData();
            navigateToAdminDashboard();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred while updating the data.");
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (nomTextField.getText().isEmpty()) {
            errors.append("Nom is required.\n");
        }
        if (prenomTextField.getText().isEmpty()) {
            errors.append("Prenom is required.\n");
        }
        if (adresseTextField.getText().isEmpty()) {
            errors.append("Adresse is required.\n");
        }
        if (codePostalTextField.getText().isEmpty()) {
            errors.append("Code Postal is required.\n");
        }
        if (numTelTextField.getText().isEmpty()) {
            errors.append("Num Tel is required.\n");
        }
        if (numCompteurTextField.getText().isEmpty()) {
            errors.append("Num Compteur is required.\n");
        }
        if (dateTextField.getText().isEmpty()) {
            errors.append("Date is required.\n");
        }
        if (lieuTextFiled.getText().isEmpty()) {
            errors.append("Lieu is required.\n");
        }
        if (objetTextField.getText().isEmpty()) {
            errors.append("Objet is required.\n");
        }
        if (marqueTextField.getText().isEmpty()) {
            errors.append("Marque is required.\n");
        }

        if (errors.length() > 0) {
            errorLabel.setText(errors.toString());
            return false;
        }

        return true;
    }

    private void updateReclamationData() throws SQLException {
        selectedReclamation.setNom(nomTextField.getText());
        selectedReclamation.setPrenom(prenomTextField.getText());
        selectedReclamation.setAdresse(adresseTextField.getText());
        selectedReclamation.setCodePostal(Integer.parseInt(codePostalTextField.getText()));
        selectedReclamation.setNumTel(numTelTextField.getText());
        selectedReclamation.setNumCompteur(numCompteurTextField.getText());

        // Ensure the date is in the correct format
        String dateString = dateTextField.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            java.util.Date utilDate = dateFormat.parse(dateString);
            Date sqlDate = new Date(utilDate.getTime());
            selectedReclamation.setDateIncident(sqlDate.toLocalDate());
        } catch (ParseException e) {
            errorLabel.setText("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        selectedReclamation.setLieuIncident(lieuTextFiled.getText());


        Connection connectDB = MyDataBase.getInstance().getconn();
        ReclamationServices reclamationServices = new ReclamationServices();
        reclamationServices.updateReclamation(connectDB, selectedReclamation);
    }
    private void navigateToAdminDashboard() throws IOException {
        Stage stage = (Stage) envoyerBtn.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminDash.fxml"));
        Parent root = loader.load();

        Stage crudStage = new Stage();
        crudStage.setScene(new Scene(root));
        crudStage.setTitle("CRUD Interface");
        crudStage.show();
    }

    public void initData(Reclamation reclamation) throws MalformedURLException {
        selectedReclamation = reclamation;

        nomTextField.setText(selectedReclamation.getNom());
        prenomTextField.setText(selectedReclamation.getPrenom());
        adresseTextField.setText(selectedReclamation.getAdresse());
        codePostalTextField.setText(String.valueOf(selectedReclamation.getCodePostal()));
        numTelTextField.setText(selectedReclamation.getNumTel());
        numCompteurTextField.setText(selectedReclamation.getNumCompteur());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTextField.setText(dateFormat.format(selectedReclamation.getDateIncident()));
        lieuTextFiled.setText(selectedReclamation.getLieuIncident());

    }
}
