package models;

import java.util.Date;

public class Reclamation {
    private int id_reclamation,codePostal;
    private String nom,prenom,adresse,numTel,numCompteur,lieuIncident,objEndommage,marque;
            private Date dateIncident;

    public Reclamation(int codePostal, String nom, String prenom, String adresse, String numTel, String numCompteur, String lieuIncident, String objEndommage, String marque, Date dateIncident) {
        this.codePostal = codePostal;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numTel = numTel;
        this.numCompteur = numCompteur;
        this.lieuIncident = lieuIncident;
        this.objEndommage = objEndommage;
        this.marque = marque;
        this.dateIncident = dateIncident;
    }

    public Reclamation(int id_reclamation, int codePostal, String nom, String prenom, String adresse, String numTel, String numCompteur, String lieuIncident, String objEndommage, String marque, Date dateIncident) {
        this.id_reclamation = id_reclamation;
        this.codePostal = codePostal;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numTel = numTel;
        this.numCompteur = numCompteur;
        this.lieuIncident = lieuIncident;
        this.objEndommage = objEndommage;
        this.marque = marque;
        this.dateIncident = dateIncident;
    }

    public Reclamation(int reclamationId, String nom, String prenom, String adresse, int codePostal, String numTel, String numCompteur, Date dateIncident, String lieuIncident, String objetEndommage, String marque) {
        this.id_reclamation = reclamationId;
        this.codePostal = codePostal;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numTel = numTel;
        this.numCompteur = numCompteur;
        this.lieuIncident = lieuIncident;
        this.objEndommage = objetEndommage;
        this.marque = marque;
        this.dateIncident = dateIncident;
    }

    public int getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getNumCompteur() {
        return numCompteur;
    }

    public void setNumCompteur(String numCompteur) {
        this.numCompteur = numCompteur;
    }

    public String getLieuIncident() {
        return lieuIncident;
    }

    public void setLieuIncident(String lieuIncident) {
        this.lieuIncident = lieuIncident;
    }

    public String getObjEndommage() {
        return objEndommage;
    }

    public void setObjEndommage(String objEndommage) {
        this.objEndommage = objEndommage;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Date getDateIncident() {
        return dateIncident;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id_reclamation=" + id_reclamation +
                ", codePostal=" + codePostal +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numTel='" + numTel + '\'' +
                ", numCompteur='" + numCompteur + '\'' +
                ", lieuIncident='" + lieuIncident + '\'' +
                ", objEndommage='" + objEndommage + '\'' +
                ", marque='" + marque + '\'' +
                ", dateIncident=" + dateIncident +
                '}';
    }

    public void setDateIncident(Date dateIncident) {
        this.dateIncident = dateIncident;
    }
}
