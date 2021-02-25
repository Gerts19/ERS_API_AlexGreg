package com.revature;

import com.revature.dtos.RbDTO;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.repositories.UserRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Driver {

  public static void main(String[] args) {


      SessionFactory sF = new Configuration()
                                .addAnnotatedClass(User.class)
                                .addAnnotatedClass(Reimbursement.class).buildSessionFactory();

      UserRepository userRepo = new UserRepository(sF);
      UserService userServ = new UserService(userRepo);
      ReimbursementsRepository rR = new ReimbursementsRepository(sF);
      ReimbursementService rS = new ReimbursementService(rR);

//      User user = new User("greg10", "pass", "Greg", "Gertson", "greg10@gmail.com");
//      userServ.register(user);

//      User newUser = new User("admin", "admin", "Admin",
//                                    "User","admin@gmail.com");
//      newUser.setUserRole(1);
//      userRepo.addUser(newUser);

      List<User> users = userServ.getAllUsers();
      for (User u : users)
          System.out.println(u.toString());


//      Reimbursement r = new Reimbursement(45.54, "Big Greg Breakfast2",
//                                            3, ReimbursementStatus.getByNumber(1),ReimbursementType.getByNumber(3));
//
//      r.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
//      rS.save(r);

//      Reimbursement r1 = new Reimbursement(543.21, "Big Alex Breakfast",
//              2, ReimbursementStatus.getByNumber(1),ReimbursementType.getByNumber(3));
//
//      r1.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
//      rS.save(r1);


      //getAReimbByReimbId


      //Good, but no method to call from Service class
//      try {
//          Optional<Reimbursement> aReimb =  rR.getAReimbByReimbId(1);
//      System.out.println(aReimb);
//      } catch (SQLException throwables) {
//          throwables.printStackTrace();
//      }


      //Good, but no method to call from Service class
//      try {
//          List<Reimbursement> allReimb =  rR.getAllReimbSetByAuthorIdAndStatus(1,ReimbursementStatus.PENDING);
//          for(Reimbursement r: allReimb){
//              System.out.println(r.toString());
//          }
//      } catch (SQLException throwables) {
//          throwables.printStackTrace();
//      }


      //Good
//      List<Reimbursement> allReimb =  rS.getReimbByUserId(1);
//      for(Reimbursement r: allReimb){
//          System.out.println(r.toString());
//      }

      //Good
//      List<Reimbursement> allReimb =  rS.getReimbByType(2);
//      for(Reimbursement r: allReimb){
//      System.out.println(r.toString());
//      }

      //Good
//      List<Reimbursement> allReimb =  rS.getReimbByStatus(1);
//      for(Reimbursement r: allReimb){
//      System.out.println(r.toString());
//      }



//      List<Reimbursement> allReimb =  rS.getAllReimb();
//
//      for(Reimbursement r: allReimb){
//
//      System.out.println(r.toString());
//      }



//      if(userServ.isUsernameAvailable("alex10")){
//          System.out.println("username is available");
//      }
//      else{
//      System.out.println("Success");
//      }


//        User u = userServ.authenticate("alex10","pass");
//        System.out.println(u.toString());

//      if(userServ.isEmailAvailable("alex10@gmail.com")){
//      System.out.println("email is available");
//      }
//      else{
//      System.out.println("Success");
//      }

//      try {
//          t = session.beginTransaction();
//          User newUser = new User("alex10", "pass", "Alex",
//                                    "Googe","alex10@gmail.com");
//          newUser.setUserRole(1);
//          int value = (int) session.save(newUser);
//          newUser.setUserId(value);
//          t.commit();
//      } catch (Exception e) {
//          if(t!=null){
//              t.rollback();
//          }
//          e.printStackTrace();
//      }

//
//          //t = session.beginTransaction();
//          Reimbursement r = new Reimbursement(123.45, "Big Dinner",
//                                            1, ReimbursementStatus.getByNumber(2),ReimbursementType.getByNumber(3));
//
//          r.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
//          rS.save(r);


  }
}
