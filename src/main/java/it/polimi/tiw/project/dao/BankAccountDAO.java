package it.polimi.tiw.project.dao;

import it.polimi.tiw.project.beans.BankAccount;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {

    private final Connection connection;

    public BankAccountDAO(Connection connection) {

        this.connection = connection;
    }

    public void addBankAccount(String username) throws SQLException {

        String update = "INSERT INTO conto (Saldo, Utente) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(update);
        statement.setFloat(1, 0);
        statement.setString(2, username);
        statement.executeUpdate();
    }

    public List<Integer> getBankAccounts(String username) throws SQLException {

        String query = "SELECT Codice FROM conto WHERE Utente = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        List<Integer> bankAccounts;

        if (!result.isBeforeFirst()) 
        	return null; // no results
        else {

            bankAccounts = new ArrayList<>();

            while(result.next()) {

                bankAccounts.add(result.getInt("Codice"));
            }
        }

        return bankAccounts;
    }

    public BankAccount getBankAccountDetails(int bankAccountID) throws SQLException {

        String query = "SELECT * FROM conto WHERE Codice = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, bankAccountID);
        ResultSet result = statement.executeQuery();

        if (!result.isBeforeFirst()) return null; // no results
        else {

            result.next();
            BankAccount bankAccount = new BankAccount();
            bankAccount.setID(result.getInt("Codice"));
            bankAccount.setAmount(result.getBigDecimal("Saldo"));
            bankAccount.setUser(result.getString("Utente"));
            return bankAccount;
        }
    }

    public void updateBankAccounts(BigDecimal amount, int fromBankAccountID, int toBankAccountID) throws SQLException {

        connection.setAutoCommit(false);

        String update1 = "UPDATE conto SET Saldo = Saldo - ? WHERE Codice = ?";
        PreparedStatement statement1 = connection.prepareStatement(update1);
        statement1.setBigDecimal(1, amount);
        statement1.setInt(2, fromBankAccountID);
        statement1.executeUpdate();

        String update2 = "UPDATE conto SET Saldo = Saldo + ? WHERE Codice = ?";
        PreparedStatement statement2 = connection.prepareStatement(update2);
        statement2.setBigDecimal(1, amount);
        statement2.setInt(2, toBankAccountID);
        statement2.executeUpdate();

        try {

            connection.commit();
        }
        catch(SQLException e) {

            connection.rollback();
        }

        connection.setAutoCommit(true);
    }
}
