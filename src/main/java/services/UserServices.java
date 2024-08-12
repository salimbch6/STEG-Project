package services;

import com.twilio.Twilio;
import com.twilio.rest.lookups.v1.PhoneNumber;
import models.User;
import utils.EncryptionUtils;
import utils.MyDataBase;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;
import org.mindrot.jbcrypt.BCrypt; // Make sure to include the BCrypt library in your dependencies
import org.apache.commons.lang3.StringUtils; // For checking if the password is blank
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserServices {
    Connection cnx = MyDataBase.getInstance().getconn();

    public void registerUser(Connection connection, String firstname, String lastname, String username, String email, String password, String img, Integer etat, String number) throws SQLException {
        String insertFields = "INSERT INTO utilisateur(id_role, firstname, lastname, username, email, password, profilePic, etat, number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // Hash the password using bcrypt
            String hashedPassword = hashPassword(password);

            PreparedStatement statement = connection.prepareStatement(insertFields);
            // Assuming "client" role has id 1 (You may need to adjust this based on your role table)
            statement.setInt(1, 1); // Set role id to "client"
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, username);
            statement.setString(5, email);
            statement.setString(6, hashedPassword); // Store hashed password
            statement.setString(7, img);
            statement.setInt(8, 0);
            statement.setString(9, number);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public static String hashPassword(String password) {
        // Generate a salt for the password
        String salt = BCrypt.gensalt(13);
        // Hash the password using the generated salt
        String hashedPassword = BCrypt.hashpw(password, salt);
        if (hashedPassword.startsWith("$2a$")){
            hashedPassword = "$2y$" + hashedPassword.substring(4);
        }
        System.out.println(hashedPassword);

        // Return the hashed password
        return hashedPassword;
    }
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (hashedPassword.startsWith("$2y$")){
            hashedPassword = "$2a$" + hashedPassword.substring(4);
        }
        // Use BCrypt to check if the provided password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }



    public int validateLogin(Connection connection, String usernameOrEmail, String password) throws SQLException {
        String verifyLogin = "SELECT account_id, password FROM utilisateur WHERE (username = ? OR email = ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(verifyLogin);
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);
            ResultSet queryResult = statement.executeQuery();
            if (queryResult.next()) {
                String hashedPasswordFromDB = queryResult.getString("password");
                int accountId = queryResult.getInt("account_id");

                // Use the verifyPassword method to check if the provided password matches the hashed password
                boolean isValidPassword = verifyPassword(password, hashedPasswordFromDB);
                if (isValidPassword) {
                    return accountId; // Login successful
                }
            }
        } catch (SQLException e) {
            // Handle SQLException appropriately, log or rethrow
            throw e;
        }
        return -1; // Login failed
    }




    public ResultSet getAllUsers(Connection connection) throws SQLException {
        String query = "SELECT * FROM utilisateur";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw e;
        }
    }

    public User getUserById_Account(int id_account) {
        User b = new User();


        try {

            String req = "SELECT * FROM utilisateur WHERE account_id= " + id_account;
            //Statement st = cnx.createStatement();
            Statement st = cnx.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(req);
            rs.beforeFirst();
            rs.next();
            b.setAccount_id(rs.getInt(1));
            b.setId_role(rs.getInt(2));
            b.setFirstname(rs.getString(3));
            b.setLastname(rs.getString(4));
            b.setUsername(rs.getString(5));
            b.setEmail(rs.getString(8));
            b.setPassword(rs.getString(6));
            b.setProfilePic(rs.getString(7));
            b.setNumber(rs.getString(10));



        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        return b;
    }

    public void deleteUser(Connection connection, int id) throws SQLException {
        String req = "DELETE FROM utilisateur WHERE account_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateUser(Connection connection, User user) throws SQLException {
        String updateQuery = "UPDATE utilisateur SET firstname=?, lastname=?, username=?, email=?, profilePic=?, number=?";

        String hashedPassword = null; // Declare the variable here

        // Conditionally update the password if a new password is provided
        if (!StringUtils.isBlank(user.getPassword())) {
            // Hash the password using bcrypt
            hashedPassword = hashPassword(user.getPassword());
            updateQuery += ", password=?";
        }

        updateQuery += " WHERE account_id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getProfilePic());
            preparedStatement.setString(6, user.getNumber());

            int parameterIndex = 7; // Start index for setting parameters

            // Conditionally set the password if a new password is provided
            if (!StringUtils.isBlank(user.getPassword())) {
                preparedStatement.setString(parameterIndex++, hashedPassword); // Store hashed password
                preparedStatement.setInt(parameterIndex++, user.getAccount_id()); // Account ID placeholder
            } else {
                preparedStatement.setInt(parameterIndex++, user.getAccount_id()); // Account ID placeholder
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }


    public ResultSet searchUsers(Connection connection, String query) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE firstname LIKE ? OR lastname LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + query + "%");
        statement.setString(2, "%" + query + "%");
        return statement.executeQuery();
    }

    public boolean isUserExistsByUsername(Connection connection, String username) throws SQLException {
        boolean userExists = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to check if the username exists in the database
            String query = "SELECT COUNT(*) FROM utilisateur WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            // If a row with the given username exists, set userExists to true
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    userExists = true;
                }
            }
        } finally {
            // Close the statement and result set
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return userExists;
    }

    public boolean isUserExistsByEmail(Connection connection, String email) throws SQLException {
        boolean userExists = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to check if the email exists in the database
            String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            // If a row with the given email exists, set userExists to true
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    userExists = true;
                }
            }
        } finally {
            // Close the statement and result set
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return userExists;
    }


    public void updateEtat(Connection connection, int accountId, int newEtat) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE utilisateur SET etat = ? WHERE account_id = ?")) {
            statement.setInt(1, newEtat);
            statement.setInt(2, accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class EmailSender {
        public void sendEmail(String email, String newPassword, String title, String contenu) {
            String from = "scarrr66@gmail.com";
            String pass = "tjps foqv ngez vibp";
            // Configuration de la session SMTP pour l'envoi d'e-mails
            Properties props = System.getProperties();
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            // Création d'une nouvelle session SMTP
            Session session = Session.getDefaultInstance(props);

            try {
                // Création du message
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // Adresse e-mail du destinataire
                message.setSubject(title);
                message.setText(contenu);

                // Envoi du message
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com", "scarrr66@gmail.com", "tjps foqv ngez vibp");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();

                System.out.println("E-mail envoyé avec succès à " + email + " avec le nouveau mot de passe.");
            } catch (MessagingException e) {
                e.printStackTrace();
                System.err.println("Erreur lors de l'envoi de l'e-mail à " + email + " : " + e.getMessage());
            }
        }
    }

    public void updatePasswordByEmail(String email, String newPassword) {
        try {
            // Hash the new password
            String hashedPassword = hashPassword(newPassword);

            // Prepare the SQL update query
            String updateQuery = "UPDATE utilisateur SET password = ? WHERE email = ?";
            PreparedStatement statement = cnx.prepareStatement(updateQuery);
            statement.setString(1, hashedPassword);
            statement.setString(2, email);

            // Execute the update query
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }

    public User getUserByUsernameOrEmail(Connection connection, String usernameOrEmail) {
        User user = null;
        String query = "SELECT * FROM utilisateur WHERE username = ? OR email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setAccount_id(resultSet.getInt("account_id"));
                user.setId_role(resultSet.getInt("id_role"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setProfilePic(resultSet.getString("profilePic"));
                user.setEtat(resultSet.getInt("etat"));
                user.setNumber(resultSet.getString("number"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return user;
    }
    /*
    public void send_SMS() {
        // Initialisation de la bibliothèque Twilio avec les informations de votre compte
        String ACCOUNT_SID = "AC2f151fcc129fb5ee57da3a935c5b897e";
        String AUTH_TOKEN = "c5e63dbe6049d51ba786b1b2072ea522";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recipientNumber = "+21625191142";
        String message = "Bonjour Mr(s) ,\n"
                + "Nous sommes ravis de vous informer qu'un reservation a été ajouté.\n "
                + "Veuillez contactez l'administration pour plus de details.\n "
                + "Merci de votre fidélité(e) et à bientôt chez Sportify.\n"
                + "Cordialement,\n"
                + "Sportify";

        com.twilio.rest.api.v2010.account.Message twilioMessage = com.twilio.rest.api.v2010.account.Message.creator(
                new com.twilio.type.PhoneNumber(recipientNumber),
                new com.twilio.type.PhoneNumber("+12674335043"), message).create();

        System.out.println("SMS envoyé : " + twilioMessage.getSid());
    }*/

}
