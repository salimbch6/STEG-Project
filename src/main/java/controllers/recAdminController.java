package controllers;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import models.Reclamation;
import models.User;
import services.ReclamationServices;
import services.UserServices;
import utils.MyDataBase;
import com.itextpdf.io.IOException;
import com.itextpdf.layout.element.Cell;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import javafx.scene.paint.Color;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
    private TableColumn<Reclamation, LocalDate> dateR;
    @FXML
    private TableColumn<Reclamation, String> lieuR;
    @FXML
    private TableColumn<Reclamation, String> objetR;
    @FXML
    private TableColumn<Reclamation, String> marqueR;
    @FXML
    private TableColumn<Reclamation, String> categorieR;
    @FXML
    private TableColumn<Reclamation, String> refR;
    @FXML
    private TableColumn<Reclamation, String> typeIncidentR;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateRecR;
    @FXML
    private TableColumn<Reclamation, Void> notifAssuranceR;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateNotifR;
    @FXML
    private TableColumn<Reclamation, Integer> dureeR;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateInspectionR;
    @FXML
    private TableColumn<Reclamation, String> responsabiliteR;
    @FXML
    private TableColumn<Reclamation, LocalDate> notifierClientR;
    @FXML
    private TableColumn<Reclamation, LocalDate> demanderFactureR;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateRecInspectionR;
    @FXML
    private TableColumn<Reclamation, LocalDate> recevoirFactureR;
    @FXML
    private TableColumn<Reclamation, Void> factureR;
    @FXML
    private TableColumn<Reclamation, String> quittanceR;
    @FXML
    private TableColumn<Reclamation, String> resolutionR;
    @FXML
    private TableColumn<Reclamation, Integer> dureeInspectionR;
    @FXML
    private TableColumn<Reclamation, Integer> dureeNotifClientR;
    @FXML
    private TableColumn<Reclamation, Integer> dureeRemboursementR;
    @FXML
    private TableColumn<Reclamation, LocalDate> signatureR;







    @FXML
    private ImageView pdfIcon;
    @FXML
    private ImageView excelIcon;

    @FXML
    private Button ajouterButton;
    @FXML
    private TableView<Reclamation> tableRec;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private TextField searchTextField;

    private ReclamationServices reclamationServices;

    MyDataBase connectNow = new MyDataBase();
    Connection connectDB = connectNow.getconn();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load and set images
        File pdfFile = new File("target/classes/pdf-file.png");
        Image pdfImage = new Image(pdfFile.toURI().toString());
        pdfIcon.setImage(pdfImage);

        File excelFile = new File("target/classes/excel.png");
        Image excelImage = new Image(excelFile.toURI().toString());
        excelIcon.setImage(excelImage);

        // Add hover effect to pdfIcon
        pdfIcon.setOnMouseEntered(event -> handleMouseEntered(pdfIcon));
        pdfIcon.setOnMouseExited(event -> handleMouseExited(pdfIcon));

        // Add hover effect to excelIcon
        excelIcon.setOnMouseEntered(event -> handleMouseEntered(excelIcon));
        excelIcon.setOnMouseExited(event -> handleMouseExited(excelIcon));

        reclamationServices = new ReclamationServices();

        // Set cell value factories
        idR.setCellValueFactory(new PropertyValueFactory<>("id_reclamation"));
        nomR.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomR.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adresseR.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        codeR.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        numtelR.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        numcompR.setCellValueFactory(new PropertyValueFactory<>("numCompteur"));
        dateR.setCellValueFactory(new PropertyValueFactory<>("dateIncident"));
        lieuR.setCellValueFactory(new PropertyValueFactory<>("lieuIncident"));
        categorieR.setCellValueFactory(new PropertyValueFactory<>("categorieIncident"));
        refR.setCellValueFactory(new PropertyValueFactory<>("reference"));
        typeIncidentR.setCellValueFactory(new PropertyValueFactory<>("typeIncident"));
        dateRecR.setCellValueFactory(new PropertyValueFactory<>("dateRec"));
        dateNotifR.setCellValueFactory(new PropertyValueFactory<>("dateNotif"));
        dureeR.setCellValueFactory(new PropertyValueFactory<>("duree"));
        dateInspectionR.setCellValueFactory(new PropertyValueFactory<>("inspection"));
        dateRecInspectionR.setCellValueFactory(new PropertyValueFactory<>("recevoirInspection"));
        dureeInspectionR.setCellValueFactory(new PropertyValueFactory<>("dureeInspection"));
        responsabiliteR.setCellValueFactory(new PropertyValueFactory<>("responsabilite"));
        notifierClientR.setCellValueFactory(new PropertyValueFactory<>("notifierClient"));
        demanderFactureR.setCellValueFactory(new PropertyValueFactory<>("demanderFacture"));
        dureeNotifClientR.setCellValueFactory(new PropertyValueFactory<>("dureeNotifierClient"));
        recevoirFactureR.setCellValueFactory(new PropertyValueFactory<>("recevoirFacture"));
        quittanceR.setCellValueFactory(new PropertyValueFactory<>("quittance"));
        signatureR.setCellValueFactory(new PropertyValueFactory<>("signature"));
        resolutionR.setCellValueFactory(new PropertyValueFactory<>("resolution"));
        dureeRemboursementR.setCellValueFactory(new PropertyValueFactory<>("dureeRemboursement"));

        // Set cell factories for buttons
        factureR.setCellFactory(column -> new DetailsButtonCell());

        responsabiliteR.setCellFactory(column -> new TableCell<Reclamation, String>() {
        });

        // Make columns editable
        makeColumnsEditable();

        // Populate the TableView with data
        populateTableRec();

        // Add listener to update duration when dateNotif is edited
        dateNotifR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateNotifR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setDateNotif(event.getNewValue());

            // Calculate and set the duration
            Integer duration = calculateDuration(reclamation.getDateRec(), reclamation.getDateNotif());
            reclamation.setDuree(duration);

            // Update the reclamation in the database
            updateReclamation(reclamation);
            tableRec.refresh(); // Refresh the table to show the updated duration
        });

        dateRecInspectionR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateRecInspectionR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setRecevoirInspection(event.getNewValue());

            // Calculate and set the duration
            Integer duration = calculateDuration(reclamation.getInspection(), reclamation.getRecevoirInspection());
            reclamation.setDureeInspection(duration);

            // Update the reclamation in the database
            updateReclamation(reclamation);
            tableRec.refresh(); // Refresh the table to show the updated duration
        });

        notifierClientR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        notifierClientR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setNotifierClient(event.getNewValue());

            // Calculate and set the duration
            Integer duration = calculateDuration(reclamation.getDateRec(), reclamation.getNotifierClient());
            reclamation.setDureeNotifierClient(duration);

            // Update the reclamation in the database
            updateReclamation(reclamation);
            tableRec.refresh(); // Refresh the table to show the updated duration
        });

        demanderFactureR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        demanderFactureR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setDemanderFacture(event.getNewValue());

            // Calculate and set the duration
            Integer duration = calculateDuration(reclamation.getDateRec(), reclamation.getDemanderFacture());
            reclamation.setDureeNotifierClient(duration);

            // Update the reclamation in the database
            updateReclamation(reclamation);
            tableRec.refresh(); // Refresh the table to show the updated duration
        });

        signatureR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        signatureR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setSignature(event.getNewValue());

            // Calculate and set the duration
            Integer duration = calculateDuration(reclamation.getDateRec(), reclamation.getSignature());
            reclamation.setDureeRemboursement(duration);

            // Update the reclamation in the database
            updateReclamation(reclamation);
            tableRec.refresh(); // Refresh the table to show the updated duration
        });

        setEmptyCellFactory(dureeR);
        setEmptyCellFactory(dureeInspectionR);
        setEmptyCellFactory(dureeNotifClientR);
        setEmptyCellFactory(dureeRemboursementR);
    }


    private void setEmptyCellFactory(TableColumn<Reclamation, Integer> column) {
        column.setCellFactory(col -> new TableCell<Reclamation, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item == 0) {
                    setText("");
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    private void handleMouseEntered(ImageView imageView) {
        // Example: Add a drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GREY);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        imageView.setEffect(dropShadow);
        imageView.setOpacity(0.8); // Optional: Change opacity on hover
    }

    // Handler for mouse exit event
    private void handleMouseExited(ImageView imageView) {
        // Remove the drop shadow effect
        imageView.setEffect(null);
        imageView.setOpacity(1.0); // Reset opacity
    }





    public void addButtonClicked(ActionEvent actionEvent) {
        // Create a new Reclamation with default or user-provided values
        Reclamation newReclamation = new Reclamation();

        // Set default values for newReclamation as needed
        newReclamation.setNom(""); // Default to empty string
        newReclamation.setPrenom(""); // Default to empty string
        newReclamation.setAdresse(""); // Default to empty string
        newReclamation.setReference(""); // Default to empty string
        newReclamation.setNumTel(""); // Default to empty string
        newReclamation.setNumCompteur(""); // Default to empty string
        newReclamation.setLieuIncident(""); // Default to empty string
        newReclamation.setTypeIncident(""); // Default to empty string
        newReclamation.setCategorieIncident(""); // Default to empty string
        newReclamation.setDateIncident(LocalDate.now()); // Set to current date if needed
        newReclamation.setDateRec(null); // Set to null if not provided
        newReclamation.setEtatNotif(0); // Default value if needed
        newReclamation.setDuree(null); // Default to null if not provided
        newReclamation.setInspection(null); // Default to null if not provided
        newReclamation.setRecevoirInspection(null); // Default to null if not provided
        newReclamation.setResponsabilite(""); // Default to empty string if needed
        newReclamation.setNotifierClient(null); // Default to null if not provided
        newReclamation.setDemanderFacture(null); // Default to null if not provided
        newReclamation.setRecevoirFacture(null); // Default to null if not provided
        newReclamation.setQuittance(""); // Default to empty string if needed
        newReclamation.setResolution(""); // Default to empty string if needed


        // Convert etatNotif to List<String> if required
        List<String> etatNotifList = Collections.singletonList(String.valueOf(newReclamation.getEtatNotif()));

        try {
            int id = reclamationServices.addReclamation(
                    connectDB,
                    newReclamation.getNom(),
                    newReclamation.getPrenom(),
                    newReclamation.getAdresse(),
                    newReclamation.getCodePostal() != null ? newReclamation.getCodePostal() : 0,
                    newReclamation.getReference(),
                    newReclamation.getNumTel(),
                    newReclamation.getNumCompteur(),
                    newReclamation.getDateIncident(),
                    newReclamation.getLieuIncident(),
                    newReclamation.getTypeIncident(),
                    newReclamation.getCategorieIncident(),
                    newReclamation.getEtatNotif()
            );

            // Update newReclamation with the generated ID
            newReclamation.setId_reclamation(id);

            // Add newReclamation to the TableView
            tableRec.getItems().add(newReclamation);

            // Optionally, scroll to the new item
            tableRec.scrollTo(newReclamation);

            // Animate the new row
            tableRec.getItems().addListener((ListChangeListener<Reclamation>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (Reclamation addedItem : change.getAddedSubList()) {
                            TableRow<Reclamation> row = (TableRow<Reclamation>) tableRec.lookup(".table-row-cell:nth-last-of-type(1)");
                            if (row != null) {
                                TranslateTransition transition = new TranslateTransition(Duration.millis(300), row);
                                transition.setByY(10); // Move down by 10 pixels
                                transition.setCycleCount(2); // Move down and then move up
                                transition.setAutoReverse(true); // Reverse animation
                                transition.play();
                            }
                        }
                    }
                }
            });

            // Optionally, refresh the entire table
            populateTableRec(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







    public class LocalDateStringConverter extends StringConverter<LocalDate> {
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return date.format(dateFormatter);
            }
            return "";
        }

        @Override
        public LocalDate fromString(String string) {
            try {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void makeColumnsEditable() {
        // Make nomR column editable
        nomR.setCellFactory(TextFieldTableCell.forTableColumn());
        nomR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setNom(event.getNewValue());
            updateReclamation(reclamation);
        });

        // Make prenomR column editable
        prenomR.setCellFactory(TextFieldTableCell.forTableColumn());
        prenomR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setPrenom(event.getNewValue());
            updateReclamation(reclamation);
        });

        // Make adresseR column editable
        adresseR.setCellFactory(TextFieldTableCell.forTableColumn());
        adresseR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setAdresse(event.getNewValue());
            // Update codePostal based on the new adresse value
            updateCodePostalBasedOnAdresse(reclamation);
            tableRec.refresh();
            updateReclamation(reclamation);
        });

        // Make codeR column editable


        // Make numtelR column editable
        numtelR.setCellFactory(TextFieldTableCell.forTableColumn());
        numtelR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setNumTel(event.getNewValue());
            updateReclamation(reclamation);
        });

        // Make numcompR column editable
        numcompR.setCellFactory(TextFieldTableCell.forTableColumn());
        numcompR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setNumCompteur(event.getNewValue());
            updateReclamation(reclamation);
        });



        // Make lieuR column editable
        lieuR.setCellFactory(TextFieldTableCell.forTableColumn());
        lieuR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setLieuIncident(event.getNewValue());
            updateReclamation(reclamation);
        });



        // Make categorieR column editable
        categorieR.setCellFactory(TextFieldTableCell.forTableColumn());
        categorieR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setCategorieIncident(event.getNewValue());
            updateReclamation(reclamation);
        });

        // Make refR column editable
        refR.setCellFactory(TextFieldTableCell.forTableColumn());
        refR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setReference(event.getNewValue());
            updateReclamation(reclamation);
        });

        // Make typeIncidentR column editable
        typeIncidentR.setCellFactory(TextFieldTableCell.forTableColumn());
        typeIncidentR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setTypeIncident(event.getNewValue());
            updateReclamation(reclamation);
        });



        // Make dateInspectionR column editable
        dateInspectionR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateInspectionR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setInspection(event.getNewValue());
            updateReclamation(reclamation);
        });
        dateR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setDateIncident(event.getNewValue());
            updateReclamation(reclamation);
        });
        dateRecR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateRecR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setDateRec(event.getNewValue());
            updateReclamation(reclamation);
        });




        responsabiliteR.setCellFactory(TextFieldTableCell.forTableColumn());
        responsabiliteR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setResponsabilite(event.getNewValue());
            updateReclamation(reclamation);
        });
        notifierClientR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        notifierClientR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setNotifierClient(event.getNewValue());
            updateReclamation(reclamation);
        });
        demanderFactureR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        demanderFactureR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
                reclamation.setDemanderFacture(event.getNewValue());
            updateReclamation(reclamation);
        });

        recevoirFactureR.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        recevoirFactureR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setRecevoirFacture(event.getNewValue());
            updateReclamation(reclamation);
        });


        // Make the duree column non-editable
        dureeR.setEditable(false);

        quittanceR.setCellFactory(TextFieldTableCell.forTableColumn());
        quittanceR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setQuittance(event.getNewValue());
            updateReclamation(reclamation);
        });
        resolutionR.setCellFactory(TextFieldTableCell.forTableColumn());
        resolutionR.setOnEditCommit(event -> {
            Reclamation reclamation = event.getRowValue();
            reclamation.setResolution(event.getNewValue());
            updateReclamation(reclamation);
        });
    }
    private Integer calculateDuration(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return (int) ChronoUnit.DAYS.between(startDate, endDate);
        }
        return null;
    }


    // Method to update codePostal based on adresse
    private void updateCodePostalBasedOnAdresse(Reclamation reclamation) {
        String adresse = reclamation.getAdresse();
        switch (adresse) {
            case "Kelibia":
                reclamation.setCodePostal(8090);
                break;
            case "Menzel Tmime":
                reclamation.setCodePostal(8080);
                break;
            case "Al Haouria":
                reclamation.setCodePostal(8045);
                break;
            case "Hammam Leghzaz":
                reclamation.setCodePostal(8025);
                break;
            default:
                // Handle other addresses or set a default codePostal value
                break;
        }
    }


    void populateTableRec() {
        // Clear existing items in the TableView
        tableRec.getItems().clear();

        try {
            // Retrieve reclamation data from the database
            ResultSet resultSet = reclamationServices.getAllReclamations(connectDB);

            // Create a list to store the reclamations
            List<Reclamation> reclamations = new ArrayList<>();

            // Add reclamations to the list
            while (resultSet.next()) {
                int reclamation_id = resultSet.getInt("id_reclamation");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String adresse = resultSet.getString("adresse");
                int codePostal = resultSet.getInt("codePostal");
                String reference = resultSet.getString("reference");
                String numTel = resultSet.getString("numTel");
                String numCompteur = resultSet.getString("numCompteur");
                LocalDate dateIncident = resultSet.getDate("dateIncident") != null ? resultSet.getDate("dateIncident").toLocalDate() : null;
                String lieuIncident = resultSet.getString("lieuIncident");
                String typeIncident = resultSet.getString("typeIncident");
                String categorieIncident = resultSet.getString("categorieIncident");
                LocalDate dateRec = resultSet.getDate("dateRec") != null ? resultSet.getDate("dateRec").toLocalDate() : null;
                LocalDate dateNotif = resultSet.getDate("dateNotif") != null ? resultSet.getDate("dateNotif").toLocalDate() : null;
                Integer duree = resultSet.getObject("duree", Integer.class);
                // Handle null values for LocalDate
                LocalDate dateInspection = resultSet.getDate("inspection") != null ? resultSet.getDate("inspection").toLocalDate() : null;
                LocalDate dateRecInspection = resultSet.getDate("recevoirIncpection") != null ? resultSet.getDate("recevoirIncpection").toLocalDate() : null;
                Integer dureeInspection = resultSet.getObject("dureeInspection", Integer.class);
                String responsabilite = resultSet.getString("responsabilite");
                LocalDate notifierClient = resultSet.getDate("notifierClient") != null ? resultSet.getDate("notifierClient").toLocalDate() : null;
                LocalDate demanderFacture = resultSet.getDate("demanderFacture") != null ? resultSet.getDate("demanderFacture").toLocalDate() : null;
                Integer dureeNotifClient = resultSet.getObject("dureeNotifierClient", Integer.class);
                LocalDate recevoirFacture = resultSet.getDate("recevoirFacture") != null ? resultSet.getDate("recevoirFacture").toLocalDate() : null;
                String quittance = resultSet.getString("quittance");
                LocalDate signature = resultSet.getDate("signature") != null ? resultSet.getDate("signature").toLocalDate() : null;
                String resolution = resultSet.getString("resolution");
                Integer dureeRemboursement = resultSet.getObject("dureeRemboursement", Integer.class);


                if ("0".equals(quittance)) {
                    quittance = "";
                }
                if ("0".equals(resolution)) {
                    resolution= "";
                }

                Reclamation reclamation = new Reclamation(reclamation_id, nom, prenom, adresse, codePostal, reference,
                        numTel, numCompteur, dateIncident, lieuIncident, typeIncident,
                        categorieIncident, dateRec, 0, dateNotif, duree, dateInspection, dateRecInspection,dureeInspection, responsabilite, notifierClient, demanderFacture,dureeNotifClient, recevoirFacture,quittance,signature,resolution,dureeRemboursement) {

                };

                reclamations.add(reclamation);
            }

            // Sort the list based on the criteria
            reclamations.sort(Comparator.comparing(Reclamation::getAdresse)
                    .thenComparing(Reclamation::getLieuIncident)
                    .thenComparing(Reclamation::getDateIncident));

            // Group reclamations by criteria and get color map
            Map<String, String> colorMap = reclamationServices.groupReclamationsByCriteria(reclamations);

            // Add the sorted list to the TableView
            tableRec.getItems().addAll(reclamations);

            // Apply colors to the TableView rows
            tableRec.setRowFactory(tv -> new TableRow<Reclamation>() {
                @Override
                protected void updateItem(Reclamation item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {
                        String key = item.getAdresse() + item.getLieuIncident() + item.getDateIncident();
                        if (colorMap.containsKey(key)) {
                            setStyle("-fx-background-color: " + colorMap.get(key) + ";");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });

            // Debug output
            if (!tableRec.getItems().isEmpty()) {
                System.out.println("Reclamations successfully retrieved and added to TableView.");
            } else {
                System.out.println("No reclamations found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        // Get the selected reclamation
        Reclamation selectedReclamation = tableRec.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                // Delete the selected reclamation from the database
                reclamationServices.deleteReclamation(connectDB, selectedReclamation.getId_reclamation());
                // Remove the reclamation from the TableView
                tableRec.getItems().remove(selectedReclamation);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateReclamation(Reclamation reclamation) {
        try {
            reclamationServices.updateReclamation(connectDB, reclamation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateButtonOnAction(ActionEvent actionEvent) {
        Reclamation selectedReclamation = tableRec.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                // Close the current window
                Stage stage = (Stage) updateButton.getScene().getWindow();
                stage.close();

                // Load the update.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateRec.fxml"));
                Parent root = loader.load();

                // Get the controller associated with the update.fxml
                updateRecController updateRecController = loader.getController();
                // Pass the selected reclamation's data to the updateController
                updateRecController.initData(selectedReclamation);
                // Create a new stage for the update.fxml window
                Stage updateStage = new Stage();
                updateStage.setScene(new Scene(root));
                updateStage.setTitle("Modifier Réclamation");

                // Show the update.fxml window
                updateStage.showAndWait();
            } catch (IOException | java.io.IOException e) {
                e.printStackTrace();
            }
        } else {
            // No reclamation selected, display an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Reclamation Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a reclamation to update.");
            alert.showAndWait();
        }
    }

    @FXML
    public void searchOnAction(ActionEvent actionEvent) {
        String query = searchTextField.getText().trim();
        if (!query.isEmpty()) {
            // Perform search based on the query
            searchReclamations(query);
        } else {
            // If search query is empty, reset the TableView to show all reclamations
            populateTableRec();
        }
    }

    private void searchReclamations(String query) {
        // Clear existing items in the TableView
        tableRec.getItems().clear();

        try {
            // Retrieve reclamation data from the database based on search query
            ResultSet resultSet = reclamationServices.searchReclamations(connectDB, query);

            // Add matching reclamations to the TableView
            while (resultSet.next()) {
                int reclamation_id = resultSet.getInt("id_reclamation");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String adresse = resultSet.getString("adresse");
                int codePostal = resultSet.getInt("codePostal");
                String reference = resultSet.getString("reference");
                String numTel = resultSet.getString("numTel");
                String numCompteur = resultSet.getString("numCompteur");
                LocalDate dateIncident = resultSet.getDate("dateIncident").toLocalDate();
                String lieuIncident = resultSet.getString("lieuIncident");
                String typeIncident = resultSet.getString("typeIncident");
                String categorieIncident = resultSet.getString("categorieIncident");
                LocalDate dateRec = resultSet.getDate("dateRec").toLocalDate();
                LocalDate dateNotif = resultSet.getDate("dateNotif").toLocalDate();
                int duree = resultSet.getInt("duree");
                LocalDate dateInspection = resultSet.getDate("inspection").toLocalDate();
                LocalDate dateRecInspection = resultSet.getDate("recevoirIncpection").toLocalDate();
                int dureeInspection = resultSet.getInt("dureeInspection");
                String responsabilite = resultSet.getString("responsabilite");
                LocalDate notifierClient = resultSet.getDate("notifierClient").toLocalDate();
                LocalDate demanderFacture = resultSet.getDate("demanderFacture").toLocalDate();
                int dureeNotifClient = resultSet.getInt("dureeNotifierClient");
                LocalDate recevoirFacture = resultSet.getDate("recevoirFacture").toLocalDate();
                String quittance = resultSet.getString("quittance");
                LocalDate signature = resultSet.getDate("signature").toLocalDate();
                int dureeRemboursement = resultSet.getInt("dureeRemboursement");
                if ("0".equals(quittance)) {
                    quittance = "";
                }
                    String resolution = resultSet.getString("resolution");
                    if ("0".equals(resolution)) {
                        resolution = "";

                }

                // Create Reclamation object with new fields
                Reclamation reclamation = new Reclamation(reclamation_id, nom, prenom, adresse, codePostal, reference, numTel, numCompteur, dateIncident, lieuIncident, typeIncident, categorieIncident, dateRec, 0, dateNotif, duree, dateInspection, dateRecInspection,dureeInspection, responsabilite, notifierClient, demanderFacture,dureeNotifClient, recevoirFacture,quittance,signature,resolution,dureeRemboursement) {

                };
                reclamation.setDateNotif(dateNotif);
                reclamation.setDuree(duree);

                tableRec.getItems().add(reclamation);
            }

            // Debug output
            if (!tableRec.getItems().isEmpty()) {
                System.out.println("Reclamations matching the search query successfully retrieved and added to TableView.");
            } else {
                System.out.println("No reclamations found matching the search query.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateOnAction(MouseEvent actionEvent) {
        Window stage = tableRec.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try {
                PdfWriter writer = new PdfWriter(selectedFile.getAbsolutePath());
                PdfDocument pdfDocument = new PdfDocument(writer);
                pdfDocument.setDefaultPageSize(PageSize.A2.rotate());
                Document document = new Document(pdfDocument);

                document.add(new Paragraph("Reclamations Report"));

                // Define the widths of each column
                float[] columnWidths = {50f, 50f, 50f, 100f, 50f, 50f, 50f, 100f, 100f, 100f, 50f, 100f, 100f, 100f, 50f, 100f, 100f, 100f, 100f, 100f, 100f, 100f};
                Table table = new Table(columnWidths);

                // Add table headers
                table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Nom").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Prénom").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Adresse").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Code Postal").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Num Tel").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Num Compteur").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Incident").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Lieu Incident").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Catégorie Incident").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Référence").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Type Incident").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Rec").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Notif").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Durée").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Inspection").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Rec Inspection").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Responsabilité").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Notifier Client").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Demander Facture").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Recevoir Facture").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Quittance").setBold()));


                Map<String, DeviceRgb> colorMap = new HashMap<>();
                Set<String> keysSet = new HashSet<>();
                DeviceRgb[] colors = {
                        new DeviceRgb(173, 216, 230), // Light Blue
                        new DeviceRgb(144, 238, 144), // Light Green
                        new DeviceRgb(255, 182, 193)  // Light Pink
                };
                int colorIndex = 0;

                // Determine which keys need coloring
                for (Reclamation reclamation : tableRec.getItems()) {
                    String key = reclamation.getAdresse() + "_" + reclamation.getLieuIncident() + "_" + (reclamation.getDateIncident() != null ? reclamation.getDateIncident().toString() : "null");
                    keysSet.add(key);
                }

                // Assign colors based on occurrences
                for (String key : keysSet) {
                    long count = tableRec.getItems().stream().filter(r -> (r.getAdresse() + "_" + r.getLieuIncident() + "_" + (r.getDateIncident() != null ? r.getDateIncident().toString() : "null")).equals(key)).count();
                    if (count > 1) {
                        colorMap.put(key, colors[colorIndex % colors.length]);
                        colorIndex++;
                    }
                }

                // Add table rows with conditional coloring
                for (Reclamation reclamation : tableRec.getItems()) {
                    String key = reclamation.getAdresse() + "_" + reclamation.getLieuIncident() + "_" + (reclamation.getDateIncident() != null ? reclamation.getDateIncident().toString() : "null");
                    DeviceRgb backgroundColor = colorMap.getOrDefault(key, null);

                    table.addCell(createCell(reclamation.getId_reclamation(), backgroundColor));
                    table.addCell(createCell(reclamation.getNom(), backgroundColor));
                    table.addCell(createCell(reclamation.getPrenom(), backgroundColor));
                    table.addCell(createCell(reclamation.getAdresse(), backgroundColor));
                    table.addCell(createCell(reclamation.getCodePostal(), backgroundColor));
                    table.addCell(createCell(reclamation.getNumTel(), backgroundColor));
                    table.addCell(createCell(reclamation.getNumCompteur(), backgroundColor));
                    table.addCell(createCell(reclamation.getDateIncident() != null ? reclamation.getDateIncident().toString() : "", backgroundColor));
                    table.addCell(createCell(reclamation.getLieuIncident(), backgroundColor));
                    table.addCell(createCell(reclamation.getCategorieIncident(), backgroundColor));
                    table.addCell(createCell(reclamation.getReference(), backgroundColor));
                    table.addCell(createCell(reclamation.getTypeIncident(), backgroundColor));
                    table.addCell(createCell(reclamation.getDateRec() != null ? reclamation.getDateRec().toString() : "", backgroundColor));
                    table.addCell(createCell(reclamation.getDateNotif() != null ? reclamation.getDateNotif().toString() : "", backgroundColor));
                    table.addCell(createCell(reclamation.getDuree(), backgroundColor));
                    table.addCell(createCell(reclamation.getInspection() != null ? reclamation.getInspection().toString() : "", backgroundColor));
                    table.addCell(createCell(reclamation.getRecevoirInspection() != null ? reclamation.getRecevoirInspection().toString() : "", backgroundColor));
                    table.addCell(createCell(reclamation.getResponsabilite(), backgroundColor));
                    table.addCell(createCell(reclamation.getNotifierClient(), backgroundColor));
                    table.addCell(createCell(reclamation.getDemanderFacture(), backgroundColor));
                    table.addCell(createCell(reclamation.getRecevoirFacture(), backgroundColor));
                    table.addCell(createCell(reclamation.getQuittance(), backgroundColor));
                }

                document.add(table);
                document.close();

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("PDF generated successfully!");
                alert.showAndWait();

            } catch (Exception e) {
                // Handle exceptions, including file writing issues
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while generating the PDF.");
                alert.showAndWait();
            }
        }
    }

    // Function to create a cell with specified background color or white if null
    private Cell createCell(Object content, DeviceRgb backgroundColor) {
        String contentStr = content != null ? content.toString() : ""; // Convert content to string
        Cell cell = new Cell().add(new Paragraph(contentStr));
        cell.setWidth(50f); // Set fixed width
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        return cell;
    }

    @FXML
    public void generateExcelOnAction(MouseEvent mouseEvent) {
        Window stage = tableRec.getScene().getWindow();

        // Choose a file to save the Excel
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Reclamations");

                // Create a header row, excluding "Notifier Assurance"
                Row headerRow = sheet.createRow(0);
                int colIndex = 0;
                for (TableColumn<Reclamation, ?> column : tableRec.getColumns()) {
                    // Skip the "Notifier Assurance" column
                    if (!"Notifier Assurance".equals(column.getText())) {
                        org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(colIndex++);
                        cell.setCellValue(column.getText());
                        CellStyle headerCellStyle = workbook.createCellStyle();
                        Font headerFont = workbook.createFont();
                        headerFont.setBold(true);
                        headerCellStyle.setFont(headerFont);
                        cell.setCellStyle(headerCellStyle);
                    }
                }

                // Map to store color assignments based on grouped criteria
                Map<String, Short> colorMap = new HashMap<>();
                Set<String> keysSet = new HashSet<>();

                // Light colors array
                short[] colors = {
                        IndexedColors.LIGHT_BLUE.getIndex(),
                        IndexedColors.LIGHT_GREEN.getIndex(),
                        IndexedColors.ROSE.getIndex()
                };

                int colorIndex = 0;

                // First pass to determine which keys need a color
                for (Reclamation reclamation : tableRec.getItems()) {
                    String key = reclamation.getAdresse() + "_" + reclamation.getLieuIncident() + "_" + reclamation.getDateIncident().toString();
                    keysSet.add(key);
                }

                // Assign colors to keys that need them
                for (String key : keysSet) {
                    long count = tableRec.getItems().stream().filter(r -> (r.getAdresse() + "_" + r.getLieuIncident() + "_" + r.getDateIncident().toString()).equals(key)).count();
                    if (count > 1) {
                        colorMap.put(key, colors[colorIndex % colors.length]);
                        colorIndex++;
                    }
                }

                // Add table rows with conditional coloring
                int rowNum = 1;
                for (Reclamation reclamation : tableRec.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    String key = reclamation.getAdresse() + "_" + reclamation.getLieuIncident() + "_" + reclamation.getDateIncident().toString();
                    Short backgroundColor = colorMap.getOrDefault(key, null);

                    colIndex = 0;
                    for (TableColumn<Reclamation, ?> column : tableRec.getColumns()) {
                        // Skip the "Notifier Assurance" column
                        if (!"Notifier Assurance".equals(column.getText())) {
                            org.apache.poi.ss.usermodel.Cell cell = row.createCell(colIndex++);
                            Object cellValue = column.getCellData(reclamation);
                            if (cellValue instanceof LocalDate) {
                                cell.setCellValue(((LocalDate) cellValue).toString());
                            } else if (cellValue instanceof Date) {
                                cell.setCellValue(((Date) cellValue).toString());
                            } else {
                                cell.setCellValue(cellValue != null ? cellValue.toString() : "");
                            }
                        }
                    }

                    if (backgroundColor != null) {
                        CellStyle style = workbook.createCellStyle();
                        style.setFillForegroundColor(backgroundColor);
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        for (org.apache.poi.ss.usermodel.Cell cell : row) {
                            cell.setCellStyle(style);
                        }
                    }
                }

                // Autosize columns
                for (int i = 0; i < tableRec.getColumns().size(); i++) {
                    // Only autosize columns that are included
                    if (!"Notifier Assurance".equals(tableRec.getColumns().get(i).getText())) {
                        sheet.autoSizeColumn(i);
                    }
                }

                // Write the output to file
                try (FileOutputStream fileOut = new FileOutputStream(selectedFile)) {
                    workbook.write(fileOut);
                }

                // Show a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Excel generated successfully!");
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public class DetailsButtonCell extends TableCell<Reclamation, Void> {
        private final Button button = new Button("Détails");
        private final ReclamationServices reclamationServices = new ReclamationServices();

        public DetailsButtonCell() {
            button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
            button.setPadding(new Insets(5, 10, 5, 10));
            button.setEffect(new DropShadow(5, Color.GRAY));

            // Hover animation
            button.setOnMouseEntered(event -> {
                button.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-font-size: 14px;");
                ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
                st.setToX(1.1);
                st.setToY(1.1);
                st.play();
            });
            button.setOnMouseExited(event -> {
                button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
                ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });

            button.setOnAction(event -> {
                Reclamation selectedReclamation = getTableView().getItems().get(getIndex());
                openFactureInterface(selectedReclamation);
            });

            setGraphic(button);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || getTableView() == null) {
                setGraphic(null);
            } else {
                setGraphic(button);
            }
        }

        private void openFactureInterface(Reclamation reclamation) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/facture.fxml"));
                Parent root = loader.load();
                factureController controller = loader.getController();
                controller.setSelectedReclamation(reclamation);

                Stage stage = new Stage();
                stage.setTitle("Facture Details");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
    }}

