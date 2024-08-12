package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ReclamationServices;
import utils.MyDataBase;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class reclamationsController implements Initializable {
    public TextField initialMarqueTextField;
    public TextField initialObjetTextField;
    @FXML
    private VBox objetsVBox;
    @FXML
    private TextField objetTextField; // Initial objet text field
    @FXML
    private Label validationLabel;
    @FXML
    private TextField marqueTextField; // Initial marque text field
    @FXML
    private TextField refTextField;
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<String> typeIncidentComboBox;
    @FXML
    private ComboBox<String> categorieIncidentComboBox;
    @FXML
    private ComboBox<String> adresseComboBox;
    @FXML
    private TextField autreCategorieTextField; // Text field for "autre" category
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
    private TextField nomTextField;
    @FXML
    private TextField numCompteurTextField;
    @FXML
    private TextField numTelTextField;
    @FXML
    private TextField prenomTextField;
    @FXML
    private VBox objetsEndommagesBox; // VBox to hold dynamically added objets endommagés and marques

    private ReclamationServices reclamationServices;
    private MyDataBase myDataBase;

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

        // Initialize typeIncidentComboBox
        typeIncidentComboBox.getItems().addAll("éléctricité", "gas");

        // Initialize categorieIncidentComboBox
        categorieIncidentComboBox.getItems().addAll("matérielle", "corporelle", "incendie", "autre");

        // Initialize adresseComboBox
        adresseComboBox.getItems().addAll("Kelibia", "Menzel Tmime", "Al Haouria", "Hammam Leghzaz");

        // Add listener to adresseComboBox
        adresseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateCodePostal(newValue));

        validationLabel.setText("");

    }

    private void updateCodePostal(String newValue) {
        switch (newValue) {
            case "Kelibia":
                codePostalTextField.setText("8090");
                break;
            case "Menzel Tmime":
                codePostalTextField.setText("8080");
                break;
            case "Al Haouria":
                codePostalTextField.setText("8045");
                break;
            case "Hammam Leghzaz":
                codePostalTextField.setText("8025");
                break;
            default:
                codePostalTextField.clear();
                break;
        }
    }

   /* @FXML
    public void envoyerBtnOnAction(ActionEvent event) {
        if (myDataBase == null) {
            return;
        }

        if (!validateFields()) {
            return;
        }

        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String adresse = adresseComboBox.getValue();
        String codePostalStr = codePostalTextField.getText();
        String reference = refTextField.getText();
        String numTel = numTelTextField.getText();
        String numCompteur = numCompteurTextField.getText();
        String dateStr = dateTextField.getText();
        String lieu = lieuTextFiled.getText();
        String typeIncident = typeIncidentComboBox.getValue();
        String categorieIncident = categorieIncidentComboBox.getValue();
        List<String> objetsEndommages = getAllObjetsEndommages();
        List<String> marques = getAllMarques();


        try {
            Integer codePostal = Integer.parseInt(codePostalStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            Date date = Date.valueOf(localDate);

            Connection connection = myDataBase.getconn();
            reclamationServices.Reclamation(connection, nom, prenom, adresse, codePostal, reference, numTel, numCompteur, date, lieu, typeIncident, categorieIncident, objetsEndommages, marques,0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Votre réclamation a été envoyée avec succès.");
            alert.showAndWait();

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }*/

    private List<String> getAllObjetsEndommages() {
        List<String> objetsEndommages = new ArrayList<>();
        // Include initial objetTextField
        if (!initialObjetTextField.getText().trim().isEmpty()) {
            objetsEndommages.add(initialObjetTextField.getText().trim());
        }
        // Include dynamically added TextFields
        for (javafx.scene.Node node : objetsVBox.getChildren()) {
            if (node instanceof HBox) {
                for (javafx.scene.Node child : ((HBox) node).getChildren()) {
                    if (child instanceof TextField) {
                        TextField textField = (TextField) child;
                        if (textField.getPromptText().equals("Objets endommagés") && !textField.getText().trim().isEmpty()) {
                            objetsEndommages.add(textField.getText().trim());
                        }
                    }
                }
            }
        }
        return objetsEndommages;
    }

    private List<String> getAllMarques() {
        List<String> marques = new ArrayList<>();
        // Include initial marqueTextField
        if (!initialMarqueTextField.getText().trim().isEmpty()) {
            marques.add(initialMarqueTextField.getText().trim());
        }
        // Include dynamically added TextFields
        for (javafx.scene.Node node : objetsVBox.getChildren()) {
            if (node instanceof HBox) {
                for (javafx.scene.Node child : ((HBox) node).getChildren()) {
                    if (child instanceof TextField) {
                        TextField textField = (TextField) child;
                        if (textField.getPromptText().equals("Marque Commerciales") && !textField.getText().trim().isEmpty()) {
                            marques.add(textField.getText().trim());
                        }
                    }
                }
            }
        }
        return marques;
    }

    @FXML
    public void cancelBtnOnAction(ActionEvent actionEvent) {
        // Handle cancel button action
    }

    @FXML
    public void addButtonOnAction() {
        HBox newHBox = new HBox(10.0); // HBox with spacing between its children

        TextField newObjetTextField = new TextField();
        TextField newMarqueTextField = new TextField();

        // Set prompt texts
        newObjetTextField.setPromptText("Objets endommagés");
        newMarqueTextField.setPromptText("Marque Commerciales");

        // Set width and height
        newObjetTextField.setPrefWidth(226);
        newObjetTextField.setPrefHeight(42);
        newMarqueTextField.setPrefWidth(226);
        newMarqueTextField.setPrefHeight(42);

        // Apply CSS stylesheet programmatically
        newObjetTextField.getStyleClass().add("text-field");
        newMarqueTextField.getStyleClass().add("text-field");

        // Add spacing between the fields
        HBox.setMargin(newObjetTextField, new Insets(0, 75, 0, 0)); // Adds margin to the right of the Objet TextField

        // Add the new TextFields to the new HBox
        newHBox.getChildren().addAll(newObjetTextField, newMarqueTextField);

        // Add the new HBox to the objetsVBox
        objetsVBox.getChildren().add(newHBox);
    }
    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (nomTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Nom' est requis.\n");
        }
        if (prenomTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Prénom' est requis.\n");
        }
        if (adresseComboBox.getValue() == null) {
            errorMessage.append("Le champ 'Adresse' est requis.\n");
        }
        if (codePostalTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Code postal' est requis.\n");
        } else {
            try {
                Integer.parseInt(codePostalTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("Le 'Code postal' doit être un nombre.\n");
            }
        }
        if (refTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Référence' est requis.\n");
        }
        if (numTelTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Numéro de téléphone' est requis.\n");
        } else {
            if (!numTelTextField.getText().matches("\\d{8}")) {
                errorMessage.append("Le 'Numéro de téléphone' doit comporter exactement 8 chiffres.\n");
            }
        }
        if (numCompteurTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Numéro de compteur' est requis.\n");
        }
        if (dateTextField.getText().isEmpty()) {
            errorMessage.append("Le champ 'Date' est requis.\n");
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate.parse(dateTextField.getText(), formatter);
            } catch (Exception e) {
                errorMessage.append("La 'Date' doit être au format d/M/yyyy.\n");
            }
        }
        if (lieuTextFiled.getText().isEmpty()) {
            errorMessage.append("Le champ 'Lieu' est requis.\n");
        }
        if (typeIncidentComboBox.getValue() == null) {
            errorMessage.append("Le champ 'Type d'incident' est requis.\n");
        }
        if (categorieIncidentComboBox.getValue() == null) {
            errorMessage.append("Le champ 'Catégorie d'incident' est requis.\n");
        }

        validationLabel.setText(errorMessage.toString());

        return errorMessage.length() == 0;
    }


}