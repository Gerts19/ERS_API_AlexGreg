package com.revature;

import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Driver {

  public static void main(String[] args) {


      SessionFactory sF = new Configuration()
                                .addAnnotatedClass(User.class)
                                .addAnnotatedClass(Reimbursement.class).buildSessionFactory();

      Session session = sF.openSession();

      Transaction t = null;

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

      try {
          t = session.beginTransaction();
          Reimbursement r = new Reimbursement(123.45, "Big lunch",
                                            1, ReimbursementStatus.getByNumber(2),ReimbursementType.getByNumber(3));

          r.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
          r.setId((int) session.save(r));
          t.commit();
      } catch (Exception e) {
          if(t!=null){
              t.rollback();
          }
          e.printStackTrace();
      }


  }
}
