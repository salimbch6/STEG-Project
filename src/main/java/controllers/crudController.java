package controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;
import utils.EncryptionUtils;
import utils.Session;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class crudController implements Initializable {
    @FXML
    private TableView<User> userT;
    @FXML
    private TableColumn<User, Void> actionColumn;
    @FXML
    private ImageView userIconView;
    @FXML
    private ImageView logoImageView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<User, String> imageColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> numberColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private Hyperlink signoutLink;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private Circle imageView;
    @FXML
    private Button pdfButton;

    private UserServices userServices;

    MyDataBase connectNow = new MyDataBase();
    Connection connectDB = connectNow.getconn();
    URL imageUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userServices = new UserServices();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("account_id"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("profilePic"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        passwordColumn.setCellValueFactory(data -> {
            String hashedPassword = EncryptionUtils.hashPassword(data.getValue().getPassword());
            return new SimpleStringProperty(hashedPassword);
        });

        actionColumn.setCellFactory(column -> new ActionButtonCell());

        populateTable();
    }

    void populateTable() {
        userT.getItems().clear();
        try {
            ResultSet resultSet = userServices.getAllUsers(connectDB);
            while (resultSet.next()) {
                int account_id = resultSet.getInt("account_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String img = resultSet.getString("profilePic");
                int etat = resultSet.getInt("etat");
                String number = resultSet.getString("number");

                User user = new User(account_id, 1, firstname, lastname, username, email, password, img, etat, number);
                userT.getItems().add(user);
            }
            if (!userT.getItems().isEmpty()) {
                System.out.println("Users successfully retrieved and added to TableView.");
            } else {
                System.out.println("No users found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        User selectedUser = userT.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userServices.deleteUser(connectDB, selectedUser.getAccount_id());
                userT.getItems().remove(selectedUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    public void updateButtonOnAction(ActionEvent actionEvent) {
        User selectedUser = userT.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                Stage stage = (Stage) updateButton.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/update.fxml"));
                Parent root = loader.load();
                updateController updateController = loader.getController();
                updateController.initData(selectedUser);
                Stage updateStage = new Stage();
                updateStage.setScene(new Scene(root));
                updateStage.setTitle("Update User");
                updateStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to update.");
            alert.showAndWait();
        }
    }



    @FXML
    public void searchOnAction(ActionEvent actionEvent) {
        String query = searchTextField.getText().trim();
        if (!query.isEmpty()) {
            searchUsers(query);
        } else {
            populateTable();
        }
    }

    private void searchUsers(String query) {
        userT.getItems().clear();
        try {
            ResultSet resultSet = userServices.searchUsers(connectDB, query);
            while (resultSet.next()) {
                int account_id = resultSet.getInt("account_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String img = resultSet.getString("profilePic");
                String number = resultSet.getString("number");
                User user = new User(account_id, 1, firstname, lastname, username, email, password, img, 0, number);
                userT.getItems().add(user);
            }
            if (!userT.getItems().isEmpty()) {
                System.out.println("Users matching the search query successfully retrieved and added to TableView.");
            } else {
                System.out.println("No users found matching the search query.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateOnAction(ActionEvent actionEvent) {
        User selectedUser = userT.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                createPdf(selectedUser);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Generated");
                alert.setHeaderText(null);
                alert.setContentText("PDF file for the selected user has been generated.");
                alert.showAndWait();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to generate PDF.");
            alert.showAndWait();
        }
    }

    private void createPdf(User user) throws FileNotFoundException {
        String dest = "user_" + user.getAccount_id() + ".pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("User Details"));
        document.add(new Paragraph("ID: " + user.getAccount_id()));
        document.add(new Paragraph("First Name: " + user.getFirstname()));
        document.add(new Paragraph("Last Name: " + user.getLastname()));
        document.add(new Paragraph("Username: " + user.getUsername()));
        document.add(new Paragraph("Email: " + user.getEmail()));
        document.add(new Paragraph("Password: " + user.getPassword()));
        document.add(new Paragraph("Profile Picture: " + user.getProfilePic()));
        document.add(new Paragraph("Number: " + user.getNumber()));

        document.close();
    }

    private class ActionButtonCell extends TableCell<User, Void> {
        private final Button actionButton = new Button();
        private final MyDataBase connectNow = new MyDataBase();
        private final UserServices userServices = new UserServices();

        ActionButtonCell() {
            actionButton.setOnAction(event -> {
                User user = getTableView().getItems().get(getIndex());
                Connection connectDB = connectNow.getconn();
                int newEtat = (user.getEtat() == 0) ? 1 : 0;
                userServices.updateEtat(connectDB, user.getAccount_id(), newEtat);
                user.setEtat(newEtat);
                getTableView().refresh();
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                User user = getTableView().getItems().get(getIndex());
                if (user.getEtat() == 0) {
                    actionButton.setText("Ban");
                    actionButton.getStyleClass().removeAll("unban-button");
                    actionButton.getStyleClass().add("ban-button");
                } else {
                    actionButton.setText("Unban");
                    actionButton.getStyleClass().removeAll("ban-button");
                    actionButton.getStyleClass().add("unban-button");
                }
                actionButton.getStyleClass().add("action-button");
                setGraphic(actionButton);
            }
        }
    }
}
