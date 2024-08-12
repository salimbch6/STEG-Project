package services;

import models.Reclamation;
import utils.MyDataBase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.*;

public class ReclamationServices {
    Connection cnx = MyDataBase.getInstance().getconn();

    public int addReclamation(Connection connection, String nom, String prenom, String adresse, int codePostal,
                              String reference, String numTel, String numCompteur, LocalDate date, String lieu,
                              String typeIncident, String categorieIncident
                              , int etatNotif) throws SQLException {

        String sql = "INSERT INTO reclamation (nom, prenom, adresse, codePostal, reference, numTel, numCompteur, dateIncident, lieuIncident, typeIncident, categorieIncident,dateRec, etatNotif, dateNotif, duree, inspection, recevoirIncpection,dureeInspection, responsabilite, notifierClient, demanderFacture,dureeNotifierClient, recevoirFacture, quittance,signature,resolution,dureeRemboursement) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, adresse);
            preparedStatement.setInt(4, codePostal);
            preparedStatement.setString(5, reference);
            preparedStatement.setString(6, numTel);
            preparedStatement.setString(7, numCompteur);
            preparedStatement.setDate(8, date != null ? java.sql.Date.valueOf(date) : null);
            preparedStatement.setString(9, lieu);
            preparedStatement.setString(10, typeIncident);
            preparedStatement.setString(11, categorieIncident);
            preparedStatement.setDate(12, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(13, etatNotif);
            preparedStatement.setDate(14, null);
            preparedStatement.setInt(15, 0);
            preparedStatement.setDate(16, null);
            preparedStatement.setDate(17, null);
            preparedStatement.setInt(18, 0);
            preparedStatement.setString(19, null);
            preparedStatement.setDate(20, null);
            preparedStatement.setDate(21, null);
            preparedStatement.setInt(22, 0);
            preparedStatement.setDate(23, null);
            preparedStatement.setString(24, null);
            preparedStatement.setDate(25, null);
            preparedStatement.setString(26, null);
            preparedStatement.setInt(27, 0);


            preparedStatement.executeUpdate();

            // Retrieve the generated keys
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated id_reclamation
                } else {
                    throw new SQLException("Creating reclamation failed, no ID obtained.");
                }
            }
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

    public void deleteReclamation(Connection connection, int id) throws SQLException {
        String req = "DELETE FROM reclamation WHERE id_reclamation = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateReclamation(Connection connectDB, Reclamation reclamation) throws SQLException {
        String updateQuery = "UPDATE reclamation SET nom = ?, prenom = ?, adresse = ?, codePostal = ?, reference = ?, numTel = ?, numCompteur = ?, dateIncident = ?, lieuIncident = ?, typeIncident = ?, categorieIncident = ?,  inspection = ?, recevoirIncpection = ?,duree = ?, responsabilite = ?, notifierClient = ?, demanderFacture = ?, dureeNotifierClient = ?, recevoirFacture = ?,quittance=?, resolution = ?, dateRec = ?, dateNotif = ?, dureeInspection = ?,dureeRemboursement = ?, signature = ?  WHERE id_reclamation = ?";

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, reclamation.getNom());
            preparedStatement.setString(2, reclamation.getPrenom());
            preparedStatement.setString(3, reclamation.getAdresse());
            preparedStatement.setInt(4, reclamation.getCodePostal() != null ? reclamation.getCodePostal() : 0);
            preparedStatement.setString(5, reclamation.getReference());
            preparedStatement.setString(6, reclamation.getNumTel());
            preparedStatement.setString(7, reclamation.getNumCompteur());
            preparedStatement.setDate(8, reclamation.getDateIncident() != null ? java.sql.Date.valueOf(reclamation.getDateIncident()) : null);
            preparedStatement.setString(9, reclamation.getLieuIncident());
            preparedStatement.setString(10, reclamation.getTypeIncident());
            preparedStatement.setString(11, reclamation.getCategorieIncident());


