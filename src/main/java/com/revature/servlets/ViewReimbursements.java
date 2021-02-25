package com.revature.servlets;

import com.revature.models.Reimbursement;
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

@WebServlet(name="ViewReimbursements", urlPatterns = "/view")
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

//                if (filter.toLowerCase().equals("status")) {
//                    writer.write("Did I get here?");
//
//                    //Status id = 4 is not showing the Reimbursement?
//                    reimbursements = ServiceUtil.getReimbService().getReimbByStatus(status);
//                } else if (filter.toLowerCase().equals("type")) {
//                    writer.write("Did I get here?");
//                    reimbursements = ServiceUtil.getReimbService().getReimbByType(type);
//                } else {
//                    writer.write("Did I get here?");
//                    reimbursements = ServiceUtil.getReimbService().getAllReimb();
//                }

                for (Reimbursement r : reimbursements) {
                    writer.write("\n" + r.toString());
                }
                break;
            case 3:
                //EMPLOYEE
                int id = (int) session.getAttribute("user_id");
                reimbursements = ServiceUtil.getReimbService().getReimbByUserId(id);

                writer.write("<p>" + session.getAttribute("name") + "'s reimbursements</p>");
                for (Reimbursement r : reimbursements) {
                    writer.write(r.toString());
                }
        }
    }
}
