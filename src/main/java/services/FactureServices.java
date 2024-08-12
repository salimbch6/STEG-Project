package services;

import models.Facture;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FactureServices {

    private Connection cnx;

    public FactureServices(Connection connection) {
        this.cnx = connection;
    }

    // Add a new Facture to the database
    public void addFacture(Facture facture) throws SQLException {
        String query = "INSERT INTO facture (id_reclamation, objet, marque,reparabilite,prix) VALUES (?, ?, ?,?,?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, facture.getId_reclamation());
            stmt.setString(2, facture.getObjet());
            stmt.setString(3, facture.getMarque());
            stmt.setBoolean(4, facture.isReparabilite());
            stmt.setDouble(5, facture.getPrix());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    facture.setId_facture(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Update an existing Facture in the database
    public void updateFacture(Facture facture) throws SQLException {
        String query = "UPDATE facture SET objet = ?, marque = ?, reparabilite = ?, prix = ? WHERE id_facture = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, facture.getObjet());
            stmt.setString(2, facture.getMarque());
            stmt.setBoolean(3, facture.isReparabilite());
            stmt.setDouble(4, facture.getPrix());
            stmt.setInt(5, facture.getId_facture());

            stmt.executeUpdate();
        }
    }

    // Retrieve Factures for a specific Reclamation
    public List<Facture> getFacturesByReclamationId(int reclamationId) throws SQLException {
        List<Facture> factures = new ArrayList<>();
        String query = "SELECT * FROM facture WHERE id_reclamation = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, reclamationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idFacture = rs.getInt("id_facture");
                    String objet = rs.getString("objet");
                    String marque = rs.getString("marque");
                    Boolean reparabilite = rs.getBoolean("reparabilite");
                    Double prix = rs.getDouble("prix");

                    Facture facture = new Facture(reclamationId, idFacture, objet, marque,reparabilite,prix);
                    factures.add(facture);
                }
            }
        }
        return factures;
    }
}
