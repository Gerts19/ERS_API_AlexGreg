package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.util.ServiceUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name="ViewReimbursements", urlPatterns = "/reimbursements")
public class ViewReimbursements extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();



        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();

        writer.write("<p>Your role value is: " + role_id + "</p>");

        switch (role_id) {
            case 2:
                //FINANCE MANAGER

                // TODO
                // Figure out how to use query params without having to use all 3 or any at all.
                String filter = "";
                int status;
                int type;

                Set<String> parameterNames = req.getParameterMap().keySet();

                if (parameterNames.contains("filter")) {
                    filter = req.getParameter("filter");
                }

                if (parameterNames.contains("status_id")) {
                    status = Integer.parseInt(req.getParameter("status_id"));
                }

                if (parameterNames.contains("type_id")) {
                    type = Integer.parseInt(req.getParameter("type_id"));
                }

                List<Reimbursement> reimbursements;

                switch (filter.toLowerCase()) {
                    case "status":
                        status = Integer.parseInt(req.getParameter("status_id"));
                        writer.write("<p> status is: " + status + "</p>");
                        reimbursements = ServiceUtil.getReimbService().getReimbByStatus(status);
                        break;
                    case "type":
                        type = Integer.parseInt(req.getParameter("type_id"));
                        writer.write(("<p> type is: " + type + "</p>"));
                        reimbursements = ServiceUtil.getReimbService().getReimbByStatus(type);
                        break;
                    default:
                        reimbursements = ServiceUtil.getReimbService().getAllReimb();
                }

                for (Reimbursement r : reimbursements) {
                    writer.write("\n" + r.toString());
                }
                break;
            case 3:
                //EMPLOYEE
                parameterNames = req.getParameterMap().keySet();
                int reimbId = 0;

                int id = (int) session.getAttribute("user_id");
                reimbursements = ServiceUtil.getReimbService().getReimbByUserId(id);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("<p>" + session.getAttribute("name") + "'s reimbursements</p>");

                if (parameterNames.contains("reimbId")) {
                    reimbId = Integer.parseInt(req.getParameter("reimbId"));
                }

                if (reimbId > 0) {
                    for (Reimbursement r : reimbursements) {
                        if (reimbId == r.getId())
                            writer.write(r.toString());
                    }
                } else {
                    for (Reimbursement r : reimbursements) {
                        writer.write(r.toString());
                    }
                }
                break;

            default:
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    // Reimbursement Type is always type 4.
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
