package it.polimi.tiw.project.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.project.beans.BankAccount;
import it.polimi.tiw.project.beans.Contact;
import it.polimi.tiw.project.beans.User;
import it.polimi.tiw.project.dao.BankAccountDAO;
import it.polimi.tiw.project.dao.ContactDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AddContact")
@MultipartConfig
public class AddContact extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    
    @Override
    public void init() throws ServletException {
        
        connection = ConnectionHandler.getConnection(getServletContext());
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        String contactUsername, contactBankAccountID;
        
        try {
    
            contactUsername = StringEscapeUtils.escapeJava(req.getParameter("contactUsername"));
            contactBankAccountID = StringEscapeUtils.escapeJava(req.getParameter("contactBankAccountID"));
            
            if(contactUsername == null || contactUsername.isEmpty() ||
                    contactBankAccountID == null || contactBankAccountID.isEmpty()) throw new Exception();
        }
        catch(Exception e) {
            
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Missing or empty field");
            return;
        }
    
        BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);
    
        try {
            
            BankAccount contactBankAccount = bankAccountDAO.getBankAccountDetails(Integer.parseInt(contactBankAccountID));
            
            if (!contactBankAccount.getUser().equals(contactUsername)) {
                
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("The selected bank account is not owned by the selected user");
                return;
            }
        }
        catch (SQLException e) {
            
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Bank account not found");
            return;
        }
    
        ContactDAO contactDAO = new ContactDAO(connection);
        User user = (User) req.getSession().getAttribute("user");
    
        try {
            
            contactDAO.addContact(user.getUsername(), contactUsername, Integer.parseInt(contactBankAccountID));
        }
        catch (SQLException e) {
            
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to add contact");
            return;
        }
    
        List<Contact> contacts;
    
        try {
            
            contacts = contactDAO.getContactsOf(user.getUsername());
        }
        catch (SQLException e) {
    
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Contact saved, but not possible to retrive updated contacts");
            return;
        }
    
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(new Gson().toJson(contacts));
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