package it.polimi.tiw.project.dao;

import it.polimi.tiw.project.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {

        this.connection = connection;
    }

    public User checkCredentials(String username, String password) throws SQLException {

        String query = "SELECT * FROM utente WHERE Username = ? AND Password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();

        if (!result.isBeforeFirst()) return null; // no results, credential check failed
        else {

            result.next();
            User user = new User();
            user.setUsername(result.getString("Username"));
            user.setName(result.getString("Nome"));
            user.setSurname(result.getString("Cognome"));
            user.setEmail(result.getString("Mail"));
            return user;
        }
    }

    public void addUser(String username, String name, String surname, String email, String password) throws SQLException {

        String update = "INSERT INTO utente VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(update);
        statement.setString(1, username);
        statement.setString(2, name);
        statement.setString(3, surname);
        statement.setString(4, password);
        statement.setString(5, email);

        statement.executeUpdate();
    }
}
