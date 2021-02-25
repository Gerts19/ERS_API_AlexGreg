package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.util.ServiceUtil;

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

@WebServlet(name="User",urlPatterns = "/user")
public class UserServlet extends HttpServlet {


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        HttpSession session = req.getSession();
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();

        switch(role_id){

            case 1:
                writer.write("<p>Confirmed: " + role_id + "</p>");
                List<User> users = ServiceUtil.getUserService().getAllUsers();
                for (User u : users) {
                    writer.write(u.toString());
                }
                break;

            default:
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                //writer.write("<p> " + role_id + " : does not have permission </p>");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        if(role_id==1){

            //Test1 - pass JSON from post request
            User newUser = new User(objectMapper.readValue(req.getInputStream(),User.class));

            writer.write(newUser.toString());

            ServiceUtil.getUserService().register(newUser);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.write("<p> Added user: "+ newUser.toString() +" </p>");


               /*
            Test2 - pass as formatted JSON object, no post data
            String json = "{ \"username\" : \"jack10\", \"password\" : \"pass\", \"firstname\" : \"Jack\" , \"lastname\" : \"Smith\" , \"email\" : \"jack10@gmail.com\"}";
            User newUser = objectMapper.readValue(json, User.class);
            ServiceUtil.getUserService().register(newUser);
            writer.write("<p> Added user: "+ newUser.toString() +" </p>");
             */

        }

        else{
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            //writer.write("<p> " + role_id + " : does not have permission </p>");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();
        //ObjectMapper objectMapper = new ObjectMapper();
        Set<String> parameterNames = req.getParameterMap().keySet();
        int delete;

        if(role_id==1){

            if (parameterNames.contains("delete")) {
                delete = Integer.parseInt(req.getParameter("delete"));
                ServiceUtil.getUserService().deleteUserById(delete);
            }
        }

        else{
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writer.write("<p> " + role_id + " : does not have permission </p>");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        HttpSession session = req.getSession();
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        if(role_id==1){

            String user = "";
            String userName = "";

            Set<String> parameterNames = req.getParameterMap().keySet();

            if (parameterNames.contains("user")) {
                userName = req.getParameter("user");
            }

            //Not great, have to handle variety of query strings for this
            User userToUpdate = ServiceUtil.getUserService().getUserByUserName(userName).get();
            writer.write("<p> User before update: "+ userToUpdate.toString() +" </p>");
            userToUpdate.setUsername("diffUserName");
            userToUpdate.setPassword("diffPassword");
            userToUpdate.setUserRole(2);
            ServiceUtil.getUserService().update(userToUpdate);
            writer.write("<p> Updated user: "+ userToUpdate.toString() +" </p>");
        }

        else{
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writer.write("<p> " + role_id + " : does not have permission </p>");
        }
    }
}
