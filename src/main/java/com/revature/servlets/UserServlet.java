package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.util.ServiceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

/**
 * An HttpServlet to handle Admin users Requests for Users
 *
 * @author Alex Googe (github: darkspearrai), Greg Gertson (github: Gerts19)
 */
@WebServlet(name="User",urlPatterns = "/user")
public class UserServlet extends HttpServlet {


    private static final Logger LOG = LogManager.getLogger(UserServlet.class);
    /**
     * Retrieves all of the Users
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        // Get the session. . .
        HttpSession session = req.getSession();

        // role_id of the User that is logged in. . .
        int role_id = (int) session.getAttribute("role");

        // Get the PrintWriter for the response. . .
        PrintWriter writer = resp.getWriter();
        LOG.info("UserServlet.doGet() invoked by requester {}", session.getAttribute("username"));

        // Check functionality based on role_id. . .
        switch(role_id){

            // ADMIN
            case 1:

                // Get all Users. . .
                List<User> users = ServiceUtil.getUserService().getAllUsers();

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                ObjectMapper mapper = new ObjectMapper();

                // For each User get JSON and send it back. . .
                LOG.info("Retrieving all users");
                for (User u : users) {
                    writer.print(mapper.writeValueAsString(u));
                }
                break;

            default:
                // ANY OTHER role_id. . .
                LOG.info("Retrieve all users failed");
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    /**
     * Adds a User
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get Session. . .
        HttpSession session = req.getSession();

        // Get logged in User role_id. . .
        int role_id = (int) session.getAttribute("role");

        // Create ObjectMapper. . .
        ObjectMapper objectMapper = new ObjectMapper();


        // If ADMIN. . .
        if(role_id==1){

            // Create a new User from the JSON. . .
            User newUser = new User(objectMapper.readValue(req.getInputStream(),User.class));

            // Using ServiceUtil to register the newUser. . .
            ServiceUtil.getUserService().register(newUser);

            // Http Code CREATED. . .
            LOG.info("Created new user");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            // Not an ADMIN. . .
            LOG.info("Failed to create new user");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    /**
     * Deletes a User
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get the Session. . .
        HttpSession session = req.getSession();

        // Get logged in User's role_id. . .
        int role_id = (int) session.getAttribute("role");

        // Get the logged in User's id. . .
        int userId = (int) session.getAttribute("user_id");

        PrintWriter writer = resp.getWriter();

        // Get parameter names in a Set. . .
        Set<String> parameterNames = req.getParameterMap().keySet();

        // User's id to delete. . .
        int delete;

        // If ADMIN. . .
        if(role_id==1){

            // If a deleteId parameter was included. . .
            if (parameterNames.contains("deleteId")) {
                delete = Integer.parseInt(req.getParameter("deleteId"));

                // If user is not trying to delete themselves. . .
                if (userId != delete) {

                    // Delete the specified id. . .
                    ServiceUtil.getUserService().deleteUserById(delete);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    LOG.info("Deleted user");
                }
            }
        } else {
            // Not an ADMIN
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            LOG.info("Failed to delete user");
        }

    }

    /**
     * Updates a User
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get the session. . .
        HttpSession session = req.getSession();

        // Get the logged in User's role. . .
        int role_id = (int) session.getAttribute("role");

        // Create an ObjectMapper. . .
        ObjectMapper objectMapper = new ObjectMapper();

        // If ADMIN. . .
        if(role_id==1){

            // Map a User from the JSON. . .
            User user = objectMapper.readValue(req.getInputStream(),User.class);

            // Update the User. . .
            ServiceUtil.getUserService().update(user);

            // Http Code OK
            resp.setStatus(HttpServletResponse.SC_OK);
            LOG.info("Updated user");
        } else {

            // Not an ADMIN. . .
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            LOG.info("Failed to update user");
        }
    }
}
