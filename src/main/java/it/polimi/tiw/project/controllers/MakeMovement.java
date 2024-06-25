package it.polimi.tiw.project.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import it.polimi.tiw.project.beans.BankAccount;
import it.polimi.tiw.project.beans.Movement;
import it.polimi.tiw.project.dao.BankAccountDAO;
import it.polimi.tiw.project.dao.MovementDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;
import it.polimi.tiw.project.utils.MovementInformation;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet("/MakeMovement")
@MultipartConfig
public class MakeMovement extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public void init() throws ServletException {

        connection = ConnectionHandler.getConnection(getServletContext());
    }
    
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        HttpSession session = req.getSession();
        
        if(session.getAttribute("bankAccountId") == null) {
            
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Cannot make movement");
        }
        
        int fromBankAccountId = (int) session.getAttribute("bankAccountId");
        
        String toUsername, causal;
        int toBankAccountID;
        BigDecimal amount;
        
        String tempToBankAccountID, tempAmount;
        
        BankAccount fromBankAccount;
        BankAccount toBankAccount;
		
		BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);
		MovementDAO movementDAO = new MovementDAO(connection);
        
        toUsername = StringEscapeUtils.escapeJava(req.getParameter("toUsername"));
        causal = StringEscapeUtils.escapeJava(req.getParameter("causal"));
        tempToBankAccountID = StringEscapeUtils.escapeJava(req.getParameter("toBankAccountID"));
        tempAmount = req.getParameter("amount");
        
        if(toUsername == null || toUsername.isEmpty() ||
                tempToBankAccountID == null || tempToBankAccountID.isEmpty() ||
                tempAmount == null || tempAmount.isEmpty()) {
            
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid or empty values");
            return;
        }
        
        try {
            
            toBankAccountID = Integer.parseInt(tempToBankAccountID);
            amount = new BigDecimal(tempAmount);
            if(amount.signum() <= 0) throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("The amount must be positive");
            return;
        }
        
        try {
            
            toBankAccount = bankAccountDAO.getBankAccountDetails(toBankAccountID);
            fromBankAccount = bankAccountDAO.getBankAccountDetails(fromBankAccountId);
            
            if(!toBankAccount.getUser().equals(toUsername)) {
                
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("The selected bank account is not owned by the selected user");
                return;
            }
            
            if(fromBankAccount.getAmount().compareTo(amount) < 0) {
                
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Not enough money on the giver bank account");
                return;
            }
        }
        catch(SQLException e) {
            
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to retrieve destination bank account.");
            return;
        }
        
        //At this point, every input is good and the transaction is made
        
        try {
         
        	bankAccountDAO.updateBankAccounts(amount, fromBankAccount.getID(), toBankAccountID);
        	movementDAO.addMovement(amount, fromBankAccount.getID(), toBankAccountID, causal);
        }
        catch (SQLException e) {
        	
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to execute transaction");
            return;
        }
        
        BankAccount updatedFromBankAccount;
        BankAccount updatedToBankAccount;
        Movement updatedMovement;
        
        try {
        	
        	updatedFromBankAccount = bankAccountDAO.getBankAccountDetails(fromBankAccount.getID());
        	updatedToBankAccount = bankAccountDAO.getBankAccountDetails(toBankAccount.getID());
        	updatedMovement = movementDAO.getLastMovement(fromBankAccount.getID());
        }
        catch (SQLException e) {
        	
        	resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to retrieve updated information");
        	return;
        }
        
        MovementInformation movementInformation = new MovementInformation(updatedFromBankAccount, updatedToBankAccount,
                updatedMovement, fromBankAccount.getAmount(), toBankAccount.getAmount());
        
		resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(movementInformation));
	}

	public void destroy() {
  
		try {
   
			ConnectionHandler.closeConnection(connection);
		}
        catch (SQLException e) {
        
			e.printStackTrace();
		}
	}
}
