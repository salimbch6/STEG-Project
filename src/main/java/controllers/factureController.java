package controllers;
import javafx.animation.FadeTransition;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import models.Facture;
import models.Reclamation;
import services.FactureServices;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import utils.MyDataBase;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class factureController implements Initializable {

    @FXML
    private TableView<Facture> factureTab;
    @FXML
    private TableColumn<Facture, String> objEndoF;
    @FXML
    private TableColumn<Facture, String> marqueF;
    @FXML
    private TableColumn<Facture, Double> prixF;
    @FXML
    private Button addButton;
    @FXML
    private TableColumn<Facture, Boolean> reparabiliteF;

    private FactureServices factureServices;
    private Reclamation selectedReclamation;
    private Facture totalRow;
    private Facture remboursementRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize FactureServices with a connection
        Connection connection = MyDataBase.getInstance().getconn();
        if (connection == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Connection Error", "Failed to connect to the database.");
            return;
        }

        factureServices = new FactureServices(connection);
        if (factureServices == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Facture Services Initialization Error", "Failed to initialize Facture Services.");
            return;
        }

        // Initialize TableView columns
        objEndoF.setCellValueFactory(new PropertyValueFactory<>("objet"));
        marqueF.setCellValueFactory(new PropertyValueFactory<>("marque"));
        reparabiliteF.setCellValueFactory(cellData -> cellData.getValue().reparabiliteProperty());
        prixF.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Set cell factories for CheckBox
        reparabiliteF.setCellFactory(col -> new TableCell<Facture, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Facture facture = getTableView().getItems().get(getIndex());
                    boolean isChecked = checkBox.isSelected();
                    facture.setReparabilite(isChecked);

                    // Recalculate the price based on reparabilite
                    double originalPrice = facture.getPrix();
                    if (!isChecked) {
                        facture.setPrix(originalPrice * 0.5); // 50% of the price if not reparable
                    } else {
                        facture.setPrix(originalPrice * 2); // Restoring to 100% if reparable
                    }
                    updateFacture(facture);
                    updateTotalRow(); // Update the total row after recalculating the price
                    getTableView().refresh(); // Refresh the table to reflect changes
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Facture facture = getTableRow().getItem();
                    if (facture.isTotalRow() || facture.isRemboursementRow()) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(item != null && item);
                        setGraphic(checkBox);
                    }
                }
            }
        });

        // Make the columns editable
        factureTab.setEditable(true);
        makeStringColumnEditable(objEndoF);
        makeStringColumnEditable(marqueF);
        makeDoubleColumnEditable(prixF);

        // Handle edit commits
        objEndoF.setOnEditCommit(event -> {
            Facture facture = event.getRowValue();
            facture.setObjet(event.getNewValue());
            updateFacture(facture);
        });

        marqueF.setOnEditCommit(event -> {
            Facture facture = event.getRowValue();
            facture.setMarque(event.getNewValue());
            updateFacture(facture);
        });

        prixF.setOnEditCommit(event -> {
            Facture facture = event.getRowValue();
            if (!facture.isTotalRow() && !facture.isRemboursementRow()) {
                String newValue = event.getNewValue() != null ? event.getNewValue().toString() : "";
                facture.setPrix(newValue.isEmpty() ? 0.0 : Double.parseDouble(newValue));
                updateFacture(facture);
                updateTotalRow();
            }
        });

        // Create and add the total row
        totalRow = Facture.createTotalRow();
        remboursementRow = Facture.createRemboursementRow(0.0);  // Initialize with 0.0
    }

    public void setSelectedReclamation(Reclamation reclamation) {
        this.selectedReclamation = reclamation;
        if (factureServices == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Facture Services Not Initialized", "Facture Services is null.");
            return;
        }
        loadFactureDetails();
    }


    private void loadFactureDetails() {
        if (selectedReclamation != null) {
            try {
                List<Facture> factures = factureServices.getFacturesByReclamationId(selectedReclamation.getId_reclamation());
                factures.forEach(facture -> {
                    // Ensure reparabilite is true by default if not explicitly set
                    if (facture.reparabiliteProperty() == null) {
                        facture.setReparabilite(true);
                    }
                    System.out.println("Reparabilite: " + facture.isReparabilite());
                });
                factureTab.setItems(FXCollections.observableArrayList(factures));
                updateTotalRow();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load facture details.", e.getMessage());
            }
        }
    }



    private void makeStringColumnEditable(TableColumn<Facture, String> column) {
        column.setCellFactory(col -> new TextFieldTableCell<>(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }) {
            @Override
            public void startEdit() {
                super.startEdit();
                TextField textField = (TextField) getGraphic();
                if (textField != null) {
                    textField.getStyleClass().add("text-field");
                }
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
            }
        });
    }

    private void makeDoubleColumnEditable(TableColumn<Facture, Double> column) {
        column.setCellFactory(col -> new TextFieldTableCell<Facture, Double>(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object == null || object == 0.0 ? "" : String.format("%,.0f", object);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return string.isEmpty() ? 0.0 : Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }) {
            @Override
            public void startEdit() {
                super.startEdit();
                TextField textField = (TextField) getGraphic();
                if (textField != null) {
                    textField.getStyleClass().add("text-field");
                }
            }

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
            }
        });
    }

    @FXML
    private void handleAddRow(ActionEvent event) {
        if (selectedReclamation != null) {
            Facture newFacture = new Facture(selectedReclamation.getId_reclamation(), 0, "", "", true,0.0);
            try {
                factureServices.addFacture(newFacture);
                factureTab.getItems().add(newFacture);
                updateTotalRow();
                animateNewRow(newFacture);  // Add this line to animate the new row
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add facture.", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No reclamation selected.", "Please select a reclamation first.");
        }
    }

    private void animateNewRow(Facture newFacture) {
        int rowIndex = factureTab.getItems().indexOf(newFacture);
        if (rowIndex >= 0) {
            TableRow<Facture> row = (TableRow<Facture>) factureTab.lookup(" #row" + rowIndex);
            if (row != null) {
                FadeTransition ft = new FadeTransition(Duration.millis(1000), row);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            }
        }
    }

    private void updateFacture(Facture facture) {
        try {
            factureServices.updateFacture(facture);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update facture.", e.getMessage());
        }
    }

    private void updateTotalRow() {
        double total = factureTab.getItems().stream()
                .filter(facture -> !facture.isTotalRow() && !facture.isRemboursementRow())
                .mapToDouble(Facture::getPrix)
                .sum();

        totalRow.setPrix(total);
        remboursementRow = Facture.createRemboursementRow(total);

        factureTab.getItems().removeIf(Facture::isTotalRow);
        factureTab.getItems().removeIf(Facture::isRemboursementRow);

        // Only add total and remboursement rows if there's a non-zero price
        if (total > 0) {
            factureTab.getItems().add(totalRow);
            factureTab.getItems().add(remboursementRow);
        }

        // Style the total and remboursement rows
        factureTab.setRowFactory(tv -> new TableRow<Facture>() {
            @Override
            protected void updateItem(Facture item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.isTotalRow()) {
                    setStyle(" -fx-text-fill: lightgreen; -fx-font-weight: bold;");
                } else if (item.isRemboursementRow()) {
                    setStyle("-fx-background-color: lightgreen; -fx-text-fill: black; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
