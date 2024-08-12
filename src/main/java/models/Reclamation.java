package models;

import org.springframework.cglib.core.Local;

import java.util.Date;
import java.time.LocalDate;


public class Reclamation extends User {
    private int id_reclamation,etatNotif;
    private Integer duree,codePostal,dureeInspection,dureeNotifierClient,dureeRemboursement;
    private String nom,prenom,adresse,reference,numTel,numCompteur,lieuIncident,typeIncident,categorieIncident,responsabilite,quittance,resolution;
            private LocalDate dateIncident;

    private LocalDate dateRec,inspection,recevoirInspection,notifierClient,demanderFacture,recevoirFacture,dateNotif,signature;

    public Reclamation() {
        this.nom = "";
        this.prenom = "";
        this.adresse = "";
        this.reference = "";
        this.numTel = "";
        this.numCompteur = "";
        this.lieuIncident = "";
        this.typeIncident = "";
        this.categorieIncident = "";
        this.responsabilite = "";
        this.quittance = "";
        this.codePostal = null;
        this.dateIncident = null;
        this.dateRec = null;
        this.inspection = null;
        this.recevoirInspection = null;
        this.notifierClient = null;
        this.demanderFacture = null;
        this.recevoirFacture = null;
        this.dateNotif = null;
        this.etatNotif = 0;
        this.duree = null;
        this.resolution = null;
        this.dureeInspection = null;
        this.dureeRemboursement = null;
        this.dureeNotifierClient = null;
        this.signature = null;
    }

    public Reclamation(int codePostal, String nom, String prenom, String adresse, String numTel, String numCompteur, String lieuIncident,  LocalDate dateIncident, int etatNotif) {
        this.codePostal = codePostal;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numTel = numTel;
        this.numCompteur = numCompteur;
        this.lieuIncident = lieuIncident;

        this.dateIncident = dateIncident;
        this.etatNotif = etatNotif;
    }

    public Reclamation(int id_reclamation, int codePostal, String nom, String prenom, String adresse, String numTel, String numCompteur, String lieuIncident,  LocalDate dateIncident,int etatNotif) {
        this.id_reclamation = id_reclamation;
        this.codePostal = codePostal;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numTel = numTel;
        this.numCompteur = numCompteur;
        this.lieuIncident = lieuIncident;

        this.dateIncident = dateIncident;
        this.etatNotif = etatNotif;
    }

    public Reclamation(int reclamationId, String nom, String prenom, String adresse, Integer codePostal,String reference, String numTel, String numCompteur, LocalDate dateIncident, String lieuIncident,String typeIncident,String categorieIncident,  LocalDate dateRec,int etatNotif,LocalDate dateNotif,Integer duree,LocalDate inspection,LocalDate recevoirIncpection,Integer dureeInspection,String responsabilite,LocalDate notifierClient,LocalDate demanderFacture,Integer dureeNotifierClient,LocalDate recevoirFacture,String quittance,LocalDate signature,String resolution,Integer dureeRemboursement) {
        this.id_reclamation = reclamationId;
        this.codePostal = codePostal;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.reference = reference;
        this.numTel = numTel;
        this.numCompteur = numCompteur;
        this.lieuIncident = lieuIncident;
        this.typeIncident = typeIncident;
        this.categorieIncident = categorieIncident;

        this.dateIncident = dateIncident;
        this.dateRec = dateRec;
        this.etatNotif = etatNotif;
        this.dateNotif = dateNotif;
        this.duree = duree;
        this.inspection = inspection;
        this.recevoirInspection = recevoirIncpection;
        this.dureeInspection = dureeInspection;
        this.responsabilite = responsabilite;
        this.notifierClient = notifierClient;
        this.demanderFacture = demanderFacture;
        this.dureeNotifierClient = dureeNotifierClient;
        this.recevoirFacture = recevoirFacture;
        this.quittance = quittance;
        this.signature = signature;
        this.resolution = resolution;
        this.dureeRemboursement = dureeRemboursement;
    }






    public int getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public Integer getCodePostal() {
        return codePostal;
    }

    public String getResolution() {
        return resolution;
    }

