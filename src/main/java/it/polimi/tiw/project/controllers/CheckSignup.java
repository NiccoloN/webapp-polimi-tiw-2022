package it.polimi.tiw.project.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.project.dao.BankAccountDAO;
import it.polimi.tiw.project.dao.UserDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.EmailValidator;

@WebServlet("/CheckSignup")
@MultipartConfig
public class CheckSignup extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    @Override
    public void init() throws ServletException {
        
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username, name, surname, email, password, repeatPassword;

        try {

            username = StringEscapeUtils.escapeJava(req.getParameter("username"));
            name = StringEscapeUtils.escapeJava(req.getParameter("name"));
            surname = StringEscapeUtils.escapeJava(req.getParameter("surname"));
            email = StringEscapeUtils.escapeJava(req.getParameter("email"));
            password = StringEscapeUtils.escapeJava(req.getParameter("password"));
            repeatPassword = StringEscapeUtils.escapeJava(req.getParameter("repeatPassword"));

            if(!validData(username, name, surname, email, password, repeatPassword)) throw new Exception();
        }
        catch(Exception e) {

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Missing or invalid field");
			return;
        }

        UserDAO userDAO = new UserDAO(connection);

        try {

            userDAO.addUser(username, name, surname, email, password);
        }
        catch(SQLException e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to add user");
            return;
        }

        BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);

        try {

            bankAccountDAO.addBankAccount(username);
        }
        catch(SQLException e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to add bank account");
            return;
        }
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    private boolean validData(String username, String name, String surname, String email, String password, String repeatPassword) {
        
        return username != null && !username.isEmpty() && username.length() <= 20
               && name != null && !name.isEmpty() && name.length() <= 20 &&
               surname != null && !surname.isEmpty() && surname.length() <= 20 &&
               email != null && !email.isEmpty() && email.length() <= 20 && EmailValidator.getInstance().isValid(email) &&
               password != null && !password.isEmpty() && password.length() <= 20 &&
               repeatPassword != null && !repeatPassword.isEmpty() &&
               password.equals(repeatPassword);
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
