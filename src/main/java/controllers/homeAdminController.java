package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import models.MonthlyStats;
import services.ReclamationServices;

import java.io.File;
import java.util.Map;

public class homeAdminController {

        @FXML
        private TableView<MonthlyStats> StatsTab;

        @FXML
        private TableColumn<?, ?> titreS;
        @FXML
        private TableColumn<MonthlyStats, Integer> janvierS;
        @FXML
        private TableColumn<MonthlyStats, Integer> fevrierS;
        @FXML
        private TableColumn<MonthlyStats, Integer> marsS;
        @FXML
        private TableColumn<MonthlyStats, Integer> avrilS;
        @FXML
        private TableColumn<MonthlyStats, Integer> maiS;
        @FXML
        private TableColumn<MonthlyStats, Integer> juinS;
        @FXML
        private TableColumn<MonthlyStats, Integer> juilletS;
        @FXML
        private TableColumn<MonthlyStats, Integer> aoutS;
        @FXML
        private TableColumn<MonthlyStats, Integer> septembreS;
        @FXML
        private TableColumn<MonthlyStats, Integer> octobreS;
        @FXML
        private TableColumn<MonthlyStats, Integer> novembreS;
        @FXML
        private TableColumn<MonthlyStats, Integer> decembreS;
        @FXML
        private ImageView leftArrow;
        @FXML
        private ImageView rightArrow;
        @FXML
        private Label label2024;
        private ReclamationServices reclamationServices = new ReclamationServices();
        private boolean is2024 = true; // Track the current year


        @FXML
        public void initialize() {
                setupTableView();
                loadData();
                File laFile = new File("target/classes/left-arrow.png");
                Image laImage = new Image(laFile.toURI().toString());
                leftArrow.setImage(laImage);

                File raFile = new File("target/classes/right-arrow.png");
                Image raImage = new Image(raFile.toURI().toString());
                rightArrow.setImage(raImage);
                label2024.setText("2024");
        }

        private void setupTableView() {
                titreS.setCellValueFactory(new PropertyValueFactory<>("titre"));
                janvierS.setCellValueFactory(new PropertyValueFactory<>("janvier"));
                fevrierS.setCellValueFactory(new PropertyValueFactory<>("fevrier"));
                marsS.setCellValueFactory(new PropertyValueFactory<>("mars"));
                avrilS.setCellValueFactory(new PropertyValueFactory<>("avril"));
                maiS.setCellValueFactory(new PropertyValueFactory<>("mai"));
                juinS.setCellValueFactory(new PropertyValueFactory<>("juin"));
                juilletS.setCellValueFactory(new PropertyValueFactory<>("juillet"));
                aoutS.setCellValueFactory(new PropertyValueFactory<>("aout"));
                septembreS.setCellValueFactory(new PropertyValueFactory<>("septembre"));
                octobreS.setCellValueFactory(new PropertyValueFactory<>("octobre"));
                novembreS.setCellValueFactory(new PropertyValueFactory<>("novembre"));
                decembreS.setCellValueFactory(new PropertyValueFactory<>("decembre"));

                centerTextInColumns(janvierS, fevrierS, marsS, avrilS, maiS, juinS, juilletS, aoutS, septembreS, octobreS, novembreS, decembreS);
        }

        @SafeVarargs
        private final void centerTextInColumns(TableColumn<MonthlyStats, Integer>... columns) {
                for (TableColumn<MonthlyStats, Integer> column : columns) {
                        column.setCellFactory(tc -> {
                                TableCell<MonthlyStats, Integer> cell = new TableCell<>() {
                                        @Override
                                        protected void updateItem(Integer item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (empty || item == null) {
                                                        setText(null);
                                                } else {
                                                        setText(item.toString());
                                                }
                                        }
                                };
                                cell.setStyle("-fx-alignment: CENTER;");
                                return cell;
                        });
                }
        }

