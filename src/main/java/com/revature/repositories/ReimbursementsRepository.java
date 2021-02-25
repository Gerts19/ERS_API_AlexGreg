package com.revature.repositories;

import com.revature.dtos.RbDTO;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.attribute.standard.RequestingUserName;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class to interact with the database to CRUD reimbursement objects
 */
public class ReimbursementsRepository {
    //base query that combines the name and resolver names from one query
    private String baseQuery = "SELECT er.id, er.amount, er.description, er.reimbursement_status_id, \n" +
            "er.reimbursement_type_id, er.resolved, er.submitted,  er.author_id , er.resolver_id,\n" +
            "author.first_name as author_first_name , author.last_name as author_last_name , \n" +
            "resolver.first_name as resolver_first_name, resolver.last_name as resolver_last_name\n" +
            "FROM project_1.ers_reimbursements er\n" +
            "left join project_1.ers_users author \n" +
            "on er.author_id = author.id\n" +
            "left join project_1.ers_users resolver \n" +
            "on er.resolver_id = resolver.id ";
    private String baseInsert = "INSERT INTO project_1.ers_reimbursements ";
    private String baseUpdate = "UPDATE project_1.ers_reimbursements er ";
    private SessionFactory sF;

    public ReimbursementsRepository(){
        super();
    }

    public ReimbursementsRepository(SessionFactory sF){

        this.sF = sF;
    }



