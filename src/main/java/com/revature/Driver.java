package com.revature;

import com.revature.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Driver {

  public static void main(String[] args) {


      SessionFactory sF = new Configuration()
                                .addAnnotatedClass(User.class).buildSessionFactory();

      Session session = sF.openSession();

      Transaction t = null;

      try {
          t = session.beginTransaction();
          User newUser = new User("alex10", "pass", "Alex",
                                    "Googe","alex10@gmail.com");
          newUser.setUserRole(1);
          int value = (int) session.save(newUser);
          newUser.setUserId(value);
          t.commit();
      } catch (Exception e) {
          if(t!=null){
              t.rollback();
          }
          e.printStackTrace();
      }


  }
}
