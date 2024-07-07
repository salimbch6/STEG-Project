package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UserServices;
import utils.MyDataBase;
import utils.Session;

import java.net.URL;
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
    private UserServices userService;
    private MyDataBase myDataBase; // Database connection

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserServices();

        try {
            // Get instance of database connection
            myDataBase = MyDataBase.getInstance();
        } catch (Exception e) {
            // Handle initialization error
            e.printStackTrace();
        }
    }

    public void registerButtonOnAction(ActionEvent event) {
        if (myDataBase == null) {

            return;
        }
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String adresse = adresseTextField.getText();
        String codePostal = codePostalTextField.getText();
        String numTel = numTelTextField.getText();
        String numCompteur = numCompteurTextField.getText();
        String date = dateTextField.getText();
        String lieu = lieuTextFiled.getText();
        String objet = objetTextField.getText();
        String marque = marqueTextField.getText();

    }
}
