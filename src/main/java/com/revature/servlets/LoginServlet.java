package com.revature.servlets;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.repositories.UserRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.revature.util.ServiceUtil;
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

@WebServlet(name="Login",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {


        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String username = req.getParameter("user");
        String password = req.getParameter("pass");

        out.println("<p>" + username + " " + password + "</p>");
        User user = ServiceUtil.getUserService().authenticate(username, password);

        if(user != null){
            out.println("<p>Login successful</p>");


            HttpSession session = req.getSession();


            session.setAttribute("user_id", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("name", user.getFirstname() + " " + user.getLastname());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getUserRole());

            resp.setStatus(HttpServletResponse.SC_OK);
            out.println("<p>Welcome " + session.getAttribute("name"));


        }
        else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("<p>Login failed</p>");
        }

    }

}