            // Convert LocalDate to java.sql.Date
            preparedStatement.setDate(12, reclamation.getInspection() != null ? java.sql.Date.valueOf(reclamation.getInspection()) : null);
            preparedStatement.setDate(13, reclamation.getRecevoirInspection() != null ? java.sql.Date.valueOf(reclamation.getRecevoirInspection()) : null);
            preparedStatement.setInt(14, reclamation.getDuree() != null ? reclamation.getDuree() : 0);
            preparedStatement.setString(15, reclamation.getResponsabilite());
            preparedStatement.setDate(16, reclamation.getNotifierClient() != null ? java.sql.Date.valueOf(reclamation.getNotifierClient()) : null);
            preparedStatement.setDate(17, reclamation.getDemanderFacture() != null ? java.sql.Date.valueOf(reclamation.getDemanderFacture()) : null);
            preparedStatement.setInt(18, reclamation.getDureeNotifierClient() != null ? reclamation.getDureeNotifierClient() : 0);
            preparedStatement.setDate(19, reclamation.getRecevoirFacture() != null ? java.sql.Date.valueOf(reclamation.getRecevoirFacture()) : null);
            preparedStatement.setString(20, reclamation.getQuittance());
            preparedStatement.setString(21, reclamation.getResolution());
            preparedStatement.setDate(22, reclamation.getDateRec() != null ? java.sql.Date.valueOf(reclamation.getDateRec()) : null);
            preparedStatement.setDate(23, reclamation.getDateNotif() != null ? java.sql.Date.valueOf(reclamation.getDateNotif()) : null);
            preparedStatement.setInt(24, reclamation.getDureeInspection() != null ? reclamation.getDureeInspection() : 0);
            preparedStatement.setInt(25, reclamation.getDureeRemboursement() != null ? reclamation.getDureeRemboursement() : 0);
            preparedStatement.setDate(26, reclamation.getSignature() != null ? java.sql.Date.valueOf(reclamation.getSignature()) : null);





            preparedStatement.setInt(27, reclamation.getId_reclamation());


