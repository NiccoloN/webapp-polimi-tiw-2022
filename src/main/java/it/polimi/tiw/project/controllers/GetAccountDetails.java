package it.polimi.tiw.project.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.project.utils.AccountDetails;
import it.polimi.tiw.project.utils.ConnectionHandler;
import it.polimi.tiw.project.beans.BankAccount;
import it.polimi.tiw.project.beans.Movement;
import it.polimi.tiw.project.beans.User;
import it.polimi.tiw.project.dao.BankAccountDAO;
import it.polimi.tiw.project.dao.MovementDAO;

@WebServlet("/GetAccountDetails")
@MultipartConfig
public class GetAccountDetails extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    @Override
    public void init() throws ServletException {

        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
    	doPost(req, resp);
	}
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
		HttpSession session = req.getSession();

		int bankAccountID;
		
		try {
			
			bankAccountID = Integer.parseInt(req.getParameter("bankAccount"));
		}
		catch (NumberFormatException | NullPointerException e) {
			
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println("Incorrect param values");
			return;
		}
		
		User user = (User) session.getAttribute("user");
		BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);
		BankAccount bankAccount;
		
		try {
			bankAccount = bankAccountDAO.getBankAccountDetails(bankAccountID);
			
			if (!bankAccount.getUser().equals(user.getUsername())) {
				
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.getWriter().println("Resource not found");
				return;
			}
			
			session.setAttribute("bankAccountId", bankAccountID);
			
		}
		catch (SQLException e) {
			
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().println("Not possible to recover bank account.");
			return;
		}
		
		List<Movement> movements;
		MovementDAO movementDAO = new MovementDAO(connection);
		
		try {
			movements = movementDAO.getAccountMovements(bankAccountID);
			
		}
		catch (SQLException e) {
			
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().println("Not possible to recover bank account.");
			return;		
		}
		
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(new Gson().toJson(new AccountDetails(bankAccount, movements)));
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
