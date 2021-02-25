package com.revature.servlets;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.repositories.UserRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name="Login",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

//        resp.setContentType("text/html");
//        PrintWriter out = resp.getWriter();
//        out.println("<p>Hello World!</p>");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String username = req.getParameter("user");
        String password = req.getParameter("pass");


        SessionFactory sF = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Reimbursement.class).buildSessionFactory();


        UserRepository userRepo = new UserRepository(sF);
        UserService userServ = new UserService(userRepo);
        ReimbursementsRepository rR = new ReimbursementsRepository(sF);
        ReimbursementService rS = new ReimbursementService(rR);


        if(userServ.authenticate(username,password)!=null){
            out.println("<p>Login successful</p>");
        }
        else{
            out.println("<p>Login failed</p>");
        }

    }

}
