package com.revature.util;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.repositories.UserRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class ServiceUtil {

    private static final UserService userService;
    private static final ReimbursementService reimbService;
    static{


        SessionFactory sF = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Reimbursement.class).buildSessionFactory();


        UserRepository userRepo = new UserRepository(sF);
        userService = new UserService(userRepo);
        ReimbursementsRepository rR = new ReimbursementsRepository(sF);
        reimbService = new ReimbursementService(rR);
    }

    public static UserService getUserService(){

        return userService;
    }

    public static ReimbursementService getReimbService(){

        return reimbService;
    }

}
