package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.User;
import com.revature.util.ServiceUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class SubmitReimbursement extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        if(role_id==3){

            //Test1 - pass JSON from post request
            Reimbursement newReimb = new Reimbursement(objectMapper.readValue(req.getInputStream(),Reimbursement.class));

            writer.write(newReimb.toString());

            ServiceUtil.getReimbService().save(newReimb);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.write("<p> Added user: "+ newReimb.toString() +" </p>");


        }

        else{
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            //writer.write("<p> " + role_id + " : does not have permission </p>");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        if(role_id==3){


            Reimbursement updateReimb = objectMapper.readValue(req.getInputStream(),Reimbursement.class);
            ReimbursementStatus status = ServiceUtil.getReimbService().getReimbByReimbId(updateReimb.getId()).getReimbursementStatus();


            if(status== ReimbursementStatus.PENDING){
                ServiceUtil.getReimbService().updateEMP(updateReimb);
                resp.setStatus(HttpServletResponse.SC_OK);

            }
            else{
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }

            writer.write("<p> Updated user: "+ updateReimb.toString() +" </p>");

        }
    }
}