        private void loadData() {
                Map<String, Integer> reclamationsPerMonth = reclamationServices.getTotalReclamationsPerMonth(selectedYear);
                Map<String, Integer> uniqueIncidentsPerMonth = reclamationServices.getTotalUniqueIncidentsPerMonth(selectedYear);
                Map<String, Integer> stegReclamationsPerMonth = reclamationServices.getTotalReclamationsSTEGPerMonth(selectedYear);
                Map<String, Integer> nonStegReclamationsPerMonth = reclamationServices.getTotalReclamationsNonSTEGPerMonth(selectedYear);
                Map<String, Integer> corporelleIncidentsPerMonth = reclamationServices.getTotalCorporelleIncidentsPerMonth(selectedYear);
                Map<String, Integer> incendieIncidentsPerMonth = reclamationServices.getTotalIncendieIncidentsPerMonth(selectedYear);
                Map<String, Integer> resoluReclamationPerMonth = reclamationServices.getTotalResoluReclamationsPerMonth(selectedYear);
                Map<String, Integer> totalPriceOfDamagedObjectsPerMonth = reclamationServices.getTotalPriceOfDamagedObjectsPerMonth(selectedYear);
                Map<String, Integer> remboursementPerMonth = reclamationServices.getTotalRemboursementPerMonth(selectedYear);
                Map<String, Integer> averageDurationPerMonth = reclamationServices.getAverageDurationPerMonth(selectedYear);
                Map<String, Integer> averageInspectionDurationPerMonth = reclamationServices.getAverageInspectionDurationPerMonth(selectedYear);
                Map<String, Integer> averageRemboursementDurationPerMonth = reclamationServices.getAverageRemboursementDurationPerMonth(selectedYear);

                MonthlyStats reclamationsStats = new MonthlyStats(
                        "Nombre totale des réclamations",
                        reclamationsPerMonth.getOrDefault("January", 0),
                        reclamationsPerMonth.getOrDefault("February", 0),
                        reclamationsPerMonth.getOrDefault("March", 0),
                        reclamationsPerMonth.getOrDefault("April", 0),
                        reclamationsPerMonth.getOrDefault("May", 0),
                        reclamationsPerMonth.getOrDefault("June", 0),
                        reclamationsPerMonth.getOrDefault("July", 0),
                        reclamationsPerMonth.getOrDefault("August", 0),
                        reclamationsPerMonth.getOrDefault("September", 0),
                        reclamationsPerMonth.getOrDefault("October", 0),
                        reclamationsPerMonth.getOrDefault("November", 0),
                        reclamationsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats uniqueIncidentsStats = new MonthlyStats(
                        "Nombre totale des incidents uniques",
                        uniqueIncidentsPerMonth.getOrDefault("January", 0),
                        uniqueIncidentsPerMonth.getOrDefault("February", 0),
                        uniqueIncidentsPerMonth.getOrDefault("March", 0),
                        uniqueIncidentsPerMonth.getOrDefault("April", 0),
                        uniqueIncidentsPerMonth.getOrDefault("May", 0),
                        uniqueIncidentsPerMonth.getOrDefault("June", 0),
                        uniqueIncidentsPerMonth.getOrDefault("July", 0),
                        uniqueIncidentsPerMonth.getOrDefault("August", 0),
                        uniqueIncidentsPerMonth.getOrDefault("September", 0),
                        uniqueIncidentsPerMonth.getOrDefault("October", 0),
                        uniqueIncidentsPerMonth.getOrDefault("November", 0),
                        uniqueIncidentsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats stegReclamationsStats = new MonthlyStats(
                        "Nombre des réclamations responsabilité STEG",
                        stegReclamationsPerMonth.getOrDefault("January", 0),
                        stegReclamationsPerMonth.getOrDefault("February", 0),
                        stegReclamationsPerMonth.getOrDefault("March", 0),
                        stegReclamationsPerMonth.getOrDefault("April", 0),
                        stegReclamationsPerMonth.getOrDefault("May", 0),
                        stegReclamationsPerMonth.getOrDefault("June", 0),
                        stegReclamationsPerMonth.getOrDefault("July", 0),
                        stegReclamationsPerMonth.getOrDefault("August", 0),
                        stegReclamationsPerMonth.getOrDefault("September", 0),
                        stegReclamationsPerMonth.getOrDefault("October", 0),
                        stegReclamationsPerMonth.getOrDefault("November", 0),
                        stegReclamationsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats nonStegReclamationsStats = new MonthlyStats(
                        "Nombre des réclamations autres",
                        nonStegReclamationsPerMonth.getOrDefault("January", 0),
                        nonStegReclamationsPerMonth.getOrDefault("February", 0),
                        nonStegReclamationsPerMonth.getOrDefault("March", 0),
                        nonStegReclamationsPerMonth.getOrDefault("April", 0),
                        nonStegReclamationsPerMonth.getOrDefault("May", 0),
                        nonStegReclamationsPerMonth.getOrDefault("June", 0),
                        nonStegReclamationsPerMonth.getOrDefault("July", 0),
                        nonStegReclamationsPerMonth.getOrDefault("August", 0),
                        nonStegReclamationsPerMonth.getOrDefault("September", 0),
                        nonStegReclamationsPerMonth.getOrDefault("October", 0),
                        nonStegReclamationsPerMonth.getOrDefault("November", 0),
                        nonStegReclamationsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats corporelleIncidentsStats = new MonthlyStats(
                        "Nombre des incidents corporels",
                        corporelleIncidentsPerMonth.getOrDefault("January", 0),
                        corporelleIncidentsPerMonth.getOrDefault("February", 0),
                        corporelleIncidentsPerMonth.getOrDefault("March", 0),
                        corporelleIncidentsPerMonth.getOrDefault("April", 0),
                        corporelleIncidentsPerMonth.getOrDefault("May", 0),
                        corporelleIncidentsPerMonth.getOrDefault("June", 0),
                        corporelleIncidentsPerMonth.getOrDefault("July", 0),
                        corporelleIncidentsPerMonth.getOrDefault("August", 0),
                        corporelleIncidentsPerMonth.getOrDefault("September", 0),
                        corporelleIncidentsPerMonth.getOrDefault("October", 0),
                        corporelleIncidentsPerMonth.getOrDefault("November", 0),
                        corporelleIncidentsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats incendieIncidentsStats = new MonthlyStats(
                        "Nombre des incidents incendie",
                        incendieIncidentsPerMonth.getOrDefault("January", 0),
                        incendieIncidentsPerMonth.getOrDefault("February", 0),
                        incendieIncidentsPerMonth.getOrDefault("March", 0),
                        incendieIncidentsPerMonth.getOrDefault("April", 0),
                        incendieIncidentsPerMonth.getOrDefault("May", 0),
                        incendieIncidentsPerMonth.getOrDefault("June", 0),
                        incendieIncidentsPerMonth.getOrDefault("July", 0),
                        incendieIncidentsPerMonth.getOrDefault("August", 0),
                        incendieIncidentsPerMonth.getOrDefault("September", 0),
                        incendieIncidentsPerMonth.getOrDefault("October", 0),
                        incendieIncidentsPerMonth.getOrDefault("November", 0),
                        incendieIncidentsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats resoluReclamationStats = new MonthlyStats(
                        "Nombre des réclamations résolues",
                        resoluReclamationPerMonth.getOrDefault("January", 0),
                        resoluReclamationPerMonth.getOrDefault("February", 0),
                        resoluReclamationPerMonth.getOrDefault("March", 0),
                        resoluReclamationPerMonth.getOrDefault("April", 0),
                        resoluReclamationPerMonth.getOrDefault("May", 0),
                        resoluReclamationPerMonth.getOrDefault("June", 0),
                        resoluReclamationPerMonth.getOrDefault("July", 0),
                        resoluReclamationPerMonth.getOrDefault("August", 0),
                        resoluReclamationPerMonth.getOrDefault("September", 0),
                        resoluReclamationPerMonth.getOrDefault("October", 0),
                        resoluReclamationPerMonth.getOrDefault("November", 0),
                        resoluReclamationPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats totalPriceOfDamagedObjectsStats = new MonthlyStats(
                        "Prix total des objets endommagés",
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("January", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("February", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("March", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("April", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("May", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("June", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("July", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("August", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("September", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("October", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("November", 0),
                        totalPriceOfDamagedObjectsPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats remboursementStats = new MonthlyStats(
                        "Montant total des remboursements",
                        remboursementPerMonth.getOrDefault("January", 0),
                        remboursementPerMonth.getOrDefault("February", 0),
                        remboursementPerMonth.getOrDefault("March", 0),
                        remboursementPerMonth.getOrDefault("April", 0),
                        remboursementPerMonth.getOrDefault("May", 0),
                        remboursementPerMonth.getOrDefault("June", 0),
                        remboursementPerMonth.getOrDefault("July", 0),
                        remboursementPerMonth.getOrDefault("August", 0),
                        remboursementPerMonth.getOrDefault("September", 0),
                        remboursementPerMonth.getOrDefault("October", 0),
                        remboursementPerMonth.getOrDefault("November", 0),
                        remboursementPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats averageDurationStats = new MonthlyStats(
                        "Durée moyenne des réclamations",
                        averageDurationPerMonth.getOrDefault("January", 0),
                        averageDurationPerMonth.getOrDefault("February", 0),
                        averageDurationPerMonth.getOrDefault("March", 0),
                        averageDurationPerMonth.getOrDefault("April", 0),
                        averageDurationPerMonth.getOrDefault("May", 0),
                        averageDurationPerMonth.getOrDefault("June", 0),
                        averageDurationPerMonth.getOrDefault("July", 0),
                        averageDurationPerMonth.getOrDefault("August", 0),
                        averageDurationPerMonth.getOrDefault("September", 0),
                        averageDurationPerMonth.getOrDefault("October", 0),
                        averageDurationPerMonth.getOrDefault("November", 0),
                        averageDurationPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats averageInspectionDurationStats = new MonthlyStats(
                        "Durée moyenne d'inspection",
                        averageInspectionDurationPerMonth.getOrDefault("January", 0),
                        averageInspectionDurationPerMonth.getOrDefault("February", 0),
                        averageInspectionDurationPerMonth.getOrDefault("March", 0),
                        averageInspectionDurationPerMonth.getOrDefault("April", 0),
                        averageInspectionDurationPerMonth.getOrDefault("May", 0),
                        averageInspectionDurationPerMonth.getOrDefault("June", 0),
                        averageInspectionDurationPerMonth.getOrDefault("July", 0),
                        averageInspectionDurationPerMonth.getOrDefault("August", 0),
                        averageInspectionDurationPerMonth.getOrDefault("September", 0),
                        averageInspectionDurationPerMonth.getOrDefault("October", 0),
                        averageInspectionDurationPerMonth.getOrDefault("November", 0),
                        averageInspectionDurationPerMonth.getOrDefault("December", 0)
                );

                MonthlyStats averageRemboursementDurationStats = new MonthlyStats(
                        "Durée moyenne de remboursement",
                        averageRemboursementDurationPerMonth.getOrDefault("January", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("February", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("March", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("April", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("May", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("June", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("July", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("August", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("September", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("October", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("November", 0),
                        averageRemboursementDurationPerMonth.getOrDefault("December", 0)
                );

                ObservableList<MonthlyStats> data = FXCollections.observableArrayList();
                data.add(reclamationsStats);
                data.add(uniqueIncidentsStats);
                data.add(stegReclamationsStats);
                data.add(nonStegReclamationsStats);
                data.add(corporelleIncidentsStats);
                data.add(incendieIncidentsStats);
                data.add(resoluReclamationStats);
                data.add(totalPriceOfDamagedObjectsStats);
                data.add(remboursementStats);
                data.add(averageDurationStats);
                data.add(averageInspectionDurationStats);
                data.add(averageRemboursementDurationStats);


                StatsTab.setItems(data);
        }

        @FXML
        private void swipeYearOnAction(MouseEvent mouseEvent) {
                if (is2024) {
                        animateYearChange("2023");
                        setSelectedYear(2023); // Update selected year
                        is2024 = false;
                }
        }

        @FXML
        private void swipeYear2OnAction(MouseEvent mouseEvent) {
                if (!is2024) {
                        animateYearChange("2024");
                        setSelectedYear(2024); // Update selected year
                        is2024 = true;
                }
        }

        private void animateYearChange(String newYear) {
                TranslateTransition transition = new TranslateTransition(Duration.millis(300));
                transition.setFromX(label2024.getTranslateX());
                transition.setToX(label2024.getTranslateX() + 20);
                transition.setCycleCount(2);
                transition.setAutoReverse(true);

                transition.setOnFinished(event -> {
                        label2024.setText(newYear);
                        loadData(); // Ensure data is loaded after the animation
                });

                transition.play();
        }

        private int selectedYear = 2024; // Default year or initial value

        public void setSelectedYear(int year) {
                this.selectedYear = year;
                loadData();
        }



}
