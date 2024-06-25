package it.polimi.tiw.project.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.project.beans.User;
import it.polimi.tiw.project.dao.BankAccountDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/GetBankAccounts")
@MultipartConfig
public class GetBankAccounts extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    @Override
    public void init() throws ServletException {
        
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
        HttpSession session = req.getSession();
        
        User user = (User) session.getAttribute("user");
        BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);

        List<Integer> bankAccounts;

        try {

            bankAccounts = bankAccountDAO.getBankAccounts(user.getUsername());
        }
        catch(SQLException e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to get bank accounts");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(new Gson().toJson(bankAccounts));
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
  
		doGet(request, response);
	}
    
    @Override
    public void destroy() {

        try {

            ConnectionHandler.closeConnection(connection);
        }
        catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
