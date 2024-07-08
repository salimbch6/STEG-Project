package services;

import utils.MyDataBase;

import java.sql.*;

public class ReclamationServices {
    Connection cnx = MyDataBase.getInstance().getconn();

    public void Reclamation(Connection connection, String nom, String prenom, String adresse, int codePostal, String numTel, String numCompteur, Date date, String lieu, String objet, String marque) throws SQLException {
        String sql = "INSERT INTO reclamation (nom, prenom, adresse, codePostal, numTel, numCompteur, dateIncident, lieuIncident, objEndommage, marque) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, adresse);
            preparedStatement.setInt(4, codePostal);
            preparedStatement.setString(5, numTel);
            preparedStatement.setString(6, numCompteur);
            preparedStatement.setDate(7, date);
            preparedStatement.setString(8, lieu);
            preparedStatement.setString(9, objet);
            preparedStatement.setString(10, marque);
            preparedStatement.executeUpdate();
        }
    }

    public ResultSet getAllReclamations(Connection connection) throws SQLException {
        String query = "SELECT * FROM reclamation";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw e;
        }
    }
}
