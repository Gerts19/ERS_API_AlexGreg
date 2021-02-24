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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Driver {

  public static void main(String[] args) {


      SessionFactory sF = new Configuration()
                                .addAnnotatedClass(User.class)
                                .addAnnotatedClass(Reimbursement.class).buildSessionFactory();

      UserRepository userRepo = new UserRepository(sF);
      UserService userServ = new UserService(userRepo);


      ReimbursementsRepository rR = new ReimbursementsRepository(sF);
      ReimbursementService rS = new ReimbursementService(rR);

      List<RbDTO> allReimb =  rS.getAllReimb();

      for(RbDTO r: allReimb){

      System.out.println(r.toString());
      }

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

//      try {
//          t = session.beginTransaction();
//          Reimbursement r = new Reimbursement(123.45, "Big lunch",
//                                            1, ReimbursementStatus.getByNumber(2),ReimbursementType.getByNumber(3));
//
//          r.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
//          r.setId((int) session.save(r));
//          t.commit();
//      } catch (Exception e) {
//          if(t!=null){
//              t.rollback();
//          }
//          e.printStackTrace();
//      }



  }
}
