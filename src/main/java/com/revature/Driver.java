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

      Reimbursement reimbursement = new Reimbursement(123.45, "Big Test", 7, ReimbursementStatus.getByNumber(1),ReimbursementType.getByNumber(1));
      reimbursement.setSubmitted(Timestamp.valueOf(LocalDateTime.now()));
      rS.save(reimbursement);


  }
}