    public LocalDate getSignature() {
        return signature;
    }

    public void setSignature(LocalDate signature) {
        this.signature = signature;
    }

    public Integer getDureeNotifierClient() {
        return dureeNotifierClient;
    }

    public void setDureeNotifierClient(Integer dureeNotifierClient) {
        this.dureeNotifierClient = dureeNotifierClient;
    }

    public Integer getDureeRemboursement() {
        return dureeRemboursement;
    }

    public void setDureeRemboursement(Integer dureeRemboursement) {
        this.dureeRemboursement = dureeRemboursement;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getDureeInspection() {
        return dureeInspection;
    }

    public void setDureeInspection(Integer dureeInspection) {
        this.dureeInspection = dureeInspection;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public int getEtatNotif() {
        return etatNotif;
    }

    public LocalDate getRecevoirFacture() {
        return recevoirFacture;
    }

    public String getQuittance() {
        return quittance;
    }

    public void setQuittance(String quittance) {
        this.quittance = quittance;
    }

    public void setRecevoirFacture(LocalDate recevoirFacture) {
        this.recevoirFacture = recevoirFacture;
    }

    public String getResponsabilite() {
        return responsabilite;
    }

    public void setResponsabilite(String responsabilite) {
        this.responsabilite = responsabilite;
    }

    public void setDateNotif(LocalDate dateNotif) {
        this.dateNotif = dateNotif;
    }

    public LocalDate getNotifierClient() {
        return notifierClient;
    }

    public void setNotifierClient(LocalDate notifierClient) {
        this.notifierClient = notifierClient;
    }

    public LocalDate getDemanderFacture() {
        return demanderFacture;
    }

    public void setDemanderFacture(LocalDate demanderFacture) {
        this.demanderFacture = demanderFacture;
    }

    public LocalDate getInspection() {
        return inspection;
    }

    public void setInspection(LocalDate inspection) {
        this.inspection = inspection;
    }

    public LocalDate getRecevoirInspection() {
        return recevoirInspection;
    }

    public void setRecevoirInspection(LocalDate recevoirInspection) {
        this.recevoirInspection = recevoirInspection;
    }


    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public LocalDate getDateNotif() {
        return dateNotif;
    }


    public void setEtatNotif(int etatNotif) {
        this.etatNotif = etatNotif;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTypeIncident() {
        return typeIncident;
    }

    public void setTypeIncident(String typeIncident) {
        this.typeIncident = typeIncident;
    }

    public String getCategorieIncident() {
        return categorieIncident;
    }

    public void setCategorieIncident(String categorieIncident) {
        this.categorieIncident = categorieIncident;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateRec() {
        return dateRec;
    }

    public void setDateRec(LocalDate dateRec) {
        this.dateRec = dateRec;
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

    public LocalDate getDateIncident() {
        return dateIncident;
    }

    public void setDateIncident(LocalDate dateIncident) {
        this.dateIncident = dateIncident;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id_reclamation=" + id_reclamation +
                ", etatNotif=" + etatNotif +
                ", duree=" + duree +
                ", codePostal=" + codePostal +
                ", dureeInspection=" + dureeInspection +
                ", dureeNotifierClient=" + dureeNotifierClient +
                ", dureeRemboursement=" + dureeRemboursement +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", reference='" + reference + '\'' +
                ", numTel='" + numTel + '\'' +
                ", numCompteur='" + numCompteur + '\'' +
                ", lieuIncident='" + lieuIncident + '\'' +
                ", typeIncident='" + typeIncident + '\'' +
                ", categorieIncident='" + categorieIncident + '\'' +
                ", responsabilite='" + responsabilite + '\'' +
                ", quittance='" + quittance + '\'' +
                ", resolution='" + resolution + '\'' +
                ", dateIncident=" + dateIncident +
                ", dateRec=" + dateRec +
                ", inspection=" + inspection +
                ", recevoirInspection=" + recevoirInspection +
                ", notifierClient=" + notifierClient +
                ", demanderFacture=" + demanderFacture +
                ", recevoirFacture=" + recevoirFacture +
                ", dateNotif=" + dateNotif +
                ", signature=" + signature +
                '}';
    }
}
