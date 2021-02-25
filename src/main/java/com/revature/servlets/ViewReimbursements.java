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
import java.util.Locale;

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

                filter = req.getParameter("filter");
                int status = Integer.parseInt(req.getParameter("status_id"));
                int type = Integer.parseInt(req.getParameter("type_id"));

                List<Reimbursement> reimbursements;

                if (filter.toLowerCase().equals("status")) {
                    writer.write("Did I get here?");

                    //Status id = 4 is not showing the Reimbursement?
                    reimbursements = ServiceUtil.getReimbService().getReimbByStatus(status);
                } else if (filter.toLowerCase().equals("type")) {
                    writer.write("Did I get here?");
                    reimbursements = ServiceUtil.getReimbService().getReimbByType(type);
                } else {
                    writer.write("Did I get here?");
                    reimbursements = ServiceUtil.getReimbService().getAllReimb();
                }

                writer.write("<p>All Reimbursements. . .</p>");
                for (Reimbursement r : reimbursements) {
                    writer.write(r.toString());
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
