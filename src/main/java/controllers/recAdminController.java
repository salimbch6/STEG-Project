package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Reclamation;
import services.ReclamationServices;
import utils.MyDataBase;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class recAdminController implements Initializable {

    @FXML
    private TableColumn<Reclamation, Integer> idR;

    @FXML
    private TableColumn<Reclamation, String> nomR;

    @FXML
    private TableColumn<Reclamation, String> prenomR;

    @FXML
    private TableColumn<Reclamation, String> adresseR;

    @FXML
    private TableColumn<Reclamation, Integer> codeR;

    @FXML
    private TableColumn<Reclamation, String> numtelR;

    @FXML
    private TableColumn<Reclamation, String> numcompR;

    @FXML
    private TableColumn<Reclamation, String> dateR;

    @FXML
    private TableColumn<Reclamation, String> lieuR;

    @FXML
    private TableColumn<Reclamation, String> objetR;

    @FXML
    private TableColumn<Reclamation, String> marqueR;

    @FXML
    private TableView<Reclamation> tableRec;

    private ReclamationServices reclamationServices;

    MyDataBase connectNow = new MyDataBase();
    Connection connectDB = connectNow.getconn();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reclamationServices = new ReclamationServices();

        idR.setCellValueFactory(new PropertyValueFactory<>("id_reclamation"));
        nomR.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomR.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adresseR.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        codeR.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        numtelR.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        numcompR.setCellValueFactory(new PropertyValueFactory<>("numCompteur"));
        dateR.setCellValueFactory(new PropertyValueFactory<>("dateIncident"));
        lieuR.setCellValueFactory(new PropertyValueFactory<>("lieuIncident"));
        objetR.setCellValueFactory(new PropertyValueFactory<>("objEndommage"));
        marqueR.setCellValueFactory(new PropertyValueFactory<>("marque"));

        populateTableRec();
    }

    void populateTableRec() {
        // Clear existing items in the TableView
        tableRec.getItems().clear();

        try {
            // Retrieve reclamation data from the database
            ResultSet resultSet = reclamationServices.getAllReclamations(connectDB);

            // Add reclamations to the TableView
            while (resultSet.next()) {
                int reclamation_id = resultSet.getInt("id_reclamation");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String adresse = resultSet.getString("adresse");
                int codePostal = resultSet.getInt("codePostal");
                String numTel = resultSet.getString("numTel");
                String numCompteur = resultSet.getString("numCompteur");
                Date dateIncident = resultSet.getDate("dateIncident");
                String lieuIncident = resultSet.getString("lieuIncident");
                String objetEndommage = resultSet.getString("objEndommage");
                String marque = resultSet.getString("marque");

                Reclamation reclamation = new Reclamation(reclamation_id,nom, prenom, adresse,codePostal, numTel,numCompteur,dateIncident,lieuIncident,objetEndommage,marque);
                tableRec.getItems().add(reclamation);
            }

            // Debug output
            if (!tableRec.getItems().isEmpty()) {
                System.out.println("Users successfully retrieved and added to TableView.");
            } else {
                System.out.println("No users found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