    //---------------------------------- CREATE -------------------------------------------- //
    /**
     * Adds a reimburement to the database, Does not handle Images!
     * @param reimbursement the reimbursement to be added to the DB
     * @throws SQLException e
     * @throws IOException e
     */
    // TODO add support to persist receipt images to data source
    //Refactored*******
    public boolean addReimbursement(Reimbursement reimbursement) {

        Session session = sF.openSession();
        Transaction t = null;
        Integer iD = 0;

        try {
            t = session.beginTransaction();
            iD = (Integer) session.save(reimbursement);
            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return iD>0;

//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseInsert +
//                    "(amount, description, author_id, " +
//                    "reimbursement_status_id, reimbursement_type_id)\n" +
//                    "VALUES(?, ?, ?, 1, ?);\n";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setDouble(1,reimbursement.getAmount());
//            ps.setString(2,reimbursement.getDescription());
//            ps.setInt(3,reimbursement.getAuthorId());
//            //Reimbursements are submitted with PENDING  status by Default!
//            ps.setInt(4,reimbursement.getReimbursementType().ordinal() + 1);
//            //get the number of affected rows
//            int rowsInserted = ps.executeUpdate();
//            return rowsInserted != 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
    }

    //---------------------------------- READ -------------------------------------------- //

    //Refactored*******
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbursements() {


        Session session = sF.openSession();
        Transaction t = null;
        List<Reimbursement> reimbs = new ArrayList<>();

        try {
            t = session.beginTransaction();
            reimbs = session.createQuery("FROM Reimbursement").list();
            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return reimbs;
        //Iterator it=l.iterator();
//        while(it.hasNext())
//        {
//            Object rows[] = (Object[])it.next();
//            System.out.println(rows[0]+ " -- " +rows[1] + "--"+rows[2]+"--"+rows[3]);
//        }
        //session.clear();
        //session.close();

//        Session session = sF.openSession();
//        Transaction t = null;
//        List<User> users = new ArrayList<>();
//
//        try {
//            t = session.beginTransaction();
//            users = session.createQuery("FROM ers_users").list();
//            t.commit();
//        } catch (Exception e) {
//            if(t!=null){
//                t.rollback();
//            }
//            e.printStackTrace();
//        }
//
//        return users;

//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + " order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ResultSet rs = ps.executeQuery();
//
//            reimbursements = mapResultSetDTO(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return reimbursements;
    }

    //Refactored*******
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByStatus(Integer statusId) {

        Session session = sF.openSession();
        Transaction t = null;
        List<Reimbursement> reimbs = new ArrayList<>();

        try {
            t = session.beginTransaction();

            CriteriaBuilder cB = session.getCriteriaBuilder();
            CriteriaQuery<Reimbursement> cR = cB.createQuery(Reimbursement.class);
            Root<Reimbursement> root = cR.from(Reimbursement.class);
            //Using the field name directly works, but not the annotation name="value", awesome!
            cR.select(root).where(cB.equal(root.get("reimbursementStatus"),statusId));
            Query qu = session.createQuery(cR);
            reimbs = qu.getResultList();
            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return reimbs;


//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.reimbursement_status_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1,statusId);
//            ResultSet rs = ps.executeQuery();
//            reimbursements = mapResultSetDTO(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return reimbursements;
    }

    /**
     * A method to get Reimbursements by the id of the reimbursement itself
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @return returns an Option Reimbursement object
     * @throws SQLException e
     */

    /*
    It is implemented but the method
    it calls to is not. Not completed
     */
    @SuppressWarnings("unchecked")
    public Optional<Reimbursement> getAReimbByReimbId(Integer reimbId) throws SQLException {

        Session session = sF.openSession();
        Transaction t = null;
        Optional<Reimbursement> reimb = Optional.empty();

        try {
            t = session.beginTransaction();

            CriteriaBuilder cB = session.getCriteriaBuilder();
            CriteriaQuery<Reimbursement> cR = cB.createQuery(Reimbursement.class);
            Root<Reimbursement> root = cR.from(Reimbursement.class);
            //Using the field name directly works, but not the annotation name="value", awesome!
            cR.select(root).where(cB.equal(root.get("id"),reimbId));
            Query qu = session.createQuery(cR);
            List<Reimbursement> results = qu.getResultList();
            reimb = results.stream().findFirst();
            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return reimb;
//        Optional<Reimbursement> reimbursement = Optional.empty();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setInt(1,reimbId);
//
//            ResultSet rs = ps.executeQuery();
//
//            reimbursement = mapResultSet(rs).stream().findFirst();
//        }
//        return reimbursement;
    }

    /**
     * A method to get all of the records for an author given their id
     * @param authorId the ID of the author of the reimbursement
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */


    //Refactored*******
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByAuthorId(Integer authorId){

        Session session = sF.openSession();
        Transaction t = null;
        List<Reimbursement> reimbs = new ArrayList<>();

        try {
            t = session.beginTransaction();

            CriteriaBuilder cB = session.getCriteriaBuilder();
            CriteriaQuery<Reimbursement> cR = cB.createQuery(Reimbursement.class);
            Root<Reimbursement> root = cR.from(Reimbursement.class);
            //Using the field name directly works, but not the annotation name="value", awesome!
            cR.select(root).where(cB.equal(root.get("authorId"),authorId));
            Query qu = session.createQuery(cR);
            reimbs = qu.getResultList();
            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return reimbs;
//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.author_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setInt(1,authorId);
//
//            ResultSet rs = ps.executeQuery();
//
//            reimbursements = mapResultSetDTO(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return reimbursements;
    }

    /**
     * A method to get all of the records for an author given their id and filter by status
     * @param authorId the ID of the author of the reimbursement
     * @param reStat the status that the reimbursement is to be set to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */

    //Refactored*******
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByAuthorIdAndStatus(Integer authorId, ReimbursementStatus reStat) throws SQLException {

        Session session = sF.openSession();
        Transaction t = null;
        List<Reimbursement> reimbs = new ArrayList<>();

        try {
            t = session.beginTransaction();

            CriteriaBuilder cB = session.getCriteriaBuilder();
            CriteriaQuery<Reimbursement> cR = cB.createQuery(Reimbursement.class);
            Root<Reimbursement> root = cR.from(Reimbursement.class);
            Predicate pred = cB.equal(root.get("authorId"),authorId);
            Predicate pred2 = cB.equal(root.get("reimbursementStatus"),reStat);

            cR.select(root).where(cB.and(pred,pred2));
            Query qu = session.createQuery(cR);
            reimbs = qu.getResultList();

            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return reimbs;
//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.author_id=? AND er.reimbursement_status_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setInt(1,authorId);
//            ps.setInt(2,reStat.ordinal() + 1);
//            ResultSet rs = ps.executeQuery();
//            reimbursements = mapResultSetDTO(rs);
//        }
//        return reimbursements;
    }

    /**
     * A method to get all of the records for an author given their id and filter by type
     * @param authorId ID of the Author User
     * @param reType the Type to update the record to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<RbDTO> getAllReimbSetByAuthorIdAndType(Integer authorId, ReimbursementType reType) throws SQLException {
        List<RbDTO> reimbursements = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseQuery + "WHERE er.author_id=? AND er.reimbursement_type_id=? order by er.id";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,authorId);
            ps.setInt(2,reType.ordinal() + 1);
            ResultSet rs = ps.executeQuery();
            reimbursements = mapResultSetDTO(rs);
        }
        return reimbursements;
    }

    //Refactored
    public List<Reimbursement> getAllReimbSetByType(Integer typeId)  {

        Session session = sF.openSession();
        Transaction t = null;
        List<Reimbursement> reimbs = new ArrayList<>();

        try {
            t = session.beginTransaction();

            CriteriaBuilder cB = session.getCriteriaBuilder();
            CriteriaQuery<Reimbursement> cR = cB.createQuery(Reimbursement.class);
            Root<Reimbursement> root = cR.from(Reimbursement.class);
            //Using the field name directly works, but not the annotation name="value", awesome!
            cR.select(root).where(cB.equal(root.get("reimbursementType"),typeId));
            Query qu = session.createQuery(cR);
            reimbs = qu.getResultList();
            t.commit();
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return reimbs;
//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.reimbursement_type_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1,typeId);
//            ResultSet rs = ps.executeQuery();
//            reimbursements = mapResultSetDTO(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return reimbursements;
    }

    /**
     * A method to get all of the records for a resolver given their id
     * @param resolverId ID of the Resolver User
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<RbDTO> getAllReimbSetByResolverId(Integer resolverId) throws SQLException {
        List<RbDTO> reimbursements = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseQuery + "WHERE er.resolver_id=? order by er.id";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,resolverId);

            ResultSet rs = ps.executeQuery();

            reimbursements = mapResultSetDTO(rs);
        }
        return reimbursements;
    }

    /**
     * A method to get all of the records for a resolver given their id and filter by status
     * @param resolverId  ID of the Resolver User
     * @param reStat the status to update the record to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<RbDTO> getAllReimbSetByResolverIdAndStatus(Integer resolverId, ReimbursementStatus reStat) throws SQLException {
        List<RbDTO> reimbursements = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseQuery + "WHERE er.resolver_id=? AND er.reimbursement_status_id=? order by er.id";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,resolverId);
            ps.setInt(2,reStat.ordinal() + 1);
            ResultSet rs = ps.executeQuery();
            reimbursements = mapResultSetDTO(rs);
        }
        return reimbursements;
    }

    /**
     * A  method to get all of the records for a resolver given their id and filter by type
     * @param resolverId ID of the Resolver User
     * @param reType type of Reimbursements to select by
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<RbDTO> getAllReimbSetByResolverIdAndType(Integer resolverId, ReimbursementType reType) throws SQLException {
        List<RbDTO> reimbursements = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseQuery + "WHERE er.resolver_id=? AND er.reimbursement_type_id=? order by er.id";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,resolverId);
            ps.setInt(2,reType.ordinal() + 1);
            ResultSet rs = ps.executeQuery();
            reimbursements = mapResultSetDTO(rs);
        }
        return reimbursements;
    }

    //---------------------------------- UPDATE -------------------------------------------- //
    public boolean updateEMP(Reimbursement reimb) {

        Session session = sF.openSession();
        Transaction t = null;

        boolean success = false;

        try {
            t = session.beginTransaction();
            session.update(reimb);
            t.commit();
            success = true;
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return success;


//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseUpdate +
//                    "SET amount=?, description=?, reimbursement_type_id=?\n" +
//                    "WHERE id=?\n";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setDouble(1, reimb.getAmount());
//            ps.setString(2, reimb.getDescription());
//            ps.setInt(3,reimb.getReimbursementType().ordinal() + 1);
//            ps.setInt(4,reimb.getId());
//            //get the number of affected rows
//            int rowsInserted = ps.executeUpdate();
//            return rowsInserted != 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
    }

    public boolean updateFIN(Integer resolverId, Integer statusId, Integer reimbId) {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET resolver_id=?, reimbursement_status_id=?, resolved=CURRENT_TIMESTAMP\n" +
                    "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resolverId);
            ps.setInt(2, statusId);
            ps.setInt(3,reimbId);

            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * A method to update only the resolved timestamp by the id of the reimbursement
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param timestamp an SQL timestamp object to set the time resolved to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateResolvedTimeStampByReimbId(Integer reimbId, Timestamp timestamp) throws SQLException {

        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                         "SET resolved=?\n" +
                         "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1,timestamp);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }

    /**
     * A method to update only the resolver ID by the id of the reimbursement
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param resolverId the ID of the user that resolves the record to update the record to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateResolverIdByReimbId(Integer reimbId, Integer resolverId) throws SQLException {

        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET resolver_id=?\n" +
                    "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,resolverId);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }

    /**
     * A method to update only the Reimb. TYPE by the id of the Reimbursement
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param reimbursementType the type to update the record to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateReimbursementTypeByReimbId(Integer reimbId, ReimbursementType reimbursementType) throws SQLException {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET reimbursement_type_id=? " +
                    "WHERE er.id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,reimbursementType.ordinal() + 1);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }

    /**
     * A method to update the status of a reimbursement in the database
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param newReimbStatus the status to update the record to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateReimbursementStatusByReimbId(Integer reimbId, ReimbursementStatus newReimbStatus) throws SQLException {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                         "SET reimbursement_status_id=? " +
                         "WHERE er.id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,newReimbStatus.ordinal() + 1);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }


    //---------------------------------- DELETE -------------------------------------------- //

    /**
     * A method to delete a single Reimbursement from the database
     * @param reimbId the ID of the record to be deleted
     * @return returns true if one and only one record is updated
     * @throws SQLException e
     */
    public boolean delete(Integer reimbId) throws SQLException {

        Session session = sF.openSession();
        Transaction t = null;
        boolean success = false;

        try {
            t = session.beginTransaction();
            session.delete(reimbId);
            t.commit();
            success = true;
        } catch (Exception e) {
            if(t!=null){
                t.rollback();
            }
            e.printStackTrace();
        }

        return success;

//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = "DELETE FROM project_1.ers_reimbursements\n" +
//                         "WHERE id=? ";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1,reimbId);
//            //get the number of affected rows
//            int rowsInserted = ps.executeUpdate();
//            return rowsInserted != 0;
 //       }
    }

    //---------------------------------- UTIL -------------------------------------------- //
    /**
     * A method to map the result sets from the reimbursement queries
     * @param rs a resultset
     * @return a set of reimbursements
     * @throws SQLException e
     */
    private Set<Reimbursement> mapResultSet(ResultSet rs) throws SQLException {
        Set<Reimbursement> reimbursements = new HashSet<>();
        while (rs.next()){
            Reimbursement temp = new Reimbursement();
            temp.setId(rs.getInt("id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted"));
            temp.setResolved(rs.getTimestamp("resolved"));
            temp.setDescription(rs.getString("description"));
            temp.setAuthorId(rs.getInt("author_id"));
            temp.setResolverId(rs.getInt("resolver_id"));
            temp.setReimbursementStatus(ReimbursementStatus.getByNumber(rs.getInt("reimbursement_status_id")));
            temp.setReimbursementType(ReimbursementType.getByNumber(rs.getInt("reimbursement_type_id")));

            reimbursements.add(temp);
        }
        return reimbursements;
    }

    private List<RbDTO> mapResultSetDTO(ResultSet rs) throws SQLException {
        List<RbDTO> reimbs = new ArrayList<>();
        while (rs.next()){
            RbDTO temp = new RbDTO();
            temp.setId(rs.getInt("id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted").toString().substring(0,19));
            temp.setDescription(rs.getString("description"));
            temp.setAuthorName(rs.getString("author_first_name") + " " + rs.getString("author_last_name"));
            temp.setStatus(ReimbursementStatus.getByNumber(rs.getInt("reimbursement_status_id")).toString());
            temp.setType(ReimbursementType.getByNumber(rs.getInt("reimbursement_type_id")).toString());
            try {
                temp.setResolved(rs.getTimestamp("resolved").toString().substring(0,19));
                temp.setResolverName(rs.getString("resolver_first_name") + " " + rs.getString("resolver_last_name"));
            } catch (NullPointerException e){
                //If Reimb. has not been resolved DB will return null for these values:
                temp.setResolved("");
                temp.setResolverName("");
            }

            reimbs.add(temp);
        }
        System.out.println(reimbs);
        return reimbs;
    }
}
