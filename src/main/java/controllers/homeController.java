package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {
    @FXML
    private ImageView homeImageView;
    @FXML
    private ImageView fbImageView;
    @FXML
    private ImageView instagImageView;
    @FXML
    private ImageView youtubeImageView;
    @FXML
    private ImageView xImageView;
    @FXML
    private ImageView linkedinImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("target/classes/homeanim.gif");
        Image userImage = new Image(logoFile.toURI().toString());
        homeImageView.setImage(userImage);
        File fbFile = new File("target/classes/facebook.png");
        Image fbImage = new Image(fbFile.toURI().toString());
        fbImageView.setImage(fbImage);
        File instFile = new File("target/classes/social.png");
        Image insImage = new Image(instFile.toURI().toString());
        instagImageView.setImage(insImage);
        File ytFile = new File("target/classes/youtube.png");
        Image ytImage = new Image(ytFile.toURI().toString());
        youtubeImageView.setImage(ytImage);
        File xFile = new File("target/classes/twitter.png");
        Image xImage = new Image(xFile.toURI().toString());
        xImageView.setImage(xImage);
        File linkedFile = new File("target/classes/linked.png");
        Image linkedImage = new Image(linkedFile.toURI().toString());
        linkedinImageView.setImage(linkedImage);

    }
    @FXML


    public void openLink(javafx.scene.input.MouseEvent mouseEvent) throws URISyntaxException, IOException {
        System.out.println("link clicked");
        Desktop.getDesktop().browse(new URI("https://www.facebook.com/steg.tunisie"));
    }

    public void openInsta(MouseEvent mouseEvent) throws URISyntaxException, IOException {
        System.out.println("link clicked");
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/stegtunisie?tknfv=%3Ae48724d2-8c24-4906-9759-f72d9fe41llp11j"));
    }

    public void openYoutube(MouseEvent mouseEvent) throws URISyntaxException, IOException {
        System.out.println("link clicked");
        Desktop.getDesktop().browse(new URI("https://www.youtube.com/channel/UCzlkr6sWAB9RKm-KSRjq9vQ"));
    }

    public void openX(MouseEvent mouseEvent) throws URISyntaxException, IOException {
        System.out.println("link clicked");
        Desktop.getDesktop().browse(new URI("https://twitter.com/STEG_Tunisie?tknfv=%3Fe48724d2-8c24-4906-9759-f72d9fe41ppp111"));
    }

    public void openLinked(MouseEvent mouseEvent) throws URISyntaxException, IOException {
        System.out.println("link clicked");
        Desktop.getDesktop().browse(new URI("https://www.linkedin.com/company/steg-tunisie/?tknfv=?e48724d2-8c24-4906-9759-f72d9fe41ppp111"));
    }
}