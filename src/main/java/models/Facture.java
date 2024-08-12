package models;

import javafx.beans.property.*;

public class Facture extends Reclamation {

    private final IntegerProperty id_facture = new SimpleIntegerProperty();
    private final StringProperty objet = new SimpleStringProperty();
    private final BooleanProperty reparabilite = new SimpleBooleanProperty();
    private final StringProperty marque = new SimpleStringProperty();
    private final DoubleProperty prix = new SimpleDoubleProperty();
    private static final int TOTAL_ROW_ID = -1;
    private static final int REMBOURSEMENT_ROW_ID = -2;

    public Facture(int id_reclamation, int id_facture, String objet, String marque, boolean reparabilite, double prix) {
        super(id_reclamation, 0, null, null, null, null, null, null, null, 0); // Adjust according to actual Reclamation constructor
        this.id_facture.set(id_facture);
        this.objet.set(objet);
        this.marque.set(marque);
        this.reparabilite.set(reparabilite);
        this.prix.set(prix);
    }

    public boolean isReparabilite() {
        return reparabilite.get();
    }

    public void setReparabilite(boolean reparabilite) {
        this.reparabilite.set(reparabilite);
    }

    public BooleanProperty reparabiliteProperty() {
        return reparabilite;
    }

    public int getId_facture() {
        return id_facture.get();
    }






    public void setId_facture(int id_facture) {
        this.id_facture.set(id_facture);
    }

    public IntegerProperty id_factureProperty() {
        return id_facture;
    }

    public String getObjet() {
        return objet.get();
    }

    public void setObjet(String objet) {
        this.objet.set(objet);
    }

    public StringProperty objetProperty() {
        return objet;
    }

    public String getMarque() {
        return marque.get();
    }

    public void setMarque(String marque) {
        this.marque.set(marque);
    }

    public StringProperty marqueProperty() {
        return marque;
    }

    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public DoubleProperty prixProperty() {
        return prix;
    }

    public boolean isTotalRow() {
        return getId_facture() == TOTAL_ROW_ID;
    }

    public static Facture createTotalRow() {
        return new Facture(0, TOTAL_ROW_ID, "Total", "",false, 0.0);
    }

    public boolean isRemboursementRow() {
        return getId_facture() == REMBOURSEMENT_ROW_ID;
    }

    public static Facture createRemboursementRow(double total) {
        double remboursement = 0.0;
        String description = "";
        if (total > 500000) {
            remboursement = total;
            description = "Le dossier doit être envoyé à l'assureur";
        } else if (total <= 200000) {
            remboursement = total;
            description = "Le montant à rembourser est de 100%";
        } else if (total < 500000) {
            remboursement = total * 0.7;
            description = "Le montant à rembourser est de 70%";
        }
        return new Facture(0, REMBOURSEMENT_ROW_ID, "Remboursement", description,false,remboursement);
    }
}
