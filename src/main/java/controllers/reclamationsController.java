package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ReclamationServices;
import utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class reclamationsController implements Initializable {
    @FXML
    private Label reclamationsLabel;

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
    private TextField nomTextField;

    @FXML
    private TextField numCompteurTextField;

    @FXML
    private TextField numTelTextField;

    @FXML
    private TextField objetTextField;

    @FXML
    private TextField prenomTextField;
    private ReclamationServices reclamationServices;
    private MyDataBase myDataBase; // Database connection

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reclamationServices = new ReclamationServices();

        try {
            // Get instance of database connection
            myDataBase = MyDataBase.getInstance();
        } catch (Exception e) {
            // Handle initialization error
            e.printStackTrace();
        }
    }

    @FXML
    public void envoyerBtnOnAction(ActionEvent event) {
        if (myDataBase == null) {
            return;
        }

        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String adresse = adresseTextField.getText();
        String codePostalStr = codePostalTextField.getText();
        String numTel = numTelTextField.getText();
        String numCompteur = numCompteurTextField.getText();
        String dateStr = dateTextField.getText();
        String lieu = lieuTextFiled.getText();
        String objet = objetTextField.getText();
        String marque = marqueTextField.getText();

        try {
            Integer codePostal = Integer.parseInt(codePostalStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            Date date = Date.valueOf(localDate);

            Connection connection = myDataBase.getconn();
            reclamationServices.Reclamation(connection, nom, prenom, adresse, codePostal, numTel, numCompteur, date, lieu, objet, marque);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Votre réclamation a été envoyée avec succès.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Handle number format exception for codePostal
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Handle invalid date format exception
            e.printStackTrace();
        }
    }

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
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
