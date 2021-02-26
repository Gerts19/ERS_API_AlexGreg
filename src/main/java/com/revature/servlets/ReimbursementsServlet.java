package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.ApproveDeny;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * HttpServlet that handles Reimbursements
 *
 * @author Alex Googe (github: darkspearrai), Greg Gertson (github: Gerts19)
 */
@WebServlet(name="Reimbursements", urlPatterns = "/reimbursements")
public class ReimbursementsServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(ReimbursementsServlet.class);

    /**
     * Handles Employee and Finance Manager GET requests for Reimbursements
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get session. . .
        HttpSession session = req.getSession();

        // Get logged in User's role. . .
        int role_id = (int) session.getAttribute("role");

        // Create an ObjectMapper. . .
        ObjectMapper mapper = new ObjectMapper();

        // Get the PrintWriter. . .
        PrintWriter writer = resp.getWriter();

        // Set content type and encoding. . .
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        LOG.info("ReimbursementServlet.doGet() invoked by requester {}", session.getAttribute("username"));

        // Switch on role_id. . .
        switch (role_id) {

            //FINANCE MANAGER
            case 2:

                // Instantiate potential filters. . .
                String filter = "";
                int status = 0;
                int type = 0;
                int reimbId = 0;

                // Get parameter names into a Set. . .
                Set<String> parameterNames = req.getParameterMap().keySet();

                // If there is a filter param. . .
                if (parameterNames.contains("filter")) {
                    filter = req.getParameter("filter");
                }

                // If there is a statusId param. . .
                if (parameterNames.contains("statusId")) {
                    status = Integer.parseInt(req.getParameter("statusId"));
                }

                // If there is a typeId param. . .
                if (parameterNames.contains("typeId")) {
                    type = Integer.parseInt(req.getParameter("typeId"));
                }

                // If there is a reimbId param. . .
                if (parameterNames.contains("reimbId")) {
                    reimbId = Integer.parseInt(req.getParameter("reimbId"));
                }

                // List of Reimbursements to return / map. . .
                List<Reimbursement> reimbursements = new ArrayList<>();

                // Switch on filter. . .
                switch (filter.toLowerCase()) {

                    // Filter by Status. . .
                    case "status":
                        reimbursements = ServiceUtil.getReimbService().getReimbByStatus(status);
                        break;

                    // Filter by Type. . .
                    case "type":
                        reimbursements = ServiceUtil.getReimbService().getReimbByStatus(type);
                        break;

                    // Filter by single Reimbursement
                    case "single":
                        reimbursements.add(ServiceUtil.getReimbService().getReimbByReimbId(reimbId));
                        break;

                    // No filter (get all). . .
                    default:
                        reimbursements = ServiceUtil.getReimbService().getAllReimb();
                }

                // For each Reimbursement, add the JSON to the response. . .
                LOG.info("Returning reimbursements");
                for (Reimbursement r : reimbursements) {
                    writer.print(mapper.writeValueAsString(r));
                }
                break;

            //EMPLOYEE
            case 3:

                // Get parameter names in a Set. . .
                parameterNames = req.getParameterMap().keySet();

                // Potential single Reimbursement to get. . .
                reimbId = 0;

                // Get logged in User's id. . .
                int id = (int) session.getAttribute("user_id");

                // Get all logged in User's Reimbursements. . .
                reimbursements = ServiceUtil.getReimbService().getReimbByUserId(id);

                // If there is a reimbId param. . .
                if (parameterNames.contains("reimbId")) {
                    reimbId = Integer.parseInt(req.getParameter("reimbId"));
                }

                // If there is a reimbId param, it should be > 0. . .
                if (reimbId > 0) {

                    // Check to see if the User is the author of that reimb. . .
                    for (Reimbursement r : reimbursements) {

                        // If this matches, the User is the author (since each r comes from the List from User Id). . .
                        LOG.info("Returning reimbursements");
                        if (reimbId == r.getId())
                            writer.print(mapper.writeValueAsString(r));
                    }

                // Mapping all User reimbursements. . .
                } else {

                    // For each reimbursement, map to JSON and add to response. . .
                    LOG.info("Returning reimbursements");
                    for (Reimbursement r : reimbursements) {
                        writer.print(mapper.writeValueAsString(r));
                    }
                }
                break;

            // Not a Finance Manager or Employee. . .
            default:

                // Http Code FORBIDDEN. . .
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * Handles Posting a new Reimbursement
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get session. . .
        HttpSession session = req.getSession();

        // Get logged in User's role. . .
        int role_id = (int) session.getAttribute("role");

        // Create ObjectMapper. . .
        ObjectMapper objectMapper = new ObjectMapper();

        // If Employee. . .
        if(role_id==3) {

            // Create a new Reimbursement from JSON. . .
            Reimbursement newReimb = new Reimbursement(objectMapper.readValue(req.getInputStream(), Reimbursement.class));

            // Save the Reimbursement. . .
            ServiceUtil.getReimbService().save(newReimb);

            // Http Code CREATED. . .
            LOG.info("Created new reimbursement");
            resp.setStatus(HttpServletResponse.SC_CREATED);

        // Not an EMPLOYEE. . .
        } else {

            // Http Code FORBIDDEN. . .
            LOG.info("Failed to create new reimbursement");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * Approves or Denies a Reimbursement if Finance Manager
     * Update a Reimbursement if Employee
     * @param req Http request object
     * @param resp Http response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get session. . .
        HttpSession session = req.getSession();

        // Get logged in User's role. . .
        int role_id = (int) session.getAttribute("role");

        PrintWriter writer = resp.getWriter();

        // Create ObjectMapper. . .
        ObjectMapper objectMapper = new ObjectMapper();

        // Functionality depending on role. . .
        switch (role_id) {

            // EMPLOYEE. . .
            case 3:

                // Create the updated Reimbursement from JSON. . .
                Reimbursement updateReimb = objectMapper.readValue(req.getInputStream(),Reimbursement.class);

                // Set the Submitted time to now since updating. . .
                updateReimb.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));

                // Check the ACTUAL status in the database. . .
                ReimbursementStatus status = ServiceUtil.getReimbService().getReimbByReimbId(updateReimb.getId()).getReimbursementStatus();

                // If is PENDING. . .
                if(status== ReimbursementStatus.PENDING){

                    // Update the Reimbursement. . .
                    ServiceUtil.getReimbService().updateEMP(updateReimb);

                    // Http Code OK. . .
                    LOG.info("Updated reimbursement");
                    resp.setStatus(HttpServletResponse.SC_OK);

                // Status is not PENDING. . .
                } else {

                    // Http Code CONFLICT. . .
                    LOG.info("Failed to update reimbursement");
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                }
                break;

            // FINANCE MANAGER. . .
            case 2:

                // Get logged in User's Id. . .
                int userId = (int) session.getAttribute("user_id");

                // Create a new ApproveDeny Object from JSON. . .
                ApproveDeny approveDeny = objectMapper.readValue(req.getInputStream(), ApproveDeny.class);

                // If .getStatus() = 0, deny. . .
                if (approveDeny.getStatus() == 0) {

                    // Deny the Reimbursement. . .
                    ServiceUtil.getReimbService().deny(userId, approveDeny.getId());

                    // Http Code OK
                    LOG.info("Denied reimbursement");
                    resp.setStatus(HttpServletResponse.SC_OK);

                // If .getStatus() == 1, approve. . .
                } else if (approveDeny.getStatus() == 1) {

                    // Approve the Reimbursement. . .
                    ServiceUtil.getReimbService().approve(userId, approveDeny.getId());

                    // Http Code OK
                    LOG.info("Approved reimbursement");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {

                    // Http Code CONFLICT (not a correct status for ApproveDeny). . .
                    LOG.info("Failed to update status of reimbursement");
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                }
                break;
        }
    }
}
