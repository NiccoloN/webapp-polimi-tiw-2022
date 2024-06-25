package it.polimi.tiw.project.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.project.beans.User;
import it.polimi.tiw.project.dao.UserDAO;
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

@WebServlet("/CheckLogin")
@MultipartConfig
public class CheckLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    @Override
    public void init() throws ServletException {
        
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username, password;

        try {

            username = StringEscapeUtils.escapeJava(req.getParameter("username"));
            password = StringEscapeUtils.escapeJava(req.getParameter("password"));

            if(username == null || username.isEmpty() || password == null || password.isEmpty()) throw new Exception();
        }
        catch(Exception e) {

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Missing or empty field");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        User user;

        try {

            user = userDAO.checkCredentials(username, password);
        }
        catch(SQLException e) {
    
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Not possible to check login");
            return;
        }

        if(user == null) {

			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Incorrect credentials");
        }
        else {
        	
            req.getSession().setAttribute("user", user);
            
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().println(new Gson().toJson(user));
        }
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