            preparedStatement.executeUpdate();
        }
    }



    public ResultSet searchReclamations(Connection connection, String query) throws SQLException {
        String sql = "SELECT * FROM reclamation WHERE nom LIKE ? OR prenom LIKE ? OR numCompteur LIKE ? OR lieuIncident LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + query + "%");
        statement.setString(2, "%" + query + "%");
        statement.setString(3, "%" + query + "%");
        statement.setString(4, "%" + query + "%");
        return statement.executeQuery();
    }

    public Map<String, String> groupReclamationsByCriteria(List<Reclamation> reclamations) {
        Map<String, String> colorMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        for (Reclamation reclamation : reclamations) {
            String key = reclamation.getAdresse() + reclamation.getLieuIncident() + reclamation.getDateIncident();
            countMap.put(key, countMap.getOrDefault(key, 0) + 1);
        }

        // Assign colors only to groups with more than one reclamation
        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                colorMap.put(entry.getKey(), getColorByIndex(colorIndex));
                colorIndex++;
            }
        }
        return colorMap;
    }

    private String getColorByIndex(int index) {
        String[] colors = {"#FFB6C1", "#ADD8E6", "#90EE90", "#FFA07A", "#20B2AA"};
        return colors[index % colors.length];
    }

    public  Map<String, Integer> getTotalReclamationsPerMonth(int year) {
        Map<String, Integer> reclamationsPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(*) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reclamationsPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reclamationsPerMonth;
    }

    public Map<String, Integer> getTotalUniqueIncidentsPerMonth(int year) {
        Map<String, Integer> uniqueIncidentsPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(DISTINCT adresse, lieuIncident, dateIncident) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                uniqueIncidentsPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uniqueIncidentsPerMonth;
    }

    public Map<String, Integer> getTotalReclamationsSTEGPerMonth(int year) {
        Map<String, Integer> stegReclamationsPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(*) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? AND responsabilite = 'STEG' "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stegReclamationsPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stegReclamationsPerMonth;
    }

    public Map<String, Integer> getTotalReclamationsNonSTEGPerMonth(int year) {
        Map<String, Integer> nonStegReclamationsPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(*) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? AND responsabilite != 'STEG' "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                nonStegReclamationsPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nonStegReclamationsPerMonth;
    }

    public Map<String, Integer> getTotalCorporelleIncidentsPerMonth(int year) {
        Map<String, Integer> corporelleIncidentsPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(*) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? AND categorieIncident = 'corporelle' "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                corporelleIncidentsPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return corporelleIncidentsPerMonth;
    }

    public Map<String, Integer> getTotalIncendieIncidentsPerMonth(int year) {
        Map<String, Integer> incendieIncidentsPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(*) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? AND categorieIncident = 'incendie' "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                incendieIncidentsPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incendieIncidentsPerMonth;
    }

    public Map<String, Integer> getTotalResoluReclamationsPerMonth(int year) {
        Map<String, Integer> resoluReclamationPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, COUNT(*) AS total "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? AND resolution = 'Résolu' "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                resoluReclamationPerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resoluReclamationPerMonth;
    }

    public Map<String, Integer> getTotalPriceOfDamagedObjectsPerMonth(int year) {
        Map<String, Integer> totalPricePerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(r.dateRec) AS month, SUM(f.prix) AS total "
                    + "FROM facture f "
                    + "JOIN reclamation r ON f.id_reclamation = r.id_reclamation "
                    + "WHERE YEAR(r.dateRec) = ? "
                    + "GROUP BY MONTH(r.dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalPricePerMonth.put(rs.getString("month"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPricePerMonth;
    }

    public Map<String, Integer> getTotalRemboursementPerMonth(int year) {
        Map<String, Integer> remboursementPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(r.dateRec) AS month, f.id_reclamation, SUM(f.prix) AS total "
                    + "FROM facture f "
                    + "JOIN reclamation r ON f.id_reclamation = r.id_reclamation "
                    + "WHERE YEAR(r.dateRec) = ? "
                    + "GROUP BY MONTH(r.dateRec), f.id_reclamation";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month");
                double total = rs.getDouble("total");
                int remboursement = (int) calculateRemboursement(total);

                remboursementPerMonth.merge(month, remboursement, Integer::sum);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remboursementPerMonth;
    }

    public Map<String, Integer> getAverageDurationPerMonth(int year) {
        Map<String, Integer> averageDurationPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(dateRec) AS month, AVG(duree) AS averageDuration "
                    + "FROM reclamation "
                    + "WHERE YEAR(dateRec) = ? "
                    + "GROUP BY MONTH(dateRec)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                averageDurationPerMonth.put(rs.getString("month"), rs.getInt("averageDuration"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return averageDurationPerMonth;
    }

    public Map<String, Integer> getAverageInspectionDurationPerMonth(int year) {
        Map<String, Integer> averageInspectionDurationPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(inspection) AS month, AVG(dureeInspection) AS averageDuration "
                    + "FROM reclamation "
                    + "WHERE YEAR(inspection) = ? "
                    + "GROUP BY MONTH(inspection)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                averageInspectionDurationPerMonth.put(rs.getString("month"), rs.getInt("averageDuration"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return averageInspectionDurationPerMonth;
    }

    public Map<String, Integer> getAverageRemboursementDurationPerMonth(int year) {
        Map<String, Integer> averageRemboursementDurationPerMonth = new HashMap<>();

        try {
            String query = "SELECT MONTHNAME(signature) AS month, AVG(dureeRemboursement) AS averageDuration "
                    + "FROM reclamation "
                    + "WHERE YEAR(signature) = ? "
                    + "GROUP BY MONTH(signature)";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                averageRemboursementDurationPerMonth.put(rs.getString("month"), rs.getInt("averageDuration"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return averageRemboursementDurationPerMonth;
    }

    private double calculateRemboursement(double total) {
        if (total > 500000) {
            return total;
        } else if (total <= 200000) {
            return total;
        } else {
            return total * 0.7;
        }
    }

}


