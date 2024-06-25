package it.polimi.tiw.project.dao;

import it.polimi.tiw.project.beans.Movement;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MovementDAO {

    private final Connection connection;

    public MovementDAO(Connection connection) {

        this.connection = connection;
    }

    public void addMovement(BigDecimal amount, int fromBankAccountID, int toBankAccountID, String causal) throws SQLException {

    	String update;
    	if(causal.isEmpty()) update = "INSERT INTO trasferimento (Data, Importo, ContoOrigine, ContoDestinazione) VALUES (?, ?, ?, ?)";
    	else update = "INSERT INTO trasferimento (Data, Importo, ContoOrigine, ContoDestinazione, Causale) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(update);
        statement.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
        statement.setBigDecimal(2, amount);
        statement.setInt(3, fromBankAccountID);
        statement.setInt(4, toBankAccountID);
        if(!causal.isEmpty()) statement.setString(5, causal);
        statement.executeUpdate();
    }

    public List<Movement> getAccountMovements(int bankAccountID) throws SQLException {

        String query = "SELECT * FROM trasferimento WHERE ContoOrigine = ? OR ContoDestinazione = ? ORDER BY Codice DESC";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, bankAccountID);
        statement.setInt(2, bankAccountID);
        ResultSet result = statement.executeQuery();

        List<Movement> movements;

        if (!result.isBeforeFirst()) return null; // no results
        else {

            movements = new ArrayList<>();

            while(result.next()) {

                Movement movement = new Movement();
                movement.setID(result.getInt("Codice"));
                movement.setDate(result.getDate("Data"));
                movement.setAmount(result.getBigDecimal("Importo"));
                movement.setFromBankAccountID(result.getInt("ContoOrigine"));
                movement.setToBankAccountID(result.getInt("ContoDestinazione"));
                movement.setCausal(result.getString("Causale"));
                movements.add(movement);
            }
        }

        return movements;
    }
    
    public Movement getLastMovement(int fromBankAccountID) throws SQLException {
    	
    	String query = "SELECT * FROM trasferimento WHERE ContoOrigine = ? ORDER BY Codice DESC LIMIT 1";
    	PreparedStatement statement = connection.prepareStatement(query);
    	statement.setInt(1, fromBankAccountID);
    	ResultSet result = statement.executeQuery();
    	
    	Movement movement = new Movement();
    	
    	if(!result.isBeforeFirst()) return null;
    	else {
    		result.next();
    		movement.setID(result.getInt("Codice"));
            movement.setDate(result.getDate("Data"));
            movement.setAmount(result.getBigDecimal("Importo"));
            movement.setFromBankAccountID(result.getInt("ContoOrigine"));
            movement.setToBankAccountID(result.getInt("ContoDestinazione"));
            movement.setCausal(result.getString("Causale"));
            
            return movement;
    	}
    }
}
