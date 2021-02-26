package com.revature.servlets;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.Credentials;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.repositories.UserRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.revature.util.ServiceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * A WebServlet to handle Logging into ERS
 *
 * @author Alex Googe (github: darkspearrai), Greg Gertson (github: Gerts19)
 */
@WebServlet(name="Login",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(LoginServlet.class.getName());


    /**
     * Handles Logging in and creating the Session
     *
     * @param req  Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get the Print Writer from the Response. . .
        PrintWriter out = resp.getWriter();

        // Create the ObjectMapper for JSON. . .
        ObjectMapper mapper = new ObjectMapper();

        // Credentials from the Request JSON. . .
        Credentials credentials = mapper.readValue(req.getInputStream(), Credentials.class);

        // The User to authen. . .
        User user;

        // We don't have a hashed admin password. . .
        if (credentials.getUsername().equals("admin")) {

            // Authenticate the username and password. . .
            user = ServiceUtil.getUserService().authenticate(credentials.getUsername(), credentials.getPassword());

            // Create the session. . .
            createSession(user, req, resp);

        // Not an ADMIN
        } else {

            // Get the user by username to access hashed password. . .
            user = ServiceUtil.getUserService().getUserByUserName(credentials.getUsername()).get();

            // Get the result on if the given credential's password is the correct password by Hash. . .
            BCrypt.Result result = BCrypt.verifyer().verify(credentials.getPassword().toCharArray(), user.getPassword());

            // If verified by hash. . .
            if (result.verified) {

                // Create session. . .
                createSession(user, req, resp);
            } else {

                // User failed login. . .
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                LOG.info("Login failed");
            }

        }


    }

    private void createSession(User user, HttpServletRequest req, HttpServletResponse resp) {

        // If we found a user. . .
        if (user != null) {

            // Create a session object to hold User values. . .
            HttpSession session = req.getSession();


            // Setting session attributes for the logged in user. . .
            session.setAttribute("user_id", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("name", user.getFirstname() + " " + user.getLastname());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getUserRole());

            // HTTP Status = CREATED. . .
            resp.setStatus(HttpServletResponse.SC_CREATED);
            LOG.info("Establishing a session for user, {}", user.getUsername());
        } else {
            // User failed login. . .
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            LOG.info("Login failed");
        }
    }
}