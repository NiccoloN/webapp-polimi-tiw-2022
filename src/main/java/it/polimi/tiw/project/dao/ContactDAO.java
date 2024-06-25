package it.polimi.tiw.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.project.beans.Contact;

public class ContactDAO {

    private final Connection connection;

    public ContactDAO(Connection connection) {

        this.connection = connection;
    }
    
    public void addContact(String ownerUsername, String contactUsername, int contactBankAccountID) throws SQLException {
    	
    	String update = "INSERT INTO contatto VALUES (?, ?, ?)";
    	PreparedStatement statement = connection.prepareStatement(update);
        statement.setString(1, ownerUsername);
        statement.setInt(2, contactBankAccountID);
        statement.setString(3, contactUsername);
        statement.executeUpdate();
    }
    
    public List<Contact> getContactsOf(String ownerUsername) throws SQLException {
    	
    	String query = "SELECT * FROM contatto WHERE Utente = ?";
    	PreparedStatement statement = connection.prepareStatement(query);
    	statement.setString(1, ownerUsername);
    	ResultSet result = statement.executeQuery();
    	
    	List<Contact> contacts;
    	
        if (!result.isBeforeFirst()) return null; // no results
        else {

            contacts = new ArrayList<>();

            while(result.next()) {

                Contact contact = new Contact();
                contact.setOwnerUsername(result.getString("Utente"));
                contact.setContactBankAccountID(result.getInt("ContoContatto"));
                contact.setContactUsername(result.getString("UsernameContatto"));
                contacts.add(contact);
            }
        }
        
        return contacts;
    }
}
